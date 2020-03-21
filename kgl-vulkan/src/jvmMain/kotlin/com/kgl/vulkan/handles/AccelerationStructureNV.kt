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
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import io.ktor.utils.io.bits.Memory
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.NVRayTracing.*
import org.lwjgl.vulkan.VK11.VK_SUCCESS
import org.lwjgl.vulkan.VkAccelerationStructureMemoryRequirementsInfoNV
import org.lwjgl.vulkan.VkMemoryRequirements2KHR

actual class AccelerationStructureNV(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val accelerationStructure = this
		val device = accelerationStructure.device
		MemoryStack.stackPush()
		try {
			vkDestroyAccelerationStructureNV(device.toVkType(), accelerationStructure.toVkType(),
					null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getHandle(pData: Memory) {
		val accelerationStructure = this
		val device = accelerationStructure.device
		MemoryStack.stackPush()
		try {
			val result = vkGetAccelerationStructureHandleNV(device.toVkType(),
					accelerationStructure.toVkType(), pData.buffer)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getMemoryRequirements(type: AccelerationStructureMemoryRequirementsTypeNV, block: AccelerationStructureMemoryRequirementsInfoNVBuilder.() -> Unit): MemoryRequirements2 {
		MemoryStack.stackPush()
		try {
			val target = VkAccelerationStructureMemoryRequirementsInfoNV.callocStack()
			val builder = AccelerationStructureMemoryRequirementsInfoNVBuilder(target)
			builder.init(type, this)
			builder.apply(block)
			val outputPtr = VkMemoryRequirements2KHR.mallocStack()
			vkGetAccelerationStructureMemoryRequirementsNV(device.toVkType(), target, outputPtr)
			return MemoryRequirements2.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

