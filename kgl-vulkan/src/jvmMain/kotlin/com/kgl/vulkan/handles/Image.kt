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
import org.lwjgl.system.*
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.EXTImageDrmFormatModifier.*
import org.lwjgl.vulkan.VK11.*

actual class Image(
	override val ptr: Long,
	actual val device: Device,
	actual val type: ImageType,
	actual val format: Format,
	actual val mipLevels: UInt,
	actual val extent: Extent3D,
	actual val arrayLayers: UInt
) : VkHandleJVM<Long>(), VkHandle {
	internal var _memory: DeviceMemory? = null
	internal var _memoryOffset: ULong = 0U

	actual val memory: DeviceMemory? get() = _memory
	actual val memoryOffset: ULong get() = _memoryOffset

	actual val memoryRequirements: MemoryRequirements
		get() {
			val image = this
			val device = image.device
			MemoryStack.stackPush()
			try {
				val outputPtr = VkMemoryRequirements.mallocStack()
				vkGetImageMemoryRequirements(device.toVkType(), image.toVkType(), outputPtr)
				return MemoryRequirements.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val sparseMemoryRequirements: List<SparseImageMemoryRequirements>
		get() {
			val image = this
			val device = image.device
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				vkGetImageSparseMemoryRequirements(
					device.toVkType(), image.toVkType(),
					outputCountPtr, null
				)
				val outputPtr = VkSparseImageMemoryRequirements.mallocStack(outputCountPtr[0])
				vkGetImageSparseMemoryRequirements(
					device.toVkType(), image.toVkType(),
					outputCountPtr, outputPtr
				)
				return List(outputCountPtr[0]) { SparseImageMemoryRequirements.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val drmFormatModifierPropertiesEXT: ImageDrmFormatModifierPropertiesEXT
		get() {
			val image = this
			val device = image.device
			MemoryStack.stackPush()
			try {
				val outputPtr = VkImageDrmFormatModifierPropertiesEXT.mallocStack()
				val result = vkGetImageDrmFormatModifierPropertiesEXT(
					device.toVkType(),
					image.toVkType(), outputPtr
				)
				if (result != VK_SUCCESS) handleVkResult(result)
				return ImageDrmFormatModifierPropertiesEXT.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		val image = this
		val device = image.device
		MemoryStack.stackPush()
		try {
			vkDestroyImage(device.toVkType(), image.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindMemory(memory: DeviceMemory, memoryOffset: ULong) {
		val image = this
		val device = image.device
		MemoryStack.stackPush()
		try {
			val result = vkBindImageMemory(
				device.toVkType(), image.toVkType(), memory.toVkType(),
				memoryOffset.toVkType()
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			_memory = memory
			_memoryOffset = memoryOffset
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSubresourceLayout(block: ImageSubresourceBuilder.() -> Unit): SubresourceLayout {
		val image = this
		val device = image.device
		MemoryStack.stackPush()
		try {
			val target = VkImageSubresource.callocStack()
			val builder = ImageSubresourceBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = VkSubresourceLayout.mallocStack()
			vkGetImageSubresourceLayout(device.toVkType(), image.toVkType(), target, outputPtr)
			return SubresourceLayout.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createView(
		viewType: ImageViewType,
		format: Format,
		block: ImageViewCreateInfoBuilder.() -> Unit
	): ImageView {
		val image = this
		val device = image.device
		MemoryStack.stackPush()
		try {
			val target = VkImageViewCreateInfo.callocStack()
			val builder = ImageViewCreateInfoBuilder(target)
			builder.init(image, viewType, format)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateImageView(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ImageView(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getMemoryRequirements2(block: ImageMemoryRequirementsInfo2Builder.() -> Unit): MemoryRequirements2 {
		val image = this
		val device = image.device
		MemoryStack.stackPush()
		try {
			val target = VkImageMemoryRequirementsInfo2.callocStack()
			val builder = ImageMemoryRequirementsInfo2Builder(target)
			builder.init(image)
			builder.apply(block)
			val outputPtr = VkMemoryRequirements2.mallocStack()
			vkGetImageMemoryRequirements2(device.toVkType(), target, outputPtr)
			return MemoryRequirements2.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSparseMemoryRequirements2(block: ImageSparseMemoryRequirementsInfo2Builder.() -> Unit): List<SparseImageMemoryRequirements2> {
		val image = this
		val device = image.device
		MemoryStack.stackPush()
		try {
			val target = VkImageSparseMemoryRequirementsInfo2.callocStack()
			val builder = ImageSparseMemoryRequirementsInfo2Builder(target)
			builder.init(image)
			builder.apply(block)
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			vkGetImageSparseMemoryRequirements2(device.toVkType(), target, outputCountPtr, null)
			val outputPtr = VkSparseImageMemoryRequirements2.mallocStack(outputCountPtr[0])
			vkGetImageSparseMemoryRequirements2(
				device.toVkType(), target, outputCountPtr,
				outputPtr
			)
			return List(outputCountPtr[0]) { SparseImageMemoryRequirements2.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}
}
