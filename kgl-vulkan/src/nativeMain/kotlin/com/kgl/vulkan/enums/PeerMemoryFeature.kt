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

actual enum class PeerMemoryFeature(override val value: VkPeerMemoryFeatureFlagBits) : VkFlag<PeerMemoryFeature> {
	COPY_SRC(VK_PEER_MEMORY_FEATURE_COPY_SRC_BIT),

	COPY_DST(VK_PEER_MEMORY_FEATURE_COPY_DST_BIT),

	GENERIC_SRC(VK_PEER_MEMORY_FEATURE_GENERIC_SRC_BIT),

	GENERIC_DST(VK_PEER_MEMORY_FEATURE_GENERIC_DST_BIT);

	companion object {
		private val enumLookUpMap: Map<UInt, PeerMemoryFeature> =
				enumValues<PeerMemoryFeature>().associateBy({ it.value })

		fun fromMultiple(value: VkPeerMemoryFeatureFlagBits): VkFlag<PeerMemoryFeature> =
				VkFlag(value)

		fun from(value: VkPeerMemoryFeatureFlagBits): PeerMemoryFeature =
				enumLookUpMap[value]!!
	}
}

