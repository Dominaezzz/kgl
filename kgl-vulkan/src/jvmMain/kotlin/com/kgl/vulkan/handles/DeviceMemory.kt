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

import com.kgl.vulkan.dsls.MemoryGetFdInfoKHRBuilder
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import kotlinx.io.core.IoBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.KHRExternalMemoryFd.vkGetMemoryFdKHR
import org.lwjgl.vulkan.VK11.*
import org.lwjgl.vulkan.VkMemoryGetFdInfoKHR

actual class DeviceMemory(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	actual val commitment: ULong
		get() {
			val memory = this
			val device = memory.device
			MemoryStack.stackPush()
			try {
				val outputPtr = MemoryStack.stackGet().mallocLong(1)
				vkGetDeviceMemoryCommitment(device.toVkType(), memory.toVkType(), outputPtr)
				return outputPtr[0].toULong()
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		val memory = this
		val device = memory.device
		MemoryStack.stackPush()
		try {
			vkFreeMemory(device.toVkType(), memory.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun map(offset: ULong, size: ULong): IoBuffer {
		val memory = this
		val device = memory.device
		MemoryStack.stackPush()
		try {
			val outputPtr = MemoryStack.stackGet().mallocPointer(1)
			val result = vkMapMemory(device.toVkType(), memory.toVkType(), offset.toVkType(),
					size.toVkType(), 0U.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return IoBuffer(outputPtr.getByteBuffer(size.toInt()))
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun unmap() {
		val memory = this
		val device = memory.device
		MemoryStack.stackPush()
		try {
			vkUnmapMemory(device.toVkType(), memory.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getFdKHR(block: MemoryGetFdInfoKHRBuilder.() -> Unit): Int {
		val memory = this
		val device = memory.device
		MemoryStack.stackPush()
		try {
			val target = VkMemoryGetFdInfoKHR.callocStack()
			val builder = MemoryGetFdInfoKHRBuilder(target)
			builder.init(memory)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetMemoryFdKHR(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputPtr[0]
		} finally {
			MemoryStack.stackPop()
		}
	}
}

