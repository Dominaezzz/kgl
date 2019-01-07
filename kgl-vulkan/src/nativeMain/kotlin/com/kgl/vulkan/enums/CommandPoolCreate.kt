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
import cvulkan.VK_COMMAND_POOL_CREATE_PROTECTED_BIT
import cvulkan.VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT
import cvulkan.VK_COMMAND_POOL_CREATE_TRANSIENT_BIT
import cvulkan.VkCommandPoolCreateFlagBits

actual enum class CommandPoolCreate(override val value: VkCommandPoolCreateFlagBits) : VkFlag<CommandPoolCreate> {
	TRANSIENT(VK_COMMAND_POOL_CREATE_TRANSIENT_BIT),

	RESET_COMMAND_BUFFER(VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT),

	PROTECTED(VK_COMMAND_POOL_CREATE_PROTECTED_BIT);

	companion object {
		private val enumLookUpMap: Map<UInt, CommandPoolCreate> =
				enumValues<CommandPoolCreate>().associateBy({ it.value })

		fun fromMultiple(value: VkCommandPoolCreateFlagBits): VkFlag<CommandPoolCreate> =
				VkFlag(value)

		fun from(value: VkCommandPoolCreateFlagBits): CommandPoolCreate =
				enumLookUpMap[value]!!
	}
}

