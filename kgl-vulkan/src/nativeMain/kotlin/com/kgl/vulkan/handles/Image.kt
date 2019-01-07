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
import com.kgl.vulkan.enums.ImageViewType
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class Image(override val ptr: VkImage, actual val device: Device) : VkHandleNative<VkImage>(), VkHandle {
	actual val memoryRequirements: MemoryRequirements
		get() {
			val image = this
			val device = image.device
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkMemoryRequirements>()
				val outputPtr = outputVar.ptr
				vkGetImageMemoryRequirements(device.toVkType(), image.toVkType(), outputPtr)
				return MemoryRequirements.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val sparseMemoryRequirements: List<SparseImageMemoryRequirements>
		get() {
			val image = this
			val device = image.device
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				vkGetImageSparseMemoryRequirements(device.toVkType(), image.toVkType(),
						outputCountPtr, null)
				val outputPtr =
						VirtualStack.allocArray<VkSparseImageMemoryRequirements>(outputCountVar.value.toInt())
				vkGetImageSparseMemoryRequirements(device.toVkType(), image.toVkType(),
						outputCountPtr, outputPtr)
				return List(outputCountVar.value.toInt()) {
					SparseImageMemoryRequirements.from(outputPtr[it])
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual val drmFormatModifierPropertiesEXT: ImageDrmFormatModifierPropertiesEXT
		get() {
			val image = this
			val device = image.device
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkImageDrmFormatModifierPropertiesEXT>()
				val outputPtr = outputVar.ptr
				val result = vkGetImageDrmFormatModifierPropertiesEXT(device.toVkType(),
						image.toVkType(), outputPtr)
				if (result != VK_SUCCESS) handleVkResult(result)
				return ImageDrmFormatModifierPropertiesEXT.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		val image = this
		val device = image.device
		VirtualStack.push()
		try {
			vkDestroyImage(device.toVkType(), image.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindMemory(memory: DeviceMemory, memoryOffset: ULong) {
		val image = this
		val device = image.device
		VirtualStack.push()
		try {
			val result = vkBindImageMemory(device.toVkType(), image.toVkType(), memory.toVkType(),
					memoryOffset.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSubresourceLayout(block: ImageSubresourceBuilder.() -> Unit): SubresourceLayout {
		val image = this
		val device = image.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkImageSubresource>().ptr
			val builder = ImageSubresourceBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkSubresourceLayout>()
			val outputPtr = outputVar.ptr
			vkGetImageSubresourceLayout(device.toVkType(), image.toVkType(), target, outputPtr)
			return SubresourceLayout.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createView(
			viewType: ImageViewType,
			format: Format,
			block: ImageViewCreateInfoBuilder.() -> Unit
	): ImageView {
		val image = this
		val device = image.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkImageViewCreateInfo>().ptr
			val builder = ImageViewCreateInfoBuilder(target.pointed)
			builder.init(image, viewType, format)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkImageViewVar>()
			val outputPtr = outputVar.ptr
			val result = vkCreateImageView(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ImageView(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getMemoryRequirements2(block: ImageMemoryRequirementsInfo2Builder.() -> Unit): MemoryRequirements2 {
		val image = this
		val device = image.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkImageMemoryRequirementsInfo2>().ptr
			val builder = ImageMemoryRequirementsInfo2Builder(target.pointed)
			builder.init(image)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkMemoryRequirements2>()
			val outputPtr = outputVar.ptr
			vkGetImageMemoryRequirements2(device.toVkType(), target, outputPtr)
			return MemoryRequirements2.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSparseMemoryRequirements2(block: ImageSparseMemoryRequirementsInfo2Builder.() -> Unit): List<SparseImageMemoryRequirements2> {
		val image = this
		val device = image.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkImageSparseMemoryRequirementsInfo2>().ptr
			val builder = ImageSparseMemoryRequirementsInfo2Builder(target.pointed)
			builder.init(image)
			builder.apply(block)
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			vkGetImageSparseMemoryRequirements2(device.toVkType(), target, outputCountPtr, null)
			val outputPtr =
					VirtualStack.allocArray<VkSparseImageMemoryRequirements2>(outputCountVar.value.toInt())
			vkGetImageSparseMemoryRequirements2(device.toVkType(), target, outputCountPtr,
					outputPtr)
			return List(outputCountVar.value.toInt()) { SparseImageMemoryRequirements2.from(outputPtr[it]) }
		} finally {
			VirtualStack.pop()
		}
	}
}

