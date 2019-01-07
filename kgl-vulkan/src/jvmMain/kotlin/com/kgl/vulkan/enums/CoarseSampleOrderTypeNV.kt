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
import org.lwjgl.vulkan.NVShadingRateImage

actual enum class CoarseSampleOrderTypeNV(override val value: Int) : VkEnum<CoarseSampleOrderTypeNV> {
	DEFAULT(NVShadingRateImage.VK_COARSE_SAMPLE_ORDER_TYPE_DEFAULT_NV),

	CUSTOM(NVShadingRateImage.VK_COARSE_SAMPLE_ORDER_TYPE_CUSTOM_NV),

	PIXEL_MAJOR(NVShadingRateImage.VK_COARSE_SAMPLE_ORDER_TYPE_PIXEL_MAJOR_NV),

	SAMPLE_MAJOR(NVShadingRateImage.VK_COARSE_SAMPLE_ORDER_TYPE_SAMPLE_MAJOR_NV);

	companion object {
		fun from(value: Int): CoarseSampleOrderTypeNV = enumValues<CoarseSampleOrderTypeNV>()[value]
	}
}

