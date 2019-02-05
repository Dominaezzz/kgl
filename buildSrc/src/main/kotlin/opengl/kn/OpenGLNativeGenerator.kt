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

		for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val pfnType = "PFN${(command.alias ?: command.name).toUpperCase()}PROC"

			glFunctions.addProperty(
					PropertySpec.builder(
							command.name,
							ClassName("copengl", pfnType).copy(nullable = true),
							KModifier.PRIVATE
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

		val existingGroups = registry.groups.map { it.name }.toSet()

		loop@for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val function = FunSpec.builder(command.name)
			val mainFunctionBody = CodeBlock.builder()

			val callBuilder = GLCallBuilder(command)

			val tryFinallyBlocks = mutableListOf<Pair<CodeBlock, CodeBlock>>()
			val ioBufferDirectBlocks = mutableListOf<Pair<String, String>>()
			var requiresArena = false

			val lengthParams = command.params.mapNotNull { it.len }
					.filter { len -> len.all { it.isLetterOrDigit() } }.toSet()
			val outputParams = command.params.takeLastWhile { it.isWritable }
			val inputParams = command.params.dropLast(outputParams.size)

			for (param in inputParams.filter { it.name !in lengthParams }) {
				val isEnum = param.type.name == "GLenum" && param.group != null &&
						param.group in existingGroups
				val isMask = param.type.name == "GLbitfield" && param.group != null &&
						param.group in existingGroups

				when (param.type.asteriskCount) {
					0 -> {
						val typeKt = if (isEnum) {
							ClassName("com.kgl.opengl.enums", param.group!!)
						} else if (isMask){
							glMaskClass.parameterizedBy(ClassName("com.kgl.opengl.enums", param.group!!))
						} else {
							primitiveTypes[param.type.name] ?: continue@loop
						}

						callBuilder[param.name] = when {
							isEnum || isMask -> "${param.name.escapeKt()}.value.toUInt()"
							typeKt is ClassName && typeKt.simpleName == "Boolean" -> "${param.name.escapeKt()}.toByte().toUByte()"
							param.type.name == "GLsync" -> "${param.name.escapeKt()}.toCPointer()"
							else -> param.name.escapeKt()
						}
						function.addParameter(param.name, typeKt)
					}
					1 -> {
						when (param.type.name) {
							"void" -> {
								// If length is clearly given.
								if (param.len in lengthParams) {
									ioBufferDirectBlocks += Pair(
											"${param.name.escapeKt()}.readDirect { kglPtr_${param.name} ->",
											"${param.name.escapeKt()}.readRemaining"
									)
									callBuilder[param.name] = "kglPtr_${param.name}"
									callBuilder[param.len!!] = "${param.name.escapeKt()}.readRemaining.convert()"
									function.addParameter(param.name, ClassName("kotlinx.io.core", "IoBuffer"))
								} else {
									callBuilder[param.name] = param.name.escapeKt() + ".toCPointer()"
									function.addParameter(param.name, LONG)
								}
							 }
							"GLchar" -> {
								requiresArena = true
								callBuilder[param.name] = "${param.name.escapeKt()}.cstr.getPointer(VirtualStack)"
								callBuilder[param.len ?: ""] = "${param.name.escapeKt()}.length"
								function.addParameter(param.name, ClassName("kotlin", "String"))
							}
							else -> {
								val baseType = primitiveTypes[param.type.name] ?: continue@loop

								val arrayType = if (baseType.simpleName == "Boolean") {
									ClassName("kotlin", "UByteArray")
								} else {
									ClassName("kotlin", baseType.simpleName + "Array")
								}
								val pinnedName = "kglpin_${param.name}"
								tryFinallyBlocks += Pair(
										CodeBlock.of("val $pinnedName = ${param.name.escapeKt()}.pin()\n"),
										CodeBlock.of("$pinnedName.unpin()\n")
								)

								callBuilder[param.name] = "$pinnedName.addressOf(0)"
								callBuilder[param.len ?: ""] = "${param.name.escapeKt()}.size"
								function.addParameter(param.name, arrayType)
							}
						}
					}
					2 -> {
						if (param.type.name == "GLchar") {
							requiresArena = true
							callBuilder[param.name] = "${param.name.escapeKt()}.toCStringArray(VirtualStack)"
							callBuilder[param.len ?: continue@loop] = "${param.name.escapeKt()}.size"
							function.addParameter(
									param.name,
									ClassName("kotlin", "Array")
											.parameterizedBy(ClassName("kotlin", "String"))
							)
						} else {
							continue@loop
						}
					}
					else -> continue@loop
				}
			}

			val visibleOutputParams = outputParams.filter { it.name !in lengthParams }
			if (outputParams.size > 1) continue@loop

			if (command.returnType.name == "void" && command.returnType.asteriskCount == 0) {
				when (visibleOutputParams.size) {
					0 -> {
						function.returns(UNIT)
						mainFunctionBody.addStatement(callBuilder.build())
					}
					1 -> {
						val param = visibleOutputParams.singleOrNull() ?: continue@loop

						when (param.type.asteriskCount) {
							0 -> TODO("Output param must be pointer")
							1 -> {
								when (param.type.name) {
									"void" -> {
										ioBufferDirectBlocks += Pair(
												"${param.name.escapeKt()}.writeDirect { kglPtr_${param.name} ->",
												"${param.name.escapeKt()}.writeRemaining"
										)
										callBuilder[param.name] = "kglPtr_${param.name}"
										callBuilder[param.len ?: ""] = "${param.name.escapeKt()}.writeRemaining.convert()"
										function.addParameter(param.name, ClassName("kotlinx.io.core", "IoBuffer"))
										function.returns(UNIT)
										mainFunctionBody.addStatement(callBuilder.build())
									}
									"GLchar" -> {
										// TODO
										continue@loop
									}
									else -> {
										val baseType = primitiveTypes[param.type.name] ?: continue@loop

										if (param.len != null) {
											val arrayType = if (baseType.simpleName == "Boolean") {
												ClassName("kotlin", "UByteArray")
											} else {
												ClassName("kotlin", baseType.simpleName + "Array")
											}
											val pinnedName = "kglpin_${param.name}"
											tryFinallyBlocks += Pair(
													CodeBlock.of("val $pinnedName = ${param.name.escapeKt()}.pin()\n"),
													CodeBlock.of("$pinnedName.unpin()\n")
											)

											callBuilder[param.name] = "$pinnedName.addressOf(0)"
											callBuilder[param.len] = "${param.name.escapeKt()}.size"
											function.addParameter(param.name, arrayType)

											function.returns(UNIT)
											mainFunctionBody.addStatement(callBuilder.build())
										} else {
											function.returns(baseType)

											val glType = ClassName("copengl", param.type.name + "Var")

											requiresArena = true
											mainFunctionBody.addStatement("val output = %T.alloc<%T>()", virtualStackClass, glType)
											callBuilder[param.name] = "output.ptr"

											mainFunctionBody.addStatement(callBuilder.build())
											mainFunctionBody.addStatement("return output.value")
										}
									}
								}
							}
							2 -> {
								if (param.type.name == "GLchar") {
									// TODO
									continue@loop
								} else {
									// TODO
									continue@loop
								}
							}
							else -> TODO("asteriskCount greater than 2 is not practical?")
						}
					}
					else -> continue@loop
				}
			} else {
				if (outputParams.isNotEmpty()) TODO("${command.name} Has output param and return type.")

				val commandCall = callBuilder.build()

				when (command.returnType.asteriskCount) {
					0 -> {
						val returnTypeKt = primitiveTypes[command.returnType.name] ?: continue@loop
						function.returns(returnTypeKt)

						mainFunctionBody.addStatement(
								when {
									returnTypeKt.simpleName == "Boolean" -> "return $commandCall.toByte().toBoolean()"
									command.returnType.name == "GLsync" -> "return $commandCall.toLong()"
									else -> "return $commandCall"
								}
						)
					}
					1 -> {
						// TODO
						continue@loop
					}
					// TODO
					else -> continue@loop
				}
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
}
