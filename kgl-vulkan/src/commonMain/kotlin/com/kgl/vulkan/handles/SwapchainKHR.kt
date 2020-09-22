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
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*

expect class SwapchainKHR : VkHandle {
	val surface: SurfaceKHR
	val device: Device
	val imageFormat: Format
	val imageExtent: Extent2D
	val imageArrayLayers: UInt

	val images: List<Image>
	val status: Boolean

	fun acquireNextImage(
		timeout: ULong = ULong.MAX_VALUE,
		semaphore: Semaphore? = null,
		fence: Fence? = null
	): Acquire

	fun acquireNextImage2(
		semaphore: Semaphore? = null,
		fence: Fence? = null,
		block: AcquireNextImageInfoKHRBuilder.() -> Unit = {}
	): Acquire

	fun getCounterEXT(counter: SurfaceCounterEXT): ULong

	val refreshCycleDurationGOOGLE: RefreshCycleDurationGOOGLE

	val pastPresentationTimingGOOGLE: List<PastPresentationTimingGOOGLE>
}
