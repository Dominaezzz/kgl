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

import com.kgl.vulkan.enums.ExternalFenceHandleType
import com.kgl.vulkan.enums.FenceCreate
import com.kgl.vulkan.enums.FenceImport
import com.kgl.vulkan.handles.Fence
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.*

actual class FenceCreateInfoBuilder(internal val target: VkFenceCreateInfo) {
	actual var flags: VkFlag<FenceCreate>?
		get() = FenceCreate.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	internal fun init() {
		target.sType(VK11.VK_STRUCTURE_TYPE_FENCE_CREATE_INFO)
		target.pNext(0)
	}
}

actual class FenceGetFdInfoKHRBuilder(internal val target: VkFenceGetFdInfoKHR) {
	actual var handleType: ExternalFenceHandleType?
		get() = ExternalFenceHandleType.from(target.handleType())
		set(value) {
			target.handleType(value.toVkType())
		}

	internal fun init(fence: Fence) {
		target.sType(KHRExternalFenceFd.VK_STRUCTURE_TYPE_FENCE_GET_FD_INFO_KHR)
		target.pNext(0)
		target.fence(fence.toVkType())
	}
}

actual class ImportFenceFdInfoKHRBuilder(internal val target: VkImportFenceFdInfoKHR) {
	actual var flags: VkFlag<FenceImport>?
		get() = FenceImport.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual var handleType: ExternalFenceHandleType?
		get() = ExternalFenceHandleType.from(target.handleType())
		set(value) {
			target.handleType(value.toVkType())
		}

	actual var fd: Int
		get() = target.fd()
		set(value) {
			target.fd(value.toVkType())
		}

	internal fun init(fence: Fence) {
		target.sType(KHRExternalFenceFd.VK_STRUCTURE_TYPE_IMPORT_FENCE_FD_INFO_KHR)
		target.pNext(0)
		target.fence(fence.toVkType())
	}
}

