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
package com.kgl.vulkan.dsls

import com.kgl.vulkan.enums.QueryPipelineStatistic
import com.kgl.vulkan.enums.QueryType
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.VK11
import org.lwjgl.vulkan.VkQueryPoolCreateInfo

actual class QueryPoolCreateInfoBuilder(internal val target: VkQueryPoolCreateInfo) {
	actual var queryType: QueryType?
		get() = QueryType.from(target.queryType())
		set(value) {
			target.queryType(value.toVkType())
		}

	actual var queryCount: UInt
		get() = target.queryCount().toUInt()
		set(value) {
			target.queryCount(value.toVkType())
		}

	actual var pipelineStatistics: VkFlag<QueryPipelineStatistic>?
		get() = QueryPipelineStatistic.fromMultiple(target.pipelineStatistics())
		set(value) {
			target.pipelineStatistics(value.toVkType())
		}

	internal fun init() {
		target.sType(VK11.VK_STRUCTURE_TYPE_QUERY_POOL_CREATE_INFO)
		target.pNext(0)
		target.flags(0)
	}
}

