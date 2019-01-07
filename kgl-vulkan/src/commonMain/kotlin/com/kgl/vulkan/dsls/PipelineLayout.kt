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
import com.kgl.vulkan.utils.VkFlag

expect class PushConstantRangeBuilder

expect class PushConstantRangesBuilder {
	fun range(
			stageFlags: VkFlag<ShaderStage>,
			offset: UInt,
			size: UInt
	)
}

expect class PipelineLayoutCreateInfoBuilder {
	fun pushConstantRanges(block: PushConstantRangesBuilder.() -> Unit)
}

