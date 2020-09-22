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
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.utils.*
import org.lwjgl.system.*
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.VK11.*

actual class CommandPool(
	override val ptr: Long,
	actual val device: Device,
	actual val queueFamilyIndex: UInt
) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val commandPool = this
		val device = commandPool.device
		MemoryStack.stackPush()
		try {
			vkDestroyCommandPool(device.toVkType(), commandPool.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun reset(flags: VkFlag<CommandPoolReset>?) {
		val commandPool = this
		val device = commandPool.device
		MemoryStack.stackPush()
		try {
			val result = vkResetCommandPool(
				device.toVkType(), commandPool.toVkType(),
				flags.toVkType()
			)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun allocate(level: CommandBufferLevel, commandBufferCount: UInt): List<CommandBuffer> {
		val commandPool = this
		val device = commandPool.device
		MemoryStack.stackPush()
		try {
			val target = VkCommandBufferAllocateInfo.callocStack()
			val builder = CommandBufferAllocateInfoBuilder(target)
			builder.init(commandPool, level, commandBufferCount)
			val outputCount = commandBufferCount.toInt()
			val outputPtr = MemoryStack.stackGet().mallocPointer(outputCount)
			val result = vkAllocateCommandBuffers(device.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { CommandBuffer(VkCommandBuffer(outputPtr[it], device.ptr), this) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun free(commandBuffers: Collection<CommandBuffer>) {
		val commandPool = this
		val device = commandPool.device
		MemoryStack.stackPush()
		try {
			vkFreeCommandBuffers(device.toVkType(), commandPool.toVkType(), commandBuffers.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun trim() {
		val commandPool = this
		val device = commandPool.device
		MemoryStack.stackPush()
		try {
			vkTrimCommandPool(device.toVkType(), commandPool.toVkType(), 0U.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}
}
