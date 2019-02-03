package opengl.kn

import com.squareup.kotlinpoet.*
import opengl.Registry
import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object OpenGLNativeGenerator {
	private val glXmlCommit = "89acc93eaa6acd97159fb069e66acb92f12d7b87"
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

		val availableEnumEntries = registry.features.filter {
			it.name == "GL_VERSION_1_0" || it.name == "GL_VERSION_1_1"
		}.asSequence().flatMap { it.requires.asSequence() }
				.flatMap { it.enums.asSequence() }.toSet()

		for (group in registry.groups) {
			val enumFile = FileSpec.builder("com.kgl.opengl.enums", group.name)

			val enumBuilder = TypeSpec.enumBuilder(group.name)

			enumBuilder.primaryConstructor(
					FunSpec.constructorBuilder().addParameter("value", INT).build()
			).addProperty(
					PropertySpec.builder("value", INT).initializer("value").build()
			)

			for (entry in group.enums) {
				if (entry !in coreEnums) continue
				if (entry.startsWith("GL_ALL_") && entry.endsWith("_BITS")) continue

				enumBuilder.addEnumConstant(
						entry.removePrefix("GL_"),
						TypeSpec.anonymousClassBuilder()
								.addSuperclassConstructorParameter(entry)
								.build()
				)

				if (entry in availableEnumEntries) {
					enumFile.addImport("platform.opengl32", entry)
				} else {
					enumFile.addImport("copengl", entry)
				}
			}

			enumFile.addType(enumBuilder.build())
			enumFile.build().writeTo(outputDir)
		}

		val glFile = FileSpec.builder("com.kgl.opengl", "GL")
				.addImport("kotlinx.cinterop", "addressOf")
				.addImport("kotlinx.cinterop", "cstr")
				.addImport("kotlinx.cinterop", "convert")
				.addImport("kotlinx.cinterop", "invoke")
				.addImport("kotlinx.cinterop", "pin")
				.addImport("kotlinx.cinterop", "reinterpret")
				.addImport("kotlinx.cinterop", "toBoolean")
				.addImport("kotlinx.cinterop", "toByte")
				.addImport("kotlinx.cinterop", "toCPointer")
				.addImport("kotlinx.cinterop", "toLong")
				.addImport("com.kgl.opengl.utils", "Loader")

		for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val pfnType = "PFN${(command.alias ?: command.name).toUpperCase()}PROC"

			glFile.addProperty(
					PropertySpec.builder(
							"kgl_${command.name}",
							ClassName("copengl", pfnType).copy(nullable = true),
							KModifier.INTERNAL
					).initializer("Loader.kglGetProcAddress(%S)?.reinterpret()", command.name).build()
			)
		}

		val existingGroups = registry.groups.map { it.name }.toSet()

		loop@for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val function = FunSpec.builder(command.name)

			val lengthParams = command.params.mapNotNull { it.len }.toSet()
			val commandArguments = mutableMapOf<String, String>()
			val tryFinallyBlocks = mutableListOf<Pair<CodeBlock, CodeBlock>>()
			val ioBufferDirectBlocks = mutableListOf<Pair<String, String>>()
			var requiresArena = false

			for (param in command.params) {
				if (param.name in lengthParams) {
					commandArguments[param.name] = "TODO()"
					continue
				}

				val isEnum = param.type.name == "GLenum" && param.group != null &&
						param.group in existingGroups

				when (param.type.asteriskCount) {
					0 -> {
						val typeKt = if (isEnum) {
							ClassName("com.kgl.opengl.enums", param.group!!)
						} else {
							primitiveTypes[param.type.name] ?: continue@loop
						}

						commandArguments[param.name] = when {
							isEnum -> "${param.name.escapeKt()}.value.toUInt()"
							typeKt.simpleName == "Boolean" -> "${param.name.escapeKt()}.toByte().toUByte()"
							param.type.name == "GLsync" -> "${param.name.escapeKt()}.toCPointer()"
							else -> param.name.escapeKt()
						}
						function.addParameter(param.name, typeKt)
					}
					1 -> {
						if (param.type.name == "void") {
							if (param.type.isConst) {
								ioBufferDirectBlocks += Pair(
										"${param.name.escapeKt()}.readDirect { kglPtr_${param.name} ->",
										"${param.name.escapeKt()}.readRemaining"
								)
								commandArguments[param.name] = "kglPtr_${param.name}"
								commandArguments[param.len ?: ""] = "${param.name.escapeKt()}.readRemaining.convert()"
								function.addParameter(param.name, ClassName("kotlinx.io.core", "IoBuffer"))
							} else {
								ioBufferDirectBlocks += Pair(
										"${param.name.escapeKt()}.writeDirect { kglPtr_${param.name} ->",
										"${param.name.escapeKt()}.writeRemaining"
								)
								commandArguments[param.name] = "kglPtr_${param.name}"
								commandArguments[param.len ?: ""] = "${param.name.escapeKt()}.writeRemaining.convert()"
								function.addParameter(param.name, ClassName("kotlinx.io.core", "IoBuffer"))
							}
						} else if (param.type.name == "GLchar") {
							if (param.type.isConst) {
								requiresArena = true
								commandArguments[param.name] = "${param.name.escapeKt()}.cstr.getPointer(kglArena)"
								commandArguments[param.len ?: ""] = "${param.name.escapeKt()}.length"
								function.addParameter(param.name, ClassName("kotlin", "String"))
							} else {
								continue@loop
							}
						} else {
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

							commandArguments[param.name] = "$pinnedName.addressOf(0)"
							commandArguments[param.len ?: continue@loop] = "${param.name.escapeKt()}.size"
							function.addParameter(param.name, arrayType)
						}
					}
					else -> continue@loop
				}
			}

			if (requiresArena) {
				tryFinallyBlocks += Pair(
						CodeBlock.of("val kglArena = %T()\n", ClassName("kotlinx.cinterop", "Arena")),
						CodeBlock.of("kglArena.clear()\n")
				)
			}

			tryFinallyBlocks.forEach { (pre, _) ->
				function.addCode(pre)
				function.beginControlFlow("try")
			}
			ioBufferDirectBlocks.forEach { function.beginControlFlow(it.first) }

			val commandCall = command.params.joinToString(", ", prefix = "kgl_${command.name}!!(", postfix = ")") {
				commandArguments[it.name]!!
			}

			val argumentsToCallCommand = command.params.map { commandArguments[it.name]!! }.toTypedArray()

			if (command.returnType.name == "void" && command.returnType.asteriskCount == 0) {
				function.returns(UNIT)
				function.addStatement(commandCall, *argumentsToCallCommand)
			} else {
				when (command.returnType.asteriskCount) {
					0 -> {
						val returnTypeKt = primitiveTypes[command.returnType.name] ?: continue@loop
						function.returns(returnTypeKt)

						function.addStatement(
								when {
									returnTypeKt.simpleName == "Boolean" -> "return $commandCall.toByte().toBoolean()"
									command.returnType.name == "GLsync" -> "return $commandCall.toLong()"
									else -> "return $commandCall"
								},
								*argumentsToCallCommand
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
}
