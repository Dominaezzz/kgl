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

import com.kgl.vulkan.enums.*
import com.kgl.vulkan.handles.Fence
import com.kgl.vulkan.handles.Semaphore
import com.kgl.vulkan.handles.SurfaceKHR
import com.kgl.vulkan.handles.SwapchainKHR
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.KHRSwapchain
import org.lwjgl.vulkan.VkAcquireNextImageInfoKHR
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR

actual class SwapchainCreateInfoKHRBuilder(internal val target: VkSwapchainCreateInfoKHR) {
	actual var flags: VkFlag<SwapchainCreateKHR>?
		get() = SwapchainCreateKHR.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual var minImageCount: UInt
		get() = target.minImageCount().toUInt()
		set(value) {
			target.minImageCount(value.toVkType())
		}

	actual var imageFormat: Format?
		get() = Format.from(target.imageFormat())
		set(value) {
			target.imageFormat(value.toVkType())
		}

	actual var imageColorSpace: ColorSpaceKHR?
		get() = ColorSpaceKHR.from(target.imageColorSpace())
		set(value) {
			target.imageColorSpace(value.toVkType())
		}

	actual var imageArrayLayers: UInt
		get() = target.imageArrayLayers().toUInt()
		set(value) {
			target.imageArrayLayers(value.toVkType())
		}

	actual var imageUsage: VkFlag<ImageUsage>?
		get() = ImageUsage.fromMultiple(target.imageUsage())
		set(value) {
			target.imageUsage(value.toVkType())
		}

	actual var imageSharingMode: SharingMode?
		get() = SharingMode.from(target.imageSharingMode())
		set(value) {
			target.imageSharingMode(value.toVkType())
		}

	actual var preTransform: SurfaceTransformKHR?
		get() = SurfaceTransformKHR.from(target.preTransform())
		set(value) {
			target.preTransform(value.toVkType())
		}

	actual var compositeAlpha: CompositeAlphaKHR?
		get() = CompositeAlphaKHR.from(target.compositeAlpha())
		set(value) {
			target.compositeAlpha(value.toVkType())
		}

	actual var presentMode: PresentModeKHR?
		get() = PresentModeKHR.from(target.presentMode())
		set(value) {
			target.presentMode(value.toVkType())
		}

	actual var clipped: Boolean
		get() = target.clipped()
		set(value) {
			target.clipped(value.toVkType())
		}

	actual fun imageExtent(width: UInt, height: UInt) {
		val subTarget = target.imageExtent()
		val builder = Extent2DBuilder(subTarget)
		builder.init(width, height)
	}

	internal fun init(
			surface: SurfaceKHR,
			queueFamilyIndices: UIntArray?,
			oldSwapchain: SwapchainKHR?
	) {
		target.sType(KHRSwapchain.VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR)
		target.pNext(0)
		target.surface(surface.toVkType())
		target.pQueueFamilyIndices(queueFamilyIndices.toVkType())
		target.oldSwapchain(oldSwapchain.toVkType())
	}
}

actual class CreateSharedSwapchainsKHRBuilder {
	val targets: MutableList<(VkSwapchainCreateInfoKHR) -> Unit> = mutableListOf()

	actual fun swapchain(
			surface: SurfaceKHR,
			queueFamilyIndices: UIntArray,
			oldSwapchain: SwapchainKHR?,
			block: SwapchainCreateInfoKHRBuilder.() -> Unit
	) {
		targets += {
			val builder = SwapchainCreateInfoKHRBuilder(it)
			builder.init(surface, queueFamilyIndices, oldSwapchain)
			builder.apply(block)
		}
	}
}

actual class AcquireNextImageInfoKHRBuilder(internal val target: VkAcquireNextImageInfoKHR) {
	actual var timeout: ULong
		get() = target.timeout().toULong()
		set(value) {
			target.timeout(value.toVkType())
		}

	actual var deviceMask: UInt
		get() = target.deviceMask().toUInt()
		set(value) {
			target.deviceMask(value.toVkType())
		}

	internal fun init(swapchain: SwapchainKHR, semaphore: Semaphore?, fence: Fence?) {
		target.sType(KHRSwapchain.VK_STRUCTURE_TYPE_ACQUIRE_NEXT_IMAGE_INFO_KHR)
		target.pNext(0)
		target.swapchain(swapchain.toVkType())
		target.semaphore(semaphore.toVkType())
		target.fence(fence.toVkType())
	}
}

