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

import com.kgl.core.utils.VirtualStack
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import cvulkan.VK_EVENT_RESET
import cvulkan.VK_EVENT_SET
import cvulkan.VK_SUCCESS
import cvulkan.VkEvent
import kotlinx.cinterop.invoke

actual class Event(override val ptr: VkEvent, actual val device: Device) : VkHandleNative<VkEvent>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val event = this
		val device = event.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyEvent(device.toVkType(), event.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual val isSignalled: Boolean
		get() {
			val event = this
			val device = event.device
			VirtualStack.push()
			try {
				val result = dispatchTable.vkGetEventStatus(device.toVkType(), event.toVkType())
				return when (result) {
					VK_EVENT_SET -> true
					VK_EVENT_RESET -> false
					else -> handleVkResult(result)
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual fun set() {
		val event = this
		val device = event.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkSetEvent(device.toVkType(), event.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun reset() {
		val event = this
		val device = event.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkResetEvent(device.toVkType(), event.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}
}

