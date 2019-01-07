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
import org.lwjgl.vulkan.NVRayTracing

actual enum class AccelerationStructureMemoryRequirementsTypeNV(override val value: Int) : VkEnum<AccelerationStructureMemoryRequirementsTypeNV> {
	OBJECT(NVRayTracing.VK_ACCELERATION_STRUCTURE_MEMORY_REQUIREMENTS_TYPE_OBJECT_NV),

	BUILD_SCRATCH(NVRayTracing.VK_ACCELERATION_STRUCTURE_MEMORY_REQUIREMENTS_TYPE_BUILD_SCRATCH_NV),

	UPDATE_SCRATCH(NVRayTracing.VK_ACCELERATION_STRUCTURE_MEMORY_REQUIREMENTS_TYPE_UPDATE_SCRATCH_NV);

	companion object {
		fun from(value: Int): AccelerationStructureMemoryRequirementsTypeNV =
				enumValues<AccelerationStructureMemoryRequirementsTypeNV>()[value]
	}
}

