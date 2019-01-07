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
import org.lwjgl.vulkan.NVShaderSubgroupPartitioned
import org.lwjgl.vulkan.VK11

actual enum class SubgroupFeature(override val value: Int) : VkFlag<SubgroupFeature> {
	BASIC(VK11.VK_SUBGROUP_FEATURE_BASIC_BIT),

	VOTE(VK11.VK_SUBGROUP_FEATURE_VOTE_BIT),

	ARITHMETIC(VK11.VK_SUBGROUP_FEATURE_ARITHMETIC_BIT),

	BALLOT(VK11.VK_SUBGROUP_FEATURE_BALLOT_BIT),

	SHUFFLE(VK11.VK_SUBGROUP_FEATURE_SHUFFLE_BIT),

	SHUFFLE_RELATIVE(VK11.VK_SUBGROUP_FEATURE_SHUFFLE_RELATIVE_BIT),

	CLUSTERED(VK11.VK_SUBGROUP_FEATURE_CLUSTERED_BIT),

	QUAD(VK11.VK_SUBGROUP_FEATURE_QUAD_BIT),

	PARTITIONED_NV(NVShaderSubgroupPartitioned.VK_SUBGROUP_FEATURE_PARTITIONED_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<Int, SubgroupFeature> =
				enumValues<SubgroupFeature>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<SubgroupFeature> = VkFlag(value)

		fun from(value: Int): SubgroupFeature = enumLookUpMap[value]!!
	}
}

