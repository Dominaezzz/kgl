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
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value

actual class Buffer(
		override val ptr: VkBuffer,
		actual val device: Device,
		actual val size: ULong
) : VkHandleNative<VkBuffer>(), VkHandle {
	internal var _memory: DeviceMemory? = null
	internal var _memoryOffset: ULong = 0U

	actual val memory: DeviceMemory? get() = _memory
	actual val memoryOffset: ULong get() = _memoryOffset

	actual val memoryRequirements: MemoryRequirements
		get() {
			val buffer = this
			val device = buffer.device
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkMemoryRequirements>()
				val outputPtr = outputVar.ptr
				vkGetBufferMemoryRequirements(device.toVkType(), buffer.toVkType(), outputPtr)
				return MemoryRequirements.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		val buffer = this
		val device = buffer.device
		VirtualStack.push()
		try {
			vkDestroyBuffer(device.toVkType(), buffer.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindMemory(memory: DeviceMemory, memoryOffset: ULong) {
		val buffer = this
		val device = buffer.device
		VirtualStack.push()
		try {
			val result = vkBindBufferMemory(device.toVkType(), buffer.toVkType(), memory.toVkType(),
					memoryOffset.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
			_memory = memory
			_memoryOffset = memoryOffset
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkBufferViewCreateInfo>().ptr
			val builder = BufferViewCreateInfoBuilder(target.pointed)
			builder.init(buffer, format, offset, range)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkBufferViewVar>()
			val outputPtr = outputVar.ptr
			val result = vkCreateBufferView(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return BufferView(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getMemoryRequirements2(block: BufferMemoryRequirementsInfo2Builder.() -> Unit): MemoryRequirements2 {
		val buffer = this
		val device = buffer.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkBufferMemoryRequirementsInfo2>().ptr
			val builder = BufferMemoryRequirementsInfo2Builder(target.pointed)
			builder.init(buffer)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkMemoryRequirements2>()
			val outputPtr = outputVar.ptr
			vkGetBufferMemoryRequirements2(device.toVkType(), target, outputPtr)
			return MemoryRequirements2.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}
}

