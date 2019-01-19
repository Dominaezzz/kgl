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

import com.kgl.vulkan.enums.*
import com.kgl.vulkan.handles.Image
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkImageCreateInfo
import cvulkan.VkImageMemoryRequirementsInfo2
import cvulkan.VkImageSparseMemoryRequirementsInfo2

actual class ImageCreateInfoBuilder(internal val target: VkImageCreateInfo) {
	actual var flags: VkFlag<ImageCreate>?
		get() = ImageCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var imageType: ImageType?
		get() = ImageType.from(target.imageType)
		set(value) {
			target.imageType = value.toVkType()
		}

	actual var format: Format?
		get() = Format.from(target.format)
		set(value) {
			target.format = value.toVkType()
		}

	actual var mipLevels: UInt
		get() = target.mipLevels
		set(value) {
			target.mipLevels = value.toVkType()
		}

	actual var arrayLayers: UInt
		get() = target.arrayLayers
		set(value) {
			target.arrayLayers = value.toVkType()
		}

	actual var samples: SampleCount?
		get() = SampleCount.from(target.samples)
		set(value) {
			target.samples = value.toVkType()
		}

	actual var tiling: ImageTiling?
		get() = ImageTiling.from(target.tiling)
		set(value) {
			target.tiling = value.toVkType()
		}

	actual var usage: VkFlag<ImageUsage>?
		get() = ImageUsage.fromMultiple(target.usage)
		set(value) {
			target.usage = value.toVkType()
		}

	actual var sharingMode: SharingMode?
		get() = SharingMode.from(target.sharingMode)
		set(value) {
			target.sharingMode = value.toVkType()
		}

	actual var initialLayout: ImageLayout?
		get() = ImageLayout.from(target.initialLayout)
		set(value) {
			target.initialLayout = value.toVkType()
		}

	actual fun extent(width: UInt, height: UInt, depth: UInt) {
		val subTarget = target.extent
		val builder = Extent3DBuilder(subTarget)
		builder.init(width, height, depth)
	}

	internal fun init(queueFamilyIndices: UIntArray?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO
		target.pNext = null
		if (queueFamilyIndices != null) {
			target.pQueueFamilyIndices = queueFamilyIndices.toVkType()
			target.queueFamilyIndexCount = queueFamilyIndices.size.toUInt()
		} else {
			target.queueFamilyIndexCount = 0U
		}
	}
}

actual class ImageMemoryRequirementsInfo2Builder(internal val target: VkImageMemoryRequirementsInfo2) {
	internal fun init(image: Image) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_IMAGE_MEMORY_REQUIREMENTS_INFO_2
		target.pNext = null
		target.image = image.toVkType()
	}
}

actual class ImageSparseMemoryRequirementsInfo2Builder(internal val target: VkImageSparseMemoryRequirementsInfo2) {
	internal fun init(image: Image) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_IMAGE_SPARSE_MEMORY_REQUIREMENTS_INFO_2
		target.pNext = null
		target.image = image.toVkType()
	}
}

