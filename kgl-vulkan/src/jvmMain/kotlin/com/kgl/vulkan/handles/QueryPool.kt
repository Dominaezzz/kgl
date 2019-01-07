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

import com.kgl.vulkan.enums.QueryResult
import com.kgl.vulkan.utils.*
import kotlinx.io.core.IoBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.VK11.*

actual class QueryPool(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val queryPool = this
		val device = queryPool.device
		MemoryStack.stackPush()
		try {
			vkDestroyQueryPool(device.toVkType(), queryPool.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getResults(
			firstQuery: UInt,
			queryCount: UInt,
			pData: IoBuffer,
			stride: ULong,
			flags: VkFlag<QueryResult>?
	): Boolean {
		MemoryStack.stackPush()
		try {
			TODO()
			pData.writeDirect(pData.writeRemaining) {
				val result = vkGetQueryPoolResults(device.toVkType(), ptr,
						firstQuery.toVkType(), queryCount.toVkType(), it,
						stride.toVkType(), flags.toVkType())
				when (result) {
					VK_SUCCESS -> true
					VK_NOT_READY -> false
					else -> handleVkResult(result)
				}
			}
		} finally {
			MemoryStack.stackPop()
		}
	}
}

