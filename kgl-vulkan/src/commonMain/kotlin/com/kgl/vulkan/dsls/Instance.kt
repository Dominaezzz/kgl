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
package com.kgl.vulkan.dsls

import com.kgl.vulkan.utils.VkVersion

expect class ApplicationInfoBuilder {
	var applicationName: String?

	var applicationVersion: VkVersion

	var engineName: String?

	var engineVersion: VkVersion

	var apiVersion: VkVersion
}

expect class InstanceCreateInfoBuilder {
	fun applicationInfo(block: ApplicationInfoBuilder.() -> Unit = {})
}

expect class DebugUtilsLabelsEXTBuilder {
	fun label(block: DebugUtilsLabelEXTBuilder.() -> Unit = {})
}

expect class DebugUtilsObjectNameInfoEXTsBuilder {
	fun info(block: DebugUtilsObjectNameInfoEXTBuilder.() -> Unit = {})
}

expect class DebugUtilsMessengerCallbackDataEXTBuilder {
	var messageIdName: String?

	var messageIdNumber: Int

	var message: String?

	fun queueLabels(block: DebugUtilsLabelsEXTBuilder.() -> Unit)

	fun cmdBufLabels(block: DebugUtilsLabelsEXTBuilder.() -> Unit)

	fun objects(block: DebugUtilsObjectNameInfoEXTsBuilder.() -> Unit)
}

