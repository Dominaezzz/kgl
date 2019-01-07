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
import org.lwjgl.vulkan.KHRSamplerMirrorClampToEdge
import org.lwjgl.vulkan.VK11

actual enum class SamplerAddressMode(override val value: Int) : VkEnum<SamplerAddressMode> {
	REPEAT(VK11.VK_SAMPLER_ADDRESS_MODE_REPEAT),

	MIRRORED_REPEAT(VK11.VK_SAMPLER_ADDRESS_MODE_MIRRORED_REPEAT),

	CLAMP_TO_EDGE(VK11.VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE),

	CLAMP_TO_BORDER(VK11.VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER),

	MIRROR_CLAMP_TO_EDGE(KHRSamplerMirrorClampToEdge.VK_SAMPLER_ADDRESS_MODE_MIRROR_CLAMP_TO_EDGE);

	companion object {
		fun from(value: Int): SamplerAddressMode = enumValues<SamplerAddressMode>()[value]
	}
}

