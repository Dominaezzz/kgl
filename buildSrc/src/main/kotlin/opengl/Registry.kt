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
package opengl

import opengl.kn.CTypeDecl
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node

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

				val funName = proto.getChild("name").textContent

				val params = commandNode.getChildren("param").map { paramNode ->
					val name = paramNode.getChild("name").textContent.trim()
					val pType = paramNode.getChildren("ptype").singleOrNull()?.textContent?.trim() ?: "void"

					val type = toType(paramNode.textContent, pType, name)

					Command.Param(
							type, name,
							paramNode.getAttribute("group").takeIf { it.isNotBlank() },
							paramNode.getAttribute("len").takeIf { it.isNotBlank() }
					)
				}.toList()

				Command(
						toType(proto.textContent, "[A-Z0-9a-z]+", funName),
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

	data class Command(val returnType: CTypeDecl, val name: String, val alias: String?, val params: List<Param>) {
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

	data class Feature(
			val api: String,
			val name: String,
			val number: String,
			val requires: List<Require>,
			val removes: List<Remove>
	)

	data class Extension(val name: String, val supported: Set<String>, val requires: List<Require>)
	data class Require(val profile: String?, val enums: Set<String>, val commands: Set<String>)
	data class Remove(val profile: String?, val enums: Set<String>, val commands: Set<String>)


	companion object {
		fun getTypeDeclRegex(type: String, name: String) : Regex {
			return Regex("""^(const )?(?:struct )?($type)\s*(\*(?:(?:\s?const)?\*)?)?\s*$name(?:\[([0-9]+|[A-Z_]+)])?$""")
		}

		fun toType(typeDecl: String, type: String, name: String): CTypeDecl {
			val (const, actualType, stars, count) = getTypeDeclRegex(type, name).matchEntire(typeDecl)!!.destructured
			return CTypeDecl(actualType, const.isNotBlank(), stars.count { it == '*' }, count)
		}

		fun Node.getChildren(name: String): Sequence<Element> {
			return childNodes.run {
				(0 until length).asSequence()
						.map { item(it) }
			}
					.filter { it.nodeName == name }
					.filter { it.nodeType == Node.ELEMENT_NODE }
					.map { it as Element }
		}

		fun Node.getChild(name: String) = getChildren(name).single()
	}
}
