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

import com.kgl.vulkan.dsls.BufferMemoryRequirementsInfo2Builder
import com.kgl.vulkan.dsls.BufferViewCreateInfoBuilder
import com.kgl.vulkan.enums.Format
import com.kgl.vulkan.structs.MemoryRequirements
import com.kgl.vulkan.structs.MemoryRequirements2
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.VK11.*
import org.lwjgl.vulkan.VkBufferMemoryRequirementsInfo2
import org.lwjgl.vulkan.VkBufferViewCreateInfo
import org.lwjgl.vulkan.VkMemoryRequirements
import org.lwjgl.vulkan.VkMemoryRequirements2

actual class Buffer(
		override val ptr: Long,
		actual val device: Device,
		actual val size: ULong
) : VkHandleJVM<Long>(), VkHandle {
	internal var _memory: DeviceMemory? = null
	internal var _memoryOffset: ULong = 0U

	actual val memory: DeviceMemory? get() = _memory
	actual val memoryOffset: ULong get() = _memoryOffset

	actual val memoryRequirements: MemoryRequirements
		get() {
			val buffer = this
			val device = buffer.device
			MemoryStack.stackPush()
			try {
				val outputPtr = VkMemoryRequirements.mallocStack()
				vkGetBufferMemoryRequirements(device.toVkType(), buffer.toVkType(), outputPtr)
				return MemoryRequirements.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		val buffer = this
		val device = buffer.device
		MemoryStack.stackPush()
		try {
			vkDestroyBuffer(device.toVkType(), buffer.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindMemory(memory: DeviceMemory, memoryOffset: ULong) {
		val buffer = this
		val device = buffer.device
		MemoryStack.stackPush()
		try {
			val result = vkBindBufferMemory(device.toVkType(), buffer.toVkType(), memory.toVkType(),
					memoryOffset.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
			_memory = memory
			_memoryOffset = memoryOffset
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createView(
			format: Format,
			offset: ULong,
			range: ULong,
			block: BufferViewCreateInfoBuilder.() -> Unit
	): BufferView {
		val buffer = this
		val device = buffer.device
		MemoryStack.stackPush()
		try {
			val target = VkBufferViewCreateInfo.callocStack()
			val builder = BufferViewCreateInfoBuilder(target)
			builder.init(buffer, format, offset, range)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateBufferView(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return BufferView(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getMemoryRequirements2(block: BufferMemoryRequirementsInfo2Builder.() -> Unit): MemoryRequirements2 {
		val buffer = this
		val device = buffer.device
		MemoryStack.stackPush()
		try {
			val target = VkBufferMemoryRequirementsInfo2.callocStack()
			val builder = BufferMemoryRequirementsInfo2Builder(target)
			builder.init(buffer)
			builder.apply(block)
			val outputPtr = VkMemoryRequirements2.mallocStack()
			vkGetBufferMemoryRequirements2(device.toVkType(), target, outputPtr)
			return MemoryRequirements2.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

