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

actual enum class SubgroupFeature(override val value: VkSubgroupFeatureFlagBits) : VkFlag<SubgroupFeature> {
	BASIC(VK_SUBGROUP_FEATURE_BASIC_BIT),

	VOTE(VK_SUBGROUP_FEATURE_VOTE_BIT),

	ARITHMETIC(VK_SUBGROUP_FEATURE_ARITHMETIC_BIT),

	BALLOT(VK_SUBGROUP_FEATURE_BALLOT_BIT),

	SHUFFLE(VK_SUBGROUP_FEATURE_SHUFFLE_BIT),

	SHUFFLE_RELATIVE(VK_SUBGROUP_FEATURE_SHUFFLE_RELATIVE_BIT),

	CLUSTERED(VK_SUBGROUP_FEATURE_CLUSTERED_BIT),

	QUAD(VK_SUBGROUP_FEATURE_QUAD_BIT),

	PARTITIONED_NV(VK_SUBGROUP_FEATURE_PARTITIONED_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<UInt, SubgroupFeature> =
				enumValues<SubgroupFeature>().associateBy({ it.value })

		fun fromMultiple(value: VkSubgroupFeatureFlagBits): VkFlag<SubgroupFeature> = VkFlag(value)

		fun from(value: VkSubgroupFeatureFlagBits): SubgroupFeature = enumLookUpMap[value]!!
	}
}

