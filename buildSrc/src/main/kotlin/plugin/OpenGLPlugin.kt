package plugin

import com.squareup.kotlinpoet.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

class OpenGLPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		val outputDir = target.buildDir.resolve("generated-src")

		val mingwDir = outputDir.resolve("mingw")

		// TODO: Should be downloaded from https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/master/xml/gl.xml
		val xmlFile = target.rootProject.file("buildSrc/src/main/resources/gl.xml")

		val xmlDoc: Document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(xmlFile).apply { documentElement.normalize() }

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
			enumFile.build().writeTo(mingwDir)
		}

		val availableCommands = registry.features.filter {
			it.name == "GL_VERSION_1_0" || it.name == "GL_VERSION_1_1"
		}.asSequence().flatMap { it.requires.asSequence() }
				.flatMap { it.commands.asSequence() }.toSet()

		val glFile = FileSpec.builder("com.kgl.opengl", "GL")
				//.addImport("kotlinx.cinterop", "invoke")
				.addImport("kotlinx.cinterop", "reinterpret")
				.addImport("com.kgl.opengl.utils", "Loader")

		for (command in registry.commands) {
			if (command.name !in coreCommands) continue

			val pfnType = "PFN${(command.alias ?: command.name).toUpperCase()}PROC"

			glFile.addProperty(
					PropertySpec.builder(
							"kgl_${command.name}",
							ClassName(
									"copengl",
//									if (command.name in availableCommands) "platform.opengl32" else "copengl",
									pfnType
							).copy(nullable = true),
							KModifier.INTERNAL
					).initializer("Loader.kglGetProcAddress(%S)?.reinterpret()", command.name).build()
			)
		}

		glFile.build().writeTo(mingwDir)
	}
}

class Registry(glXml: Document) {
	val root = glXml.getChild("registry")

	val groups = root.getChild("groups")
			.getChildren("group")
			.map { group ->
				Group(
						group.getAttribute("name"),
						group.getChildren("enum").map { it.getAttribute("name") }
								.toList()
				)
			}
			.toList()

	val enums = root.getChildren("enums")
			.map { enumsNode ->
				Enum(
						enumsNode.getAttribute("namespace"),
						enumsNode.getAttribute("group"),
						enumsNode.getAttribute("type"),
						enumsNode.getAttribute("vendor").takeIf { it.isNotBlank() },
						enumsNode.getChildren("enum").associate {
							it.getAttribute("name") to it.getAttribute("value")
						}
				)
			}
			.toList()

	val commands = root.getChild("commands")
			.getChildren("command")
			.map { commandNode ->
				val proto = commandNode.getChild("proto")

				val (returnType, funName) = definitionRegex.matchEntire(proto.textContent)!!.destructured

				val params = mutableListOf<Command.Param>()
				for (paramNode in commandNode.getChildren("param")) {
					val name = paramNode.getChild("name").textContent.trim()
					val pType = paramNode.getChildren("ptype").singleOrNull()?.textContent?.trim() ?: "void"

					val type = toType(paramNode.textContent, pType, name)

					params += Command.Param(
							type, name,
							paramNode.getAttribute("group").takeIf { it.isNotBlank() },
							paramNode.getAttribute("len").takeIf { it.isNotBlank() }
					)
				}

				Command(
						returnType.trim(),
						funName,
						commandNode.getChildren("alias").singleOrNull()?.getAttribute("name"),
						params
				)
			}
			.toList()

	val features = root.getChildren("feature")
			.map { feature ->
				Feature(
						feature.getAttribute("api"),
						feature.getAttribute("name"),
						feature.getAttribute("number"),
						feature.getChildren("require").map { require ->
							Require(
									require.getAttribute("profile").takeIf { it.isNotBlank() },
									require.getChildren("enum").map { it.getAttribute("name") }.toSet(),
									require.getChildren("command").map { it.getAttribute("name") }.toSet()
							)
						}.toList(),
						feature.getChildren("remove").map { remove ->
							Remove(
									remove.getAttribute("profile").takeIf { it.isNotBlank() },
									remove.getChildren("enum").map { it.getAttribute("name") }.toSet(),
									remove.getChildren("command").map { it.getAttribute("name") }.toSet()
							)
						}.toList()
				)
			}
			.toList()

	val extensions = root.getChild("extensions").getChildren("extension")
			.map { extension ->
				Extension(
						extension.getAttribute("name"),
						extension.getAttribute("supported").split("|").toSet(),
						extension.getChildren("require").map { require ->
							Require(
									require.getAttribute("profile").takeIf { it.isNotBlank() },
									require.getChildren("enum").map { it.getAttribute("name") }.toSet(),
									require.getChildren("command").map { it.getAttribute("name") }.toSet()
							)
						}.toList()
				)
			}
			.toList()

	data class Group(val name: String, val enums: List<String>)
	data class Enum(
			val namespace: String,
			val group: String,
			val type: String,
			val vendor: String?,
			val entries: Map<String, String>
	)

	data class Command(val returnType: String, val name: String, val alias: String?, val params: List<Param>) {
		data class Param(
				val type: CTypeDecl,
				val name: String,
				val group: String?,
				val len: String?
		) {
			override fun toString() = buildString {
				append(name)
//				if (count != null) {
//					append('[')
//					append(count)
//					append(']')
//				}
				append(": ")
				append(type)
			}
		}

		override fun toString() = "fun $name(${params.joinToString(", ")}): $returnType"
	}

	data class Feature(val api: String, val name: String, val number: String, val requires: List<Require>, val removes: List<Remove>)
	data class Extension(val name: String, val supported: Set<String>, val requires: List<Require>)
	data class Require(val profile: String?, val enums: Set<String>, val commands: Set<String>)
	data class Remove(val profile: String?, val enums: Set<String>, val commands: Set<String>)
}

data class CTypeDecl(val name: String, val isConst: Boolean, val asteriskCount: Int, val count: String) {
	override fun toString(): String {
		return buildString {
			if (isConst) append("const ")
			append(name)
			repeat(asteriskCount) {
				append('*')
			}
			if (count.isNotBlank()) {
				append('[')
				append(count)
				append(']')
			}
		}
	}
}

fun getTypeDeclRegex(type: String, name: String) : Regex {
	return Regex("""^(const )?(?:struct )?$type\s*(\*(?:(?:\s?const)?\*)?)?\s*$name(?:\[([0-9]+|[A-Z_]+)])?$""")
}

fun toType(typeDecl: String, type: String, name: String): CTypeDecl {
	val (const, stars, count) = getTypeDeclRegex(type, name).matchEntire(typeDecl)!!.destructured
	return CTypeDecl(type, const.isNotBlank(), stars.count { it == '*' }, count)
}

val definitionRegex = """^(?:const )?((?:struct )?[_A-Za-z0-9\s]+ (?:\*(?:const)?)*)([A-Za-z0-9_]+(?:\[([0-9]+)])?)$""".toRegex()

fun Node.getChildren(name: String): Sequence<Element> {
	return childNodes.run { (0 until length).asSequence()
			.map { item(it) } }
			.filter { it.nodeName == name }
			.filter { it.nodeType == Node.ELEMENT_NODE }
			.map { it as Element }
}

fun Node.getChild(name: String) = getChildren(name).single()
