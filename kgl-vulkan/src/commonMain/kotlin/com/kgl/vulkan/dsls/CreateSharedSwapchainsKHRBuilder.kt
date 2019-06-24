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

import com.kgl.vulkan.handles.SurfaceKHR
import com.kgl.vulkan.handles.SwapchainKHR
import com.kgl.vulkan.utils.StructMarker

@StructMarker
class CreateSharedSwapchainsKHRBuilder {
	internal val targets: MutableList<(SwapchainCreateInfoKHRBuilder) -> Unit> = mutableListOf()
	internal val surfaces: MutableList<SurfaceKHR> = mutableListOf()

	fun swapchain(
			surface: SurfaceKHR,
			queueFamilyIndices: UIntArray,
			oldSwapchain: SwapchainKHR? = null,
			block: SwapchainCreateInfoKHRBuilder.() -> Unit
	) {
		targets += {
			it.init(surface, queueFamilyIndices, oldSwapchain)
			it.apply(block)
		}
		surfaces += surface
	}
}
