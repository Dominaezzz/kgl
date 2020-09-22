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

import com.kgl.vulkan.utils.*
import io.ktor.utils.io.bits.*
import org.lwjgl.system.*
import org.lwjgl.vulkan.EXTValidationCache.*
import org.lwjgl.vulkan.VK11.*

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

	actual fun getData(data: Memory?) {
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
