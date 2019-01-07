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

import com.kgl.vulkan.handles.DisplayModeKHR
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.*

actual class DisplayModeParametersKHRBuilder(internal val target: VkDisplayModeParametersKHR) {
	actual var refreshRate: UInt
		get() = target.refreshRate().toUInt()
		set(value) {
			target.refreshRate(value.toVkType())
		}

	actual fun visibleRegion(width: UInt, height: UInt) {
		val subTarget = target.visibleRegion()
		val builder = Extent2DBuilder(subTarget)
		builder.init(width, height)
	}

	internal fun init() {
	}
}

actual class DisplayModeCreateInfoKHRBuilder(internal val target: VkDisplayModeCreateInfoKHR) {
	actual fun parameters(block: DisplayModeParametersKHRBuilder.() -> Unit) {
		val subTarget = target.parameters()
		val builder = DisplayModeParametersKHRBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init() {
		target.sType(KHRDisplay.VK_STRUCTURE_TYPE_DISPLAY_MODE_CREATE_INFO_KHR)
		target.pNext(0)
		target.flags(0)
	}
}

actual class DisplayPlaneInfo2KHRBuilder(internal val target: VkDisplayPlaneInfo2KHR) {
	actual var planeIndex: UInt
		get() = target.planeIndex().toUInt()
		set(value) {
			target.planeIndex(value.toVkType())
		}

	internal fun init(mode: DisplayModeKHR) {
		target.sType(KHRGetDisplayProperties2.VK_STRUCTURE_TYPE_DISPLAY_PLANE_INFO_2_KHR)
		target.pNext(0)
		target.mode(mode.toVkType())
	}
}

