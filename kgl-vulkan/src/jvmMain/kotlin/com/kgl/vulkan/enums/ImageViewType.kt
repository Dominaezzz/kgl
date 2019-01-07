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
import org.lwjgl.vulkan.VK11

actual enum class ImageViewType(override val value: Int) : VkEnum<ImageViewType> {
	`1D`(VK11.VK_IMAGE_VIEW_TYPE_1D),

	`2D`(VK11.VK_IMAGE_VIEW_TYPE_2D),

	`3D`(VK11.VK_IMAGE_VIEW_TYPE_3D),

	CUBE(VK11.VK_IMAGE_VIEW_TYPE_CUBE),

	`1D_ARRAY`(VK11.VK_IMAGE_VIEW_TYPE_1D_ARRAY),

	`2D_ARRAY`(VK11.VK_IMAGE_VIEW_TYPE_2D_ARRAY),

	CUBE_ARRAY(VK11.VK_IMAGE_VIEW_TYPE_CUBE_ARRAY);

	companion object {
		fun from(value: Int): ImageViewType = enumValues<ImageViewType>()[value]
	}
}

