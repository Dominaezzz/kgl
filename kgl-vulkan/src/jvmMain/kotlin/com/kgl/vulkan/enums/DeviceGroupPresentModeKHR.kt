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
import org.lwjgl.vulkan.KHRSwapchain

actual enum class DeviceGroupPresentModeKHR(override val value: Int) : VkFlag<DeviceGroupPresentModeKHR> {
	LOCAL(KHRSwapchain.VK_DEVICE_GROUP_PRESENT_MODE_LOCAL_BIT_KHR),

	REMOTE(KHRSwapchain.VK_DEVICE_GROUP_PRESENT_MODE_REMOTE_BIT_KHR),

	SUM(KHRSwapchain.VK_DEVICE_GROUP_PRESENT_MODE_SUM_BIT_KHR),

	LOCAL_MULTI_DEVICE(KHRSwapchain.VK_DEVICE_GROUP_PRESENT_MODE_LOCAL_MULTI_DEVICE_BIT_KHR);

	companion object {
		private val enumLookUpMap: Map<Int, DeviceGroupPresentModeKHR> =
				enumValues<DeviceGroupPresentModeKHR>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<DeviceGroupPresentModeKHR> = VkFlag(value)

		fun from(value: Int): DeviceGroupPresentModeKHR = enumLookUpMap[value]!!
	}
}

