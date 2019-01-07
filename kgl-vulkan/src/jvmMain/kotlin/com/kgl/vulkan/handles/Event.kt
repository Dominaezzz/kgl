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

import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.VK11.*

actual class Event(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val event = this
		val device = event.device
		MemoryStack.stackPush()
		try {
			vkDestroyEvent(device.toVkType(), event.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual val isSignalled: Boolean
		get() {
			val event = this
			val device = event.device
			MemoryStack.stackPush()
			try {
				val result = vkGetEventStatus(device.toVkType(), event.toVkType())
				return when (result) {
					VK_EVENT_SET -> true
					VK_EVENT_RESET -> false
					else -> handleVkResult(result)
				}
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual fun set() {
		val event = this
		val device = event.device
		MemoryStack.stackPush()
		try {
			val result = vkSetEvent(device.toVkType(), event.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun reset() {
		val event = this
		val device = event.device
		MemoryStack.stackPush()
		try {
			val result = vkResetEvent(device.toVkType(), event.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

