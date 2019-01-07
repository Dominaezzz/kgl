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
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.VkHandle

expect class PhysicalDevice : VkHandle {
	val instance: Instance

	val properties: PhysicalDeviceProperties

	val queueFamilyProperties: List<QueueFamilyProperties>

	val memoryProperties: PhysicalDeviceMemoryProperties

	val features: PhysicalDeviceFeatures

	val deviceLayerProperties: List<LayerProperties>

	val displayPropertiesKHR: List<DisplayPropertiesKHR>

	val displayPlanePropertiesKHR: List<DisplayPlanePropertiesKHR>

	val features2: PhysicalDeviceFeatures2

	val properties2: PhysicalDeviceProperties2

	val queueFamilyProperties2: List<QueueFamilyProperties2>

	val memoryProperties2: PhysicalDeviceMemoryProperties2

	val displayProperties2KHR: List<DisplayProperties2KHR>

	val displayPlaneProperties2KHR: List<DisplayPlaneProperties2KHR>

	val calibrateableTimeDomainsEXT: List<TimeDomainEXT>

	fun getFormatProperties(format: Format): FormatProperties

	fun getImageFormatProperties(
			format: Format,
			type: ImageType,
			tiling: ImageTiling,
			usage: VkFlag<ImageUsage>,
			flags: VkFlag<ImageCreate>?
	): ImageFormatProperties

	fun createDevice(
			enabledLayerNames: Collection<String>? = null,
			enabledExtensionNames: Collection<String>? = null,
			block: DeviceCreateInfoBuilder.() -> Unit
	): Device

	fun getDeviceExtensionProperties(layerName: String? = null): List<ExtensionProperties>

	fun getSparseImageFormatProperties(
			format: Format,
			type: ImageType,
			samples: SampleCount,
			usage: VkFlag<ImageUsage>,
			tiling: ImageTiling
	): List<SparseImageFormatProperties>

	fun getDisplayPlaneSupportedDisplaysKHR(planeIndex: UInt): List<DisplayKHR>

	fun getDisplayModePropertiesKHR(display: DisplayKHR): List<DisplayModePropertiesKHR>

	fun createDisplayModeKHR(display: DisplayKHR, block: DisplayModeCreateInfoKHRBuilder.() -> Unit): DisplayModeKHR

	fun getSurfaceSupportKHR(queueFamilyIndex: UInt, surface: SurfaceKHR): Boolean

	fun getSurfaceCapabilitiesKHR(surface: SurfaceKHR): SurfaceCapabilitiesKHR

	fun getSurfaceFormatsKHR(surface: SurfaceKHR): List<SurfaceFormatKHR>

	fun getSurfacePresentModesKHR(surface: SurfaceKHR): List<PresentModeKHR>

	fun getExternalImageFormatPropertiesNV(
			format: Format,
			type: ImageType,
			tiling: ImageTiling,
			usage: VkFlag<ImageUsage>,
			flags: VkFlag<ImageCreate>?,
			externalHandleType: VkFlag<ExternalMemoryHandleTypeNV>?
	): ExternalImageFormatPropertiesNV

	fun getFormatProperties2(format: Format): FormatProperties2

	fun getImageFormatProperties2(block: PhysicalDeviceImageFormatInfo2Builder.() -> Unit = {}): ImageFormatProperties2

	fun getSparseImageFormatProperties2(block: PhysicalDeviceSparseImageFormatInfo2Builder.() -> Unit = {}): List<SparseImageFormatProperties2>

	fun getExternalBufferProperties(block: PhysicalDeviceExternalBufferInfoBuilder.() -> Unit = {}): ExternalBufferProperties

	fun getExternalSemaphoreProperties(block: PhysicalDeviceExternalSemaphoreInfoBuilder.() -> Unit = {}): ExternalSemaphoreProperties

	fun getExternalFenceProperties(block: PhysicalDeviceExternalFenceInfoBuilder.() -> Unit = {}): ExternalFenceProperties

	fun releaseDisplayEXT(display: DisplayKHR)

	fun getSurfaceCapabilities2EXT(surface: SurfaceKHR): SurfaceCapabilities2EXT

	fun getPresentRectanglesKHR(surface: SurfaceKHR): List<Rect2D>

	fun getMultisamplePropertiesEXT(samples: SampleCount): MultisamplePropertiesEXT

	fun getSurfaceCapabilities2KHR(surface: SurfaceKHR, block: PhysicalDeviceSurfaceInfo2KHRBuilder.() -> Unit = {}): SurfaceCapabilities2KHR

	fun getSurfaceFormats2KHR(surface: SurfaceKHR, block: PhysicalDeviceSurfaceInfo2KHRBuilder.() -> Unit = {}): List<SurfaceFormat2KHR>

	fun getDisplayModeProperties2KHR(display: DisplayKHR): List<DisplayModeProperties2KHR>
}

