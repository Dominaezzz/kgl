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
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import javax.xml.parsers.DocumentBuilderFactory

@Suppress("UnstableApiUsage")
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
	}

	private val typeOverrides: Map<String, String> by lazy {
		(Parser.default()
				.parse(this::class.java.getResourceAsStream("/vk_type_overrides.json")) as JsonObject)
				.mapValues { it.value as String }
	}

	private val structInitMembers: Map<String, Set<String>> by lazy {
		(Parser.default()
				.parse(this::class.java.getResourceAsStream("/vk_struct_init_params.json")) as JsonObject)
				.mapValues { (it.value as JsonArray<*>).map { it as String }.toSet() }
	}

	private val availableStructInits: Map<String, List<List<ParameterSpec>>> by lazy {
		fun param(name: String, type: TypeName, vararg modifiers: KModifier): ParameterSpec {
			val param = ParameterSpec.builder(name, type, *modifiers)
			if (type.isNullable) param.defaultValue("null")
			return param.build()
		}
		fun lambdaParam(builderName: String, withDefault: Boolean = false): ParameterSpec {
			val buildClass = ClassName("com.kgl.vulkan.dsls", builderName)
			val param = ParameterSpec.builder(
					"block", LambdaTypeName.get(buildClass, returnType = UNIT)
			)
			if (withDefault) param.defaultValue("{}")
			return param.build()
		}
		fun handle(name: String) = ClassName("com.kgl.vulkan.handles", name)

		mapOf(
				"VkDescriptorSetLayoutBinding" to listOf(
						listOf(
								param("immutableSamplers", COLLECTION.parameterizedBy(handle("Sampler"))),
								lambdaParam("DescriptorSetLayoutBindingBuilder", withDefault = true)
						),
						listOf(
								param("descriptorCount", U_INT),
								lambdaParam("DescriptorSetLayoutBindingBuilder")
						)
				),
				"VkSubmitInfo" to listOf(
						listOf(
								param("waitSemaphores", COLLECTION.parameterizedBy(
										PAIR.parameterizedBy(
												handle("Semaphore"),
												VK_FLAG.parameterizedBy(
														ClassName("com.kgl.vulkan.enums", "PipelineStage")
												)
										)
								).copy(nullable = true)),
								param("commandBuffers", COLLECTION.parameterizedBy(handle("CommandBuffer")).copy(nullable = true)),
								param("signalSemaphores", COLLECTION.parameterizedBy(handle("Semaphore")).copy(nullable = true)),
								lambdaParam("SubmitInfoBuilder", withDefault = true)
						)
				),
				"VkSubpassDescription" to listOf(
						listOf(
								param(
										"preserveAttachments",
										ClassName("kotlin", "UIntArray").copy(nullable = true)
								),
								lambdaParam("SubpassDescriptionBuilder")
						)
				),
				"VkSubpassDescription2KHR" to listOf(
						listOf(
								param("preserveAttachments", ClassName("kotlin", "UIntArray")),
								lambdaParam("SubpassDescription2KHRBuilder")
						)
				),
				"VkWriteDescriptorSet" to listOf(
						listOf(
								param("dstSet", handle("DescriptorSet")),
								param(
										"texelBufferView",
										COLLECTION.parameterizedBy(handle("BufferView") )
												.copy(nullable = true)),
								lambdaParam("WriteDescriptorSetBuilder")
						)
				),
				"VkClearValue" to listOf(
						listOf(lambdaParam("ClearValueBuilder"))
				),
				"VkDeviceQueueCreateInfo" to listOf(
						listOf(
								param("queueFamilyIndex", U_INT),
								param("queuePriorities", FLOAT, KModifier.VARARG),
								lambdaParam("DeviceQueueCreateInfoBuilder", true)
						)
				)
		)
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

		val vkTypeMap = (registry.enums + registry.structs + registry.unions + registry.handles + registry.funcPointers + registry.flags + primitives)
				.associateBy { it.name }

		val outputStructs: Set<VkStruct> = run {
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

			val rootOutputStructs = registry.commands.asSequence()
					.flatMap { command -> command.params.takeLastWhile { it.type.isWritable }.asSequence() }
					.plus(registry.funcPointers.asSequence().flatMap { it.params.asSequence() })
					.map { vkTypeMap[it.type.name] }
					.filterIsInstance<VkStruct>()

			rootOutputStructs.forEach(::crawlOutputStruct)

			val names = rootOutputStructs.map { it.name }.toSet()
			registry.structs.filter { it.structExtends.any(names::contains) }.forEach(::crawlOutputStruct)

			outputStructs
		}

		// i.e VkInstance -> com.kgl.vulkan.handles.Instance
		val kglClassMap: Map<String, TypeName> = buildMap {
			for (handle in registry.handles) {
				put(handle.name, ClassName("com.kgl.vulkan.handles", handle.name.removePrefix("Vk")))
			}
			for (primitive in primitives) {
				put(primitive.name, primitive.nameClass)
			}
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
					val kglClass = kglClassMap[typeSpec.name] as? ClassName
					if (kglClass != null) {
						code.replaceWith(TextNode("[${typeSpec.name}][${kglClass.canonicalName}]"))
					}
				} else if (code.text() == "NULL") {
					code.replaceWith(TextNode("`null`"))
				}
			}

			for (link in element.getElementsByTag("a")) {
				val typeSpec = vkTypeMap[link.text().removePrefix("#")]
				if (typeSpec != null) {
					val kglClass = kglClassMap[typeSpec.name] as? ClassName
					if (kglClass != null) {
						link.replaceWith(TextNode("[${kglClass.simpleName}][${kglClass.canonicalName}]"))
					}
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
				if (srcExtension != null) {
					if (srcExtension.supported != "vulkan") continue
				}

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
						addKdoc("%L", para)
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
						value.contains('U') && value.contains('L') -> U_LONG
						value.contains('U') -> U_INT
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

		fun generateOutputStructs() {
			val arrayClassesMap = primitives.associate {
				it.name to it.nameClass.peerClass("${it.nameClass.simpleName}Array")
			}

			// Returns representation of type in KGL.
			// If collectionType is specified then the type will be wrapped if applicable
			fun IVkParam.getKGLClass(collectionType: ClassName = COLLECTION): TypeName {
				val typeName = registry.structAliases[type.name] ?: type.name
				return when (type.asteriskCount) {
					0 -> {
						val mainType = kglClassMap[typeName] ?: TODO("$type has no KGL representation.")
						when {
							type.count.isEmpty() -> mainType
							type.name == "char" -> STRING
							else -> arrayClassesMap[typeName] ?: collectionType.parameterizedBy(mainType)
						}
					}
					1 -> {
						when(typeName) {
							"char" -> STRING
							"void" -> if (len.isNotEmpty()) MEMORY else LONG
							else -> {
								val mainType = kglClassMap[typeName] ?: TODO("$type has no KGL representation.")
								arrayClassesMap[typeName] ?: collectionType.parameterizedBy(mainType)
							}
						}
					}
					2 -> {
						when (typeName) {
							"char" -> if (type.isWritable) {
								STRING
							} else {
								collectionType.parameterizedBy(STRING)
							}
							else -> TODO("$type has no KGL representation.")
						}
					}
					else -> TODO("$type has no KGL representation.")
				}
			}
			fun IVkParam.isArray(): Boolean {
				val typeName = registry.structAliases[type.name] ?: type.name
				return when (type.asteriskCount) {
					0 -> type.count.isNotEmpty() && type.name != "char"
					1 -> typeName != "char" && typeName != "void"
					2 -> when (typeName) {
						"char" -> !type.isWritable
						else -> TODO("$type has no KGL representation.")
					}
					else -> TODO("$type has no KGL representation.")
				}
			}

			for (struct in outputStructs) {
				// Skip platform specific structs for now.
				if (extensionInvMap[struct.name]?.platform != null) continue

				val structClass = kglClassMap[struct.name] as ClassName


				val jvmStructFile = FileSpec.builder("com.kgl.vulkan.structs", structClass.simpleName)
				val nativeStructFile = FileSpec.builder("com.kgl.vulkan.structs", structClass.simpleName)

				val commonStruct = TypeSpec.classBuilder(structClass).addModifiers(KModifier.DATA)
				val commonConstructor = FunSpec.constructorBuilder()

				val lengthMembers = struct.members.flatMap { it.len }

				val fromFun = buildPlatformFunction({ FunSpec.builder("from") }) { platform ->
					receiver(structClass.nestedClass("Companion"))
					returns(structClass)

					addParameter("ptr", ClassName(if (platform == Platform.JVM) LWJGLVulkanPackage else "cvulkan", struct.name))

					addCode(buildCodeBlock {
						add("return %T(", structClass)

						var isPastFirst = false

						for (member in struct.members) {
							// Skip length members
							if (member.name in lengthMembers) continue

							val memberAbsoluteName = "${struct.name}.${member.name}"

							// Skip unnecessary members.
							if (memberAbsoluteName in hiddenEntries) continue

							val type = vkTypeMap[member.type.name]
							if (type is VkFlag && type.requires == null) continue

							val isVkVersion = typeOverrides[memberAbsoluteName] == "VkVersion"

							val propertyClass = when {
								isVkVersion -> VK_VERSION
								member.name == "pNext" -> BASE_OUT_STRUCTURE.copy(nullable = true)
								else -> member.getKGLClass(LIST).copy(nullable = member.optional && !(type is VkPrimitive && member.type.asteriskCount == 0))
							}

							if (member.type.count.isNotBlank()) {
								if (member.type.count.startsWith("VK_")) {
									nativeStructFile.addImport("cvulkan", member.type.count)
									val ext = extensionInvMap[member.type.count]
									jvmStructFile.addImport(ext?.getLWJGLClass() ?: VK11, member.type.count)
								}
							}

							if (isPastFirst) {
								add(", ")
							}

							if (member.name == "pNext") {
								add(buildString {
									append("%T.from(ptr.pNext")
									if (platform == Platform.JVM) append("()")
									append(")")
								}, BASE_OUT_STRUCTURE)
							} else {
								when (type) {
									is VkPrimitive -> {
										if (member.isArray()) {
											add(buildString {
												append("ptr.")
												append(member.name)
												if (platform == Platform.JVM) append("()")
												if (propertyClass.isNullable) {
													append("?")
												} else if (member.type.asteriskCount > 0) {
													append("!!")
												}
												append(".let { target -> %T(")
												if (member.type.count.isNotEmpty()) {
													append(member.type.count)
												} else {
													append("ptr.")
													if (member.len.isNotEmpty()) {
														append(member.len.first())
													} else if (member.name.endsWith("s")) {
														append(member.name.removeSuffix("s"))
														append("Count")
													} else TODO("Cannot find count!")
													append("()")
													append(".toInt()")
												}
												append(") { target[it]")
												if (platform == Platform.JVM && type.fromJVMVkType.isNotBlank()) {
													append('.')
													append(type.fromJVMVkType)
												}
												append(" } }")
											}, propertyClass.copy(nullable = false))
										} else {
											val code = buildString {
												append("ptr.")
												append(member.name)
												if (platform == Platform.JVM) {
													if (propertyClass is ClassName && propertyClass.simpleName == "String") {
														append("String")
													}
													append("()")
													if (type.fromJVMVkType.isNotBlank()) {
														append('.')
														append(type.fromJVMVkType)
													}
												} else {
													if (type.name == "void") {
														if (member.len.isEmpty()) {
															append(".toLong()")
														}
													} else if (type.name == "VkBool32") {
														append(".toBoolean()")
													} else if (propertyClass is ClassName && propertyClass.simpleName == "String") {
														if (propertyClass.isNullable) {
															append("?")
														} else if (member.type.asteriskCount > 0) {
															append("!!")
														}
														append(".toKString()")
													}
												}
											}
											if (isVkVersion) {
												add("%T($code)", VK_VERSION)
											} else {
												add(code)
											}
										}
									}
									is VkStruct -> {
										if (propertyClass is ParameterizedTypeName) {
											add(buildString {
												append("ptr.")
												append(member.name)
												if (platform == Platform.JVM) append("()")
												if (propertyClass.isNullable) {
													append("?")
												} else if (member.type.asteriskCount > 0) {
													append("!!")
												}
												append(".let { target -> %T(ptr.")
												if (member.len.isNotEmpty()) {
													append(member.len.first())
												} else if (member.name.endsWith("s")) {
													append(member.name.removeSuffix("s"))
													append("Count")
												}
												if (platform == Platform.NATIVE) append(".toInt")
												append("()) { %T.from(target[it]) } }")
											}, propertyClass.copy(nullable = false), propertyClass.typeArguments[0])
										} else {
											add(buildString {
												append("%T.from(ptr.")
												append(member.name)
												if (platform == Platform.JVM) append("()")
												append(")")
											}, propertyClass as ClassName)
										}
									}
									is VkEnum -> {
										add(buildString {
											append("%T.from(ptr.")
											append(member.name)
											if (platform == Platform.JVM) append("()")
											append(")")
										}, propertyClass as ClassName)
									}
									is VkFlag -> {
										add(buildString {
											append("%T.fromMultiple(ptr.")
											append(member.name)
											if (platform == Platform.JVM) append("()")
											append(")")
										}, (propertyClass as ParameterizedTypeName).typeArguments[0] as ClassName)
									}
									is VkHandle -> {
										if (propertyClass is ParameterizedTypeName) {
											add(buildString {
												append("ptr.")
												append(member.name)
												if (platform == Platform.JVM) append("()")
												if (propertyClass.isNullable) {
													append("?")
												} else if (member.type.asteriskCount > 0) {
													append("!!")
												}
												append(".let { target -> %T(ptr.")
												if (member.len.isNotEmpty()) {
													append(member.len.first())
												} else if (member.name.endsWith("s")) {
													append(member.name.removeSuffix("s"))
													append("Count")
												}
												if (platform == Platform.NATIVE) append(".toInt")
												append("()) { target[it]; TODO() } }")
											}, propertyClass.copy(nullable = false))
										} else {
											add("TODO()")
										}
									}
									null -> TODO(struct.name + "->" + member.name + " has no KGL representation")
								}
							}

							isPastFirst = true
						}
						add(")")
					})
				}

				val manPageDoc = apiSpecDoc.select("div.sect2:has(h3#_${struct.name.toLowerCase()}3)").single()
				val divs = manPageDoc.select("div.sect3")

				val summary = divs[0].select("div.paragraph").single().toKDocText()
				val description = divs[2]
				val memberDescriptionMap = description.select("> div.ulist:first-of-type > ul > li > p")
						.associateBy({ it.children().first { it.tagName() == "code" }.text() }, { it.toKDocText() })
				val paragraphs = description.select("div.paragraph").map { it.toKDocText() }

				val seeAlso = divs[4].select("div.paragraph > p > a")

				for (member in struct.members) {
					// Skip length members
					if (member.name in lengthMembers) continue

					val memberAbsoluteName = "${struct.name}.${member.name}"

					// Skip unnecessary members.
					if (memberAbsoluteName in hiddenEntries) continue

					val type = vkTypeMap[member.type.name]

					if (type is VkFlag && type.requires == null) continue

					val isVkVersion = typeOverrides[memberAbsoluteName] == "VkVersion"

					val propertyClass = when {
						isVkVersion -> VK_VERSION
						member.name == "pNext" -> BASE_OUT_STRUCTURE.copy(nullable = true)
						else -> member.getKGLClass(LIST).copy(nullable = member.optional && !(type is VkPrimitive && member.type.asteriskCount == 0))
					}

					val memberNameKt = if (member.name[0] == 'p' && member.name[1].isUpperCase()) {
						member.name.removePrefix("p").decapitalize()
					} else {
						member.name
					}

					commonConstructor.addParameter(memberNameKt, propertyClass)
					commonStruct.addProperty(PropertySpec.builder(memberNameKt, propertyClass)
							.initializer(memberNameKt)
							.apply {
								if (member.name == "pNext" || member.name == "sType") {
									addModifiers(KModifier.OVERRIDE)
								}

								val desc = memberDescriptionMap[member.name]
								if (desc != null) {
									addKdoc(desc)
								}
							}
							.build())
				}

				commonStruct.primaryConstructor(commonConstructor.build())
						.addType(TypeSpec.companionObjectBuilder().build())
						.addKdoc(summary)
				paragraphs.forEach { commonStruct.addKdoc(it) }
				seeAlso.forEach {
					val kglClass = kglClassMap[it.text()]
					if (kglClass != null) {
						commonStruct.addKdoc("@see %T\n", kglClass)
					}
				}

				if (struct.members.any { it.name == "pNext" || it.name == "sType" }) {
					commonStruct.superclass(BASE_OUT_STRUCTURE)
				}

				FileSpec.get("com.kgl.vulkan.structs", commonStruct.build()).writeTo(commonDir.get().asFile)

				jvmStructFile.addFunction(fromFun.jvm!!)
						.build()
						.writeTo(jvmDir.get().asFile)

				nativeStructFile.addImport("com.kgl.vulkan.utils", "toBoolean")
						.addImport("kotlinx.cinterop", "get")
						.addImport("kotlinx.cinterop", "pointed")
						.addImport("kotlinx.cinterop", "toLong")
						.addImport("kotlinx.cinterop", "toKString")
						.addFunction(fromFun.native!!)
						.build()
						.writeTo(nativeDir.get().asFile)
			}


			// Struct pNext stuff
			val baseOutFrom = buildPlatformFunction({ FunSpec.builder("from") }) { platform ->
				receiver(BASE_OUT_STRUCTURE.nestedClass("Companion"))
				returns(BASE_OUT_STRUCTURE.copy(nullable = true))
				addParameter("ptr", if (platform == Platform.JVM) LONG else C_OPAQUE_POINTER.copy(nullable = true))

				if (platform == Platform.JVM) {
					addStatement("val base = %T.createSafe(ptr) ?: return null", ClassName(LWJGLVulkanPackage, "VkBaseOutStructure"))
				} else {
					addStatement("if (ptr == null) return null")
					addStatement("val base = ptr.%M<%T>().%M",
							KtxC.REINTERPRET,
							ClassName("cvulkan", "VkBaseOutStructure"),
							KtxC.POINTED)
				}

				beginControlFlow(buildString {
					append("return when(base.sType")
					if (platform == Platform.JVM) append("()")
					append(")")
				})

				for (struct in outputStructs) {
					val extension = extensionInvMap[struct.name]

					// Skip platform specific structs for now.
					if (extension?.platform != null) continue

					val sTypeMember = struct.members.singleOrNull { it.name == "sType" } ?: continue

					val structClassKt = kglClassMap.getValue(struct.name)

					if (platform == Platform.JVM) {
						addStatement(
								"%T.${sTypeMember.values[0]} -> %T.from(%T.create(ptr))",
								extension?.getLWJGLClass() ?: VK11,
								structClassKt,
								ClassName(LWJGLVulkanPackage, struct.name)
						)
					} else {
						addStatement(
								"%M -> %T.from(ptr.%M<%T>().%M)",
								MemberName("cvulkan", sTypeMember.values[0]),
								structClassKt,
								KtxC.REINTERPRET,
								ClassName("cvulkan", struct.name),
								KtxC.POINTED
						)
					}
				}

				addStatement(buildString {
					append("else -> throw Error(%S + base.sType")
					if (platform == Platform.JVM) append("()")
					append(")")
				}, "Could not find structure with sType = ")
				endControlFlow()
			}

			FileSpec.builder("com.kgl.vulkan.structs", "BaseOutStructure")
					.addFunction(baseOutFrom.jvm!!)
					.build().writeTo(jvmDir.get().asFile)

			FileSpec.builder("com.kgl.vulkan.structs", "BaseOutStructure")
					.addFunction(baseOutFrom.native!!)
					.build().writeTo(nativeDir.get().asFile)
		}

		fun generateInputStructBuilders() {
			val inputStructs = mutableListOf<VkStruct>()
			val resolvedStructs = mutableSetOf<String>()
			val requiredArrayBuilders = mutableSetOf<String>()

			val structDependencies = mutableMapOf<String, Set<String>>()
			fun crawlInputStruct(struct: VkStruct) {
				if (struct.name in structDependencies) return

				val deps = mutableSetOf<String>()
				for (member in struct.members) {
					val type = vkTypeMap[member.type.name]
					if (type is VkStruct) {
						crawlInputStruct(type)
						deps += type.name
						if (member.type.asteriskCount > 0 && member.len.isNotEmpty()) {
							requiredArrayBuilders += type.name
						}
					}
					if (type is VkUnion) {
						if (member.type.asteriskCount > 0 && member.len.isNotEmpty()) {
							requiredArrayBuilders += type.name
						}
					}
				}
				structDependencies[struct.name] = deps
			}
			registry.commands.asSequence().flatMap { it.params.asSequence() }
					.filter { it.type.isConst }
					.forEach {
						val type = vkTypeMap[it.type.name]
						if (type is VkStruct) {
							crawlInputStruct(type)
							if (it.len.isNotEmpty()) {
								requiredArrayBuilders += type.name
							}
						}
					}

			while (resolvedStructs.size < structDependencies.size) {
				var resolvedNoStruct = true
				for ((struct, deps) in structDependencies) {
					if (struct !in resolvedStructs && resolvedStructs.containsAll(deps)) {
						inputStructs += vkTypeMap[struct] as VkStruct
						resolvedStructs += struct
						resolvedNoStruct = false
					}
				}

				if (resolvedNoStruct) TODO("Struct dependency graph couldn't be resolved")
			}

			fun getContainingLWJGLClass(symbolName: String) : ClassName {
				return extensionInvMap[symbolName]?.getLWJGLClass() ?: VK11
			}

			val structInits = availableStructInits.toMutableMap()

			structLoop@for (struct in inputStructs) {
				if (extensionInvMap[struct.name]?.platform != null) {
					val platform = extensionInvMap[struct.name]?.platform
					println("Skipping `${struct.name}` because it is exclusive to $platform.")
					continue
				}

				if (struct.name in hiddenEntries) continue

				val maxLengthGroupSize = struct.members.flatMap { it.len }
						.filter { it != NULL_TERMINATED }
						.groupBy { it }
						.map { it.value.size }
						.max() ?: 0
				if (maxLengthGroupSize > 1) {
					println("Skipping `${struct.name}` because length member is shared.")
					continue
				}

				val lengthMembers = struct.members.flatMap { it.len }.toSet()
				val visibleMembers = struct.members.filter { it.name !in lengthMembers }

				val structBuilderName = struct.name.removePrefix("Vk") + "Builder"

				val initFun = try {
					buildFunSpec({ FunSpec.builder("init").addModifiers(KModifier.INTERNAL) }) { platform ->
						struct.members.singleOrNull { it.name == "sType" }?.also { member ->
							val value = member.values[0]
							if (platform == Platform.JVM) {
								addStatement("target.sType(%T.$value)", getContainingLWJGLClass(value))
							} else {
								addStatement("target.sType = %M", MemberName("cvulkan", value))
							}
						}

						if (struct.members.any { it.name == "pNext" }) {
							addStatement(if (platform == Platform.JVM) {
								"target.pNext(0)"
							} else {
								"target.pNext = null"
							})
						}

						struct.members.singleOrNull { it.name == "flags" }?.also { member ->
							val memberType = vkTypeMap[member.type.name]
							if (memberType is VkFlag && memberType.requires == null) {
								addStatement(if (platform == Platform.JVM) {
									"target.flags(0)"
								} else {
									"target.flags = 0U"
								})
							}
						}

						for (member in visibleMembers) {
							if (member.name == "sType" || member.name == "pNext") continue

							val memberType = vkTypeMap[member.type.name]
							checkNotNull(memberType)

							val memberNameKt = member.name
									.removePrefix("".padEnd(member.type.asteriskCount, 'p'))
									.decapitalize()

							if (memberType is VkStruct || memberType is VkUnion) continue

							val initRequested = structInitMembers[struct.name]?.contains(member.name) == true
							val isString = member.type.asteriskCount == 1 && member.type.name == "char"
							val isBlob = member.type.asteriskCount == 1 && member.type.name == "void"
							val isCollection = member.type.asteriskCount > 0 && !isString && !isBlob
							val mustInit = isCollection || isBlob || memberType is VkHandle

							if (!mustInit && !initRequested) continue

							if (isCollection) {
								val typeCollection = if (memberType is VkPrimitive) {
									if (member.type.name == "char") {
										COLLECTION.parameterizedBy(STRING)
									} else {
										memberType.nameClass.run { peerClass(simpleName + "Array") }
									}
								} else {
									COLLECTION.parameterizedBy(kglClassMap.getValue(memberType.name))
								}

								val lengthParam = struct.members.singleOrNull { it.name == member.len[0] }
								val isOptional = member.optional || lengthParam?.optional == true

								addParameter(memberNameKt, typeCollection.copy(nullable = isOptional))

								if (platform == Platform.JVM) {
									addStatement(if (isOptional) {
										"target.${member.name}($memberNameKt?.%M())"
									} else {
										"target.${member.name}($memberNameKt.%M())"
									}, KglUtils.TO_VKTYPE)
								} else {
									if (isOptional && lengthParam == null) {
										addStatement("target.${member.name} = $memberNameKt?.%M()", KglUtils.TO_VKTYPE)
									} else {
										if (isOptional) beginControlFlow("if ($memberNameKt != null)")
										addStatement("target.${member.name} = $memberNameKt.%M()", KglUtils.TO_VKTYPE)
										addStatement("target.${member.len[0]} = $memberNameKt.size.toUInt()")
										if (isOptional) {
											nextControlFlow("else")
											addStatement("target.${member.len[0]} = 0u")
											endControlFlow()
										}
									}
								}
								continue
							}

							if (memberType is VkEnum || memberType is VkFlag || memberType is VkHandle) {
								val kglClass = kglClassMap.getValue(memberType.name)

								addParameter(memberNameKt, kglClass.copy(nullable = member.optional))
								val innerProp = if (memberType is VkHandle) "ptr" else "value"
								addStatement(buildString {
									append("target.")
									append(member.name.escapeKt())
									if (platform == Platform.JVM) {
										append("(")
										append(memberNameKt)
										if (member.optional) append('?')
										append('.')
										append(innerProp)
										if (member.optional && !(memberType is VkHandle && !memberType.isNonDispatchable)) {
											append(" ?: 0")
										}
										append(')')
									} else {
										append(" = ")
										append(memberNameKt)
										if (member.optional) append('?')
										append('.')
										append(innerProp)
										if (member.optional && memberType !is VkHandle) append(" ?: 0U")
									}
								})
							}
							if (memberType is VkPrimitive) {
								if (isBlob) {
									val lengthParamName = member.len.getOrNull(0)
									val lengthParam = if (lengthParamName != null) {
										struct.members.singleOrNull { it.name == lengthParamName }
									} else {
										null
									}
									val isOptional = member.optional || lengthParam?.optional == true
									if (lengthParam != null) {
										addParameter(memberNameKt, MEMORY.copy(nullable = isOptional))

										val assert = if (isOptional) "?" else ""
										if (platform == Platform.JVM) {
											addStatement("target.${member.name}($memberNameKt$assert.buffer)")
										} else {
											if (isOptional) beginControlFlow("if ($memberNameKt != null)")
											addStatement("target.${member.name} = $memberNameKt.pointer")
											addStatement("target.${member.len[0]} = $memberNameKt.size.toULong()")
											if (isOptional) {
												nextControlFlow("else")
												addStatement("target.${member.name} = null")
												addStatement("target.${member.len[0]} = 0u")
												endControlFlow()
											}
										}
									} else {
										TODO("Cannot handle `void*` without length yet.")
									}
								} else if (isString) {
									check(member.len[0] == NULL_TERMINATED) {
										"DSL string fields must be null terminated."
									}

									val isOptional = member.optional
									addParameter(memberNameKt, STRING.copy(nullable = isOptional))

									val getValue = if (isOptional) {
										"$memberNameKt?.%M()"
									} else {
										"$memberNameKt.%M()"
									}
									if (platform == Platform.JVM) {
										addStatement("target.${member.name.escapeKt()}($getValue)", KglUtils.TO_VKTYPE)
									} else {
										addStatement("target.${member.name.escapeKt()} = $getValue", KglUtils.TO_VKTYPE)
									}
								} else {
									addParameter(memberNameKt, memberType.nameClass)
									addStatement(buildString {
										append("target.")
										append(member.name.escapeKt())
										if (platform == Platform.JVM) {
											append("(")
											append(memberNameKt)
											if (memberType.toJVMVkType.isNotBlank()) {
												append('.')
												append(memberType.toJVMVkType)
											}
											append(')')
										} else {
											append(" = ")
											append(memberNameKt)
											if (memberType.toNativeVkType.isNotBlank()) {
												append('.')
												append(memberType.toNativeVkType)
											}
										}
									})
								}
							}
							if (memberType is VkFuncPointer) TODO("Cannot handle function pointers yet.")
						}
					}
				} catch (e: NotImplementedError) {
					println("Skipping `${struct.name}` because ${e.message}.")
					continue@structLoop
				}

				val builder = buildTypeSpec({ TypeSpec.classBuilder(structBuilderName) }) { platform ->
					addAnnotation(AnnotationSpec.builder(STRUCT_MARKER).build())

					if (platform != Platform.COMMON) {
						val vkStructClass = ClassName(if (platform == Platform.JVM) {
							LWJGLVulkanPackage
						} else {
							"cvulkan"
						}, struct.name)

						primaryConstructor(FunSpec.constructorBuilder().addParameter("target", vkStructClass).build())
						addProperty(PropertySpec.builder("target", vkStructClass, KModifier.INTERNAL).initializer("target").build())
					}

					addFunction(when (platform) {
						Platform.COMMON -> initFun.common
						Platform.JVM -> initFun.jvm
						Platform.NATIVE -> initFun.native
					})
				}.map { it.toBuilder() }

				var requiredCount = 0

				for (member in visibleMembers) {
					if (member.name == "sType") continue

					val memberType = vkTypeMap[member.type.name]
					checkNotNull(memberType)

					val memberNameKt = member.name
							.replace("CreateInfo", "")
							.removePrefix("".padEnd(member.type.asteriskCount, 'p'))
							.decapitalize()

					if (memberType is VkStruct || memberType is VkUnion) {
						val subBuilderName: String = memberType.name.removePrefix("Vk") + "Builder"
						val commonParamss = structInits[memberType.name]
						if (commonParamss == null) {
							println("Skipping `${struct.name}` because ${memberType.name} was skipped.")
							continue@structLoop
						}
						check(commonParamss.isNotEmpty())

						val subBuilderTypeName = ClassName("com.kgl.vulkan.dsls", subBuilderName)

						if (!member.type.count.isBlank()) {
							for (commonParams in commonParamss) {
								val hasLambda = commonParams.lastOrNull()?.let { it.type is LambdaTypeName } == true
								val platformParams = commonParams.map {
									ParameterSpec.builder(it.name, it.type, *it.modifiers.toTypedArray()).build()
								}
								val passedParams = platformParams.dropLast(if (hasLambda) 1 else 0)

								for (i in 0 until member.type.count.toInt()) {
									val functionName = memberNameKt.removeSuffix("s") + i
									val function = buildFunSpec({ FunSpec.builder(functionName) }, auto = false) {
										if (it == Platform.COMMON) {
											addParameters(commonParams)
										} else {
											addParameters(platformParams)
											addStatement(if (it == Platform.JVM) {
												"val subTarget = target.${member.name}($i)"
											} else {
												"val subTarget = target.${member.name}[$i]"
											})
											addStatement("val builder = %T(subTarget)", subBuilderTypeName)
											addStatement(passedParams.joinToString(", ", prefix = "builder.init(", postfix = ")") { it.name })
											if (hasLambda) addStatement("builder.apply(block)")
										}
									}
									builder.doForEach(function) { addFunction(it) }
								}
							}
						} else {
							if (member.type.asteriskCount > 0 && member.len.isNotEmpty() && member.len[0] != "1") {
								val builderType = ClassName("com.kgl.vulkan.dsls", memberType.name.removePrefix("Vk") + "sBuilder")
								val subBuilderType = ClassName("com.kgl.vulkan.dsls", memberType.name.removePrefix("Vk") + "Builder")

								val lambdaTypeName = LambdaTypeName.get(builderType, returnType = UNIT)
								val function = buildFunSpec({ FunSpec.builder(memberNameKt) }) {
									addParameter("block", lambdaTypeName)

									if (it == Platform.JVM) {
										addStatement("val targets = %T().apply(block).targets", builderType)
										addStatement(
												"target.${member.name}(targets.%M(%T::callocStack, ::%T))",
												KglUtils.MAP_TO_STACK_ARRAY,
												ClassName(LWJGLVulkanPackage, member.type.name),
												subBuilderType
										)
										// TODO: INIT JVM COUNT!!!!!
									} else {
										addStatement("val targets = %T().apply(block).targets", builderType)
										addStatement("target.${member.name} = targets.%M(::%T)",
												KglUtils.MAP_TO_STACK_ARRAY, subBuilderType)
										val lengthParam = member.len.firstOrNull { it != NULL_TERMINATED }
										if (lengthParam != null) {
											addStatement("target.$lengthParam = targets.size.toUInt()")
										}
									}
								}

								builder.doForEach(function) { addFunction(it) }
							} else {
								for (commonParams in commonParamss) {
									val hasLambda = commonParams.lastOrNull()?.let { it.type is LambdaTypeName } == true
									val platformParams = commonParams.map {
										ParameterSpec.builder(it.name, it.type, *it.modifiers.toTypedArray()).build()
									}
									val passedParams = platformParams.dropLast(if (hasLambda) 1 else 0)

									val function = buildFunSpec({ FunSpec.builder(memberNameKt) }, auto = false) {
										if (it == Platform.COMMON) {
											addParameters(commonParams)
										} else {
											addParameters(platformParams)
											if (member.type.asteriskCount == 0) {
												addStatement(buildString {
													append("val subTarget = target.")
													append(member.name)
													if (it == Platform.JVM) append("()")
												})
											} else if (it == Platform.JVM) {
												addStatement("val subTarget = %T.callocStack()", ClassName(LWJGLVulkanPackage, member.type.name))
												addStatement("target.${member.name}(subTarget)")
											} else {
												addStatement("val subTarget = %T.%M<%T>()", VIRTUAL_STACK,
														KtxC.ALLOC, ClassName("cvulkan", member.type.name))
												addStatement("target.${member.name} = subTarget.%M", KtxC.PTR)
											}

											addStatement("val builder = %T(subTarget)", subBuilderTypeName)
											addStatement(passedParams.joinToString(", ", prefix = "builder.init(", postfix = ")") { it.name })
											if (hasLambda) addStatement("builder.apply(block)")
										}
									}
									builder.doForEach(function) { addFunction(it) }
								}
							}
						}

						if (!member.optional) requiredCount++
						continue
					}
					if (memberType is VkFuncPointer) {
						println("Skipping `${struct.name}` because function pointer.")
						continue@structLoop
					}

					if (member.type.count.isNotBlank()) {
						if (memberType is VkPrimitive) {
							val function = buildFunSpec({ FunSpec.builder(memberNameKt) }) {
								for (i in 0 until member.type.count.toInt()) {
									val argName = "arg$i"
									addParameter(argName, memberType.nameClass)

									if (it == Platform.JVM) {
										addStatement("target.${member.name}($i, $argName)")
									} else if (it == Platform.NATIVE) {
										addStatement("target.${member.name}[$i] = $argName")
									}
								}
							}

							builder.doForEach(function) { addFunction(it) }
							// if (!member.optional) requiredCount++
							continue
						} else {
							println("Skipping `${struct.name}` because has non-primitve C-Style array member.")
							continue@structLoop
						}
					}

					val initRequested = structInitMembers[struct.name]?.contains(member.name) == true
					val isString = member.type.asteriskCount == 1 && member.type.name == "char"
					val isBlob = member.type.asteriskCount == 1 && member.type.name == "void"
					val isCollection = member.type.asteriskCount > 0 && !isString && !isBlob
					val mustInit = isCollection || isBlob || memberType is VkHandle

					if (mustInit || initRequested) continue

					if (memberType is VkFlag) {
						// If there are no flagbits yet, skip generation.
						if (memberType.requires == null) continue

						val flagClassKt = kglClassMap.getValue(memberType.name)
						val enumClassKt = kglClassMap.getValue(memberType.requires)
						val property = buildPropertySpec({ PropertySpec.builder(memberNameKt, flagClassKt.copy(nullable = true)) }) {
							mutable()
							val getter = FunSpec.getterBuilder()
							val setter = FunSpec.setterBuilder().addParameter("value", flagClassKt)
							if (it == Platform.JVM) {
								getter.addStatement("return %T.fromMultiple(target.${member.name}())", enumClassKt)
								setter.addStatement("target.${member.name}(value?.value ?: 0)")
							} else {
								getter.addStatement("return %T.fromMultiple(target.${member.name})", enumClassKt)
								setter.addStatement("target.${member.name} = value?.value ?: 0u")
							}
							getter(getter.build())
							setter(setter.build())
						}
						builder.doForEach(property) { addProperty(it) }
					}
					if (memberType is VkEnum) {
						val enumClassKt = kglClassMap.getValue(memberType.name)
						val property = buildPropertySpec({ PropertySpec.builder(memberNameKt, enumClassKt.copy(nullable = true)) }) {
							mutable()
							val getter = FunSpec.getterBuilder()
							val setter = FunSpec.setterBuilder().addParameter("value", enumClassKt)
							if (it == Platform.JVM) {
								getter.addStatement("return %T.from(target.${member.name}())", enumClassKt)
								setter.addStatement("target.${member.name}(value?.value ?: 0)")
							} else {
								getter.addStatement("return %T.from(target.${member.name})", enumClassKt)
								setter.addStatement("target.${member.name} = value?.value ?: 0u")
							}
							getter(getter.build())
							setter(setter.build())
						}
						builder.doForEach(property) { addProperty(it) }
					}
					if (memberType is VkPrimitive) {
						val property = if (member.type.asteriskCount == 1 && member.type.name == "char") {
							buildPropertySpec({ PropertySpec.builder(memberNameKt, STRING.copy(nullable = true)) }) {
								mutable()
								val getter = FunSpec.getterBuilder()
								val setter = FunSpec.setterBuilder().addParameter("value", STRING)
								if (it == Platform.JVM) {
									getter.addStatement("return target.${member.name.escapeKt()}String()")
									setter.addStatement("target.${member.name.escapeKt()}(value?.%M())", KglUtils.TO_VKTYPE)
								} else {
									getter.addStatement("return target.${member.name.escapeKt()}?.%M()", KtxC.TO_KSTRING)
									setter.addStatement("target.${member.name.escapeKt()} = value?.%M()", KglUtils.TO_VKTYPE)
								}
								getter(getter.build())
								setter(setter.build())
							}
						} else {
							val isVkVersion = typeOverrides["${struct.name}.${member.name}"] == "VkVersion"

							val memberTypeKt = if (isVkVersion) VK_VERSION else memberType.nameClass

							buildPropertySpec({ PropertySpec.builder(memberNameKt, memberTypeKt) }) { platform ->
								mutable()
								getter(FunSpec.getterBuilder().addStatement(buildString {
									append("return ")
									if (isVkVersion) append("VkVersion(")
									append("target.")
									append(member.name.escapeKt())
									if (platform == Platform.JVM) append("()")
									val vkType = memberType.run { if (platform == Platform.JVM) fromJVMVkType else fromNativeVkType }
									if (vkType.isNotBlank()) {
										append('.')
										append(vkType)
									}
									if (isVkVersion) append(")")
								}).build())
								setter(FunSpec.setterBuilder().addParameter("value", memberTypeKt).addStatement(buildString {
									append("target.")
									append(member.name.escapeKt())
									if (platform == Platform.JVM) append("(") else append(" = ")
									append("value")
									if (isVkVersion) append(".value")
									val vkType = memberType.run { if (platform == Platform.JVM) toJVMVkType else toNativeVkType }
									if (vkType.isNotBlank()) {
										append('.')
										append(vkType)
									}
									if (platform == Platform.JVM) append(")")
								}).build())
							}
						}
						builder.doForEach(property) { addProperty(it) }
					}
				}

				val hasPNext = struct.members.any { it.name == "pNext" }

				val initParams = initFun.common.parameters.mapTo(mutableListOf()) {
					if (it.type.isNullable) it.toBuilder().defaultValue("null").build() else it
				}
				val lambdaType = LambdaTypeName.get(ClassName("com.kgl.vulkan.dsls", structBuilderName), returnType = UNIT)
				if (requiredCount > 0) {
					initParams.add(ParameterSpec.builder("block", lambdaType).build())
				} else if (hasPNext || builder.common.propertySpecs.isNotEmpty() || builder.common.funSpecs.count { it.name != "init" } > 0) {
					initParams.add(ParameterSpec.builder("block", lambdaType).defaultValue("{}").build())
				}

				structInits.putIfAbsent(struct.name, listOf(initParams))

				FileSpec.get("com.kgl.vulkan.dsls", builder.common.build()).writeTo(commonDir.get().asFile)
				FileSpec.get("com.kgl.vulkan.dsls", builder.jvm.build()).writeTo(jvmDir.get().asFile)
				FileSpec.builder("com.kgl.vulkan.dsls", structBuilderName)
						.addType(builder.native.build())
						.addImport("com.kgl.vulkan.utils", "toVkBool")
						.addImport("com.kgl.vulkan.utils", "toBoolean")
						.addImport("kotlinx.cinterop", "get")
						.addImport("kotlinx.cinterop", "set")
						.build()
						.writeTo(nativeDir.get().asFile)
			}

			for (structName in requiredArrayBuilders) {
				val commonParamss = structInits[structName] ?: continue
				if (commonParamss.isEmpty()) continue

				val structBuilderClass = ClassName("com.kgl.vulkan.dsls", structName.removePrefix("Vk") + "Builder")

				val extension = extensionInvMap[structName]
				val cleanStructName = (if (extension != null) {
					structName.removeSuffix(extension.author)
				} else {
					structName
				}).removeSuffix("CreateInfo").removeSuffix("Info")

				val functionName = run {
					val startIndex = cleanStructName.dropLastWhile { it.isDigit() || it.isUpperCase() }
							.indexOfLast { it.isUpperCase() }
					check(startIndex >= 0) { "Cannot name $structName for struct array builder function." }

					cleanStructName.substring(startIndex).decapitalize()
				}

				val builder = with(TypeSpec.classBuilder(structName.removePrefix("Vk") + "sBuilder")) {
					val lamdaType = LambdaTypeName.get(parameters = *arrayOf(structBuilderClass), returnType = UNIT)
					addProperty(PropertySpec.builder("targets", MUTABLE_LIST.parameterizedBy(lamdaType), KModifier.INTERNAL)
							.initializer("mutableListOf()").build())
					addAnnotation(AnnotationSpec.builder(STRUCT_MARKER).build())

					for (commonParams in commonParamss) {
						val hasLambda = commonParams.lastOrNull()?.let { it.type is LambdaTypeName } == true
						val platformParams = commonParams.map {
							ParameterSpec.builder(it.name, it.type, *it.modifiers.toTypedArray()).build()
						}
						val passedParams = platformParams.dropLast(if (hasLambda) 1 else 0)

						addFunction(FunSpec.builder(functionName).run {
							addParameters(commonParams)

							beginControlFlow("targets += {")
							addStatement(passedParams.joinToString(", ", prefix = "it.init(", postfix = ")") { it.name })
							if (hasLambda) addStatement("it.apply(block)")
							endControlFlow()
							build()
						})
					}
					build()
				}

				FileSpec.get("com.kgl.vulkan.dsls", builder).writeTo(commonDir.get().asFile)
			}
		}

		generateDispatchTables()
		generateEnums()
		generateAPIConstants()
		generateExceptions()
		generateOutputStructs()
		generateInputStructBuilders()
	}
}
