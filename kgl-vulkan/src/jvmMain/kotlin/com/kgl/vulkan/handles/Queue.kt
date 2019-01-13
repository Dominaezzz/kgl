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
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.EXTDebugUtils.*
import org.lwjgl.vulkan.KHRSwapchain.VK_SUBOPTIMAL_KHR
import org.lwjgl.vulkan.KHRSwapchain.vkQueuePresentKHR
import org.lwjgl.vulkan.NVDeviceDiagnosticCheckpoints.vkGetQueueCheckpointDataNV
import org.lwjgl.vulkan.VK11.*

actual class Queue(override val ptr: VkQueue, actual val device: Device, actual val queueFamilyIndex: UInt) : VkHandleJVM<VkQueue>(), VkHandle {
	actual val checkpointDataNV: List<CheckpointDataNV>
		get() {
			val queue = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				vkGetQueueCheckpointDataNV(queue.toVkType(), outputCountPtr, null)
				val outputPtr = VkCheckpointDataNV.mallocStack(outputCountPtr[0])
				vkGetQueueCheckpointDataNV(queue.toVkType(), outputCountPtr, outputPtr)
				return List(outputCountPtr[0]) { CheckpointDataNV.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		TODO()
	}

	actual fun submit(fence: Fence?, block: QueueSubmitBuilder.() -> Unit) {
		val queue = this
		MemoryStack.stackPush()
		try {
			val targets = QueueSubmitBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkSubmitInfo::callocStack)
			val result = vkQueueSubmit(queue.toVkType(), targetArray, fence.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun presentKHR(swapchains: Collection<Pair<SwapchainKHR, UInt>>, waitSemaphores: Collection<Semaphore>?, block: PresentInfoKHRBuilder.() -> Unit): Boolean {
		val queue = this
		MemoryStack.stackPush()
		try {
			val target = VkPresentInfoKHR.callocStack()
			val builder = PresentInfoKHRBuilder(target)
			builder.init(swapchains, waitSemaphores)
			builder.apply(block)
			val result = vkQueuePresentKHR(queue.toVkType(), target)
			return when (result) {
				VK_SUCCESS -> true
				VK_SUBOPTIMAL_KHR -> false
				else -> handleVkResult(result)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun waitIdle() {
		val queue = this
		MemoryStack.stackPush()
		try {
			val result = vkQueueWaitIdle(queue.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindSparse(fence: Fence?, block: QueueBindSparseBuilder.() -> Unit) {
		val queue = this
		MemoryStack.stackPush()
		try {
			val targets = QueueBindSparseBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkBindSparseInfo::callocStack)
			val result = vkQueueBindSparse(queue.toVkType(), targetArray, fence.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun beginDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val queue = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsLabelEXT.callocStack()
			val builder = DebugUtilsLabelEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkQueueBeginDebugUtilsLabelEXT(queue.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endDebugUtilsLabelEXT() {
		val queue = this
		MemoryStack.stackPush()
		try {
			vkQueueEndDebugUtilsLabelEXT(queue.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun insertDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val queue = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsLabelEXT.callocStack()
			val builder = DebugUtilsLabelEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkQueueInsertDebugUtilsLabelEXT(queue.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

