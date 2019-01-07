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
import cvulkan.*

actual enum class PrimitiveTopology(override val value: VkPrimitiveTopology) : VkEnum<PrimitiveTopology> {
	POINT_LIST(VK_PRIMITIVE_TOPOLOGY_POINT_LIST),

	LINE_LIST(VK_PRIMITIVE_TOPOLOGY_LINE_LIST),

	LINE_STRIP(VK_PRIMITIVE_TOPOLOGY_LINE_STRIP),

	TRIANGLE_LIST(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST),

	TRIANGLE_STRIP(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_STRIP),

	TRIANGLE_FAN(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_FAN),

	LINE_LIST_WITH_ADJACENCY(VK_PRIMITIVE_TOPOLOGY_LINE_LIST_WITH_ADJACENCY),

	LINE_STRIP_WITH_ADJACENCY(VK_PRIMITIVE_TOPOLOGY_LINE_STRIP_WITH_ADJACENCY),

	TRIANGLE_LIST_WITH_ADJACENCY(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST_WITH_ADJACENCY),

	TRIANGLE_STRIP_WITH_ADJACENCY(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_STRIP_WITH_ADJACENCY),

	PATCH_LIST(VK_PRIMITIVE_TOPOLOGY_PATCH_LIST);

	companion object {
		fun from(value: VkPrimitiveTopology): PrimitiveTopology =
				enumValues<PrimitiveTopology>()[value.toInt()]
	}
}

