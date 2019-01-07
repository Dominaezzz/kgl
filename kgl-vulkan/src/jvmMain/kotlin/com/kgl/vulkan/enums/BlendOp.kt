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
import org.lwjgl.vulkan.EXTBlendOperationAdvanced
import org.lwjgl.vulkan.VK11

actual enum class BlendOp(override val value: Int) : VkEnum<BlendOp> {
	ADD(VK11.VK_BLEND_OP_ADD),

	ZERO_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_ZERO_EXT),

	SUBTRACT(VK11.VK_BLEND_OP_SUBTRACT),

	SRC_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_SRC_EXT),

	REVERSE_SUBTRACT(VK11.VK_BLEND_OP_REVERSE_SUBTRACT),

	DST_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_DST_EXT),

	MIN(VK11.VK_BLEND_OP_MIN),

	SRC_OVER_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_SRC_OVER_EXT),

	MAX(VK11.VK_BLEND_OP_MAX),

	DST_OVER_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_DST_OVER_EXT),

	SRC_IN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_SRC_IN_EXT),

	DST_IN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_DST_IN_EXT),

	SRC_OUT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_SRC_OUT_EXT),

	DST_OUT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_DST_OUT_EXT),

	SRC_ATOP_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_SRC_ATOP_EXT),

	DST_ATOP_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_DST_ATOP_EXT),

	XOR_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_XOR_EXT),

	MULTIPLY_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_MULTIPLY_EXT),

	SCREEN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_SCREEN_EXT),

	OVERLAY_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_OVERLAY_EXT),

	DARKEN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_DARKEN_EXT),

	LIGHTEN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_LIGHTEN_EXT),

	COLORDODGE_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_COLORDODGE_EXT),

	COLORBURN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_COLORBURN_EXT),

	HARDLIGHT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_HARDLIGHT_EXT),

	SOFTLIGHT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_SOFTLIGHT_EXT),

	DIFFERENCE_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_DIFFERENCE_EXT),

	EXCLUSION_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_EXCLUSION_EXT),

	INVERT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_INVERT_EXT),

	INVERT_RGB_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_INVERT_RGB_EXT),

	LINEARDODGE_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_LINEARDODGE_EXT),

	LINEARBURN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_LINEARBURN_EXT),

	VIVIDLIGHT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_VIVIDLIGHT_EXT),

	LINEARLIGHT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_LINEARLIGHT_EXT),

	PINLIGHT_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_PINLIGHT_EXT),

	HARDMIX_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_HARDMIX_EXT),

	HSL_HUE_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_HSL_HUE_EXT),

	HSL_SATURATION_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_HSL_SATURATION_EXT),

	HSL_COLOR_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_HSL_COLOR_EXT),

	HSL_LUMINOSITY_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_HSL_LUMINOSITY_EXT),

	PLUS_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_PLUS_EXT),

	PLUS_CLAMPED_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_PLUS_CLAMPED_EXT),

	PLUS_CLAMPED_ALPHA_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_PLUS_CLAMPED_ALPHA_EXT),

	PLUS_DARKER_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_PLUS_DARKER_EXT),

	MINUS_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_MINUS_EXT),

	MINUS_CLAMPED_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_MINUS_CLAMPED_EXT),

	CONTRAST_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_CONTRAST_EXT),

	INVERT_OVG_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_INVERT_OVG_EXT),

	RED_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_RED_EXT),

	GREEN_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_GREEN_EXT),

	BLUE_EXT(EXTBlendOperationAdvanced.VK_BLEND_OP_BLUE_EXT);

	companion object {
		private val enumLookUpMap: Map<Int, BlendOp> = enumValues<BlendOp>().associateBy({
			it.value
		})

		fun from(value: Int): BlendOp = enumLookUpMap[value]!!
	}
}

