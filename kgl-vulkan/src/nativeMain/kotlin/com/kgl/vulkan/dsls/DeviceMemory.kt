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
package com.kgl.vulkan.dsls

import com.kgl.vulkan.enums.ExternalMemoryHandleType
import com.kgl.vulkan.handles.DeviceMemory
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkMemoryGetFdInfoKHR

actual class MemoryGetFdInfoKHRBuilder(internal val target: VkMemoryGetFdInfoKHR) {
	actual var handleType: ExternalMemoryHandleType?
		get() = ExternalMemoryHandleType.from(target.handleType)
		set(value) {
			target.handleType = value.toVkType()
		}

	internal fun init(memory: DeviceMemory) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_MEMORY_GET_FD_INFO_KHR
		target.pNext = null
		target.memory = memory.toVkType()
	}
}

