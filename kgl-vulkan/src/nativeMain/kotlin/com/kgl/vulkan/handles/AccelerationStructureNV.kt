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

import com.kgl.vulkan.dsls.AccelerationStructureMemoryRequirementsInfoNVBuilder
import com.kgl.vulkan.enums.AccelerationStructureMemoryRequirementsTypeNV
import com.kgl.vulkan.structs.MemoryRequirements2
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.io.core.IoBuffer

actual class AccelerationStructureNV(override val ptr: VkAccelerationStructureNV, actual val device: Device) : VkHandleNative<VkAccelerationStructureNV>(), VkHandle {
	override fun close() {
		val accelerationStructure = this
		val device = accelerationStructure.device
		VirtualStack.push()
		try {
			vkDestroyAccelerationStructureNV(device.toVkType(), accelerationStructure.toVkType(),
					null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getHandle(pData: IoBuffer) {
		val accelerationStructure = this
		val device = accelerationStructure.device
		VirtualStack.push()
		try {
			pData.writeDirect {
				val result = vkGetAccelerationStructureHandleNV(device.toVkType(),
						accelerationStructure.toVkType(), pData.writeRemaining.toULong(), it)
				if (result != VK_SUCCESS) handleVkResult(result)
				pData.writeRemaining
			}
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
			vkGetAccelerationStructureMemoryRequirementsNV(device.toVkType(), target, outputPtr)
			return MemoryRequirements2.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}
}

