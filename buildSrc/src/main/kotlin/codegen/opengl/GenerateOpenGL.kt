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
import codegen.BYTE_VAR
import codegen.CTypeDecl
import codegen.KtxC
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

			val tryFinallyBlocks = mutableListOf<Pair<CodeBlock, CodeBlock>>()
			val ioBufferDirectBlocks = mutableListOf<Pair<String, String>>()
			var requiresArena = false
			var isReturnNotFriendly = false

			val parameters = mutableListOf<ParameterSpec>()
			val arguments = mutableMapOf<String, CodeBlock>()
			for (param in command.params) {
				val typeKt = param.type.toKtInteropParamType()

				arguments[param.name] = CodeBlock.of(if (param.type.asteriskCount > 0 && param.type.isConst) {
					requiresArena = true
					param.name.escapeKt() + "?.getPointer(VirtualStack.currentFrame!!)"
				} else {
					param.name.escapeKt()
				})
				parameters.add(ParameterSpec(param.name, typeKt))
			}

			val shouldReturnString = command.returnType.matches("GLubyte", 1, true)
			val shouldReturnBoolean = command.returnType.matches("GLboolean", 0, false)
			if (shouldReturnString || shouldReturnBoolean) {
				isReturnNotFriendly = true
			}

			val function = FunSpec.builder(if (isReturnNotFriendly) "n${command.name}" else command.name)
					.addParameters(parameters)
			val commandCall = command.params.map { arguments.getValue(it.name) }
					.joinToCode(prefix = "gl.${command.name}!!(", suffix = ")\n")

			val mainFunctionBody = CodeBlock.builder()
			if (command.returnType.name == "void" && command.returnType.asteriskCount == 0) {
				mainFunctionBody.add(commandCall)
				function.returns(UNIT)
			} else {
				mainFunctionBody.add("return ")
				mainFunctionBody.add(commandCall)
				function.returns(command.returnType.toKtInteropType())
			}

			if (requiresArena) {
				tryFinallyBlocks += Pair(
						CodeBlock.of("%T.push()\n", VIRTUAL_STACK),
						CodeBlock.of("%T.pop()\n", VIRTUAL_STACK)
				)
			}

			val functionBody = buildCodeBlock {
				tryFinallyBlocks.forEach { (pre, _) ->
					add(pre)
					beginControlFlow("try")
				}
				ioBufferDirectBlocks.forEach { beginControlFlow(it.first) }

				add(mainFunctionBody.build())

				ioBufferDirectBlocks.forEach {
					addStatement(it.second)
					endControlFlow()
				}
				tryFinallyBlocks.reversed().forEach { (_, post) ->
					nextControlFlow("finally")
					add(post)
					endControlFlow()
				}
			}
			function.addCode(functionBody)
			val originalFunction = function.build()
			glFile.addFunction(originalFunction)

			if (isReturnNotFriendly) {
				val internalFunCall = parameters.joinToString(prefix = "n${command.name}(", postfix = ")") { it.name }
				val wrapperFun = FunSpec.builder(command.name)
				wrapperFun.addParameters(parameters)
				if (shouldReturnString) {
					wrapperFun.returns(STRING.copy(nullable = true))
					wrapperFun.addCode(buildCodeBlock {
						add("return ")
						add(internalFunCall)
						add("?.%M<%T>()?.%M()\n", KtxC.REINTERPRET, BYTE_VAR, KtxC.TO_KSTRING)
					})
				}
				if (shouldReturnBoolean) {
					wrapperFun.returns(BOOLEAN)
					wrapperFun.addCode(buildCodeBlock {
						add("return ")
						add(internalFunCall)
						add(".toUInt() == GL_TRUE\n")
					})
				}
				glFile.addFunction(wrapperFun.build())
			}
			if (command.name.endsWith('v')) {
				val lastArg = command.params.last()
				// val newFunctionName = command.name.replace(overloadRegex, "")
				val newFunctionName = command.name.dropLast(1).removeSuffix("i_")

				if (command.returnType.matches("void", 0) && !lastArg.type.isConst && lastArg.type.asteriskCount == 1) {
					val wrapperFun = FunSpec.builder(newFunctionName)
					wrapperFun.addParameters(parameters.dropLast(1))

					val internalFunCall = (parameters.dropLast(1).map { CodeBlock.of(it.name) } + CodeBlock.of("retValue.%M", KtxC.PTR))
							.joinToCode(prefix = "${command.name}(", suffix = ")\n")

					wrapperFun.returns(ClassName("copengl", lastArg.type.name))
					wrapperFun.addCode(buildCodeBlock {
						addStatement("%T.push()", VIRTUAL_STACK)
						beginControlFlow("try")
						addStatement("val retValue = %T.%M<%T>()", VIRTUAL_STACK, KtxC.ALLOC, ClassName("copengl", lastArg.type.name + "Var"))
						add(internalFunCall)
						addStatement("return retValue.%M", KtxC.VALUE)
						nextControlFlow("finally")
						addStatement("%T.pop()", VIRTUAL_STACK)
						endControlFlow()
					})
					glFile.addFunction(wrapperFun.build())
				}
			}
			if (command.name.endsWith('s') && (command.name.startsWith("glCreate") || command.name.startsWith("glGen"))) {
				val lastArg = command.params.last()
				val newFunctionName = if (command.name.endsWith("ies")) {
					command.name.dropLast(3) + "y"
				} else {
					command.name.dropLast(1)
				}

				if (command.returnType.matches("void", 0) && !lastArg.type.isConst && lastArg.type.asteriskCount == 1) {
					val wrapperFun = FunSpec.builder(newFunctionName)
					wrapperFun.returns(ClassName("copengl", lastArg.type.name))
					wrapperFun.addParameters(parameters.dropLast(2))

					val internalFunCall = (parameters.dropLast(2).map { CodeBlock.of(it.name) } + CodeBlock.of("1") + CodeBlock.of("retValue.%M", KtxC.PTR))
							.joinToCode(prefix = "${command.name}(", suffix = ")\n")

					wrapperFun.addCode(buildCodeBlock {
						addStatement("%T.push()", VIRTUAL_STACK)
						beginControlFlow("try")
						addStatement("val retValue = %T.%M<%T>()", VIRTUAL_STACK, KtxC.ALLOC, ClassName("copengl", lastArg.type.name + "Var"))
						add(internalFunCall)
						addStatement("return retValue.%M", KtxC.VALUE)
						nextControlFlow("finally")
						addStatement("%T.pop()", VIRTUAL_STACK)
						endControlFlow()
					})
					glFile.addFunction(wrapperFun.build())
					println(newFunctionName)
				}
			}

			// Map of param name to a pair of friendlier type and converter.
			val paramMapping = mutableMapOf<String, Pair<TypeName, CodeBlock>>()
			for (param in command.params) {
				if (param.type.matches("GLchar", 1, true)) {
					paramMapping[param.name] = STRING to CodeBlock.of("${param.name}.%M", KtxC.CSTR)
				}
				if (param.type.matches("GLboolean", 0)) {
					paramMapping[param.name] = BOOLEAN to CodeBlock.of("(if (${param.name}) GL_TRUE else GL_FALSE).toUByte()")
				}
			}
			// If params can be nicer, then make a friendlier wrapper function.
			if (paramMapping.isNotEmpty()) {
				val internalFunCall = parameters.map { paramMapping[it.name]?.second ?: CodeBlock.of(it.name) }
						.joinToCode(prefix = "${command.name}(", suffix = ")\n")
				val wrapperFun = FunSpec.builder(command.name)
				wrapperFun.addParameters(parameters.map { param -> paramMapping[param.name]?.let { ParameterSpec(param.name, it.first) } ?: param })
				originalFunction.returnType?.let { wrapperFun.returns(it) }
				wrapperFun.addCode(buildCodeBlock {
					add("return ")
					add(internalFunCall)
				})
				glFile.addFunction(wrapperFun.build())
			}
 		}

		with(glFile.build()) {
			writeTo(mingwDir.get().asFile)
			writeTo(linuxDir.get().asFile)
			writeTo(macosDir.get().asFile)
		}
	}

	private val overloadRegex = Regex("([1234])?(b|s|i|i64|f|d|ub|us|ui|ui64|i_)?(v)?$")

	private fun CTypeDecl.matches(name: String, astCount: Int, isConst: Boolean): Boolean {
		return this.name == name &&
				this.asteriskCount == astCount &&
				this.isConst == isConst
	}

	private fun CTypeDecl.matches(name: String, astCount: Int): Boolean {
		return this.name == name &&
				this.asteriskCount == astCount
	}

	private fun CTypeDecl.matches(name: String): Boolean {
		return this.name == name
	}
}
