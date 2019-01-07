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

actual enum class CompositeAlphaKHR(override val value: VkCompositeAlphaFlagBitsKHR) : VkFlag<CompositeAlphaKHR> {
	OPAQUE(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR),

	PRE_MULTIPLIED(VK_COMPOSITE_ALPHA_PRE_MULTIPLIED_BIT_KHR),

	POST_MULTIPLIED(VK_COMPOSITE_ALPHA_POST_MULTIPLIED_BIT_KHR),

	INHERIT(VK_COMPOSITE_ALPHA_INHERIT_BIT_KHR);

	companion object {
		private val enumLookUpMap: Map<UInt, CompositeAlphaKHR> =
				enumValues<CompositeAlphaKHR>().associateBy({ it.value })

		fun fromMultiple(value: VkCompositeAlphaFlagBitsKHR): VkFlag<CompositeAlphaKHR> =
				VkFlag(value)

		fun from(value: VkCompositeAlphaFlagBitsKHR): CompositeAlphaKHR =
				enumLookUpMap[value]!!
	}
}

