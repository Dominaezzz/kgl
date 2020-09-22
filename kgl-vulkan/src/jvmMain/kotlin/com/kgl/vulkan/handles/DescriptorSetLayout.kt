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

actual class DescriptorSetLayout(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val descriptorSetLayout = this
		val device = descriptorSetLayout.device
		MemoryStack.stackPush()
		try {
			vkDestroyDescriptorSetLayout(device.toVkType(), descriptorSetLayout.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDescriptorUpdateTemplate(
		pipelineLayout: PipelineLayout,
		block: DescriptorUpdateTemplateCreateInfoBuilder.() -> Unit
	): DescriptorUpdateTemplate {
		val descriptorSetLayout = this
		val device = descriptorSetLayout.device
		MemoryStack.stackPush()
		try {
			val target = VkDescriptorUpdateTemplateCreateInfo.callocStack()
			val builder = DescriptorUpdateTemplateCreateInfoBuilder(target)
			builder.init(descriptorSetLayout, pipelineLayout)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateDescriptorUpdateTemplate(
				device.toVkType(), target, null,
				outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DescriptorUpdateTemplate(outputPtr[0], this.device)
		} finally {
			MemoryStack.stackPop()
		}
	}
}
