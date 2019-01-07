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

actual enum class ShadingRatePaletteEntryNV(override val value: VkShadingRatePaletteEntryNV) : VkEnum<ShadingRatePaletteEntryNV> {
	NO_INVOCATIONS(VK_SHADING_RATE_PALETTE_ENTRY_NO_INVOCATIONS_NV),

	`16_INVOCATIONS_PER_PIXEL`(VK_SHADING_RATE_PALETTE_ENTRY_16_INVOCATIONS_PER_PIXEL_NV),

	`8_INVOCATIONS_PER_PIXEL`(VK_SHADING_RATE_PALETTE_ENTRY_8_INVOCATIONS_PER_PIXEL_NV),

	`4_INVOCATIONS_PER_PIXEL`(VK_SHADING_RATE_PALETTE_ENTRY_4_INVOCATIONS_PER_PIXEL_NV),

	`2_INVOCATIONS_PER_PIXEL`(VK_SHADING_RATE_PALETTE_ENTRY_2_INVOCATIONS_PER_PIXEL_NV),

	`1_INVOCATION_PER_PIXEL`(VK_SHADING_RATE_PALETTE_ENTRY_1_INVOCATION_PER_PIXEL_NV),

	`1_INVOCATION_PER_2X1_PIXELS`(VK_SHADING_RATE_PALETTE_ENTRY_1_INVOCATION_PER_2X1_PIXELS_NV),

	`1_INVOCATION_PER_1X2_PIXELS`(VK_SHADING_RATE_PALETTE_ENTRY_1_INVOCATION_PER_1X2_PIXELS_NV),

	`1_INVOCATION_PER_2X2_PIXELS`(VK_SHADING_RATE_PALETTE_ENTRY_1_INVOCATION_PER_2X2_PIXELS_NV),

	`1_INVOCATION_PER_4X2_PIXELS`(VK_SHADING_RATE_PALETTE_ENTRY_1_INVOCATION_PER_4X2_PIXELS_NV),

	`1_INVOCATION_PER_2X4_PIXELS`(VK_SHADING_RATE_PALETTE_ENTRY_1_INVOCATION_PER_2X4_PIXELS_NV),

	`1_INVOCATION_PER_4X4_PIXELS`(VK_SHADING_RATE_PALETTE_ENTRY_1_INVOCATION_PER_4X4_PIXELS_NV);

	companion object {
		fun from(value: VkShadingRatePaletteEntryNV): ShadingRatePaletteEntryNV =
				enumValues<ShadingRatePaletteEntryNV>()[value.toInt()]
	}
}

