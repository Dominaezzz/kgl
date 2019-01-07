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
package com.kgl.vulkan.enums

import com.kgl.vulkan.utils.VkFlag
import cvulkan.*

actual enum class QueryPipelineStatistic(override val value: VkQueryPipelineStatisticFlagBits) : VkFlag<QueryPipelineStatistic> {
	INPUT_ASSEMBLY_VERTICES(VK_QUERY_PIPELINE_STATISTIC_INPUT_ASSEMBLY_VERTICES_BIT),

	INPUT_ASSEMBLY_PRIMITIVES(VK_QUERY_PIPELINE_STATISTIC_INPUT_ASSEMBLY_PRIMITIVES_BIT),

	VERTEX_SHADER_INVOCATIONS(VK_QUERY_PIPELINE_STATISTIC_VERTEX_SHADER_INVOCATIONS_BIT),

	GEOMETRY_SHADER_INVOCATIONS(VK_QUERY_PIPELINE_STATISTIC_GEOMETRY_SHADER_INVOCATIONS_BIT),

	GEOMETRY_SHADER_PRIMITIVES(VK_QUERY_PIPELINE_STATISTIC_GEOMETRY_SHADER_PRIMITIVES_BIT),

	CLIPPING_INVOCATIONS(VK_QUERY_PIPELINE_STATISTIC_CLIPPING_INVOCATIONS_BIT),

	CLIPPING_PRIMITIVES(VK_QUERY_PIPELINE_STATISTIC_CLIPPING_PRIMITIVES_BIT),

	FRAGMENT_SHADER_INVOCATIONS(VK_QUERY_PIPELINE_STATISTIC_FRAGMENT_SHADER_INVOCATIONS_BIT),

	TESSELLATION_CONTROL_SHADER_PATCHES(VK_QUERY_PIPELINE_STATISTIC_TESSELLATION_CONTROL_SHADER_PATCHES_BIT),

	TESSELLATION_EVALUATION_SHADER_INVOCATIONS(VK_QUERY_PIPELINE_STATISTIC_TESSELLATION_EVALUATION_SHADER_INVOCATIONS_BIT),

	COMPUTE_SHADER_INVOCATIONS(VK_QUERY_PIPELINE_STATISTIC_COMPUTE_SHADER_INVOCATIONS_BIT);

	companion object {
		private val enumLookUpMap: Map<UInt, QueryPipelineStatistic> =
				enumValues<QueryPipelineStatistic>().associateBy({ it.value })

		fun fromMultiple(value: VkQueryPipelineStatisticFlagBits): VkFlag<QueryPipelineStatistic> =
				VkFlag(value)

		fun from(value: VkQueryPipelineStatisticFlagBits): QueryPipelineStatistic =
				enumLookUpMap[value]!!
	}
}

