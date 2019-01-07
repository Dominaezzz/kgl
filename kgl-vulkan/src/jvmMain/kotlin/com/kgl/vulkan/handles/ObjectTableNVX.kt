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

import com.kgl.vulkan.dsls.RegisterObjectsNVXBuilder
import com.kgl.vulkan.enums.ObjectEntryTypeNVX
import com.kgl.vulkan.utils.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.NVXDeviceGeneratedCommands.vkDestroyObjectTableNVX
import org.lwjgl.vulkan.NVXDeviceGeneratedCommands.vkUnregisterObjectsNVX
import org.lwjgl.vulkan.VK11.VK_SUCCESS
import org.lwjgl.vulkan.VkObjectTableEntryNVX

actual class ObjectTableNVX(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val objectTable = this
		val device = objectTable.device
		MemoryStack.stackPush()
		try {
			vkDestroyObjectTableNVX(device.toVkType(), objectTable.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun registerObjects(objectIndices: UIntArray, block: RegisterObjectsNVXBuilder.() -> Unit) {
		TODO()
		val objectTable = this
		val device = objectTable.device
		MemoryStack.stackPush()
		try {
			val targets = RegisterObjectsNVXBuilder().apply(block).targets
			MemoryStack.stackGet().mallocPointer(targets.size).also {
				targets.forEachIndexed { index, item ->
					val target = VkObjectTableEntryNVX.callocStack()
					item(target)
					// it[index] = target
				}
			}
			val targetArray = targets.mapToStackArray(VkObjectTableEntryNVX::callocStack)
//			val result = vkRegisterObjectsNVX(device.toVkType(), objectTable.toVkType(),
//					targetArray, objectIndices.toVkType())
//			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun unregisterObjects(objectEntryTypes: Collection<ObjectEntryTypeNVX>, objectIndices: UIntArray) {
		val objectTable = this
		val device = objectTable.device
		MemoryStack.stackPush()
		try {
			val result = vkUnregisterObjectsNVX(device.toVkType(), objectTable.toVkType(),
					objectEntryTypes.toVkType(), objectIndices.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

