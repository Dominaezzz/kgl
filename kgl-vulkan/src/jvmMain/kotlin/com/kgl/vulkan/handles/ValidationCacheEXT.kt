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
import io.ktor.utils.io.core.IoBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.EXTValidationCache.vkDestroyValidationCacheEXT
import org.lwjgl.vulkan.EXTValidationCache.vkMergeValidationCachesEXT
import org.lwjgl.vulkan.VK11.VK_SUCCESS

actual class ValidationCacheEXT(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val validationCache = this
		val device = validationCache.device
		MemoryStack.stackPush()
		try {
			vkDestroyValidationCacheEXT(device.toVkType(), validationCache.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getData(data: IoBuffer?) {
		TODO()
		val validationCache = this
		val device = validationCache.device
		MemoryStack.stackPush()
		try {
//			val result = vkGetValidationCacheDataEXT(device.toVkType(), validationCache.toVkType(),
//					data.toVkType())
//			when (result) {
//				VK_SUCCESS -> Unit
//				VK_INCOMPLETE -> Unit
//				else -> handleVkResult(result)
//			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun merge(srcCaches: Collection<ValidationCacheEXT>) {
		val dstCache = this
		val device = dstCache.device
		MemoryStack.stackPush()
		try {
			val result = vkMergeValidationCachesEXT(device.toVkType(), dstCache.toVkType(), srcCaches.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

