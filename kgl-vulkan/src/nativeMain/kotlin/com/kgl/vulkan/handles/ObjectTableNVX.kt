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
package com.kgl.vulkan.handles

import com.kgl.core.utils.VirtualStack
import com.kgl.vulkan.dsls.RegisterObjectsNVXBuilder
import com.kgl.vulkan.enums.ObjectEntryTypeNVX
import com.kgl.vulkan.utils.*
import cvulkan.VK_SUCCESS
import cvulkan.VkObjectTableNVX
import kotlinx.cinterop.invoke

actual class ObjectTableNVX(override val ptr: VkObjectTableNVX, actual val device: Device) : VkHandleNative<VkObjectTableNVX>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val objectTable = this
		val device = objectTable.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyObjectTableNVX!!(device.toVkType(), objectTable.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun registerObjects(objectIndices: UIntArray, block: RegisterObjectsNVXBuilder.() -> Unit) {
		TODO()
		val objectTable = this
		val device = objectTable.device
		VirtualStack.push()
		try {
			val targets = RegisterObjectsNVXBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
//			val result = dispatchTable.vkRegisterObjectsNVX!!(device.toVkType(), objectTable.toVkType(),
//					targets.size.toUInt(), targetArray, objectIndices.toVkType())
//			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun unregisterObjects(objectEntryTypes: Collection<ObjectEntryTypeNVX>, objectIndices: UIntArray) {
		val objectTable = this
		val device = objectTable.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkUnregisterObjectsNVX!!(device.toVkType(), objectTable.toVkType(),
					objectEntryTypes.size.toUInt(), objectEntryTypes.toVkType(),
					objectIndices.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}
}

