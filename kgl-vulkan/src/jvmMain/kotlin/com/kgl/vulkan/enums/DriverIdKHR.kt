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
import org.lwjgl.vulkan.KHRDriverProperties

actual enum class DriverIdKHR(override val value: Int) : VkEnum<DriverIdKHR> {
	AMD_PROPRIETARY(KHRDriverProperties.VK_DRIVER_ID_AMD_PROPRIETARY_KHR),

	AMD_OPEN_SOURCE(KHRDriverProperties.VK_DRIVER_ID_AMD_OPEN_SOURCE_KHR),

	MESA_RADV(KHRDriverProperties.VK_DRIVER_ID_MESA_RADV_KHR),

	NVIDIA_PROPRIETARY(KHRDriverProperties.VK_DRIVER_ID_NVIDIA_PROPRIETARY_KHR),

	INTEL_PROPRIETARY_WINDOWS(KHRDriverProperties.VK_DRIVER_ID_INTEL_PROPRIETARY_WINDOWS_KHR),

	INTEL_OPEN_SOURCE_MESA(KHRDriverProperties.VK_DRIVER_ID_INTEL_OPEN_SOURCE_MESA_KHR),

	IMAGINATION_PROPRIETARY(KHRDriverProperties.VK_DRIVER_ID_IMAGINATION_PROPRIETARY_KHR),

	QUALCOMM_PROPRIETARY(KHRDriverProperties.VK_DRIVER_ID_QUALCOMM_PROPRIETARY_KHR),

	ARM_PROPRIETARY(KHRDriverProperties.VK_DRIVER_ID_ARM_PROPRIETARY_KHR);

	companion object {
		private val enumLookUpMap: Map<Int, DriverIdKHR> = enumValues<DriverIdKHR>().associateBy({
			it.value
		})

		fun from(value: Int): DriverIdKHR = enumLookUpMap[value]!!
	}
}

