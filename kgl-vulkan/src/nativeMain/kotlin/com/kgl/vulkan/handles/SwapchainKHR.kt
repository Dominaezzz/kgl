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

import com.kgl.core.utils.VirtualStack
import com.kgl.vulkan.dsls.AcquireNextImageInfoKHRBuilder
import com.kgl.vulkan.enums.Format
import com.kgl.vulkan.enums.ImageType
import com.kgl.vulkan.enums.SurfaceCounterEXT
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class SwapchainKHR(
		override val ptr: VkSwapchainKHR,
		actual val surface: SurfaceKHR,
		actual val device: Device,
		actual val imageFormat: Format,
		actual val imageExtent: Extent2D,
		actual val imageArrayLayers: UInt
) : VkHandleNative<VkSwapchainKHR>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		VirtualStack.push()
		try {
			dispatchTable.vkDestroySwapchainKHR!!(device.toVkType(), ptr, null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual val images: List<Image>
		get() {
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = dispatchTable.vkGetSwapchainImagesKHR!!(device.toVkType(), toVkType(),
						outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VirtualStack.allocArray<VkImageVar>(outputCountVar.value.toInt())
				val result1 = dispatchTable.vkGetSwapchainImagesKHR!!(device.toVkType(), toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) { Image(outputPtr[it]!!, device, ImageType.`2D`, imageFormat, 1U,
						Extent3D(imageExtent.width, imageExtent.height, 1U), imageArrayLayers) }
			} finally {
				VirtualStack.pop()
			}
		}

	actual val status: Boolean
		get() {
			VirtualStack.push()
			try {
				val result = dispatchTable.vkGetSwapchainStatusKHR!!(device.toVkType(), toVkType())
				return when (result) {
					VK_SUCCESS -> true
					VK_SUBOPTIMAL_KHR -> false
					else -> handleVkResult(result)
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual fun acquireNextImage(timeout: ULong, semaphore: Semaphore?, fence: Fence?): Acquire {
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<UIntVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkAcquireNextImageKHR!!(device.toVkType(), toVkType(),
					timeout.toVkType(), semaphore.toVkType(), fence.toVkType(), outputPtr)
			return when (result) {
				VK_SUCCESS -> Acquire.Success(outputPtr[0].toUInt(), false)
				VK_TIMEOUT -> Acquire.Timeout
				VK_NOT_READY -> Acquire.NotReady
				VK_SUBOPTIMAL_KHR -> Acquire.Success(outputPtr[0].toUInt(), true)
				else -> handleVkResult(result)
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun acquireNextImage2(semaphore: Semaphore?, fence: Fence?, block: AcquireNextImageInfoKHRBuilder.() -> Unit): Acquire {
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkAcquireNextImageInfoKHR>().ptr
			val builder = AcquireNextImageInfoKHRBuilder(target.pointed)
			builder.init(this, semaphore, fence)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<UIntVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkAcquireNextImage2KHR!!(device.toVkType(), target, outputPtr)
			return when (result) {
				VK_SUCCESS -> Acquire.Success(outputPtr[0].toUInt(), false)
				VK_TIMEOUT -> Acquire.Timeout
				VK_NOT_READY -> Acquire.NotReady
				VK_SUBOPTIMAL_KHR -> Acquire.Success(outputPtr[0].toUInt(), true)
				else -> handleVkResult(result)
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getCounterEXT(counter: SurfaceCounterEXT): ULong {
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<ULongVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetSwapchainCounterEXT!!(device.toVkType(), toVkType(),
					counter.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputVar.value
		} finally {
			VirtualStack.pop()
		}
	}

	actual val refreshCycleDurationGOOGLE: RefreshCycleDurationGOOGLE
		get() {
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkRefreshCycleDurationGOOGLE>()
				val outputPtr = outputVar.ptr
				val result = dispatchTable.vkGetRefreshCycleDurationGOOGLE!!(device.toVkType(), toVkType(),
						outputPtr)
				if (result != VK_SUCCESS) handleVkResult(result)
				return RefreshCycleDurationGOOGLE.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val pastPresentationTimingGOOGLE: List<PastPresentationTimingGOOGLE>
		get() {
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = dispatchTable.vkGetPastPresentationTimingGOOGLE!!(device.toVkType(), toVkType(),
						outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkPastPresentationTimingGOOGLE>(outputCountVar.value.toInt())
				val result1 = dispatchTable.vkGetPastPresentationTimingGOOGLE!!(device.toVkType(), toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) { PastPresentationTimingGOOGLE.from(outputPtr[it]) }
			} finally {
				VirtualStack.pop()
			}
		}
}

