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

import com.kgl.vulkan.enums.ShaderStage
import com.kgl.vulkan.handles.DescriptorSetLayout
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkPipelineLayoutCreateInfo
import cvulkan.VkPushConstantRange

actual class PushConstantRangeBuilder(internal val target: VkPushConstantRange) {
	internal fun init(
			stageFlags: VkFlag<ShaderStage>,
			offset: UInt,
			size: UInt
	) {
		target.stageFlags = stageFlags.toVkType()
		target.offset = offset.toVkType()
		target.size = size.toVkType()
	}
}

actual class PushConstantRangesBuilder {
	val targets: MutableList<(VkPushConstantRange) -> Unit> = mutableListOf()

	actual fun range(
			stageFlags: VkFlag<ShaderStage>,
			offset: UInt,
			size: UInt
	) {
		targets += {
			val builder = PushConstantRangeBuilder(it)
			builder.init(stageFlags, offset, size)
		}
	}
}

actual class PipelineLayoutCreateInfoBuilder(internal val target: VkPipelineLayoutCreateInfo) {
	actual fun pushConstantRanges(block: PushConstantRangesBuilder.() -> Unit) {
		val targets = PushConstantRangesBuilder().apply(block).targets
		target.pPushConstantRanges = targets.mapToStackArray()
		target.pushConstantRangeCount = targets.size.toUInt()
	}

	internal fun init(setLayouts: Collection<DescriptorSetLayout>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.pSetLayouts = setLayouts?.toVkType()
		target.setLayoutCount = setLayouts?.size?.toUInt() ?: 0U
	}
}

