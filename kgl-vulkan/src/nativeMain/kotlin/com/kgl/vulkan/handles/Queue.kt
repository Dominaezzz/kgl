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

import com.kgl.vulkan.dsls.DebugUtilsLabelEXTBuilder
import com.kgl.vulkan.dsls.PresentInfoKHRBuilder
import com.kgl.vulkan.dsls.QueueBindSparseBuilder
import com.kgl.vulkan.dsls.QueueSubmitBuilder
import com.kgl.vulkan.structs.CheckpointDataNV
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class Queue(override val ptr: VkQueue, actual val device: Device, actual val queueFamilyIndex: UInt) : VkHandleNative<VkQueue>(), VkHandle {
	actual val checkpointDataNV: List<CheckpointDataNV>
		get() {
			val queue = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				vkGetQueueCheckpointDataNV(queue.toVkType(), outputCountPtr, null)
				val outputPtr =
						VirtualStack.allocArray<VkCheckpointDataNV>(outputCountVar.value.toInt())
				vkGetQueueCheckpointDataNV(queue.toVkType(), outputCountPtr, outputPtr)
				return List(outputCountVar.value.toInt()) { CheckpointDataNV.from(outputPtr[it]) }
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		TODO()
	}

	actual fun submit(fence: Fence?, block: QueueSubmitBuilder.() -> Unit) {
		val queue = this
		VirtualStack.push()
		try {
			val targets = QueueSubmitBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			val result = vkQueueSubmit(queue.toVkType(), targets.size.toUInt(), targetArray,
					fence.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun presentKHR(swapchains: Collection<Pair<SwapchainKHR, UInt>>, waitSemaphores: Collection<Semaphore>?, block: PresentInfoKHRBuilder.() -> Unit): Boolean {
		val queue = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPresentInfoKHR>()
			val builder = PresentInfoKHRBuilder(target)
			builder.init(swapchains, waitSemaphores)
			builder.apply(block)
			val result = vkQueuePresentKHR(queue.toVkType(), target.ptr)
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
			val result = vkQueueWaitIdle(queue.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindSparse(fence: Fence?, block: QueueBindSparseBuilder.() -> Unit) {
		val queue = this
		VirtualStack.push()
		try {
			val targets = QueueBindSparseBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			val result = vkQueueBindSparse(queue.toVkType(), targets.size.toUInt(), targetArray,
					fence.toVkType())
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
			vkQueueBeginDebugUtilsLabelEXT(queue.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endDebugUtilsLabelEXT() {
		val queue = this
		VirtualStack.push()
		try {
			vkQueueEndDebugUtilsLabelEXT(queue.toVkType())
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
			vkQueueInsertDebugUtilsLabelEXT(queue.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}
}

