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
import org.lwjgl.vulkan.KHRSharedPresentableImage
import org.lwjgl.vulkan.KHRSwapchain
import org.lwjgl.vulkan.NVShadingRateImage
import org.lwjgl.vulkan.VK11

actual enum class ImageLayout(override val value: Int) : VkEnum<ImageLayout> {
	UNDEFINED(VK11.VK_IMAGE_LAYOUT_UNDEFINED),

	DEPTH_READ_ONLY_STENCIL_ATTACHMENT_OPTIMAL(VK11.VK_IMAGE_LAYOUT_DEPTH_READ_ONLY_STENCIL_ATTACHMENT_OPTIMAL),

	SHARED_PRESENT_KHR(KHRSharedPresentableImage.VK_IMAGE_LAYOUT_SHARED_PRESENT_KHR),

	GENERAL(VK11.VK_IMAGE_LAYOUT_GENERAL),

	DEPTH_ATTACHMENT_STENCIL_READ_ONLY_OPTIMAL(VK11.VK_IMAGE_LAYOUT_DEPTH_ATTACHMENT_STENCIL_READ_ONLY_OPTIMAL),

	COLOR_ATTACHMENT_OPTIMAL(VK11.VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL),

	PRESENT_SRC_KHR(KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR),

	DEPTH_STENCIL_ATTACHMENT_OPTIMAL(VK11.VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL),

	SHADING_RATE_OPTIMAL_NV(NVShadingRateImage.VK_IMAGE_LAYOUT_SHADING_RATE_OPTIMAL_NV),

	DEPTH_STENCIL_READ_ONLY_OPTIMAL(VK11.VK_IMAGE_LAYOUT_DEPTH_STENCIL_READ_ONLY_OPTIMAL),

	SHADER_READ_ONLY_OPTIMAL(VK11.VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL),

	TRANSFER_SRC_OPTIMAL(VK11.VK_IMAGE_LAYOUT_TRANSFER_SRC_OPTIMAL),

	TRANSFER_DST_OPTIMAL(VK11.VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL),

	PREINITIALIZED(VK11.VK_IMAGE_LAYOUT_PREINITIALIZED);

	companion object {
		private val enumLookUpMap: Map<Int, ImageLayout> = enumValues<ImageLayout>().associateBy({
			it.value
		})

		fun from(value: Int): ImageLayout = enumLookUpMap[value]!!
	}
}

