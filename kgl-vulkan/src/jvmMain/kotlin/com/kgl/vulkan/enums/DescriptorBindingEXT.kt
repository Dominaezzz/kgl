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
import org.lwjgl.vulkan.EXTDescriptorIndexing

actual enum class DescriptorBindingEXT(override val value: Int) : VkFlag<DescriptorBindingEXT> {
	UPDATE_AFTER_BIND(EXTDescriptorIndexing.VK_DESCRIPTOR_BINDING_UPDATE_AFTER_BIND_BIT_EXT),

	UPDATE_UNUSED_WHILE_PENDING(EXTDescriptorIndexing.VK_DESCRIPTOR_BINDING_UPDATE_UNUSED_WHILE_PENDING_BIT_EXT),

	PARTIALLY_BOUND(EXTDescriptorIndexing.VK_DESCRIPTOR_BINDING_PARTIALLY_BOUND_BIT_EXT),

	VARIABLE_DESCRIPTOR_COUNT(EXTDescriptorIndexing.VK_DESCRIPTOR_BINDING_VARIABLE_DESCRIPTOR_COUNT_BIT_EXT);

	companion object {
		private val enumLookUpMap: Map<Int, DescriptorBindingEXT> =
				enumValues<DescriptorBindingEXT>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<DescriptorBindingEXT> = VkFlag(value)

		fun from(value: Int): DescriptorBindingEXT = enumLookUpMap[value]!!
	}
}

