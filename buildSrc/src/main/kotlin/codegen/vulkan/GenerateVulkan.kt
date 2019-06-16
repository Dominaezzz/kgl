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
import codegen.STRING
import codegen.UINT
import codegen.ULONG
import com.beust.klaxon.JsonArray
import com.beust.klaxon.Parser
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.lang.Exception
import javax.xml.parsers.DocumentBuilderFactory

open class GenerateVulkan : DefaultTask() {
	@InputDirectory
	val docsDir = project.objects.directoryProperty()

	@InputFile
	val registryFile = docsDir.file("xml/vk.xml")

	@OutputDirectory
	val outputDir = project.objects.directoryProperty()

	@OutputDirectory
	val commonDir = outputDir.dir("common")

	@OutputDirectory
	val jvmDir = outputDir.dir("jvm")

	@OutputDirectory
	val nativeDir = outputDir.dir("native")

	private val hiddenEntries: Set<String> by lazy {
		(Parser.default()
				.parse(this::class.java.getResourceAsStream("/vk_hidden_entries.json")) as JsonArray<*>)
				.map { it as String }.toSet()
		setOf()
	}

	@TaskAction
	fun generate() {
		// Clear output directory.
		outputDir.get().asFile.deleteRecursively()

		val registry = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(registryFile.get().asFile)
				.let {
					it.documentElement.normalize()
					Registry(it)
				}

		val apiSpecDoc = Jsoup.parse(docsDir.file("out/apispec.html").get().asFile, Charsets.UTF_8.name())

		val vkTypeMap = (registry.enums + registry.structs + registry.handles + registry.funcPointers + registry.flags + primitives)
				.associateBy { it.name }

		// i.e VkInstance -> com.kgl.vulkan.handles.Instance
		val kglClassMap: Map<String, TypeName> = buildMap {
			for (handle in registry.handles) {
				put(handle.name, ClassName("com.kgl.vulkan.handles", handle.name.removePrefix("Vk")))
			}
			for (primitive in primitives) {
				put(primitive.name, primitive.nameClass)
			}

			val outputStructs = mutableSetOf<VkStruct>()
			fun crawlOutputStruct(struct: VkStruct) {
				outputStructs += struct
				for (member in struct.members) {
					val type = vkTypeMap[member.type.name]
					if (type is VkStruct) {
						crawlOutputStruct(type)
					}
				}
			}
			registry.commands.asSequence()
					.flatMap { command -> command.params.takeLastWhile { it.type.isWritable }.asSequence() }
					.map { vkTypeMap[it.type.name] }
					.plus(registry.funcPointers.asSequence()
							.flatMap { it.params.asSequence() }
							.map { vkTypeMap[it.type.name] })
					.filterIsInstance<VkStruct>()
					.forEach { crawlOutputStruct(it) }

			for (struct in outputStructs) {
				put(struct.name, ClassName("com.kgl.vulkan.structs", struct.name.removePrefix("Vk")))
			}
			for (enum in registry.enums) {
				put(enum.name, ClassName("com.kgl.vulkan.enums", if (enum.isBitMask) {
					val enumName = enum.name.removePrefix("Vk").replace("FlagBits", "")
					if (containsKey("Vk$enumName")) {
						enumName + "Flag"
					} else {
						enumName
					}
				} else {
					enum.name.removePrefix("Vk")
				}))
			}
			for (flag in registry.flags) {
				if (flag.requires != null) {
					put(flag.name, VK_FLAG.parameterizedBy(getValue(flag.requires)))
				}
			}
		}

		val extensionInvMap: Map<String, Registry.Extension> = buildMap {
			val commandLookup = registry.commands.associateBy { it.name }

			for (extension in registry.extensions.filter { it.name.startsWith("VK") }) {
				val tagName = extension.name.splitToSequence('_').elementAt(1)

				fun crawl(member: IVkParam) {
					val vkType = vkTypeMap[member.type.name] ?: return

					if (vkType.name.endsWith(tagName)) {
						putIfAbsent(vkType.name, extension)
						if (vkType is VkFlag && vkType.requires != null) {
							putIfAbsent(vkType.requires, extension)
						}
					} else if (vkType is VkEnum && member is VkStruct.Member && vkType.name == "VkStructureType") {
						member.values.filter { it.endsWith(tagName) }.forEach {
							putIfAbsent(it, extension)
						}
					}
					if (vkType is VkStruct) {
						vkType.members.forEach(::crawl)
					} else if (vkType is VkFuncPointer) {
						vkType.params.forEach(::crawl)
					}
				}

				for (require in extension.requires) {
					require.commands.forEach { putIfAbsent(it, extension) }
					require.types.forEach { putIfAbsent(it, extension) }
					require.constants.keys.forEach { putIfAbsent(it, extension) }

					require.commands.asSequence()
							.map { commandLookup[it] }
							.filterNotNull()
							.flatMap { it.params.asSequence() }
							.forEach(::crawl)

					require.types.asSequence().map { vkTypeMap[it] }
							.filterIsInstance<VkStruct>()
							.flatMap { it.members.asSequence() }
							.forEach(::crawl)
				}
			}
		}

		fun Registry.Extension.getLWJGLClass(): ClassName {
			return ClassName(LWJGLVulkanPackage, buildString {
				val temp = name.removePrefix("VK_")
				val tag = temp.takeWhile { it != '_' }
				append(tag)
				append(temp.removePrefix("${tag}_").snakeToPascalCase())
			})
		}

		fun Element.toKDocText(): String {
			val element = this.clone()

			if (element.tagName() == "table") {
				val columnCount = element.select("colgroup > col").count()
				val headers = element.select("thead > tr > th").map { it.toKDocText() }
				val body = element.select("tbody > tr").map { it.select("td").map { it.toKDocText().trim() } }

				val columnWidths = (0 until columnCount).map { index ->
					(body.map { it[index] } + headers.getOrElse(index) { "" }).flatMap {
						it.split('\n')
					}.map { it.length }.max()!!
				}

				return buildString {
					appendln("```")

					if (headers.isNotEmpty()) {
						headers.mapIndexed { index, it -> it.padEnd(columnWidths[index], ' ') }
								.joinTo(this, "|", "|", "|")
						appendln()
					}

					columnWidths.joinTo(this, "|", "|", "|") { "-".padEnd(it, '-') }
					appendln()

					for (row in body) {
						val rowLines = row.map { it.split('\n') }

						for (subRowIndex in 0 until rowLines.map { it.size }.max()!!) {
							append('|')
							for ((columnIndex, rowList) in rowLines.withIndex()) {
								val subRow = rowList.getOrElse(subRowIndex) { " " }
										.padEnd(columnWidths[columnIndex], ' ')

								append(subRow)
								append("|")
							}
							appendln()
						}

						columnWidths.joinTo(this, "|", "|", "|") { "-".padEnd(it, '-') }
						appendln()
					}
					appendln()
					appendln("```")
				}
			}

			for (strong in element.getElementsByTag("strong")) {
				strong.replaceWith(TextNode("**${strong.wholeText()}**"))
			}

			for (code in element.getElementsByTag("code")) {
				val typeSpec = vkTypeMap[code.text()]
				if (typeSpec != null) {
					code.replaceWith(TextNode("[${typeSpec.name}]"))
				}
			}

			for (para in element.getElementsByTag("p")) {
				para.appendText("\n\n")
			}

			return element.wholeText()
		}

		// Generate dispatch tables for native
		fun generateDispatchTables() {
			val getProcAddrType = LambdaTypeName.get(
					parameters = *arrayOf(STRING),
					returnType = ClassName("cvulkan", "PFN_vkVoidFunction")
							.copy(nullable = true)
			)

			val globalDispatchTable = TypeSpec.classBuilder("GlobalDispatchTable").addModifiers(KModifier.INTERNAL)
			val instanceDispatchTable = TypeSpec.classBuilder("InstanceDispatchTable").addModifiers(KModifier.INTERNAL)
			val deviceDispatchTable = TypeSpec.classBuilder("DeviceDispatchTable").addModifiers(KModifier.INTERNAL)

			val ctor = FunSpec.constructorBuilder()
					.addParameter("getProcAddr", getProcAddrType)
					.build()

			globalDispatchTable.primaryConstructor(ctor)
			instanceDispatchTable.primaryConstructor(ctor)
			deviceDispatchTable.primaryConstructor(ctor)

			val coreCommands = registry.features.single { it.name == "VK_VERSION_1_0" }.requires.flatMap { it.commands }.toSet()
			for (command in registry.commands) {
				// Cannot load itself.
				if (command.name == "vkGetInstanceProcAddr") continue

				val extension = extensionInvMap[command.name]

				if (extension != null) {
					// Skip disabled extensions
					if (extension.supported != "vulkan") continue

					// Skip platform specific extensions.
					if (extension.platform != null)  continue
				}

				val firstParam = command.params[0]

				val firstParamType = vkTypeMap[firstParam.type.name]

				val dispatchTable = when {
					firstParamType !is VkHandle -> globalDispatchTable
					firstParamType.name == "VkQueue" || firstParamType.name == "VkCommandBuffer" -> deviceDispatchTable
					command.name == "vkGetDeviceProcAddr" -> instanceDispatchTable
					firstParamType.name == "VkDevice" -> deviceDispatchTable
					else -> instanceDispatchTable
				}

				val mustBeAvailable = command.name in coreCommands

				val commandProtoType = ClassName("cvulkan", "PFN_${command.name}")
				dispatchTable.addProperty(PropertySpec.builder(command.name, commandProtoType.copy(nullable = !mustBeAvailable))
						.initializer(
								if (mustBeAvailable) {
									"getProcAddr(%S)!!.%M()"
								} else {
									"getProcAddr(%S)?.%M()"
								},
								command.name, KtxC.REINTERPRET
						)
						.build())
			}

			FileSpec.get("com.kgl.vulkan.utils", globalDispatchTable.build()).writeTo(nativeDir.get().asFile)
			FileSpec.get("com.kgl.vulkan.utils", instanceDispatchTable.build()).writeTo(nativeDir.get().asFile)
			FileSpec.get("com.kgl.vulkan.utils", deviceDispatchTable.build()).writeTo(nativeDir.get().asFile)
		}

		data class EnumEntry(val value: Int, val nameVk: String, val jvmClass: ClassName)
		fun getEnumEntries(enum: VkEnum): List<EnumEntry> = sequence {
			val mainJvmEnumClass = extensionInvMap[enum.name]?.getLWJGLClass() ?: VK11

			for (entry in enum.entries) {
				val value = entry.value ?: entry.bitPosition ?: TODO("${enum.name}.${entry.name} has no value")
				yield(EnumEntry(value, entry.name, mainJvmEnumClass))
			}

			for (feature in registry.features) {
				val featureClass = when (feature.number) {
					"1.0" -> VK10
					"1.1" -> VK11
					else -> throw TODO("Unkown Vulkan feature '${feature.number}'.")
				}
				for (require in feature.requires) {
					for ((entryName, enumName, value) in require.enums) {
						if (enumName == enum.name) {
							yield(EnumEntry(value, entryName, featureClass))
						}
					}
				}
			}

			// Enum entries
			val visitedExtensions = mutableSetOf<String>()
			for (extension in registry.extensions) {
				// Skip disabled extensions
				if (extension.supported != "vulkan") continue

				// Skip platform specific enums for now.
				if (extension.platform != null) continue

				for (require in extension.requires) {
					for ((entryName, enumName, value) in require.enums) {
						if (enumName == enum.name && entryName !in visitedExtensions) {
							visitedExtensions += entryName

							yield(EnumEntry(value, entryName, extension.getLWJGLClass()))
						}
					}
				}
			}
		}.sortedBy { it.value }.toList()

		fun generateEnums() {
			for (enum in registry.enums) {
				// Is specially handled.
				if (enum.name == "VkResult") continue

				// VkRenderPassCreateFlags exists but VkRenderPassCreateFlagBits doesn't.
				// The enum is empty.
				if (enum.name == "VkRenderPassCreateFlagBits") continue

				val srcExtension = extensionInvMap[enum.name]

				val enumVkPrefix = enum.name.let {
					if (srcExtension != null) it.removeSuffix(srcExtension.author) else it
				}.let {
					if (enum.isBitMask) it.removeSuffix("FlagBits") else it
				}.pascalToSnakeCase().toUpperCase() + "_"

				val enumClassKt = kglClassMap[enum.name] as ClassName

				val entrySuffix = buildString {
					if (enum.isBitMask) append("_BIT")
					if (srcExtension != null) {
						append('_')
						append(srcExtension.author)
					}
				}

				fun String.entryKtName() = removeSuffix(entrySuffix)
						.removePrefix(enumVkPrefix.commonPrefixWith(this))
						.let { if (it[0].isDigit()) "`$it`" else it }

				val enumEntries = getEnumEntries(enum)

				val manPageDoc = apiSpecDoc.select("div.sect2:has(h3#_${enum.name.toLowerCase()}3)").single()
				val divs = manPageDoc.select("div.sect3")

				val summary = divs[0].select("div.paragraph").single().toKDocText()
				val description = divs[2]
				val entryDescriptionMap = description.select("> div.ulist:first-of-type > ul > li > p")
						.associateBy({ it.child(0).text() }, { it.toKDocText() })
				val paragraphs = description.select("div.paragraph,table.tableblock").map { it.toKDocText() }

				val seeAlso = divs[3].select("div.paragraph > p > a")

				val enumTypeSpec = buildTypeSpec({ TypeSpec.enumBuilder(enumClassKt) }) { platform ->
					addKdoc(summary)
					for (para in paragraphs) {
						addKdoc(para)
					}
					for (other in seeAlso) {
						val kglClass = kglClassMap[other.text()]
						if (kglClass != null) {
							addKdoc("@see %T\n", kglClass)
						}
					}

					addSuperinterface((if (enum.isBitMask) VK_FLAG else VK_ENUM).parameterizedBy(enumClassKt))

					for (entry in enumEntries) {
						if (entry.nameVk in hiddenEntries) continue

						val nameKt = entry.nameVk.entryKtName()

						addEnumConstant(nameKt, TypeSpec.anonymousClassBuilder().apply {
							val desc = entryDescriptionMap[entry.nameVk]
							if (desc != null) {
								addKdoc(desc)
							}

							val member = when (platform) {
								Platform.COMMON -> return@apply
								Platform.JVM -> MemberName(entry.jvmClass, entry.nameVk)
								Platform.NATIVE -> MemberName("cvulkan", entry.nameVk)
							}
							addSuperclassConstructorParameter("%M", member)
						}.build())
					}

					val companionBuilder = TypeSpec.companionObjectBuilder()

					if (platform != Platform.COMMON) {
						val wrappedType = when (platform) {
							Platform.JVM -> INT
							Platform.NATIVE -> ClassName("cvulkan", enum.name)
							else -> throw Exception("Ummm, Kotlin broke.")
						}

						primaryConstructor(FunSpec.constructorBuilder()
								.addParameter("value", wrappedType)
								.build()
						)
						addProperty(PropertySpec.builder("value", wrappedType, KModifier.OVERRIDE)
								.initializer("value")
								.build()
						)

						companionBuilder.addModifiers(KModifier.ACTUAL)

						if (enum.isBitMask) {
							val flagsType = when (platform) {
								Platform.JVM -> INT
								Platform.NATIVE -> ClassName("cvulkan", enum.name.replace("FlagBits", "Flags"))
								else -> throw Exception("Ummm, Kotlin broke.")
							}

							companionBuilder.addFunction(
									FunSpec.builder("fromMultiple")
											.addParameter("value", flagsType)
											.returns(VK_FLAG.parameterizedBy(enumClassKt))
											.addStatement("return %T(value)", VK_FLAG)
											.build()
							)
						}

						val fromBuilder = FunSpec.builder("from")
								.addParameter("value", wrappedType)
								.returns(enumClassKt)

						if (!enum.isBitMask && enumEntries.withIndex().all { (index, it) -> it.value == index }) {
							val converter = if (platform == Platform.NATIVE) ".toInt()" else ""
							fromBuilder.addStatement("return enumValues<%T>()[value$converter]", enumClassKt)
						} else {
							fromBuilder.addStatement("return enumLookUpMap[value]!!")

							companionBuilder.addProperty(PropertySpec.builder(
									"enumLookUpMap",
									Map::class.asClassName().parameterizedBy(wrappedType, enumClassKt),
									KModifier.PRIVATE
							).initializer("enumValues<%T>().associateBy({ it.value })", enumClassKt).build())
						}

						companionBuilder.addFunction(fromBuilder.build())
					}

					addType(companionBuilder.build())
				}

				val enumFileName = enumClassKt.simpleName

				FileSpec.builder("com.kgl.vulkan.enums", enumFileName)
						.addType(enumTypeSpec.common)
						.apply {
							for ((name, alias) in enum.aliases) {
								if (name in hiddenEntries) continue

								val nameKt = name.entryKtName()
								val aliasKt = alias.entryKtName()

								addProperty(PropertySpec.builder(nameKt, enumClassKt)
										.receiver(enumClassKt.nestedClass("Companion"))
										.getter(FunSpec.getterBuilder().addModifiers(KModifier.INLINE)
												.addStatement("return %T.$aliasKt", enumClassKt).build())
										.build())
							}
						}
						.build()
						.writeTo(commonDir.get().asFile)

				FileSpec.get("com.kgl.vulkan.enums", enumTypeSpec.jvm).writeTo(jvmDir.get().asFile)
				FileSpec.get("com.kgl.vulkan.enums", enumTypeSpec.native).writeTo(nativeDir.get().asFile)
			}
		}

		fun generateAPIConstants() {
			val vkClass = buildTypeSpec({ TypeSpec.objectBuilder("Vk") }) { platform ->
				for ((name, value) in registry.constants) {
					// Skip aliases for now.
					if (value.startsWith("VK_")) continue

					val constantType = when {
						value.contains('U') && value.contains('L') -> ULONG
						value.contains('U') -> UINT
						value.endsWith('f') -> FLOAT
						value.endsWith('"') -> STRING
						else -> INT
					}

					val nameKt = name.removePrefix("VK_")

					val property = PropertySpec.builder(nameKt, constantType)
					if (platform != Platform.COMMON) {
						property.addModifiers(KModifier.ACTUAL)

						if (platform == Platform.JVM) {
							property.initializer(
									buildString {
										append("%T.")
										append(name)
										if (constantType.simpleName[0] == 'U') {
											append(".to")
											append(constantType.simpleName)
											append("()")
										}
									},
									extensionInvMap[name]?.getLWJGLClass() ?: VK11
							)
						} else {
							property.initializer("%M", MemberName("cvulkan", name))
						}
					}

					addProperty(property.build())
				}

				for (extension in registry.extensions) {
					// Skip disabled extensions
					if (extension.supported != "vulkan") continue

					// Skip platform specific enums for now.
					if (extension.platform != null) continue

					val lwjglClass = extension.getLWJGLClass()

					for (require in extension.requires) {
						for ((name, value) in require.constants) {
							if (value == null) continue

							val constantType = when {
								name.endsWith("_NAME") -> STRING
								name.endsWith("_VERSION") -> INT
								else -> TODO("Extension constant $name not expected.")
							}

							val nameKt = name.removePrefix("VK_")

							val property = PropertySpec.builder(nameKt, constantType)
							if (platform != Platform.COMMON) {
								property.addModifiers(KModifier.ACTUAL)

								property.initializer("%M", if (platform == Platform.JVM) {
									MemberName(lwjglClass, name)
								} else {
									MemberName("cvulkan", name)
								})
							}
							addProperty(property.build())
						}
					}
				}
			}

			FileSpec.get("com.kgl.vulkan", vkClass.common).writeTo(commonDir.get().asFile)
			FileSpec.get("com.kgl.vulkan", vkClass.native).writeTo(nativeDir.get().asFile)
			FileSpec.get("com.kgl.vulkan", vkClass.jvm).writeTo(jvmDir.get().asFile)
		}

		fun generateExceptions() {
			val errorsFile = MultiPlatform(
					FileSpec.builder("com.kgl.vulkan.utils", "Errors"),
					FileSpec.builder("com.kgl.vulkan.utils", "Errors"),
					FileSpec.builder("com.kgl.vulkan.utils", "Errors")
			)

			val vulkanError = ClassName("com.kgl.vulkan.utils", "VulkanError")

			errorsFile.common.addType(TypeSpec.classBuilder(vulkanError)
					.superclass(ClassName("kotlin", "Error"))
					.addModifiers(KModifier.SEALED)
					.build())

			val handleResultJvm = FunSpec.builder("handleVkResult").addParameter("resultCode", INT)
			val handleResultNative = FunSpec.builder("handleVkResult").addParameter("resultCode", INT)

			handleResultJvm.returns(NOTHING)
			handleResultNative.returns(NOTHING)

			handleResultJvm.beginControlFlow("return when(resultCode)")
			handleResultNative.beginControlFlow("return when(resultCode)")

			val manPageDoc = apiSpecDoc.select("div.sect2:has(h3#_vkresult3)").single()
			val divs = manPageDoc.select("div.sect3")

			val description = divs[2]
			val entryDescriptionMap = description.select("div#fundamentals-errorcodes > ul > li > p")
					.associateBy({ it.child(0).text() }, { it.toKDocText() })

			for (entry in getEnumEntries(registry.enums.single { it.name == "VkResult" })) {
				if (!entry.nameVk.startsWith("VK_ERROR_")) continue

				val errorType = ClassName("com.kgl.vulkan.utils",
						entry.nameVk.removePrefix("VK_ERROR_").snakeToPascalCase() + "Error")

				errorsFile.common.addType(TypeSpec.classBuilder(errorType)
						.superclass(vulkanError)
						.apply {
							val doc = entryDescriptionMap[entry.nameVk]
							if (doc != null) {
								addKdoc(doc)
							}
						}
						.build())

				handleResultJvm.addStatement("%T.${entry.nameVk} -> throw %T()", entry.jvmClass, errorType)
				handleResultNative.addStatement("%M -> throw %T()", MemberName("cvulkan", entry.nameVk), errorType)
			}

			val unknownVulkanError = ClassName("com.kgl.vulkan.utils", "UnknownVulkanError")

			errorsFile.common.addType(TypeSpec.classBuilder(unknownVulkanError)
					.superclass(vulkanError)
					.primaryConstructor(FunSpec.constructorBuilder()
							.addParameter("resultCode", INT).build())
					.addProperty(PropertySpec.builder("resultCode", INT).initializer("resultCode").build())
					.build())
			handleResultJvm.addStatement("else -> throw %T(resultCode)", unknownVulkanError)
			handleResultNative.addStatement("else -> throw %T(resultCode)", unknownVulkanError)

			handleResultJvm.endControlFlow()
			handleResultNative.endControlFlow()

			errorsFile.jvm.addFunction(handleResultJvm.build())
			errorsFile.native.addFunction(handleResultNative.build())

			errorsFile.common.build().writeTo(commonDir.get().asFile)
			errorsFile.jvm.build().writeTo(jvmDir.get().asFile)
			errorsFile.native.build().writeTo(nativeDir.get().asFile)
		}

		generateDispatchTables()
		generateEnums()
		generateAPIConstants()
		generateExceptions()
	}
}
