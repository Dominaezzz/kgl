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
package codegen.vulkan

import codegen.*
import com.squareup.kotlinpoet.*
import org.w3c.dom.Document
import org.w3c.dom.Element

class Registry(vkXML: Document) {
	val root = vkXML.getChild("registry")

	val platforms = root.getChild("platforms")
			.getChildren("platform")
			.map {
				Platform(
						it.getAttribute("name"),
						it.getAttribute("protect"),
						it.getAttribute("comment")
				)
			}.toList()

	val tags = root.getChild("tags")
			.getChildren("tag")
			.map { tagNode ->
				Tag(
						tagNode.getAttribute("name"),
						tagNode.getAttribute("author"),
						tagNode.getAttribute("contact").split(", ").filter { it.isNotBlank() }
				)
			}.toList()

	val typeNodes = root.getChild("types").getChildren("type").toList()

	val flags = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "bitmask" }
			.filterNot { it.hasAttribute("alias") }
			.map { type ->
				VkFlag(
						type.getChild("name").textContent,
						type.getAttribute("requires").takeIf { type.hasAttribute("requires") }
				)
			}

	val flagAliases = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "bitmask" }
			.filter { it.hasAttribute("alias") }
			.associate {
				it.getAttribute("alias")!! to it.getAttribute("name")!!
			}

	val handles = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "handle" }
			.filter { !it.hasAttribute("alias") }
			.map {
				VkHandle(
						it.getChild("name").textContent,
						it.getChild("type").textContent == "VK_DEFINE_NON_DISPATCHABLE_HANDLE",
						it.getAttribute("parent").split(",").filter { it.isNotBlank() }
				)
			}
			.toList()

	val handleAliases = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "handle" }
			.filter { it.hasAttribute("alias") }
			.associate {
				it.getAttribute("alias")!! to it.getAttribute("name")!!
			}

	val funcPointers = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "funcpointer" }
			.map { funcPointerNode ->
				VkFuncPointer(
						funcPointerNode.getChild("name").textContent,
						run {
							val temp = funcPointerNode.textContent
							val typeDecl = temp.substring("typedef".length, temp.indexOf('(')).trim()
							toType(typeDecl, "[A-Za-z0-9_]+", "")
						},
						funcPointerNode.textContent.run { substring(indexOf(")(") + 2, lastIndexOf(')')) }
								.trim('(', ')')
								.splitToSequence(',')
								.map { it.trim() }
								.map {
									VkFuncPointer.Param(
											it.takeLastWhile { it.isJavaIdentifierPart() },
											toType(it, "[A-Za-z0-9_]+", "[A-Za-z0-9_]+")
									)
								}
								.toList()
				)
			}

	val structs = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "struct" }
			.filterNot { it.hasAttribute("alias") }
			.map { structNode ->
				VkStruct(
						structNode.getAttribute("name"),
						structNode.getAttribute("returnedonly") == "true",
						structNode.getAttribute("structextends")
								.split(",").filter { it.isNotBlank() },
						structNode.getChildren("member").map { memberNode ->
							val typeDecl = (memberNode.cloneNode(true) as Element).run {
								val comment = getChildren("comment").singleOrNull()
								if (comment != null) {
									removeChild(comment)
								}
								textContent.trim()
							}

							val name = memberNode.getChild("name").textContent
							val type = memberNode.getChild("type").textContent

							VkStruct.Member(
									name,
									toType(typeDecl, type, name),
									memberNode.getAttribute("len").splitToSequence(',').filter { it.isNotBlank() }.toList(),
									memberNode.getAttribute("optional") == "true",
									memberNode.getAttribute("externsync") == "true",
									memberNode.getChildren("comment").singleOrNull()?.textContent,
									memberNode.getAttribute("values").split(",")
							)
						}.toList()
				)
			}
			.toList()

	val structAliases = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "struct" }
			.filter { it.hasAttribute("alias") }
			.associate { it.getAttribute("name")!! to it.getAttribute("alias")!! }

	val unions = typeNodes.asSequence()
			.filter { it.getAttribute("category") == "union" }
			.filterNot { it.hasAttribute("alias") }
			.map { structNode ->
				VkUnion(
						structNode.getAttribute("name"),
						structNode.getChildren("member").map { memberNode ->
							val typeDecl = (memberNode.cloneNode(true) as Element).run {
								val comment = getChildren("comment").singleOrNull()
								if (comment != null) {
									removeChild(comment)
								}
								textContent.trim()
							}

							val name = memberNode.getChild("name").textContent
							val type = memberNode.getChild("type").textContent

							VkUnion.Member(
									name,
									toType(typeDecl, type, name),
									memberNode.getChildren("comment").singleOrNull()?.textContent
							)
						}.toList()
				)
			}
			.toList()

	val constants = root.getChildren("enums")
			.single { it.getAttribute("name") == "API Constants" }
			.getChildren("enum").map {
				Constant(
						it.getAttribute("name"),
						it.getAttribute("value").takeIf { it.isNotBlank() } ?: it.getAttribute("alias")
				)
			}.toList()

	val enums = root.getChildren("enums")
			.filter { it.hasAttribute("type") }
			.map { enumsNode ->
				VkEnum(
						enumsNode.getAttribute("name"),
						when (enumsNode.getAttribute("type")) {
							"enum" -> false
							"bitmask" -> true
							else -> TODO()
						},
						enumsNode.getChildren("enum")
								.filter { !it.hasAttribute("alias") }
								.map {
									VkEnum.Entry(
											it.getAttribute("name"),
											if (it.hasAttribute("value")) Integer.decode(it.getAttribute("value")) else null,
											if (it.hasAttribute("bitpos")) it.getAttribute("bitpos").toInt() else null,
											if (it.hasAttribute("comment")) it.getAttribute("comment") else null
									)
								}.toList(),
						enumsNode.getChildren("enum")
								.filter { it.hasAttribute("alias") }
								.map {
									it.getAttribute("name") to it.getAttribute("alias")
								}.toList()
				)
			}
			.toList()

	val commands = root.getChild("commands").getChildren("command")
			.filterNot { it.hasAttribute("alias") }
			.map { commandNode ->
				val protoNode = commandNode.getChild("proto")
				val name = protoNode.getChild("name").textContent
				val type = protoNode.getChild("type").textContent

				VkCommand(
						name,
						commandNode.getAttribute("successcodes").split(",").filter { it.isNotBlank() },
						commandNode.getAttribute("errorcodes").split(",").filter { it.isNotBlank() },
						commandNode.getAttribute("queues").split(",").filter { it.isNotBlank() }.toSet(),
						commandNode.getAttribute("renderpass").takeIf { it.isNotBlank() },
						commandNode.getAttribute("cmdbufferlevel").split(",").filter { it.isNotBlank() }.toSet(),
						commandNode.getAttribute("pipeline").takeIf { it.isNotBlank() },
						toType(protoNode.textContent, type, name),
						commandNode.getChildren("param").map { paramNode ->
							val paramName = paramNode.getChild("name").textContent
							val paramType = paramNode.getChild("type").textContent

							VkCommand.Param(
									paramName,
									toType(paramNode.textContent, paramType, paramName),
									paramNode.getAttribute("len").split(',').filter { it.isNotBlank() },
									paramNode.getAttribute("optional") == "true",
									paramNode.getAttribute("externsync").split(',').filter { it.isNotBlank() }.toSet()
							)
						}.toList(),
						commandNode.getAttribute("comment").takeIf { it.isNotBlank() }
				)
			}
			.toList()

	val features = root.getChildren("feature").map { featureNode ->
		Feature(
				featureNode.getAttribute("name"),
				featureNode.getAttribute("number"),
				featureNode.getAttribute("comment"),
				featureNode.getChildren("require").map { requireNode ->
					Require(
							requireNode.getChildren("enum")
									.filter { it.hasAttribute("extends") }
									.filterNot { it.hasAttribute("alias") }
									.map {
										Enum(
												it.getAttribute("name"),
												it.getAttribute("extends"),
												it.getAttribute("offset").toIntOrNull() ?: it.getAttribute("bitpos").toInt()
										)
									}.toList(),
							requireNode.getChildren("enum")
									.filter { !it.hasAttribute("extends") }
									.associate { it.getAttribute("name") to it.getAttribute("value").takeIf { it.isNotBlank() } },
							requireNode.getChildren("type").map { it.getAttribute("name") }.toSet(),
							requireNode.getChildren("command").map { it.getAttribute("name") }.toSet()
					)
				}.toList()
		)
	}.toList()

	val extensions = root.getChild("extensions").getChildren("extension")
			.map { extNode ->
				Extension(
						extNode.getAttribute("name"),
						extNode.getAttribute("author"),
						extNode.getAttribute("contact").split(",").filter { it.isNotBlank() },
						extNode.getChildren("require").map { requireNode ->
							Require(
									requireNode.getChildren("enum").filter { it.hasAttribute("extends") && !it.hasAttribute("alias") }.map {
										Enum(
												it.getAttribute("name"),
												it.getAttribute("extends"),
												it.getAttribute("value").toIntOrNull() ?: it.getAttribute("offset").toIntOrNull() ?: it.getAttribute("bitpos").toInt()
										)
									}.toList(),
									requireNode.getChildren("enum")
											.filter { !it.hasAttribute("extends") }
											.associate { it.getAttribute("name") to it.getAttribute("value").takeIf { it.isNotBlank() } },
									requireNode.getChildren("type").map { it.getAttribute("name") }.toSet(),
									requireNode.getChildren("command").map { it.getAttribute("name") }.toSet()
							)
						}.toList(),
						extNode.getAttribute("platform").takeIf { it.isNotBlank() },
						extNode.getAttribute("supported")
				)
			}.toList()

	data class Platform(val name: String, val protect: String, val comment: String)
	data class Tag(val name: String, val author: String, val contacts: List<String>)
	data class Constant(val name: String, val value: String) // Value might be another constant (alias).
	data class Feature(val name: String, val number: String, val comment: String, val requires: List<Require>)
	data class Extension(val name: String, val author: String, val contacts: List<String>, val requires: List<Require>, val platform: String?, val supported: String)
	data class Require(
			/**
			 * Map of enum entry to name of extended enum.
			 * */
			val enums: List<Enum>,
			val constants: Map<String, String?>,
			val types: Set<String>,
			val commands: Set<String>
	)
	data class Enum(val name: String, val extends: String, val offsetOrBitPos: Int)
}

