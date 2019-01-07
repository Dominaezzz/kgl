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

import com.kgl.vulkan.dsls.ImportSemaphoreFdInfoKHRBuilder
import com.kgl.vulkan.dsls.SemaphoreGetFdInfoKHRBuilder
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.KHRExternalSemaphoreFd.vkGetSemaphoreFdKHR
import org.lwjgl.vulkan.KHRExternalSemaphoreFd.vkImportSemaphoreFdKHR
import org.lwjgl.vulkan.VK11.VK_SUCCESS
import org.lwjgl.vulkan.VK11.vkDestroySemaphore
import org.lwjgl.vulkan.VkImportSemaphoreFdInfoKHR
import org.lwjgl.vulkan.VkSemaphoreGetFdInfoKHR

actual class Semaphore(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val semaphore = this
		val device = semaphore.device
		MemoryStack.stackPush()
		try {
			vkDestroySemaphore(device.toVkType(), semaphore.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getFdKHR(block: SemaphoreGetFdInfoKHRBuilder.() -> Unit): Int {
		val semaphore = this
		val device = semaphore.device
		MemoryStack.stackPush()
		try {
			val target = VkSemaphoreGetFdInfoKHR.callocStack()
			val builder = SemaphoreGetFdInfoKHRBuilder(target)
			builder.init(semaphore)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetSemaphoreFdKHR(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputPtr[0]
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun importFdKHR(block: ImportSemaphoreFdInfoKHRBuilder.() -> Unit) {
		val semaphore = this
		val device = semaphore.device
		MemoryStack.stackPush()
		try {
			val target = VkImportSemaphoreFdInfoKHR.callocStack()
			val builder = ImportSemaphoreFdInfoKHRBuilder(target)
			builder.init(semaphore)
			builder.apply(block)
			val result = vkImportSemaphoreFdKHR(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

