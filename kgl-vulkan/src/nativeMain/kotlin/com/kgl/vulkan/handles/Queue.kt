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

import com.kgl.core.*
import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class Queue(
	override val ptr: VkQueue,
	actual val device: Device,
	actual val queueFamilyIndex: UInt
) : VkHandleNative<VkQueue>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	actual val checkpointDataNV: List<CheckpointDataNV>
		get() {
			val queue = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				dispatchTable.vkGetQueueCheckpointDataNV!!(queue.toVkType(), outputCountPtr, null)
				val outputPtr =
					VirtualStack.allocArray<VkCheckpointDataNV>(outputCountVar.value.toInt())
				dispatchTable.vkGetQueueCheckpointDataNV!!(queue.toVkType(), outputCountPtr, outputPtr)
				return List(outputCountVar.value.toInt()) { CheckpointDataNV.from(outputPtr[it]) }
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		TODO()
	}

	actual fun submit(fence: Fence?, block: SubmitInfosBuilder.() -> Unit) {
		val queue = this
		VirtualStack.push()
		try {
			val targets = SubmitInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::SubmitInfoBuilder)
			val result = dispatchTable.vkQueueSubmit(
				queue.toVkType(), targets.size.toUInt(), targetArray,
				fence.toVkType()
			)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun presentKHR(
		swapchains: Collection<Pair<SwapchainKHR, UInt>>,
		waitSemaphores: Collection<Semaphore>?,
		block: PresentInfoKHRBuilder.() -> Unit
	): Boolean {
		val queue = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPresentInfoKHR>()
			val builder = PresentInfoKHRBuilder(target)
			builder.init(swapchains, waitSemaphores)
			builder.apply(block)
			val result = dispatchTable.vkQueuePresentKHR!!(queue.toVkType(), target.ptr)
			return when (result) {
				VK_SUCCESS -> true
				VK_SUBOPTIMAL_KHR -> false
				else -> handleVkResult(result)
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun waitIdle() {
		val queue = this
		VirtualStack.push()
		try {
			val result = dispatchTable.vkQueueWaitIdle(queue.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindSparse(fence: Fence?, block: BindSparseInfosBuilder.() -> Unit) {
		val queue = this
		VirtualStack.push()
		try {
			val targets = BindSparseInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::BindSparseInfoBuilder)
			val result = dispatchTable.vkQueueBindSparse(
				queue.toVkType(), targets.size.toUInt(), targetArray,
				fence.toVkType()
			)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun beginDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val queue = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsLabelEXT>().ptr
			val builder = DebugUtilsLabelEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkQueueBeginDebugUtilsLabelEXT!!(queue.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endDebugUtilsLabelEXT() {
		val queue = this
		VirtualStack.push()
		try {
			dispatchTable.vkQueueEndDebugUtilsLabelEXT!!(queue.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun insertDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val queue = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsLabelEXT>().ptr
			val builder = DebugUtilsLabelEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkQueueInsertDebugUtilsLabelEXT!!(queue.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}
}
