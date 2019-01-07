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
import com.kgl.vulkan.enums.DescriptorUpdateTemplateType
import com.kgl.vulkan.enums.PipelineBindPoint
import com.kgl.vulkan.handles.DescriptorSetLayout
import com.kgl.vulkan.handles.PipelineLayout
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.VK11
import org.lwjgl.vulkan.VkDescriptorUpdateTemplateCreateInfo
import org.lwjgl.vulkan.VkDescriptorUpdateTemplateEntry

actual class DescriptorUpdateTemplateEntryBuilder(internal val target: VkDescriptorUpdateTemplateEntry) {
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

	actual var descriptorCount: UInt
		get() = target.descriptorCount().toUInt()
		set(value) {
			target.descriptorCount(value.toVkType())
		}

	actual var descriptorType: DescriptorType?
		get() = DescriptorType.from(target.descriptorType())
		set(value) {
			target.descriptorType(value.toVkType())
		}

	actual var offset: ULong
		get() = target.offset().toULong()
		set(value) {
			target.offset(value.toVkType())
		}

	actual var stride: ULong
		get() = target.stride().toULong()
		set(value) {
			target.stride(value.toVkType())
		}

	internal fun init() {
	}
}

actual class DescriptorUpdateTemplateEntriesBuilder {
	val targets: MutableList<(VkDescriptorUpdateTemplateEntry) -> Unit> = mutableListOf()

	actual fun entry(block: DescriptorUpdateTemplateEntryBuilder.() -> Unit) {
		targets += {
			val builder = DescriptorUpdateTemplateEntryBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class DescriptorUpdateTemplateCreateInfoBuilder(internal val target: VkDescriptorUpdateTemplateCreateInfo) {
	actual var templateType: DescriptorUpdateTemplateType?
		get() = DescriptorUpdateTemplateType.from(target.templateType())
		set(value) {
			target.templateType(value.toVkType())
		}

	actual var pipelineBindPoint: PipelineBindPoint?
		get() = PipelineBindPoint.from(target.pipelineBindPoint())
		set(value) {
			target.pipelineBindPoint(value.toVkType())
		}

	actual var set: UInt
		get() = target.set().toUInt()
		set(value) {
			target.set(value.toVkType())
		}

	actual fun descriptorUpdateEntries(block: DescriptorUpdateTemplateEntriesBuilder.() -> Unit) {
		val targets = DescriptorUpdateTemplateEntriesBuilder().apply(block).targets
		target.pDescriptorUpdateEntries(targets.mapToStackArray(VkDescriptorUpdateTemplateEntry::callocStack))
	}

	internal fun init(descriptorSetLayout: DescriptorSetLayout?, pipelineLayout: PipelineLayout) {
		target.sType(VK11.VK_STRUCTURE_TYPE_DESCRIPTOR_UPDATE_TEMPLATE_CREATE_INFO)
		target.pNext(0)
		target.flags(0)
		target.descriptorSetLayout(descriptorSetLayout.toVkType())
		target.pipelineLayout(pipelineLayout.toVkType())
	}
}

