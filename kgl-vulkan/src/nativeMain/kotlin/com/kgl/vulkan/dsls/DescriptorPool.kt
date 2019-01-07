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

import com.kgl.vulkan.enums.DescriptorPoolCreate
import com.kgl.vulkan.enums.DescriptorType
import com.kgl.vulkan.handles.DescriptorPool
import com.kgl.vulkan.handles.DescriptorSetLayout
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkDescriptorPoolCreateInfo
import cvulkan.VkDescriptorPoolSize
import cvulkan.VkDescriptorSetAllocateInfo

actual class DescriptorPoolSizeBuilder(internal val target: VkDescriptorPoolSize) {
	internal fun init(type: DescriptorType, descriptorCount: UInt) {
		target.type = type.toVkType()
		target.descriptorCount = descriptorCount.toVkType()
	}
}

actual class DescriptorPoolSizesBuilder {
	val targets: MutableList<(VkDescriptorPoolSize) -> Unit> = mutableListOf()

	actual fun size(type: DescriptorType, descriptorCount: UInt) {
		targets += {
			val builder = DescriptorPoolSizeBuilder(it)
			builder.init(type, descriptorCount)
		}
	}
}

actual class DescriptorPoolCreateInfoBuilder(internal val target: VkDescriptorPoolCreateInfo) {
	actual var flags: VkFlag<DescriptorPoolCreate>?
		get() = DescriptorPoolCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual fun poolSizes(block: DescriptorPoolSizesBuilder.() -> Unit) {
		val targets = DescriptorPoolSizesBuilder().apply(block).targets
		target.pPoolSizes = targets.mapToStackArray()
		target.poolSizeCount = targets.size.toUInt()
	}

	internal fun init(maxSets: UInt) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO
		target.pNext = null
		target.maxSets = maxSets.toVkType()
	}
}

actual class DescriptorSetAllocateInfoBuilder(internal val target: VkDescriptorSetAllocateInfo) {
	internal fun init(descriptorPool: DescriptorPool, setLayouts: Collection<DescriptorSetLayout>) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO
		target.pNext = null
		target.descriptorPool = descriptorPool.toVkType()
		target.pSetLayouts = setLayouts.toVkType()
		target.descriptorSetCount = setLayouts.size.toUInt()
	}
}

