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

import com.kgl.core.VirtualStack
import com.kgl.vulkan.enums.PipelineStage
import com.kgl.vulkan.enums.SparseMemoryBind
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.mapToCArray
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import cvulkan.*
import kotlinx.cinterop.value

actual class SubmitInfoBuilder(internal val target: VkSubmitInfo) {
	internal fun init(
			waitSemaphores: Collection<Pair<Semaphore, VkFlag<PipelineStage>>>?,
			commandBuffers: Collection<CommandBuffer>?,
			signalSemaphores: Collection<Semaphore>?
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SUBMIT_INFO
		target.pNext = null

		if (waitSemaphores != null) {
			target.pWaitSemaphores = waitSemaphores.map { it.first }.toVkType()
			target.pWaitDstStageMask = waitSemaphores.map { it.second }.toVkType()
			target.waitSemaphoreCount = waitSemaphores.size.toUInt()
		} else {
			target.waitSemaphoreCount = 0U
		}

		target.pCommandBuffers = commandBuffers?.toVkType()
		target.commandBufferCount = commandBuffers?.size?.toUInt() ?: 0U
		target.pSignalSemaphores = signalSemaphores?.toVkType()
		target.signalSemaphoreCount = signalSemaphores?.size?.toUInt() ?: 0U
	}
}

actual class QueueSubmitBuilder {
	val targets: MutableList<(VkSubmitInfo) -> Unit> = mutableListOf()

	actual fun info(
			waitSemaphores: Collection<Pair<Semaphore, VkFlag<PipelineStage>>>?,
			commandBuffers: Collection<CommandBuffer>?,
			signalSemaphores: Collection<Semaphore>?,
			block: SubmitInfoBuilder.() -> Unit
	) {
		targets += {
			val builder = SubmitInfoBuilder(it)
			builder.init(waitSemaphores, commandBuffers, signalSemaphores)
			builder.apply(block)
		}
	}
}

actual class PresentInfoKHRBuilder(internal val target: VkPresentInfoKHR) {
	internal fun init(swapchains: Collection<Pair<SwapchainKHR, UInt>>, waitSemaphores: Collection<Semaphore>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR
		target.pNext = null
		target.pResults = null

		target.pSwapchains = swapchains.map { it.first }.toVkType()
		target.pImageIndices = swapchains.mapToCArray(VirtualStack) { value = it.second }
		target.swapchainCount = swapchains.size.toUInt()

		target.pWaitSemaphores = waitSemaphores?.toVkType()
		target.waitSemaphoreCount = waitSemaphores?.size?.toUInt() ?: 0U
	}
}

actual class SparseMemoryBindBuilder(internal val target: VkSparseMemoryBind) {
	actual var resourceOffset: ULong
		get() = target.resourceOffset
		set(value) {
			target.resourceOffset = value.toVkType()
		}

	actual var size: ULong
		get() = target.size
		set(value) {
			target.size = value.toVkType()
		}

	actual var memoryOffset: ULong
		get() = target.memoryOffset
		set(value) {
			target.memoryOffset = value.toVkType()
		}

	actual var flags: VkFlag<SparseMemoryBind>?
		get() = SparseMemoryBind.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	internal fun init(memory: DeviceMemory?) {
		target.memory = memory?.toVkType()
	}
}

actual class SparseMemoryBindsBuilder {
	val targets: MutableList<(VkSparseMemoryBind) -> Unit> = mutableListOf()

	actual fun bind(memory: DeviceMemory?, block: SparseMemoryBindBuilder.() -> Unit) {
		targets += {
			val builder = SparseMemoryBindBuilder(it)
			builder.init(memory)
			builder.apply(block)
		}
	}
}

actual class SparseBufferMemoryBindInfoBuilder(internal val target: VkSparseBufferMemoryBindInfo) {
	actual fun binds(block: SparseMemoryBindsBuilder.() -> Unit) {
		val targets = SparseMemoryBindsBuilder().apply(block).targets
		target.pBinds = targets.mapToStackArray()
		target.bindCount = targets.size.toUInt()
	}

	internal fun init(buffer: Buffer) {
		target.buffer = buffer.toVkType()
	}
}

actual class SparseBufferMemoryBindInfosBuilder {
	val targets: MutableList<(VkSparseBufferMemoryBindInfo) -> Unit> = mutableListOf()

	actual fun info(buffer: Buffer, block: SparseBufferMemoryBindInfoBuilder.() -> Unit) {
		targets += {
			val builder = SparseBufferMemoryBindInfoBuilder(it)
			builder.init(buffer)
			builder.apply(block)
		}
	}
}

actual class SparseImageOpaqueMemoryBindInfoBuilder(internal val target: VkSparseImageOpaqueMemoryBindInfo) {
	actual fun binds(block: SparseMemoryBindsBuilder.() -> Unit) {
		val targets = SparseMemoryBindsBuilder().apply(block).targets
		target.pBinds = targets.mapToStackArray()
		target.bindCount = targets.size.toUInt()
	}

	internal fun init(image: Image) {
		target.image = image.toVkType()
	}
}

