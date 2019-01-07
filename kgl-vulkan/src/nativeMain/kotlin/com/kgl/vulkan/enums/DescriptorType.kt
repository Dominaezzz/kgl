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

actual enum class DescriptorType(override val value: VkDescriptorType) : VkEnum<DescriptorType> {
	SAMPLER(VK_DESCRIPTOR_TYPE_SAMPLER),

	INLINE_UNIFORM_BLOCK_EXT(VK_DESCRIPTOR_TYPE_INLINE_UNIFORM_BLOCK_EXT),

	ACCELERATION_STRUCTURE_NV(VK_DESCRIPTOR_TYPE_ACCELERATION_STRUCTURE_NV),

	COMBINED_IMAGE_SAMPLER(VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER),

	SAMPLED_IMAGE(VK_DESCRIPTOR_TYPE_SAMPLED_IMAGE),

	STORAGE_IMAGE(VK_DESCRIPTOR_TYPE_STORAGE_IMAGE),

	UNIFORM_TEXEL_BUFFER(VK_DESCRIPTOR_TYPE_UNIFORM_TEXEL_BUFFER),

	STORAGE_TEXEL_BUFFER(VK_DESCRIPTOR_TYPE_STORAGE_TEXEL_BUFFER),

	UNIFORM_BUFFER(VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER),

	STORAGE_BUFFER(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER),

	UNIFORM_BUFFER_DYNAMIC(VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER_DYNAMIC),

	STORAGE_BUFFER_DYNAMIC(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER_DYNAMIC),

	INPUT_ATTACHMENT(VK_DESCRIPTOR_TYPE_INPUT_ATTACHMENT);

	companion object {
		private val enumLookUpMap: Map<UInt, DescriptorType> =
				enumValues<DescriptorType>().associateBy({ it.value })

		fun from(value: VkDescriptorType): DescriptorType = enumLookUpMap[value]!!
	}
}

