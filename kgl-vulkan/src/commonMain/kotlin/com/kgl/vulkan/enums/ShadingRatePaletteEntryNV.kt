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

expect enum class ShadingRatePaletteEntryNV : VkEnum<ShadingRatePaletteEntryNV> {
	NO_INVOCATIONS,

	`16_INVOCATIONS_PER_PIXEL`,

	`8_INVOCATIONS_PER_PIXEL`,

	`4_INVOCATIONS_PER_PIXEL`,

	`2_INVOCATIONS_PER_PIXEL`,

	`1_INVOCATION_PER_PIXEL`,

	`1_INVOCATION_PER_2X1_PIXELS`,

	`1_INVOCATION_PER_1X2_PIXELS`,

	`1_INVOCATION_PER_2X2_PIXELS`,

	`1_INVOCATION_PER_4X2_PIXELS`,

	`1_INVOCATION_PER_2X4_PIXELS`,

	`1_INVOCATION_PER_4X4_PIXELS`
}