interface IVkParam {
	val name: String
	val type: CTypeDecl
	val len: List<String>
	val optional: Boolean
}

sealed class VkType(val name: String)

class VkEnum(name: String, val isBitMask: Boolean, val entries: List<Entry>, val aliases: List<Pair<String, String>>) : VkType(name) {
	data class Entry(val name: String, val value: Int?, val bitPosition: Int?, val comment: String?)
}

class VkFlag(name: String, val requires: String?) : VkType(name)

class VkHandle(name: String, val isNonDispatchable: Boolean, val parents: List<String>) : VkType(name)

class VkFuncPointer(name: String, val returnType: CTypeDecl, val params: List<VkFuncPointer.Param>) : VkType(name) {
	data class Param(override val name: String, override val type: CTypeDecl) : IVkParam {
		override val len: List<String> get() = emptyList()
		override val optional: Boolean get() = false
	}
}

class VkPrimitive(
		nameVk: String,
		val nameClass: ClassName,
		val defaultValueJVM: String,
		val defaultValueNative: String = defaultValueJVM,
		val toJVMVkType: String = "",
		val fromJVMVkType: String = "",
		val toNativeVkType: String = "",
		val fromNativeVkType: String = ""
) : VkType(nameVk)

class VkStruct(name: String, val returnedOnly: Boolean, val structExtends: List<String>, val members: List<Member>) : VkType(name) {
	data class Member(
			override val name: String,
			override val type: CTypeDecl,
			override val len: List<String>,
			override val optional: Boolean,
			val externSync: Boolean,
			val comment: String?,
			val values: List<String>
	) : IVkParam
}

