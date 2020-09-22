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

import com.kgl.core.*
import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class DescriptorPool(
	override val ptr: VkDescriptorPool,
	actual val device: Device,
	actual val maxSets: UInt
) : VkHandleNative<VkDescriptorPool>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val descriptorPool = this
		val device = descriptorPool.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyDescriptorPool(device.toVkType(), descriptorPool.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun reset() {
		val descriptorPool = this
		val device = descriptorPool.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkResetDescriptorPool(
				device.toVkType(), descriptorPool.toVkType(),
				0U.toVkType()
			)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun allocate(setLayouts: Collection<DescriptorSetLayout>): List<DescriptorSet> {
		val descriptorPool = this
		val device = descriptorPool.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDescriptorSetAllocateInfo>().ptr
			val builder = DescriptorSetAllocateInfoBuilder(target.pointed)
			builder.init(descriptorPool, setLayouts)
			val outputCount = setLayouts.size
			val outputPtr = VirtualStack.allocArray<VkDescriptorSetVar>(outputCount)
			val result = dispatchTable.vkAllocateDescriptorSets(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { DescriptorSet(outputPtr[it]!!, this) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun free(descriptorSets: Collection<DescriptorSet>) {
		val descriptorPool = this
		val device = descriptorPool.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkFreeDescriptorSets(
				device.toVkType(), descriptorPool.toVkType(),
				descriptorSets.size.toUInt(), descriptorSets.toVkType()
			)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}
}
