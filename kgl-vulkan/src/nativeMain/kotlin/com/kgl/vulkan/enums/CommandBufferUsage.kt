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
package com.kgl.vulkan.enums

import com.kgl.vulkan.utils.VkFlag
import cvulkan.VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT
import cvulkan.VK_COMMAND_BUFFER_USAGE_RENDER_PASS_CONTINUE_BIT
import cvulkan.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT
import cvulkan.VkCommandBufferUsageFlagBits

actual enum class CommandBufferUsage(override val value: VkCommandBufferUsageFlagBits) : VkFlag<CommandBufferUsage> {
	ONE_TIME_SUBMIT(VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT),

	RENDER_PASS_CONTINUE(VK_COMMAND_BUFFER_USAGE_RENDER_PASS_CONTINUE_BIT),

	SIMULTANEOUS_USE(VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT);

	companion object {
		private val enumLookUpMap: Map<UInt, CommandBufferUsage> =
				enumValues<CommandBufferUsage>().associateBy({ it.value })

		fun fromMultiple(value: VkCommandBufferUsageFlagBits): VkFlag<CommandBufferUsage> =
				VkFlag(value)

		fun from(value: VkCommandBufferUsageFlagBits): CommandBufferUsage =
				enumLookUpMap[value]!!
	}
}

