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
import cvulkan.VK_SHADER_INFO_TYPE_BINARY_AMD
import cvulkan.VK_SHADER_INFO_TYPE_DISASSEMBLY_AMD
import cvulkan.VK_SHADER_INFO_TYPE_STATISTICS_AMD
import cvulkan.VkShaderInfoTypeAMD

actual enum class ShaderInfoTypeAMD(override val value: VkShaderInfoTypeAMD) : VkEnum<ShaderInfoTypeAMD> {
	STATISTICS(VK_SHADER_INFO_TYPE_STATISTICS_AMD),

	BINARY(VK_SHADER_INFO_TYPE_BINARY_AMD),

	DISASSEMBLY(VK_SHADER_INFO_TYPE_DISASSEMBLY_AMD);

	companion object {
		fun from(value: VkShaderInfoTypeAMD): ShaderInfoTypeAMD =
				enumValues<ShaderInfoTypeAMD>()[value.toInt()]
	}
}

