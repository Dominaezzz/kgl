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

import com.kgl.vulkan.dsls.FenceGetFdInfoKHRBuilder
import com.kgl.vulkan.dsls.ImportFenceFdInfoKHRBuilder
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.KHRExternalFenceFd.vkGetFenceFdKHR
import org.lwjgl.vulkan.KHRExternalFenceFd.vkImportFenceFdKHR
import org.lwjgl.vulkan.VK11.*
import org.lwjgl.vulkan.VkFenceGetFdInfoKHR
import org.lwjgl.vulkan.VkImportFenceFdInfoKHR

actual class Fence(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val fence = this
		val device = fence.device
		MemoryStack.stackPush()
		try {
			vkDestroyFence(device.toVkType(), fence.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual val isSignalled: Boolean
		get() {
			val fence = this
			val device = fence.device
			MemoryStack.stackPush()
			try {
				val result = vkGetFenceStatus(device.toVkType(), fence.toVkType())
				return when (result) {
					VK_SUCCESS -> true
					VK_NOT_READY -> false
					else -> handleVkResult(result)
				}
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual fun getFdKHR(block: FenceGetFdInfoKHRBuilder.() -> Unit): Int {
		val fence = this
		val device = fence.device
		MemoryStack.stackPush()
		try {
			val target = VkFenceGetFdInfoKHR.callocStack()
			val builder = FenceGetFdInfoKHRBuilder(target)
			builder.init(fence)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetFenceFdKHR(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputPtr[0]
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun importFdKHR(block: ImportFenceFdInfoKHRBuilder.() -> Unit) {
		val fence = this
		val device = fence.device
		MemoryStack.stackPush()
		try {
			val target = VkImportFenceFdInfoKHR.callocStack()
			val builder = ImportFenceFdInfoKHRBuilder(target)
			builder.init(fence)
			builder.apply(block)
			val result = vkImportFenceFdKHR(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

