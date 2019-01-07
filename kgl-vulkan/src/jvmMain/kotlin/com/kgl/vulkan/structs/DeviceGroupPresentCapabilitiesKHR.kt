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
package com.kgl.vulkan.structs

import com.kgl.vulkan.enums.DeviceGroupPresentModeKHR
import org.lwjgl.vulkan.VK11.VK_MAX_DEVICE_GROUP_SIZE
import org.lwjgl.vulkan.VkDeviceGroupPresentCapabilitiesKHR

fun DeviceGroupPresentCapabilitiesKHR.Companion.from(ptr: VkDeviceGroupPresentCapabilitiesKHR): DeviceGroupPresentCapabilitiesKHR = DeviceGroupPresentCapabilitiesKHR(
		UIntArray(VK_MAX_DEVICE_GROUP_SIZE) { ptr.presentMask(it).toUInt() },
		DeviceGroupPresentModeKHR.fromMultiple(ptr.modes())
)
