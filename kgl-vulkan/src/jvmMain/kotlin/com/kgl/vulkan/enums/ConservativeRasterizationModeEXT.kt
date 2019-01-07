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
import org.lwjgl.vulkan.EXTConservativeRasterization

actual enum class ConservativeRasterizationModeEXT(override val value: Int) : VkEnum<ConservativeRasterizationModeEXT> {
	DISABLED(EXTConservativeRasterization.VK_CONSERVATIVE_RASTERIZATION_MODE_DISABLED_EXT),

	OVERESTIMATE(EXTConservativeRasterization.VK_CONSERVATIVE_RASTERIZATION_MODE_OVERESTIMATE_EXT),

	UNDERESTIMATE(EXTConservativeRasterization.VK_CONSERVATIVE_RASTERIZATION_MODE_UNDERESTIMATE_EXT);

	companion object {
		fun from(value: Int): ConservativeRasterizationModeEXT =
				enumValues<ConservativeRasterizationModeEXT>()[value]
	}
}

