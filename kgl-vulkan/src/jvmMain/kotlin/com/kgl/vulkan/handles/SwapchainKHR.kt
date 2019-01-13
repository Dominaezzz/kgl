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

import com.kgl.vulkan.dsls.AcquireNextImageInfoKHRBuilder
import com.kgl.vulkan.enums.Format
import com.kgl.vulkan.enums.ImageType
import com.kgl.vulkan.enums.SurfaceCounterEXT
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.EXTDisplayControl.vkGetSwapchainCounterEXT
import org.lwjgl.vulkan.GOOGLEDisplayTiming.vkGetRefreshCycleDurationGOOGLE
import org.lwjgl.vulkan.KHRSharedPresentableImage.vkGetSwapchainStatusKHR
import org.lwjgl.vulkan.KHRSwapchain.*

actual class SwapchainKHR(
		override val ptr: Long,
		actual val surface: SurfaceKHR,
		actual val device: Device,
		actual val imageFormat: Format,
		actual val imageExtent: Extent2D,
		actual val imageArrayLayers: UInt
) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		MemoryStack.stackPush()
		try {
			vkDestroySwapchainKHR(device.toVkType(), ptr, null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual val images: List<Image>
		get() {
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkGetSwapchainImagesKHR(device.toVkType(), toVkType(),
						outputCountPtr, null)
				when (result) {
					VK11.VK_SUCCESS -> Unit
					VK11.VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = MemoryStack.stackGet().mallocLong(outputCountPtr[0])
				val result1 = vkGetSwapchainImagesKHR(device.toVkType(), toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK11.VK_SUCCESS -> Unit
					VK11.VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { Image(outputPtr[it], device, ImageType.`2D`, imageFormat, 1U,
						Extent3D(imageExtent.width, imageExtent.height, 1U), imageArrayLayers) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val status: Boolean
		get() {
			MemoryStack.stackPush()
			try {
				val result = vkGetSwapchainStatusKHR(device.toVkType(), toVkType())
				return when (result) {
					VK11.VK_SUCCESS -> true
					KHRSwapchain.VK_SUBOPTIMAL_KHR -> false
					else -> handleVkResult(result)
				}
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual fun acquireNextImage(timeout: ULong, semaphore: Semaphore?, fence: Fence?): Acquire {
		MemoryStack.stackPush()
		try {
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkAcquireNextImageKHR(device.toVkType(), toVkType(),
					timeout.toVkType(), semaphore.toVkType(), fence.toVkType(), outputPtr)
			return when (result) {
				VK11.VK_SUCCESS -> Acquire.Success(outputPtr[0].toUInt(), false)
				VK11.VK_TIMEOUT -> Acquire.Timeout
				VK11.VK_NOT_READY -> Acquire.NotReady
				KHRSwapchain.VK_SUBOPTIMAL_KHR -> Acquire.Success(outputPtr[0].toUInt(), true)
				else -> handleVkResult(result)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun acquireNextImage2(semaphore: Semaphore?, fence: Fence?, block: AcquireNextImageInfoKHRBuilder.() -> Unit): Acquire {
		MemoryStack.stackPush()
		try {
			val target = VkAcquireNextImageInfoKHR.callocStack()
			val builder = AcquireNextImageInfoKHRBuilder(target)
			builder.init(this, semaphore, fence)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkAcquireNextImage2KHR(device.toVkType(), target, outputPtr)
			return when (result) {
				VK11.VK_SUCCESS -> Acquire.Success(outputPtr[0].toUInt(), false)
				VK11.VK_TIMEOUT -> Acquire.Timeout
				VK11.VK_NOT_READY -> Acquire.NotReady
				KHRSwapchain.VK_SUBOPTIMAL_KHR -> Acquire.Success(outputPtr[0].toUInt(), true)
				else -> handleVkResult(result)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getCounterEXT(counter: SurfaceCounterEXT): ULong {
		MemoryStack.stackPush()
		try {
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkGetSwapchainCounterEXT(device.toVkType(), toVkType(), counter.toVkType(), outputPtr)
			if (result != VK11.VK_SUCCESS) handleVkResult(result)
			return outputPtr[0].toULong()
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual val refreshCycleDurationGOOGLE: RefreshCycleDurationGOOGLE
		get() {
			MemoryStack.stackPush()
			try {
				val outputPtr = VkRefreshCycleDurationGOOGLE.mallocStack()
				val result = vkGetRefreshCycleDurationGOOGLE(device.toVkType(), toVkType(), outputPtr)
				if (result != VK11.VK_SUCCESS) handleVkResult(result)
				return RefreshCycleDurationGOOGLE.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val pastPresentationTimingGOOGLE: List<PastPresentationTimingGOOGLE>
		get() {
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = GOOGLEDisplayTiming.vkGetPastPresentationTimingGOOGLE(device.toVkType(), toVkType(),
						outputCountPtr, null)
				when (result) {
					VK11.VK_SUCCESS -> Unit
					VK11.VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkPastPresentationTimingGOOGLE.mallocStack(outputCountPtr[0])
				val result1 = GOOGLEDisplayTiming.vkGetPastPresentationTimingGOOGLE(device.toVkType(), toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK11.VK_SUCCESS -> Unit
					VK11.VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { PastPresentationTimingGOOGLE.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}
}

