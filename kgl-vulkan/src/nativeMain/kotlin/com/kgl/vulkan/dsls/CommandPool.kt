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

import com.kgl.vulkan.enums.CommandBufferLevel
import com.kgl.vulkan.enums.CommandPoolCreate
import com.kgl.vulkan.handles.CommandPool
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkCommandBufferAllocateInfo
import cvulkan.VkCommandPoolCreateInfo

actual class CommandPoolCreateInfoBuilder(internal val target: VkCommandPoolCreateInfo) {
	actual var flags: VkFlag<CommandPoolCreate>?
		get() = CommandPoolCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	internal fun init(queueFamilyIndex: UInt) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO
		target.pNext = null
		target.queueFamilyIndex = queueFamilyIndex.toVkType()
	}
}

actual class CommandBufferAllocateInfoBuilder(internal val target: VkCommandBufferAllocateInfo) {

	internal fun init(commandPool: CommandPool, level: CommandBufferLevel, commandBufferCount: UInt) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO
		target.pNext = null
		target.commandPool = commandPool.toVkType()
		target.level = level.toVkType()
		target.commandBufferCount = commandBufferCount.toVkType()
	}
}

