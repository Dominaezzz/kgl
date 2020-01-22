/**
 * Copyright [2019] [Dominic Fischer]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codegen.opengl

import codegen.C_OPAQUE_POINTER
import codegen.THREAD_LOCAL
import codegen.VIRTUAL_STACK
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.xml.parsers.DocumentBuilderFactory

open class GenerateOpenGL : DefaultTask() {
	@InputFile
	val registryFile = project.objects.fileProperty()

	@OutputDirectory
	val outputDir = project.objects.directoryProperty()

	@get:OutputDirectory
	val commonNativeDir = outputDir.file("native")

	@get:OutputDirectory
	val mingwDir = outputDir.file("mingw")

	@get:OutputDirectory
	val linuxDir = outputDir.file("linux")

	@get:OutputDirectory
	val macosDir = outputDir.file("macos")

	@TaskAction
	fun generate() {
		val registry = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(registryFile.get().asFile)
				.let {
					it.documentElement.normalize()
					Registry(it)
				}

		val coreCommands = mutableSetOf<String>()
		val coreEnums = mutableSetOf<String>()

		for (feature in registry.features.filter { it.api == "gl" }) {
			coreCommands += feature.requires.filter { it.profile != "compatibility" }.flatMap { it.commands }
			coreCommands -= feature.removes.flatMap { it.commands }

			coreEnums += feature.requires.filter { it.profile != "compatibility" }.flatMap { it.enums }
			coreEnums -= feature.removes.flatMap { it.enums }
		}

		val bitMasks = registry.enums.filter { it.type == "bitmask" }.map { it.group }.toSet()

		for (group in registry.groups) {
			val isBitmask = group.name in bitMasks

			val enumBuilder = TypeSpec.enumBuilder(group.name)

			enumBuilder.primaryConstructor(FunSpec.constructorBuilder().addParameter("value", INT).build())

			enumBuilder.addProperty(
					PropertySpec.builder("value", INT)
							.apply {
								if (isBitmask) {
									addModifiers(KModifier.OVERRIDE)
								}
							}
							.initializer("value")
							.build()
			)

			if (isBitmask) {
				enumBuilder.addSuperinterface(GL_MASK.parameterizedBy(
						ClassName("com.kgl.opengl.enums", group.name)
				))
			}

			val enumFile = FileSpec.builder("com.kgl.opengl.enums", group.name)
			for (entry in group.enums) {
				if (entry !in coreEnums) continue
				if (entry.startsWith("GL_ALL_") && entry.endsWith("_BITS")) continue

				enumBuilder.addEnumConstant(
						entry.removePrefix("GL_").let {
							if (isBitmask) {
								it.removeSuffix("_BIT")
							} else {
								it
							}
						},
						TypeSpec.anonymousClassBuilder()
								.addSuperclassConstructorParameter(entry)
								.build()
				)

				enumFile.addImport("copengl", entry)
			}

			enumFile.addType(enumBuilder.build())

			with(enumFile.build()) {
				writeTo(mingwDir.get().asFile)
				writeTo(linuxDir.get().asFile)
				writeTo(macosDir.get().asFile)
			}
		}

		val glFile = FileSpec.builder("com.kgl.opengl", "GL")
				.addImport("kotlinx.cinterop", "addressOf")
				.addImport("kotlinx.cinterop", "alloc")
				.addImport("kotlinx.cinterop", "cstr")
				.addImport("kotlinx.cinterop", "convert")
				.addImport("kotlinx.cinterop", "invoke")
				.addImport("kotlinx.cinterop", "pin")
				.addImport("kotlinx.cinterop", "ptr")
				.addImport("kotlinx.cinterop", "value")
				.addImport("kotlinx.cinterop", "reinterpret")
				.addImport("kotlinx.cinterop", "toBoolean")
				.addImport("kotlinx.cinterop", "toByte")
				.addImport("kotlinx.cinterop", "toCPointer")
				.addImport("kotlinx.cinterop", "toCStringArray")
				.addImport("kotlinx.cinterop", "toLong")
				.addImport("com.kgl.opengl.utils", "Loader")

		for (enum in registry.enums) {
			for ((name, _) in enum.entries) {
				if (name !in coreEnums) continue

				// Add KModifier.CONST once `toUInt()` is constexpr
				glFile.addProperty(PropertySpec.builder(name, U_INT)
						.initializer("copengl.$name.toUInt()").build())
			}
		}

		val getProcAddressType = LambdaTypeName.get(null, listOf(ParameterSpec.unnamed(STRING)), C_OPAQUE_POINTER.copy(nullable = true))

		val glFunctions = TypeSpec.classBuilder("GLFunctions")
		glFunctions.addModifiers(KModifier.INTERNAL)
		glFunctions.primaryConstructor(FunSpec.constructorBuilder()
				.addParameter("getProcAddress", getProcAddressType)
				.build())

		for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val pfnType = "PFN${(command.alias ?: command.name).toUpperCase()}PROC"

			glFunctions.addProperty(
					PropertySpec.builder(
							command.name,
							ClassName("copengl", pfnType).copy(nullable = true)
					).initializer("getProcAddress(%S)?.reinterpret()", command.name).build()
			)
		}

		glFile.addType(glFunctions.build())
		glFile.addProperty(PropertySpec.builder(
				"gl",
				ClassName("com.kgl.opengl", "GLFunctions"),
				KModifier.INTERNAL
		).delegate("lazy { GLFunctions(Loader::kglGetProcAddress) }").addAnnotation(THREAD_LOCAL).build())

		loop@for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val function = FunSpec.builder(command.name)
			val mainFunctionBody = CodeBlock.builder()

			val callBuilder = GLCallBuilder(command)

			val tryFinallyBlocks = mutableListOf<Pair<CodeBlock, CodeBlock>>()
			val ioBufferDirectBlocks = mutableListOf<Pair<String, String>>()
			var requiresArena = false

			for (param in command.params) {
				val typeKt = param.type.toKtInteropParamType()

				callBuilder[param.name] = if (param.type.asteriskCount > 0 && param.type.isConst) {
					requiresArena = true
					param.name.escapeKt() + "?.getPointer(VirtualStack.currentFrame!!)"
				} else {
					param.name.escapeKt()
				}
				function.addParameter(param.name, typeKt)
			}

			val commandCall = callBuilder.build()
			if (command.returnType.name == "void" && command.returnType.asteriskCount == 0) {
				mainFunctionBody.addStatement(commandCall)
				function.returns(UNIT)
			} else {
				mainFunctionBody.addStatement("return $commandCall")
				function.returns(command.returnType.toKtInteropType())
			}

			if (requiresArena) {
				tryFinallyBlocks += Pair(
						CodeBlock.of("%T.push()\n", VIRTUAL_STACK),
						CodeBlock.of("%T.pop()\n", VIRTUAL_STACK)
				)
			}

			tryFinallyBlocks.forEach { (pre, _) ->
				function.addCode(pre)
				function.beginControlFlow("try")
			}
			ioBufferDirectBlocks.forEach { function.beginControlFlow(it.first) }

			function.addCode(mainFunctionBody.build())

			ioBufferDirectBlocks.forEach {
				function.addStatement(it.second)
				function.endControlFlow()
			}
			tryFinallyBlocks.reversed().forEach { (_, post) ->
				function.nextControlFlow("finally")
				function.addCode(post)
				function.endControlFlow()
			}

			glFile.addFunction(function.build())
		}

		with(glFile.build()) {
			writeTo(mingwDir.get().asFile)
			writeTo(linuxDir.get().asFile)
			writeTo(macosDir.get().asFile)
		}
	}
}
