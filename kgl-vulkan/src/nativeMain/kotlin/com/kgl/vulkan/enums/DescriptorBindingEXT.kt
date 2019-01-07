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

actual enum class DescriptorBindingEXT(override val value: VkDescriptorBindingFlagBitsEXT) : VkFlag<DescriptorBindingEXT> {
	UPDATE_AFTER_BIND(VK_DESCRIPTOR_BINDING_UPDATE_AFTER_BIND_BIT_EXT),

	UPDATE_UNUSED_WHILE_PENDING(VK_DESCRIPTOR_BINDING_UPDATE_UNUSED_WHILE_PENDING_BIT_EXT),

	PARTIALLY_BOUND(VK_DESCRIPTOR_BINDING_PARTIALLY_BOUND_BIT_EXT),

	VARIABLE_DESCRIPTOR_COUNT(VK_DESCRIPTOR_BINDING_VARIABLE_DESCRIPTOR_COUNT_BIT_EXT);

	companion object {
		private val enumLookUpMap: Map<UInt, DescriptorBindingEXT> =
				enumValues<DescriptorBindingEXT>().associateBy({ it.value })

		fun fromMultiple(value: VkDescriptorBindingFlagBitsEXT): VkFlag<DescriptorBindingEXT> =
				VkFlag(value)

		fun from(value: VkDescriptorBindingFlagBitsEXT): DescriptorBindingEXT =
				enumLookUpMap[value]!!
	}
}

