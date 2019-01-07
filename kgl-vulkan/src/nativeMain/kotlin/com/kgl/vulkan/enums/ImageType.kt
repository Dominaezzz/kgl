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
import cvulkan.VK_IMAGE_TYPE_1D
import cvulkan.VK_IMAGE_TYPE_2D
import cvulkan.VK_IMAGE_TYPE_3D
import cvulkan.VkImageType

actual enum class ImageType(override val value: VkImageType) : VkEnum<ImageType> {
	`1D`(VK_IMAGE_TYPE_1D),

	`2D`(VK_IMAGE_TYPE_2D),

	`3D`(VK_IMAGE_TYPE_3D);

	companion object {
		fun from(value: VkImageType): ImageType = enumValues<ImageType>()[value.toInt()]
	}
}

