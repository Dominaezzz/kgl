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
import com.kgl.vulkan.enums.QueryResult
import com.kgl.vulkan.utils.*
import cvulkan.VK_NOT_READY
import cvulkan.VK_SUCCESS
import cvulkan.VkQueryPool
import kotlinx.cinterop.invoke
import kotlinx.io.core.IoBuffer

actual class QueryPool(override val ptr: VkQueryPool, actual val device: Device) : VkHandleNative<VkQueryPool>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val queryPool = this
		val device = queryPool.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyQueryPool(device.toVkType(), queryPool.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getResults(
			firstQuery: UInt,
			queryCount: UInt,
			data: IoBuffer,
			stride: ULong,
			flags: VkFlag<QueryResult>?
	): Boolean {
		val queryPool = this
		val device = queryPool.device
		VirtualStack.push()
		try {
			TODO()
			data.writeDirect {
				val result = dispatchTable.vkGetQueryPoolResults!!(device.toVkType(), queryPool.toVkType(),
						firstQuery.toVkType(), queryCount.toVkType(),
						data.writeRemaining.toULong(), it,
						stride.toVkType(), flags.toVkType())
				when (result) {
					VK_SUCCESS -> true
					VK_NOT_READY -> false
					else -> handleVkResult(result)
				}
				data.writeRemaining
			}
		} finally {
			VirtualStack.pop()
		}
	}
}

