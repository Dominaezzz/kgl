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

import com.kgl.vulkan.enums.PhysicalDeviceType
import cvulkan.VK_UUID_SIZE
import cvulkan.VkPhysicalDeviceProperties
import kotlinx.cinterop.get
import kotlinx.cinterop.toKString

fun PhysicalDeviceProperties.Companion.from(ptr: VkPhysicalDeviceProperties): PhysicalDeviceProperties = PhysicalDeviceProperties(
		ptr.apiVersion,
		ptr.driverVersion,
		ptr.vendorID,
		ptr.deviceID,
		PhysicalDeviceType.from(ptr.deviceType),
		ptr.deviceName.toKString(),
		UByteArray(VK_UUID_SIZE) { ptr.pipelineCacheUUID[it] },
		PhysicalDeviceLimits.from(ptr.limits),
		PhysicalDeviceSparseProperties.from(ptr.sparseProperties)
)
