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

import com.kgl.vulkan.enums.DescriptorType
import com.kgl.vulkan.handles.BufferView
import com.kgl.vulkan.handles.DescriptorSet
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkWriteDescriptorSet

actual class WriteDescriptorSetBuilder(internal val target: VkWriteDescriptorSet) {
	actual var dstBinding: UInt
		get() = target.dstBinding
		set(value) {
			target.dstBinding = value.toVkType()
		}

	actual var dstArrayElement: UInt
		get() = target.dstArrayElement
		set(value) {
			target.dstArrayElement = value.toVkType()
		}

	actual var descriptorType: DescriptorType?
		get() = DescriptorType.from(target.descriptorType)
		set(value) {
			target.descriptorType = value.toVkType()
		}

	actual fun imageInfo(block: DescriptorImageInfosBuilder.() -> Unit) {
		val targets = DescriptorImageInfosBuilder().apply(block).targets
		target.pImageInfo = targets.mapToStackArray(::DescriptorImageInfoBuilder)
		target.descriptorCount = targets.size.toUInt()
	}

	actual fun bufferInfo(block: DescriptorBufferInfosBuilder.() -> Unit) {
		val targets = DescriptorBufferInfosBuilder().apply(block).targets
		target.pBufferInfo = targets.mapToStackArray(::DescriptorBufferInfoBuilder)
		target.descriptorCount = targets.size.toUInt()
	}

	internal actual fun init(dstSet: DescriptorSet, texelBufferView: Collection<BufferView>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET
		target.pNext = null
		target.dstSet = dstSet.toVkType()
		target.pTexelBufferView = texelBufferView?.toVkType()
		target.descriptorCount = texelBufferView?.size?.toUInt() ?: 0U
	}
}
