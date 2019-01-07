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

actual enum class DynamicState(override val value: VkDynamicState) : VkEnum<DynamicState> {
	VIEWPORT(VK_DYNAMIC_STATE_VIEWPORT),

	VIEWPORT_W_SCALING_NV(VK_DYNAMIC_STATE_VIEWPORT_W_SCALING_NV),

	DISCARD_RECTANGLE_EXT(VK_DYNAMIC_STATE_DISCARD_RECTANGLE_EXT),

	SAMPLE_LOCATIONS_EXT(VK_DYNAMIC_STATE_SAMPLE_LOCATIONS_EXT),

	SCISSOR(VK_DYNAMIC_STATE_SCISSOR),

	EXCLUSIVE_SCISSOR_NV(VK_DYNAMIC_STATE_EXCLUSIVE_SCISSOR_NV),

	LINE_WIDTH(VK_DYNAMIC_STATE_LINE_WIDTH),

	DEPTH_BIAS(VK_DYNAMIC_STATE_DEPTH_BIAS),

	BLEND_CONSTANTS(VK_DYNAMIC_STATE_BLEND_CONSTANTS),

	VIEWPORT_SHADING_RATE_PALETTE_NV(VK_DYNAMIC_STATE_VIEWPORT_SHADING_RATE_PALETTE_NV),

	DEPTH_BOUNDS(VK_DYNAMIC_STATE_DEPTH_BOUNDS),

	STENCIL_COMPARE_MASK(VK_DYNAMIC_STATE_STENCIL_COMPARE_MASK),

	VIEWPORT_COARSE_SAMPLE_ORDER_NV(VK_DYNAMIC_STATE_VIEWPORT_COARSE_SAMPLE_ORDER_NV),

	STENCIL_WRITE_MASK(VK_DYNAMIC_STATE_STENCIL_WRITE_MASK),

	STENCIL_REFERENCE(VK_DYNAMIC_STATE_STENCIL_REFERENCE);

	companion object {
		private val enumLookUpMap: Map<UInt, DynamicState> = enumValues<DynamicState>().associateBy({
			it.value
		})

		fun from(value: VkDynamicState): DynamicState = enumLookUpMap[value]!!
	}
}

