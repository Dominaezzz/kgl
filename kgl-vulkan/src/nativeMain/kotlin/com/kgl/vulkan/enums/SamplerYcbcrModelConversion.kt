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

actual enum class SamplerYcbcrModelConversion(override val value: VkSamplerYcbcrModelConversion) : VkEnum<SamplerYcbcrModelConversion> {
	RGB_IDENTITY(VK_SAMPLER_YCBCR_MODEL_CONVERSION_RGB_IDENTITY),

	YCBCR_IDENTITY(VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_IDENTITY),

	YCBCR_709(VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_709),

	YCBCR_601(VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_601),

	YCBCR_2020(VK_SAMPLER_YCBCR_MODEL_CONVERSION_YCBCR_2020);

	companion object {
		fun from(value: VkSamplerYcbcrModelConversion): SamplerYcbcrModelConversion =
				enumValues<SamplerYcbcrModelConversion>()[value.toInt()]
	}
}

