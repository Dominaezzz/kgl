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

actual enum class ColorSpaceKHR(override val value: VkColorSpaceKHR) : VkEnum<ColorSpaceKHR> {
	SRGB_NONLINEAR(VK_COLOR_SPACE_SRGB_NONLINEAR_KHR),

	DISPLAY_P3_NONLINEAR_EXT(VK_COLOR_SPACE_DISPLAY_P3_NONLINEAR_EXT),

	EXTENDED_SRGB_LINEAR_EXT(VK_COLOR_SPACE_EXTENDED_SRGB_LINEAR_EXT),

	DCI_P3_LINEAR_EXT(VK_COLOR_SPACE_DCI_P3_LINEAR_EXT),

	DCI_P3_NONLINEAR_EXT(VK_COLOR_SPACE_DCI_P3_NONLINEAR_EXT),

	BT709_LINEAR_EXT(VK_COLOR_SPACE_BT709_LINEAR_EXT),

	BT709_NONLINEAR_EXT(VK_COLOR_SPACE_BT709_NONLINEAR_EXT),

	BT2020_LINEAR_EXT(VK_COLOR_SPACE_BT2020_LINEAR_EXT),

	HDR10_ST2084_EXT(VK_COLOR_SPACE_HDR10_ST2084_EXT),

	DOLBYVISION_EXT(VK_COLOR_SPACE_DOLBYVISION_EXT),

	HDR10_HLG_EXT(VK_COLOR_SPACE_HDR10_HLG_EXT),

	ADOBERGB_LINEAR_EXT(VK_COLOR_SPACE_ADOBERGB_LINEAR_EXT),

	ADOBERGB_NONLINEAR_EXT(VK_COLOR_SPACE_ADOBERGB_NONLINEAR_EXT),

	PASS_THROUGH_EXT(VK_COLOR_SPACE_PASS_THROUGH_EXT),

	EXTENDED_SRGB_NONLINEAR_EXT(VK_COLOR_SPACE_EXTENDED_SRGB_NONLINEAR_EXT);

	companion object {
		fun from(value: VkColorSpaceKHR): ColorSpaceKHR = enumValues<ColorSpaceKHR>()[value.toInt()]
	}
}

