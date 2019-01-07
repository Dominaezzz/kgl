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
import org.lwjgl.vulkan.NVRayTracing
import org.lwjgl.vulkan.VK11

actual enum class PipelineCreate(override val value: Int) : VkFlag<PipelineCreate> {
	DISABLE_OPTIMIZATION(VK11.VK_PIPELINE_CREATE_DISABLE_OPTIMIZATION_BIT),

	ALLOW_DERIVATIVES(VK11.VK_PIPELINE_CREATE_ALLOW_DERIVATIVES_BIT),

	DERIVATIVE(VK11.VK_PIPELINE_CREATE_DERIVATIVE_BIT),

	VIEW_INDEX_FROM_DEVICE_INDEX(VK11.VK_PIPELINE_CREATE_VIEW_INDEX_FROM_DEVICE_INDEX_BIT),

	DISPATCH_BASE(VK11.VK_PIPELINE_CREATE_DISPATCH_BASE),

	DEFER_COMPILE_NV(NVRayTracing.VK_PIPELINE_CREATE_DEFER_COMPILE_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<Int, PipelineCreate> =
				enumValues<PipelineCreate>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<PipelineCreate> = VkFlag(value)

		fun from(value: Int): PipelineCreate = enumLookUpMap[value]!!
	}
}

