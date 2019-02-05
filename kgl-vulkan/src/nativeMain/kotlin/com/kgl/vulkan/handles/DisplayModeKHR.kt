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
import com.kgl.vulkan.dsls.DisplayPlaneInfo2KHRBuilder
import com.kgl.vulkan.structs.DisplayPlaneCapabilities2KHR
import com.kgl.vulkan.structs.DisplayPlaneCapabilitiesKHR
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import cvulkan.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.invoke
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr

actual class DisplayModeKHR(
		override val ptr: VkDisplayModeKHR,
		actual val physicalDevice: PhysicalDevice,
		actual val display: DisplayKHR
) : VkHandleNative<VkDisplayModeKHR>(), VkHandle {
	internal val dispatchTable = physicalDevice.dispatchTable

	override fun close() {
		TODO()
	}

	actual fun getDisplayPlaneCapabilities(planeIndex: UInt): DisplayPlaneCapabilitiesKHR {
		val mode = this
		val physicalDevice = mode.physicalDevice
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkDisplayPlaneCapabilitiesKHR>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetDisplayPlaneCapabilitiesKHR!!(physicalDevice.toVkType(),
					mode.toVkType(), planeIndex.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DisplayPlaneCapabilitiesKHR.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getDisplayPlaneCapabilities2(block: DisplayPlaneInfo2KHRBuilder.() -> Unit): DisplayPlaneCapabilities2KHR {
		val mode = this
		val physicalDevice = mode.physicalDevice
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDisplayPlaneInfo2KHR>().ptr
			val builder = DisplayPlaneInfo2KHRBuilder(target.pointed)
			builder.init(mode)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDisplayPlaneCapabilities2KHR>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetDisplayPlaneCapabilities2KHR!!(physicalDevice.toVkType(), target,
					outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DisplayPlaneCapabilities2KHR.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}
}

