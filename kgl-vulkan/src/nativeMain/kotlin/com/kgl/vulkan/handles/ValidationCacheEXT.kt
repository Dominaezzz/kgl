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
import cvulkan.VK_SUCCESS
import cvulkan.VkValidationCacheEXT
import kotlinx.io.core.IoBuffer

actual class ValidationCacheEXT(override val ptr: VkValidationCacheEXT, actual val device: Device) : VkHandleNative<VkValidationCacheEXT>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val validationCache = this
		val device = validationCache.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyValidationCacheEXT!!(device.toVkType(), validationCache.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getData(data: IoBuffer?) {
		TODO()
		val validationCache = this
		val device = validationCache.device
		VirtualStack.push()
		try {
//            val result = dispatchTable.vkGetValidationCacheDataEXT!!(device.toVkType(), validationCache.toVkType(),
//                    data?.readRemaining?.toULong() ?: 0U, data.toVkType())
//            when (result) {
//                VK_SUCCESS -> Unit
//                VK_INCOMPLETE -> Unit
//                else -> handleVkResult(result)
//            }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun merge(srcCaches: Collection<ValidationCacheEXT>) {
		val dstCache = this
		val device = dstCache.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkMergeValidationCachesEXT!!(device.toVkType(), dstCache.toVkType(),
					srcCaches.size.toUInt(), srcCaches.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}
}

