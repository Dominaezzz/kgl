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

import com.kgl.vulkan.enums.*
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.*
import org.lwjgl.vulkan.*

actual class WriteDescriptorSetBuilder(internal val target: VkWriteDescriptorSet) {
	actual var dstBinding: UInt
		get() = target.dstBinding().toUInt()
		set(value) {
			target.dstBinding(value.toVkType())
		}

	actual var dstArrayElement: UInt
		get() = target.dstArrayElement().toUInt()
		set(value) {
			target.dstArrayElement(value.toVkType())
		}

	actual var descriptorType: DescriptorType?
		get() = DescriptorType.from(target.descriptorType())
		set(value) {
			target.descriptorType(value.toVkType())
		}

	actual fun imageInfo(block: DescriptorImageInfosBuilder.() -> Unit) {
		val targets = DescriptorImageInfosBuilder().apply(block).targets
		target.pImageInfo(targets.mapToStackArray(VkDescriptorImageInfo::callocStack, ::DescriptorImageInfoBuilder))
	}

	actual fun bufferInfo(block: DescriptorBufferInfosBuilder.() -> Unit) {
		val targets = DescriptorBufferInfosBuilder().apply(block).targets
		target.pBufferInfo(targets.mapToStackArray(VkDescriptorBufferInfo::callocStack, ::DescriptorBufferInfoBuilder))
	}

	actual fun next(block: Next<WriteDescriptorSetBuilder>.() -> Unit) {
		Next(this).apply(block)
	}

	internal actual fun init(dstSet: DescriptorSet, texelBufferView: Collection<BufferView>?) {
		target.sType(VK11.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
		target.pNext(0)
		target.dstSet(dstSet.toVkType())
		target.pTexelBufferView(texelBufferView?.toVkType())
	}
}
