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
import cvulkan.VK_CONSERVATIVE_RASTERIZATION_MODE_DISABLED_EXT
import cvulkan.VK_CONSERVATIVE_RASTERIZATION_MODE_OVERESTIMATE_EXT
import cvulkan.VK_CONSERVATIVE_RASTERIZATION_MODE_UNDERESTIMATE_EXT
import cvulkan.VkConservativeRasterizationModeEXT

actual enum class ConservativeRasterizationModeEXT(override val value: VkConservativeRasterizationModeEXT) : VkEnum<ConservativeRasterizationModeEXT> {
	DISABLED(VK_CONSERVATIVE_RASTERIZATION_MODE_DISABLED_EXT),

	OVERESTIMATE(VK_CONSERVATIVE_RASTERIZATION_MODE_OVERESTIMATE_EXT),

	UNDERESTIMATE(VK_CONSERVATIVE_RASTERIZATION_MODE_UNDERESTIMATE_EXT);

	companion object {
		fun from(value: VkConservativeRasterizationModeEXT): ConservativeRasterizationModeEXT =
				enumValues<ConservativeRasterizationModeEXT>()[value.toInt()]
	}
}

