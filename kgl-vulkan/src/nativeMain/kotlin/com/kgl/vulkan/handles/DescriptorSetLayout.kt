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

actual class DescriptorSetLayout(
	override val ptr: VkDescriptorSetLayout,
	actual val device: Device
) : VkHandleNative<VkDescriptorSetLayout>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val descriptorSetLayout = this
		val device = descriptorSetLayout.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyDescriptorSetLayout(device.toVkType(), descriptorSetLayout.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDescriptorUpdateTemplate(
		pipelineLayout: PipelineLayout,
		block: DescriptorUpdateTemplateCreateInfoBuilder.() -> Unit
	): DescriptorUpdateTemplate {
		val descriptorSetLayout = this
		val device = descriptorSetLayout.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDescriptorUpdateTemplateCreateInfo>().ptr
			val builder = DescriptorUpdateTemplateCreateInfoBuilder(target.pointed)
			builder.init(descriptorSetLayout, pipelineLayout)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDescriptorUpdateTemplateVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateDescriptorUpdateTemplate!!(
				device.toVkType(), target, null,
				outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DescriptorUpdateTemplate(outputVar.value!!, this.device)
		} finally {
			VirtualStack.pop()
		}
	}
}
