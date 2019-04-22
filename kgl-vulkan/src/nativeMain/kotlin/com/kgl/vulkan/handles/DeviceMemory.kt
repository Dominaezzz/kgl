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

import com.kgl.core.VirtualStack
import com.kgl.vulkan.dsls.MemoryGetFdInfoKHRBuilder
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import cvulkan.VK_SUCCESS
import cvulkan.VkDeviceMemory
import cvulkan.VkMemoryGetFdInfoKHR
import kotlinx.cinterop.*
import kotlinx.io.core.IoBuffer

actual class DeviceMemory(
		override val ptr: VkDeviceMemory,
		actual val device: Device,
		actual val size: ULong,
		actual val memoryTypeIndex: UInt
) : VkHandleNative<VkDeviceMemory>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	actual val commitment: ULong
		get() {
			val memory = this
			val device = memory.device
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<ULongVar>()
				val outputPtr = outputVar.ptr
				dispatchTable.vkGetDeviceMemoryCommitment(device.toVkType(), memory.toVkType(), outputPtr)
				return outputVar.value
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		val memory = this
		val device = memory.device
		VirtualStack.push()
		try {
			dispatchTable.vkFreeMemory(device.toVkType(), memory.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun map(offset: ULong, size: ULong): IoBuffer {
		val memory = this
		val device = memory.device
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<COpaquePointerVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkMapMemory(device.toVkType(), memory.toVkType(), offset.toVkType(),
					size.toVkType(), 0U.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return IoBuffer(outputVar.value!!.reinterpret(), size.toInt())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun unmap() {
		val memory = this
		val device = memory.device
		VirtualStack.push()
		try {
			dispatchTable.vkUnmapMemory(device.toVkType(), memory.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getFdKHR(block: MemoryGetFdInfoKHRBuilder.() -> Unit): Int {
		val memory = this
		val device = memory.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkMemoryGetFdInfoKHR>().ptr
			val builder = MemoryGetFdInfoKHRBuilder(target.pointed)
			builder.init(memory)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<IntVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetMemoryFdKHR!!(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputVar.value
		} finally {
			VirtualStack.pop()
		}
	}
}

