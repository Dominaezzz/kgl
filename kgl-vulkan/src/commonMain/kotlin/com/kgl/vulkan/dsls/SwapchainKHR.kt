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
import com.kgl.vulkan.handles.SurfaceKHR
import com.kgl.vulkan.handles.SwapchainKHR
import com.kgl.vulkan.utils.VkFlag

expect class SwapchainCreateInfoKHRBuilder {
	var flags: VkFlag<SwapchainCreateKHR>?

	var minImageCount: UInt

	var imageFormat: Format?

	var imageColorSpace: ColorSpaceKHR?

	var imageArrayLayers: UInt

	var imageUsage: VkFlag<ImageUsage>?

	var imageSharingMode: SharingMode?

	var preTransform: SurfaceTransformKHR?

	var compositeAlpha: CompositeAlphaKHR?

	var presentMode: PresentModeKHR?

	var clipped: Boolean

	fun imageExtent(width: UInt, height: UInt)
}

expect class CreateSharedSwapchainsKHRBuilder {
	fun swapchain(
			surface: SurfaceKHR,
			queueFamilyIndices: UIntArray,
			oldSwapchain: SwapchainKHR? = null,
			block: SwapchainCreateInfoKHRBuilder.() -> Unit
	)
}

expect class AcquireNextImageInfoKHRBuilder {
	var timeout: ULong

	var deviceMask: UInt
}

