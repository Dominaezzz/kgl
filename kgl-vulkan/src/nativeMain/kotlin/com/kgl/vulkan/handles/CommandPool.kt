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

import com.kgl.vulkan.dsls.CommandBufferAllocateInfoBuilder
import com.kgl.vulkan.enums.CommandBufferLevel
import com.kgl.vulkan.enums.CommandPoolReset
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class CommandPool(override val ptr: VkCommandPool, actual val device: Device) : VkHandleNative<VkCommandPool>(), VkHandle {
	override fun close() {
		val commandPool = this
		val device = commandPool.device
		VirtualStack.push()
		try {
			vkDestroyCommandPool(device.toVkType(), commandPool.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun reset(flags: VkFlag<CommandPoolReset>?) {
		val commandPool = this
		val device = commandPool.device
		VirtualStack.push()
		try {
			val result = vkResetCommandPool(device.toVkType(), commandPool.toVkType(),
					flags.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun allocate(level: CommandBufferLevel, commandBufferCount: UInt): List<CommandBuffer> {
		val commandPool = this
		val device = commandPool.device
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkCommandBufferAllocateInfo>().ptr
			val builder = CommandBufferAllocateInfoBuilder(target.pointed)
			builder.init(commandPool, level, commandBufferCount)

			val outputPtr = VirtualStack.allocArray<VkCommandBufferVar>(commandBufferCount.toInt())
			val result = vkAllocateCommandBuffers(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(commandBufferCount.toInt()) { CommandBuffer(outputPtr[it]!!, this) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun free(commandBuffers: Collection<CommandBuffer>) {
		val commandPool = this
		val device = commandPool.device
		VirtualStack.push()
		try {
			vkFreeCommandBuffers(device.toVkType(), commandPool.toVkType(),
					commandBuffers.size.toUInt(), commandBuffers.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun trim() {
		val commandPool = this
		val device = commandPool.device
		VirtualStack.push()
		try {
			vkTrimCommandPool(device.toVkType(), commandPool.toVkType(), 0U.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}
}

