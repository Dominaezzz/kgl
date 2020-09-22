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

import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.utils.*
import org.lwjgl.system.*
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.VK11.*

actual class DescriptorPool(
	override val ptr: Long,
	actual val device: Device,
	actual val maxSets: UInt
) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val descriptorPool = this
		val device = descriptorPool.device
		MemoryStack.stackPush()
		try {
			vkDestroyDescriptorPool(device.toVkType(), descriptorPool.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun reset() {
		val descriptorPool = this
		val device = descriptorPool.device
		MemoryStack.stackPush()
		try {
			val result = vkResetDescriptorPool(
				device.toVkType(), descriptorPool.toVkType(),
				0U.toVkType()
			)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun allocate(setLayouts: Collection<DescriptorSetLayout>): List<DescriptorSet> {
		val descriptorPool = this
		val device = descriptorPool.device
		MemoryStack.stackPush()
		try {
			val target = VkDescriptorSetAllocateInfo.callocStack()
			val builder = DescriptorSetAllocateInfoBuilder(target)
			builder.init(descriptorPool, setLayouts)
			val outputCount = setLayouts.size
			val outputPtr = MemoryStack.stackGet().mallocLong(outputCount)
			val result = vkAllocateDescriptorSets(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { DescriptorSet(outputPtr[it], this) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun free(descriptorSets: Collection<DescriptorSet>) {
		val descriptorPool = this
		val device = descriptorPool.device
		MemoryStack.stackPush()
		try {
			val result = vkFreeDescriptorSets(device.toVkType(), descriptorPool.toVkType(), descriptorSets.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}
