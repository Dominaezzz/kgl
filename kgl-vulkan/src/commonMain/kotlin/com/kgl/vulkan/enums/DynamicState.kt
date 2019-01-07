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

expect enum class DynamicState : VkEnum<DynamicState> {
	VIEWPORT,

	VIEWPORT_W_SCALING_NV,

	DISCARD_RECTANGLE_EXT,

	SAMPLE_LOCATIONS_EXT,

	SCISSOR,

	EXCLUSIVE_SCISSOR_NV,

	LINE_WIDTH,

	DEPTH_BIAS,

	BLEND_CONSTANTS,

	VIEWPORT_SHADING_RATE_PALETTE_NV,

	DEPTH_BOUNDS,

	STENCIL_COMPARE_MASK,

	VIEWPORT_COARSE_SAMPLE_ORDER_NV,

	STENCIL_WRITE_MASK,

	STENCIL_REFERENCE
}

