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

actual enum class ImageAspect(override val value: VkImageAspectFlagBits) : VkFlag<ImageAspect> {
	COLOR(VK_IMAGE_ASPECT_COLOR_BIT),

	DEPTH(VK_IMAGE_ASPECT_DEPTH_BIT),

	STENCIL(VK_IMAGE_ASPECT_STENCIL_BIT),

	METADATA(VK_IMAGE_ASPECT_METADATA_BIT),

	PLANE_0(VK_IMAGE_ASPECT_PLANE_0_BIT),

	PLANE_1(VK_IMAGE_ASPECT_PLANE_1_BIT),

	PLANE_2(VK_IMAGE_ASPECT_PLANE_2_BIT),

	MEMORY_PLANE_0_EXT(VK_IMAGE_ASPECT_MEMORY_PLANE_0_BIT_EXT),

	MEMORY_PLANE_1_EXT(VK_IMAGE_ASPECT_MEMORY_PLANE_1_BIT_EXT),

	MEMORY_PLANE_2_EXT(VK_IMAGE_ASPECT_MEMORY_PLANE_2_BIT_EXT),

	MEMORY_PLANE_3_EXT(VK_IMAGE_ASPECT_MEMORY_PLANE_3_BIT_EXT);

	companion object {
		private val enumLookUpMap: Map<UInt, ImageAspect> = enumValues<ImageAspect>().associateBy({
			it.value
		})

		fun fromMultiple(value: VkImageAspectFlagBits): VkFlag<ImageAspect> = VkFlag(value)

		fun from(value: VkImageAspectFlagBits): ImageAspect = enumLookUpMap[value]!!
	}
}