actual class SparseImageOpaqueMemoryBindInfosBuilder {
	val targets: MutableList<(VkSparseImageOpaqueMemoryBindInfo) -> Unit> = mutableListOf()

	actual fun info(image: Image, block: SparseImageOpaqueMemoryBindInfoBuilder.() -> Unit) {
		targets += {
			val builder = SparseImageOpaqueMemoryBindInfoBuilder(it)
			builder.init(image)
			builder.apply(block)
		}
	}
}

actual class SparseImageMemoryBindBuilder(internal val target: VkSparseImageMemoryBind) {
	actual var memoryOffset: ULong
		get() = target.memoryOffset
		set(value) {
			target.memoryOffset = value.toVkType()
		}

	actual var flags: VkFlag<SparseMemoryBind>?
		get() = SparseMemoryBind.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual fun subresource(block: ImageSubresourceBuilder.() -> Unit) {
		val subTarget = target.subresource
		val builder = ImageSubresourceBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun offset(
			x: Int,
			y: Int,
			z: Int
	) {
		val subTarget = target.offset
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun extent(
			width: UInt,
			height: UInt,
			depth: UInt
	) {
		val subTarget = target.extent
		val builder = Extent3DBuilder(subTarget)
		builder.init(width, height, depth)
	}

	internal fun init(memory: DeviceMemory?) {
		target.memory = memory?.toVkType()
	}
}

actual class SparseImageMemoryBindsBuilder {
	val targets: MutableList<(VkSparseImageMemoryBind) -> Unit> = mutableListOf()

	actual fun bind(memory: DeviceMemory?, block: SparseImageMemoryBindBuilder.() -> Unit) {
		targets += {
			val builder = SparseImageMemoryBindBuilder(it)
			builder.init(memory)
			builder.apply(block)
		}
	}
}

actual class SparseImageMemoryBindInfoBuilder(internal val target: VkSparseImageMemoryBindInfo) {
	actual fun binds(block: SparseImageMemoryBindsBuilder.() -> Unit) {
		val targets = SparseImageMemoryBindsBuilder().apply(block).targets
		target.pBinds = targets.mapToStackArray()
		target.bindCount = targets.size.toUInt()
	}

	internal fun init(image: Image) {
		target.image = image.toVkType()
	}
}

actual class SparseImageMemoryBindInfosBuilder {
	val targets: MutableList<(VkSparseImageMemoryBindInfo) -> Unit> = mutableListOf()

	actual fun info(image: Image, block: SparseImageMemoryBindInfoBuilder.() -> Unit) {
		targets += {
			val builder = SparseImageMemoryBindInfoBuilder(it)
			builder.init(image)
			builder.apply(block)
		}
	}
}

actual class BindSparseInfoBuilder(internal val target: VkBindSparseInfo) {
	actual fun bufferBinds(block: SparseBufferMemoryBindInfosBuilder.() -> Unit) {
		val targets = SparseBufferMemoryBindInfosBuilder().apply(block).targets
		target.pBufferBinds = targets.mapToStackArray()
		target.bufferBindCount = targets.size.toUInt()
	}

	actual fun imageOpaqueBinds(block: SparseImageOpaqueMemoryBindInfosBuilder.() -> Unit) {
		val targets = SparseImageOpaqueMemoryBindInfosBuilder().apply(block).targets
		target.pImageOpaqueBinds = targets.mapToStackArray()
		target.imageOpaqueBindCount = targets.size.toUInt()
	}

	actual fun imageBinds(block: SparseImageMemoryBindInfosBuilder.() -> Unit) {
		val targets = SparseImageMemoryBindInfosBuilder().apply(block).targets
		target.pImageBinds = targets.mapToStackArray()
		target.imageBindCount = targets.size.toUInt()
	}

	internal fun init(waitSemaphores: Collection<Semaphore>?, signalSemaphores: Collection<Semaphore>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_BIND_SPARSE_INFO
		target.pNext = null
		target.pWaitSemaphores = waitSemaphores?.toVkType()
		target.waitSemaphoreCount = waitSemaphores?.size?.toUInt() ?: 0U
		target.pSignalSemaphores = signalSemaphores?.toVkType()
		target.signalSemaphoreCount = signalSemaphores?.size?.toUInt() ?: 0U
	}
}

actual class QueueBindSparseBuilder {
	val targets: MutableList<(VkBindSparseInfo) -> Unit> = mutableListOf()

	actual fun info(
			waitSemaphores: Collection<Semaphore>?,
			signalSemaphores: Collection<Semaphore>?,
			block: BindSparseInfoBuilder.() -> Unit
	) {
		targets += {
			val builder = BindSparseInfoBuilder(it)
			builder.init(waitSemaphores, signalSemaphores)
			builder.apply(block)
		}
	}
}

