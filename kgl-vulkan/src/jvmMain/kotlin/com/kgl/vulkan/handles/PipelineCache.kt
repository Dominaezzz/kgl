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

import com.kgl.core.ByteBuffer
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.VK11.*

actual class PipelineCache(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val pipelineCache = this
		val device = pipelineCache.device
		MemoryStack.stackPush()
		try {
			vkDestroyPipelineCache(device.toVkType(), pipelineCache.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual val dataSize: ULong
		get() {
			val pipelineCache = this
			val device = pipelineCache.device
			MemoryStack.stackPush()
			try {
				val outputSize = MemoryStack.stackGet().mallocPointer(1)

				val result = vkGetPipelineCacheData(device.toVkType(), pipelineCache.toVkType(), outputSize, null)
				return when (result) {
					VK_SUCCESS -> outputSize.get().toULong()
					VK_INCOMPLETE -> 0UL
					else -> handleVkResult(result)
				}
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual fun getData(data: ByteBuffer): Boolean {
		val pipelineCache = this
		val device = pipelineCache.device
		MemoryStack.stackPush()
		try {
			val outputSize = MemoryStack.stackGet().mallocPointer(1)

			val result = vkGetPipelineCacheData(device.toVkType(), pipelineCache.toVkType(), outputSize, data.asJvmByteBuffer())
			return when (result) {
				VK_SUCCESS -> true
				VK_INCOMPLETE -> false
				else -> handleVkResult(result)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun merge(srcCaches: Collection<PipelineCache>) {
		val dstCache = this
		val device = dstCache.device
		MemoryStack.stackPush()
		try {
			val result = vkMergePipelineCaches(device.toVkType(), dstCache.toVkType(),
					srcCaches.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

