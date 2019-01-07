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

expect enum class BlendOp : VkEnum<BlendOp> {
	ADD,

	ZERO_EXT,

	SUBTRACT,

	SRC_EXT,

	REVERSE_SUBTRACT,

	DST_EXT,

	MIN,

	SRC_OVER_EXT,

	MAX,

	DST_OVER_EXT,

	SRC_IN_EXT,

	DST_IN_EXT,

	SRC_OUT_EXT,

	DST_OUT_EXT,

	SRC_ATOP_EXT,

	DST_ATOP_EXT,

	XOR_EXT,

	MULTIPLY_EXT,

	SCREEN_EXT,

	OVERLAY_EXT,

	DARKEN_EXT,

	LIGHTEN_EXT,

	COLORDODGE_EXT,

	COLORBURN_EXT,

	HARDLIGHT_EXT,

	SOFTLIGHT_EXT,

	DIFFERENCE_EXT,

	EXCLUSION_EXT,

	INVERT_EXT,

	INVERT_RGB_EXT,

	LINEARDODGE_EXT,

	LINEARBURN_EXT,

	VIVIDLIGHT_EXT,

	LINEARLIGHT_EXT,

	PINLIGHT_EXT,

	HARDMIX_EXT,

	HSL_HUE_EXT,

	HSL_SATURATION_EXT,

	HSL_COLOR_EXT,

	HSL_LUMINOSITY_EXT,

	PLUS_EXT,

	PLUS_CLAMPED_EXT,

	PLUS_CLAMPED_ALPHA_EXT,

	PLUS_DARKER_EXT,

	MINUS_EXT,

	MINUS_CLAMPED_EXT,

	CONTRAST_EXT,

	INVERT_OVG_EXT,

	RED_EXT,

	GREEN_EXT,

	BLUE_EXT
}

