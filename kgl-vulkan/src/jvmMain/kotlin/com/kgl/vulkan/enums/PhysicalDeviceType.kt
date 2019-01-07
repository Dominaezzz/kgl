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
import org.lwjgl.vulkan.VK11

actual enum class PhysicalDeviceType(override val value: Int) : VkEnum<PhysicalDeviceType> {
	OTHER(VK11.VK_PHYSICAL_DEVICE_TYPE_OTHER),

	INTEGRATED_GPU(VK11.VK_PHYSICAL_DEVICE_TYPE_INTEGRATED_GPU),

	DISCRETE_GPU(VK11.VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU),

	VIRTUAL_GPU(VK11.VK_PHYSICAL_DEVICE_TYPE_VIRTUAL_GPU),

	CPU(VK11.VK_PHYSICAL_DEVICE_TYPE_CPU);

	companion object {
		fun from(value: Int): PhysicalDeviceType = enumValues<PhysicalDeviceType>()[value]
	}
}

