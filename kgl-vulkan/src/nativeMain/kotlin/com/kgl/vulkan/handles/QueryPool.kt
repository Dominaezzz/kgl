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
import cvulkan.*
import kotlinx.io.core.IoBuffer

actual class QueryPool(override val ptr: VkQueryPool, actual val device: Device) : VkHandleNative<VkQueryPool>(), VkHandle {
	override fun close() {
		val queryPool = this
		val device = queryPool.device
		VirtualStack.push()
		try {
			vkDestroyQueryPool(device.toVkType(), queryPool.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getResults(
			firstQuery: UInt,
			queryCount: UInt,
			pData: IoBuffer,
			stride: ULong,
			flags: VkFlag<QueryResult>?
	): Boolean {
		val queryPool = this
		val device = queryPool.device
		VirtualStack.push()
		try {
			TODO()
			pData.writeDirect {
				val result = vkGetQueryPoolResults(device.toVkType(), queryPool.toVkType(),
						firstQuery.toVkType(), queryCount.toVkType(),
						pData.writeRemaining.toULong(), it,
						stride.toVkType(), flags.toVkType())
				when (result) {
					VK_SUCCESS -> true
					VK_NOT_READY -> false
					else -> handleVkResult(result)
				}
				pData.writeRemaining
			}
		} finally {
			VirtualStack.pop()
		}
	}
}

