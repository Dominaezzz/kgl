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
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import cvulkan.VK_INCOMPLETE
import cvulkan.VK_SUCCESS
import cvulkan.VkPipelineCache
import cvulkan.VkResult
import kotlinx.cinterop.*
import io.ktor.utils.io.core.IoBuffer

actual class PipelineCache(override val ptr: VkPipelineCache, actual val device: Device) : VkHandleNative<VkPipelineCache>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val pipelineCache = this
		val device = pipelineCache.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyPipelineCache(device.toVkType(), pipelineCache.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual val dataSize: ULong
		get() {
			val pipelineCache = this
			val device = pipelineCache.device
			VirtualStack.push()
			try {
				val outputSize = VirtualStack.alloc<ULongVar>()

				val result = dispatchTable.vkGetPipelineCacheData(device.toVkType(), pipelineCache.toVkType(), outputSize.ptr, null)
				return when (result) {
					VK_SUCCESS -> outputSize.value
					VK_INCOMPLETE -> 0UL
					else -> handleVkResult(result)
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual fun getData(data: IoBuffer): Boolean {
		val pipelineCache = this
		val device = pipelineCache.device
		VirtualStack.push()
		try {
			val outputSize = VirtualStack.alloc<ULongVar>()
			outputSize.value = data.writeRemaining.toULong()

			var result: VkResult = VK_INCOMPLETE
			data.writeDirect {
				result = dispatchTable.vkGetPipelineCacheData(device.toVkType(), pipelineCache.toVkType(), outputSize.ptr, it)
				outputSize.value.toInt()
			}
			return when (result) {
				VK_SUCCESS -> true
				VK_INCOMPLETE -> false
				else -> handleVkResult(result)
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun merge(srcCaches: Collection<PipelineCache>) {
		val dstCache = this
		val device = dstCache.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkMergePipelineCaches(device.toVkType(), dstCache.toVkType(),
					srcCaches.size.toUInt(), srcCaches.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}
}

