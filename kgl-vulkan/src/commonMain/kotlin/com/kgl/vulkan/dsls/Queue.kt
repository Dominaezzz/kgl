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

import com.kgl.vulkan.enums.PipelineStage
import com.kgl.vulkan.enums.SparseMemoryBind
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.VkFlag

expect class SubmitInfoBuilder

expect class QueueSubmitBuilder {
	fun info(
			waitSemaphores: Collection<Pair<Semaphore, VkFlag<PipelineStage>>>? = null,
			commandBuffers: Collection<CommandBuffer>? = null,
			signalSemaphores: Collection<Semaphore>? = null,
			block: SubmitInfoBuilder.() -> Unit = {}
	)
}

expect class PresentInfoKHRBuilder

expect class SparseMemoryBindBuilder {
	var resourceOffset: ULong

	var size: ULong

	var memoryOffset: ULong

	var flags: VkFlag<SparseMemoryBind>?
}

expect class SparseMemoryBindsBuilder {
	fun bind(memory: DeviceMemory?, block: SparseMemoryBindBuilder.() -> Unit = {})
}

expect class SparseBufferMemoryBindInfoBuilder {
	fun binds(block: SparseMemoryBindsBuilder.() -> Unit)
}

expect class SparseBufferMemoryBindInfosBuilder {
	fun info(buffer: Buffer, block: SparseBufferMemoryBindInfoBuilder.() -> Unit)
}

expect class SparseImageOpaqueMemoryBindInfoBuilder {
	fun binds(block: SparseMemoryBindsBuilder.() -> Unit)
}

expect class SparseImageOpaqueMemoryBindInfosBuilder {
	fun info(image: Image, block: SparseImageOpaqueMemoryBindInfoBuilder.() -> Unit)
}

expect class SparseImageMemoryBindBuilder {
	var memoryOffset: ULong

	var flags: VkFlag<SparseMemoryBind>?

	fun subresource(block: ImageSubresourceBuilder.() -> Unit = {})

	fun offset(
			x: Int,
			y: Int,
			z: Int
	)

	fun extent(
			width: UInt,
			height: UInt,
			depth: UInt
	)
}

expect class SparseImageMemoryBindsBuilder {
	fun bind(memory: DeviceMemory?, block: SparseImageMemoryBindBuilder.() -> Unit)
}

expect class SparseImageMemoryBindInfoBuilder {
	fun binds(block: SparseImageMemoryBindsBuilder.() -> Unit)
}

expect class SparseImageMemoryBindInfosBuilder {
	fun info(image: Image, block: SparseImageMemoryBindInfoBuilder.() -> Unit)
}

expect class BindSparseInfoBuilder {
	fun bufferBinds(block: SparseBufferMemoryBindInfosBuilder.() -> Unit)

	fun imageOpaqueBinds(block: SparseImageOpaqueMemoryBindInfosBuilder.() -> Unit)

	fun imageBinds(block: SparseImageMemoryBindInfosBuilder.() -> Unit)
}

expect class QueueBindSparseBuilder {
	fun info(
			waitSemaphores: Collection<Semaphore>?,
			signalSemaphores: Collection<Semaphore>?,
			block: BindSparseInfoBuilder.() -> Unit
	)
}

