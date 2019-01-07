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

import com.kgl.vulkan.utils.VkFlag
import org.lwjgl.vulkan.NVMeshShader
import org.lwjgl.vulkan.NVRayTracing
import org.lwjgl.vulkan.VK11

actual enum class ShaderStage(override val value: Int) : VkFlag<ShaderStage> {
	VERTEX(VK11.VK_SHADER_STAGE_VERTEX_BIT),

	TESSELLATION_CONTROL(VK11.VK_SHADER_STAGE_TESSELLATION_CONTROL_BIT),

	TESSELLATION_EVALUATION(VK11.VK_SHADER_STAGE_TESSELLATION_EVALUATION_BIT),

	GEOMETRY(VK11.VK_SHADER_STAGE_GEOMETRY_BIT),

	FRAGMENT(VK11.VK_SHADER_STAGE_FRAGMENT_BIT),

	COMPUTE(VK11.VK_SHADER_STAGE_COMPUTE_BIT),

	TASK_NV(NVMeshShader.VK_SHADER_STAGE_TASK_BIT_NV),

	MESH_NV(NVMeshShader.VK_SHADER_STAGE_MESH_BIT_NV),

	RAYGEN_NV(NVRayTracing.VK_SHADER_STAGE_RAYGEN_BIT_NV),

	ANY_HIT_NV(NVRayTracing.VK_SHADER_STAGE_ANY_HIT_BIT_NV),

	CLOSEST_HIT_NV(NVRayTracing.VK_SHADER_STAGE_CLOSEST_HIT_BIT_NV),

	MISS_NV(NVRayTracing.VK_SHADER_STAGE_MISS_BIT_NV),

	INTERSECTION_NV(NVRayTracing.VK_SHADER_STAGE_INTERSECTION_BIT_NV),

	CALLABLE_NV(NVRayTracing.VK_SHADER_STAGE_CALLABLE_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<Int, ShaderStage> = enumValues<ShaderStage>().associateBy({
			it.value
		})

		fun fromMultiple(value: Int): VkFlag<ShaderStage> = VkFlag(value)

		fun from(value: Int): ShaderStage = enumLookUpMap[value]!!
	}
}