class VkUnion(name: String, val members: List<Member>) : VkType(name) {
	data class Member(
			val name: String,
			val type: CTypeDecl,
			val comment: String?
	)// : IVkParam
}

data class VkCommand(
		val name: String,
		val successCodes: List<String>,
		val errorCodes: List<String>,
		val queues: Set<String>,
		val renderPass: String?,
		val cmdBufferLevel: Set<String>,
		val pipeline: String?,
		val returnType: CTypeDecl,
		val params: List<Param>,
		val comment: String?
) {
	data class Param(
			override val name: String,
			override val type: CTypeDecl,
			override val len: List<String>,
			override val optional: Boolean,
			val externSync: Set<String>
	) : IVkParam
}

const val NULL_TERMINATED = "null-terminated"

val primitives = listOf(
		VkPrimitive("char", BYTE, "0.toByte()"),
		VkPrimitive("int", INT, "0"),
		VkPrimitive("int32_t", INT, "0"),
		VkPrimitive("uint8_t", U_BYTE, "0.toByte()", "0.toUByte()", "toByte()", "toUByte()"),
		VkPrimitive("uint16_t", U_SHORT, "0.toShort()", "0.toUShort()", "toShort()", "toUShort()"),
		VkPrimitive("uint32_t", U_INT, "0", "0U", "toInt()", "toUInt()"),
		VkPrimitive("uint64_t", U_LONG, "0L", "0UL", "toLong()", "toULong()"),
		VkPrimitive("VkBool", BOOLEAN, "false", toNativeVkType = "toVkBool()", fromNativeVkType = "toBoolean()"),
		VkPrimitive("VkBool32", BOOLEAN, "false", toNativeVkType = "toVkBool()", fromNativeVkType = "toBoolean()"),
		VkPrimitive("float", FLOAT, "0f"),
		VkPrimitive("void", UNIT, "Unit"),
		VkPrimitive("size_t", U_LONG, "0L", "0UL", "toLong()", "toULong()"),
		VkPrimitive("VkDeviceSize", U_LONG, "0L", "0UL", "toLong()", "toULong()"),
		VkPrimitive("VkSampleMask", U_INT, "0", "0U", "toInt()", "toUInt()")
)
