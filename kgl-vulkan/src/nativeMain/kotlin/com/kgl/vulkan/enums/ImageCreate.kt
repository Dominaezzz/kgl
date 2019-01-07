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

actual enum class ImageCreate(override val value: VkImageCreateFlagBits) : VkFlag<ImageCreate> {
	SPARSE_BINDING(VK_IMAGE_CREATE_SPARSE_BINDING_BIT),

	SPARSE_RESIDENCY(VK_IMAGE_CREATE_SPARSE_RESIDENCY_BIT),

	SPARSE_ALIASED(VK_IMAGE_CREATE_SPARSE_ALIASED_BIT),

	MUTABLE_FORMAT(VK_IMAGE_CREATE_MUTABLE_FORMAT_BIT),

	CUBE_COMPATIBLE(VK_IMAGE_CREATE_CUBE_COMPATIBLE_BIT),

	`2D_ARRAY_COMPATIBLE`(VK_IMAGE_CREATE_2D_ARRAY_COMPATIBLE_BIT),

	SPLIT_INSTANCE_BIND_REGIONS(VK_IMAGE_CREATE_SPLIT_INSTANCE_BIND_REGIONS_BIT),

	BLOCK_TEXEL_VIEW_COMPATIBLE(VK_IMAGE_CREATE_BLOCK_TEXEL_VIEW_COMPATIBLE_BIT),

	EXTENDED_USAGE(VK_IMAGE_CREATE_EXTENDED_USAGE_BIT),

	DISJOINT(VK_IMAGE_CREATE_DISJOINT_BIT),

	ALIAS(VK_IMAGE_CREATE_ALIAS_BIT),

	PROTECTED(VK_IMAGE_CREATE_PROTECTED_BIT),

	SAMPLE_LOCATIONS_COMPATIBLE_DEPTH_EXT(VK_IMAGE_CREATE_SAMPLE_LOCATIONS_COMPATIBLE_DEPTH_BIT_EXT),

	CORNER_SAMPLED_NV(VK_IMAGE_CREATE_CORNER_SAMPLED_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<UInt, ImageCreate> = enumValues<ImageCreate>().associateBy({
			it.value
		})

		fun fromMultiple(value: VkImageCreateFlagBits): VkFlag<ImageCreate> = VkFlag(value)

		fun from(value: VkImageCreateFlagBits): ImageCreate = enumLookUpMap[value]!!
	}
}

