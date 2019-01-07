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

import com.kgl.vulkan.enums.ExternalSemaphoreHandleType
import com.kgl.vulkan.enums.SemaphoreImport
import com.kgl.vulkan.handles.Semaphore
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.*

actual class SemaphoreCreateInfoBuilder(internal val target: VkSemaphoreCreateInfo) {
	internal fun init() {
		target.sType(VK11.VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO)
		target.pNext(0)
		target.flags(0)
	}
}

actual class SemaphoreGetFdInfoKHRBuilder(internal val target: VkSemaphoreGetFdInfoKHR) {
	actual var handleType: ExternalSemaphoreHandleType?
		get() = ExternalSemaphoreHandleType.from(target.handleType())
		set(value) {
			target.handleType(value.toVkType())
		}

	internal fun init(semaphore: Semaphore) {
		target.sType(KHRExternalSemaphoreFd.VK_STRUCTURE_TYPE_SEMAPHORE_GET_FD_INFO_KHR)
		target.pNext(0)
		target.semaphore(semaphore.toVkType())
	}
}

actual class ImportSemaphoreFdInfoKHRBuilder(internal val target: VkImportSemaphoreFdInfoKHR) {
	actual var flags: VkFlag<SemaphoreImport>?
		get() = SemaphoreImport.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual var handleType: ExternalSemaphoreHandleType?
		get() = ExternalSemaphoreHandleType.from(target.handleType())
		set(value) {
			target.handleType(value.toVkType())
		}

	actual var fd: Int
		get() = target.fd()
		set(value) {
			target.fd(value.toVkType())
		}

	internal fun init(semaphore: Semaphore) {
		target.sType(KHRExternalSemaphoreFd.VK_STRUCTURE_TYPE_IMPORT_SEMAPHORE_FD_INFO_KHR)
		target.pNext(0)
		target.semaphore(semaphore.toVkType())
	}
}

