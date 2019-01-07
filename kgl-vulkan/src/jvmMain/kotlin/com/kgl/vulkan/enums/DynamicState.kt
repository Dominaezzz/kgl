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
import org.lwjgl.vulkan.*

actual enum class DynamicState(override val value: Int) : VkEnum<DynamicState> {
	VIEWPORT(VK11.VK_DYNAMIC_STATE_VIEWPORT),

	VIEWPORT_W_SCALING_NV(NVClipSpaceWScaling.VK_DYNAMIC_STATE_VIEWPORT_W_SCALING_NV),

	DISCARD_RECTANGLE_EXT(EXTDiscardRectangles.VK_DYNAMIC_STATE_DISCARD_RECTANGLE_EXT),

	SAMPLE_LOCATIONS_EXT(EXTSampleLocations.VK_DYNAMIC_STATE_SAMPLE_LOCATIONS_EXT),

	SCISSOR(VK11.VK_DYNAMIC_STATE_SCISSOR),

	EXCLUSIVE_SCISSOR_NV(NVScissorExclusive.VK_DYNAMIC_STATE_EXCLUSIVE_SCISSOR_NV),

	LINE_WIDTH(VK11.VK_DYNAMIC_STATE_LINE_WIDTH),

	DEPTH_BIAS(VK11.VK_DYNAMIC_STATE_DEPTH_BIAS),

	BLEND_CONSTANTS(VK11.VK_DYNAMIC_STATE_BLEND_CONSTANTS),

	VIEWPORT_SHADING_RATE_PALETTE_NV(NVShadingRateImage.VK_DYNAMIC_STATE_VIEWPORT_SHADING_RATE_PALETTE_NV),

	DEPTH_BOUNDS(VK11.VK_DYNAMIC_STATE_DEPTH_BOUNDS),

	STENCIL_COMPARE_MASK(VK11.VK_DYNAMIC_STATE_STENCIL_COMPARE_MASK),

	VIEWPORT_COARSE_SAMPLE_ORDER_NV(NVShadingRateImage.VK_DYNAMIC_STATE_VIEWPORT_COARSE_SAMPLE_ORDER_NV),

	STENCIL_WRITE_MASK(VK11.VK_DYNAMIC_STATE_STENCIL_WRITE_MASK),

	STENCIL_REFERENCE(VK11.VK_DYNAMIC_STATE_STENCIL_REFERENCE);

	companion object {
		private val enumLookUpMap: Map<Int, DynamicState> = enumValues<DynamicState>().associateBy({
			it.value
		})

		fun from(value: Int): DynamicState = enumLookUpMap[value]!!
	}
}

