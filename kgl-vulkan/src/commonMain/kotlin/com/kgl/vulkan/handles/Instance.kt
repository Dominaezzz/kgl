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
package com.kgl.vulkan.handles

import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*

expect class Instance : VkHandle {
	val physicalDevices: List<PhysicalDevice>

	val physicalDeviceGroups: List<PhysicalDeviceGroupProperties>

	fun getProcAddr(name: String): Long

	fun createDisplayPlaneSurfaceKHR(
		displayMode: DisplayModeKHR,
		block: DisplaySurfaceCreateInfoKHRBuilder.() -> Unit
	): SurfaceKHR

	fun createDebugUtilsMessengerEXT(block: DebugUtilsMessengerCreateInfoEXTBuilder.() -> Unit): DebugUtilsMessengerEXT

	fun createDebugReportCallbackEXT(block: DebugReportCallbackCreateInfoEXTBuilder.() -> Unit): DebugReportCallbackEXT

	fun debugReportMessageEXT(
		flags: VkFlag<DebugReportEXT>,
		objectType: DebugReportObjectTypeEXT,
		`object`: ULong,
		location: ULong,
		messageCode: Int,
		layerPrefix: String,
		message: String
	)

	fun submitDebugUtilsMessageEXT(
		messageSeverity: DebugUtilsMessageSeverityEXT,
		messageTypes: VkFlag<DebugUtilsMessageTypeEXT>,
		block: DebugUtilsMessengerCallbackDataEXTBuilder.() -> Unit
	)

	companion object {
		val version: VkVersion

		val layerProperties: List<LayerProperties>

		fun getExtensionProperties(layerName: String? = null): List<ExtensionProperties>

		fun create(
			enabledLayerNames: Collection<String>? = null,
			enabledExtensionNames: Collection<String>? = null,
			block: InstanceCreateInfoBuilder.() -> Unit = {}
		): Instance
	}
}
