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

import com.kgl.core.ByteBuffer
import com.kgl.core.VirtualStack
import com.kgl.vulkan.dsls.AccelerationStructureMemoryRequirementsInfoNVBuilder
import com.kgl.vulkan.enums.AccelerationStructureMemoryRequirementsTypeNV
import com.kgl.vulkan.structs.MemoryRequirements2
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import cvulkan.VK_SUCCESS
import cvulkan.VkAccelerationStructureMemoryRequirementsInfoNV
import cvulkan.VkAccelerationStructureNV
import cvulkan.VkMemoryRequirements2
import kotlinx.cinterop.alloc
import kotlinx.cinterop.invoke
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr

actual class AccelerationStructureNV(override val ptr: VkAccelerationStructureNV, actual val device: Device) : VkHandleNative<VkAccelerationStructureNV>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val accelerationStructure = this
		val device = accelerationStructure.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyAccelerationStructureNV!!(device.toVkType(), accelerationStructure.toVkType(),
					null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getHandle(pData: ByteBuffer) {
		val accelerationStructure = this
		val device = accelerationStructure.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkGetAccelerationStructureHandleNV!!(device.toVkType(),
					accelerationStructure.toVkType(), pData.size.toULong(), pData.asCPointer())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getMemoryRequirements(type: AccelerationStructureMemoryRequirementsTypeNV, block: AccelerationStructureMemoryRequirementsInfoNVBuilder.() -> Unit): MemoryRequirements2 {
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkAccelerationStructureMemoryRequirementsInfoNV>().ptr
			val builder = AccelerationStructureMemoryRequirementsInfoNVBuilder(target.pointed)
			builder.init(type, this)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkMemoryRequirements2>()
			val outputPtr = outputVar.ptr
			dispatchTable.vkGetAccelerationStructureMemoryRequirementsNV!!(device.toVkType(), target, outputPtr)
			return MemoryRequirements2.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}
}

