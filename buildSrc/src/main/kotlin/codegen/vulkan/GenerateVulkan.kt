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
	}
}
