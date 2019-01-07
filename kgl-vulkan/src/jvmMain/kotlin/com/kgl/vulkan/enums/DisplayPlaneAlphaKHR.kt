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
import org.lwjgl.vulkan.KHRDisplay

actual enum class DisplayPlaneAlphaKHR(override val value: Int) : VkFlag<DisplayPlaneAlphaKHR> {
	OPAQUE(KHRDisplay.VK_DISPLAY_PLANE_ALPHA_OPAQUE_BIT_KHR),

	GLOBAL(KHRDisplay.VK_DISPLAY_PLANE_ALPHA_GLOBAL_BIT_KHR),

	PER_PIXEL(KHRDisplay.VK_DISPLAY_PLANE_ALPHA_PER_PIXEL_BIT_KHR),

	PER_PIXEL_PREMULTIPLIED(KHRDisplay.VK_DISPLAY_PLANE_ALPHA_PER_PIXEL_PREMULTIPLIED_BIT_KHR);

	companion object {
		private val enumLookUpMap: Map<Int, DisplayPlaneAlphaKHR> =
				enumValues<DisplayPlaneAlphaKHR>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<DisplayPlaneAlphaKHR> = VkFlag(value)

		fun from(value: Int): DisplayPlaneAlphaKHR = enumLookUpMap[value]!!
	}
}

