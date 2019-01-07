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

actual enum class BuildAccelerationStructureNV(override val value: Int) : VkFlag<BuildAccelerationStructureNV> {
	ALLOW_UPDATE(NVRayTracing.VK_BUILD_ACCELERATION_STRUCTURE_ALLOW_UPDATE_BIT_NV),

	ALLOW_COMPACTION(NVRayTracing.VK_BUILD_ACCELERATION_STRUCTURE_ALLOW_COMPACTION_BIT_NV),

	PREFER_FAST_TRACE(NVRayTracing.VK_BUILD_ACCELERATION_STRUCTURE_PREFER_FAST_TRACE_BIT_NV),

	PREFER_FAST_BUILD(NVRayTracing.VK_BUILD_ACCELERATION_STRUCTURE_PREFER_FAST_BUILD_BIT_NV),

	LOW_MEMORY(NVRayTracing.VK_BUILD_ACCELERATION_STRUCTURE_LOW_MEMORY_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<Int, BuildAccelerationStructureNV> =
				enumValues<BuildAccelerationStructureNV>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<BuildAccelerationStructureNV> = VkFlag(value)

		fun from(value: Int): BuildAccelerationStructureNV = enumLookUpMap[value]!!
	}
}

