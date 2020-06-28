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
package com.kgl.vulkan.handles

import com.kgl.core.DirectMemory
import com.kgl.vulkan.enums.ShaderInfoTypeAMD
import com.kgl.vulkan.enums.ShaderStage
import com.kgl.vulkan.utils.VkHandle

expect class Pipeline : VkHandle {
	val device: Device

	fun getShaderInfoAMD(
			shaderStage: ShaderStage,
			infoType: ShaderInfoTypeAMD,
			info: DirectMemory?
	)

	fun compileDeferredNV(shader: UInt)

	fun getRayTracingShaderGroupHandlesNV(
			firstGroup: UInt,
			groupCount: UInt,
			data: DirectMemory
	)
}

