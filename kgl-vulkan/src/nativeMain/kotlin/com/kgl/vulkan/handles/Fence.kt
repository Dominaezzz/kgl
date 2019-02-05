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
import com.kgl.vulkan.dsls.FenceGetFdInfoKHRBuilder
import com.kgl.vulkan.dsls.ImportFenceFdInfoKHRBuilder
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import cvulkan.*
import kotlinx.cinterop.*

actual class Fence(override val ptr: VkFence, actual val device: Device) : VkHandleNative<VkFence>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val fence = this
		val device = fence.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyFence(device.toVkType(), fence.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual val isSignalled: Boolean
		get() {
			val fence = this
			val device = fence.device
			VirtualStack.push()
			try {
				val result = dispatchTable.vkGetFenceStatus(device.toVkType(), fence.toVkType())
				return when (result) {
					VK_SUCCESS -> true
					VK_NOT_READY -> false
					else -> handleVkResult(result)
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual fun getFdKHR(block: FenceGetFdInfoKHRBuilder.() -> Unit): Int {
		val fence = this
		val device = fence.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkFenceGetFdInfoKHR>().ptr
			val builder = FenceGetFdInfoKHRBuilder(target.pointed)
			builder.init(fence)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<IntVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetFenceFdKHR!!(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputVar.value
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun importFdKHR(block: ImportFenceFdInfoKHRBuilder.() -> Unit) {
		val fence = this
		val device = fence.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkImportFenceFdInfoKHR>().ptr
			val builder = ImportFenceFdInfoKHRBuilder(target.pointed)
			builder.init(fence)
			builder.apply(block)
			val result = dispatchTable.vkImportFenceFdKHR!!(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}
}

