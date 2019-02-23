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
package com.kgl.vulkan.dsls

import com.kgl.vulkan.utils.toVkType
import cvulkan.VkShaderModuleCreateInfo
import kotlinx.cinterop.reinterpret

actual class ShaderModuleCreateInfoBuilder(internal val target: VkShaderModuleCreateInfo) {
	internal fun init(code: UByteArray) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.pCode = code.toVkType().reinterpret()
		target.codeSize = code.size.toULong()
	}
}

