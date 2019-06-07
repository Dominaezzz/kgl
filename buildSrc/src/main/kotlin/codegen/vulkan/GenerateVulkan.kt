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

import codegen.KtxC
import codegen.STRING
import com.squareup.kotlinpoet.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
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

		val vkTypeMap = (registry.enums + registry.structs + registry.handles + registry.funcPointers + registry.flags + primitives)
				.associateBy { it.name }

		val extensionInvMap: Map<String, Registry.Extension> = run {
			val commandLookup = registry.commands.associateBy { it.name }

			val extMap = mutableMapOf<String, Registry.Extension>()

			for (extension in registry.extensions.filter { it.name.startsWith("VK") }) {
				val tagName = extension.name.splitToSequence('_').elementAt(1)

				fun crawl(member: IVkParam) {
					val vkType = vkTypeMap[member.type.name] ?: return

					if (vkType.name.endsWith(tagName)) {
						extMap.putIfAbsent(vkType.name, extension)
						if (vkType is VkFlag && vkType.requires != null) {
							extMap.putIfAbsent(vkType.requires, extension)
						}
					} else if (vkType is VkEnum && member is VkStruct.Member && vkType.name == "VkStructureType") {
						member.values.filter { it.endsWith(tagName) }.forEach {
							extMap.putIfAbsent(it, extension)
						}
					}
					if (vkType is VkStruct) {
						vkType.members.forEach(::crawl)
					} else if (vkType is VkFuncPointer) {
						vkType.params.forEach(::crawl)
					}
				}

				for (require in extension.requires) {
					require.commands.forEach { extMap.putIfAbsent(it, extension) }
					require.types.forEach { extMap.putIfAbsent(it, extension) }
					require.constants.keys.forEach { extMap.putIfAbsent(it, extension) }

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
			extMap
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


		generateDispatchTables()
	}
}
