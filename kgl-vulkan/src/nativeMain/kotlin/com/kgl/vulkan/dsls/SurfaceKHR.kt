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

import com.kgl.vulkan.enums.DisplayPlaneAlphaKHR
import com.kgl.vulkan.enums.SurfaceTransformKHR
import com.kgl.vulkan.handles.DisplayModeKHR
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkDisplaySurfaceCreateInfoKHR

actual class DisplaySurfaceCreateInfoKHRBuilder(internal val target: VkDisplaySurfaceCreateInfoKHR) {
	actual var planeIndex: UInt
		get() = target.planeIndex
		set(value) {
			target.planeIndex = value.toVkType()
		}

	actual var planeStackIndex: UInt
		get() = target.planeStackIndex
		set(value) {
			target.planeStackIndex = value.toVkType()
		}

	actual var transform: SurfaceTransformKHR?
		get() = SurfaceTransformKHR.from(target.transform)
		set(value) {
			target.transform = value.toVkType()
		}

	actual var globalAlpha: Float
		get() = target.globalAlpha
		set(value) {
			target.globalAlpha = value.toVkType()
		}

	actual var alphaMode: DisplayPlaneAlphaKHR?
		get() = DisplayPlaneAlphaKHR.from(target.alphaMode)
		set(value) {
			target.alphaMode = value.toVkType()
		}

	actual fun imageExtent(width: UInt, height: UInt) {
		val subTarget = target.imageExtent
		val builder = Extent2DBuilder(subTarget)
		builder.init(width, height)
	}

	internal fun init(displayMode: DisplayModeKHR) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DISPLAY_SURFACE_CREATE_INFO_KHR
		target.pNext = null
		target.flags = 0U
		target.displayMode = displayMode.toVkType()
	}
}

