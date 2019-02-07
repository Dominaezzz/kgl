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
package opengl.kn

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import opengl.Registry
import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object OpenGLNativeGenerator {
	private const val glXmlCommit = "89acc93eaa6acd97159fb069e66acb92f12d7b87"
	private val primitiveTypes = mapOf(
			"GLchar" to BYTE,

			"GLboolean" to BOOLEAN,
			"GLbyte" to BYTE,
			"GLubyte" to ClassName("kotlin", "UByte"),
			"GLshort" to SHORT,
			"GLushort" to ClassName("kotlin", "UShort"),
			"GLint" to INT,
			"GLuint" to ClassName("kotlin", "UInt"),
			"GLfixed" to INT,
			"GLint64" to LONG,
			"GLuint64" to ClassName("kotlin", "ULong"),

			// "GLsizei" to ClassName("kotlin", "UInt"),
			"GLsizei" to INT,

			//"GLenum" to INT,
			"GLenum" to ClassName("kotlin", "UInt"),

			/* These should be able to store sizeof(void*) */
			"GLintptr" to LONG ,
			"GLsizeiptr" to LONG,
			// "GLsizeiptr" to ClassName("kotlin", "ULong"),
			"GLsync" to LONG /* Probably should be here since, `typedef struct __GLsync *GLsync;` */,

			"GLbitfield" to ClassName("kotlin", "UInt"),
			// "GLbitfield" to INT,

			"GLhalf" to null,
			"GLfloat" to FLOAT,
			"GLclampf" to FLOAT /* From 0.0f to 1.0f */,
			"GLdouble" to DOUBLE,
			"GLclampd" to DOUBLE /* From 0.0 to 1.0 */
	)
	private val primitiveTypeAliases = mapOf(
			// "GLeglImageOES" to "void*",
			"GLint64EXT" to "GLint64",
			"GLuint64EXT" to "GLuint64"
	)

	private val virtualStackClass = ClassName("com.kgl.core.utils", "VirtualStack")
	private val glMaskClass = ClassName("com.kgl.opengl.utils", "GLMask")
	private val cOpaquePointerType = ClassName("kotlinx.interop", "COpaquePointer")

	fun generate(outputDir: File) {
		val xmlDoc: Document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse("https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/$glXmlCommit/xml/gl.xml")
				.apply { documentElement.normalize() }

		val registry = Registry(xmlDoc)

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
			val enumFile = FileSpec.builder("com.kgl.opengl.enums", group.name)

			val enumBuilder = TypeSpec.enumBuilder(group.name)

			val isBitmask = group.name in bitMasks

			enumBuilder.primaryConstructor(
					FunSpec.constructorBuilder().addParameter("value", INT).build()
			).addProperty(
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
				enumBuilder.addSuperinterface(glMaskClass.parameterizedBy(
						ClassName("com.kgl.opengl.enums", group.name)
				))
			}

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
			enumFile.build().writeTo(outputDir)
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

		val glFunctions = TypeSpec.classBuilder("GLFunctions")

		for (enum in registry.enums) {
			val UINT = ClassName("kotlin", "UInt")
			for ((name, _) in enum.entries) {
				if (name !in coreEnums) continue

				// Add KModifier.CONST once `toUInt()` is constexpr
				glFile.addProperty(PropertySpec.builder(name, UINT)
						.initializer("copengl.$name.toUInt()").build())
			}
		}

		for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val pfnType = "PFN${(command.alias ?: command.name).toUpperCase()}PROC"

			glFunctions.addProperty(
					PropertySpec.builder(
							command.name,
							ClassName("copengl", pfnType).copy(nullable = true)
					).initializer("Loader.kglGetProcAddress(%S)?.reinterpret()", command.name).build()
			)
		}

		glFile.addType(glFunctions.build())
		glFile.addProperty(PropertySpec.builder(
				"gl",
				ClassName("com.kgl.opengl", "GLFunctions"),
				KModifier.INTERNAL
		).delegate("lazy { GLFunctions() }")
				.addAnnotation(ClassName("kotlin.native.concurrent", "ThreadLocal"))
				.build())

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
					param.name.escapeKt() + "?.getPointer(VirtualStack)"
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
						CodeBlock.of("%T.push()\n", virtualStackClass),
						CodeBlock.of("%T.pop()\n", virtualStackClass)
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

		glFile.build().writeTo(outputDir)
	}

	private fun String.escapeKt(): String = when (this) {
		"val" -> "`val`"
		else -> this
	}

	private val Registry.Command.Param.isWritable: Boolean get() = type.asteriskCount > 0 && !type.isConst

	private class GLCallBuilder(private val command: Registry.Command) {
		private val arguments = mutableMapOf<String, String>()

		operator fun set(paramName: String, argument: String) {
			arguments[paramName] = argument
		}

		fun build(): String {
			return command.params.joinToString(", ",
					prefix = "gl.${command.name}!!(",
					postfix = ")"
			) {
				arguments[it.name] ?: "TODO()"
			}
		}
	}

	private fun CTypeDecl.toKtInteropParamType(): TypeName {
		return if (name == "void" && asteriskCount == 1) {
			if (isConst) {
				ClassName("kotlinx.cinterop", "CValuesRef")
						.parameterizedBy(STAR)
			} else {
				ClassName("kotlinx.cinterop", "COpaquePointer")
			}.copy(nullable = true)
		} else if (asteriskCount > 0) {
			ClassName("kotlinx.cinterop", if (isConst) "CValuesRef" else "CPointer")
					.parameterizedBy(glTypeToKtInteropType(name, asteriskCount - 1))
					.copy(nullable = true)
		} else {
			if (name == "GLsync") {
				ClassName("copengl", name).copy(nullable = true)
			} else {
				ClassName("copengl", name)
			}
		}
	}

	private fun CTypeDecl.toKtInteropType(): TypeName {
		return if (name == "void" && asteriskCount == 1) {
			ClassName("kotlinx.cinterop", "COpaquePointer")
					.copy(nullable = true)
		} else if (asteriskCount > 0) {
			ClassName("kotlinx.cinterop", "CPointer")
					.parameterizedBy(glTypeToKtInteropType(name, asteriskCount - 1))
					.copy(nullable = true)
		} else {
			if (name == "GLsync") {
				ClassName("copengl", name).copy(nullable = true)
			} else {
				ClassName("copengl", name)
			}
		}
	}

	private fun glTypeToKtInteropType(name: String, asteriskCount: Int): TypeName {
		return if (name == "void" && asteriskCount == 1) {
			ClassName("kotlinx.cinterop", "COpaquePointerVar")
		} else if (asteriskCount > 0) {
			ClassName("kotlinx.cinterop", "CPointerVar")
					.parameterizedBy(glTypeToKtInteropType(name, asteriskCount - 1))
		} else {
			ClassName("copengl", name + "Var")
		}
	}
}
