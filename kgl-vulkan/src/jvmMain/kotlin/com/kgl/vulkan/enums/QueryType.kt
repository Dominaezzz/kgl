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

import com.kgl.vulkan.utils.VkEnum
import org.lwjgl.vulkan.EXTTransformFeedback
import org.lwjgl.vulkan.NVRayTracing
import org.lwjgl.vulkan.VK11

actual enum class QueryType(override val value: Int) : VkEnum<QueryType> {
	OCCLUSION(VK11.VK_QUERY_TYPE_OCCLUSION),

	ACCELERATION_STRUCTURE_COMPACTED_SIZE_NV(NVRayTracing.VK_QUERY_TYPE_ACCELERATION_STRUCTURE_COMPACTED_SIZE_NV),

	PIPELINE_STATISTICS(VK11.VK_QUERY_TYPE_PIPELINE_STATISTICS),

	TIMESTAMP(VK11.VK_QUERY_TYPE_TIMESTAMP),

	TRANSFORM_FEEDBACK_STREAM_EXT(EXTTransformFeedback.VK_QUERY_TYPE_TRANSFORM_FEEDBACK_STREAM_EXT);

	companion object {
		private val enumLookUpMap: Map<Int, QueryType> = enumValues<QueryType>().associateBy({
			it.value
		})

		fun from(value: Int): QueryType = enumLookUpMap[value]!!
	}
}
