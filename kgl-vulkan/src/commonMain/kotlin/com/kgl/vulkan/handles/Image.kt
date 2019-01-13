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

import com.kgl.vulkan.dsls.ImageMemoryRequirementsInfo2Builder
import com.kgl.vulkan.dsls.ImageSparseMemoryRequirementsInfo2Builder
import com.kgl.vulkan.dsls.ImageSubresourceBuilder
import com.kgl.vulkan.dsls.ImageViewCreateInfoBuilder
import com.kgl.vulkan.enums.Format
import com.kgl.vulkan.enums.ImageType
import com.kgl.vulkan.enums.ImageViewType
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.VkHandle

expect class Image : VkHandle {
	val device: Device
	val type: ImageType
	val format: Format
	val mipLevels: UInt
	val extent: Extent3D
	val arrayLayers: UInt

	val memory: DeviceMemory?
	val memoryOffset: ULong

	val memoryRequirements: MemoryRequirements

	val sparseMemoryRequirements: List<SparseImageMemoryRequirements>

	val drmFormatModifierPropertiesEXT: ImageDrmFormatModifierPropertiesEXT

	fun bindMemory(memory: DeviceMemory, memoryOffset: ULong)

	fun getSubresourceLayout(block: ImageSubresourceBuilder.() -> Unit = {}): SubresourceLayout

	fun createView(
			viewType: ImageViewType,
			format: Format,
			block: ImageViewCreateInfoBuilder.() -> Unit
	): ImageView

	fun getMemoryRequirements2(block: ImageMemoryRequirementsInfo2Builder.() -> Unit = {}): MemoryRequirements2

	fun getSparseMemoryRequirements2(block: ImageSparseMemoryRequirementsInfo2Builder.() -> Unit =
			                                 {}): List<SparseImageMemoryRequirements2>
}

