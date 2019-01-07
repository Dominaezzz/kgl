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

import com.kgl.vulkan.dsls.DisplayPlaneInfo2KHRBuilder
import com.kgl.vulkan.structs.DisplayPlaneCapabilities2KHR
import com.kgl.vulkan.structs.DisplayPlaneCapabilitiesKHR
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.KHRDisplay.vkGetDisplayPlaneCapabilitiesKHR
import org.lwjgl.vulkan.KHRGetDisplayProperties2.vkGetDisplayPlaneCapabilities2KHR
import org.lwjgl.vulkan.VK11.VK_SUCCESS
import org.lwjgl.vulkan.VkDisplayPlaneCapabilities2KHR
import org.lwjgl.vulkan.VkDisplayPlaneCapabilitiesKHR
import org.lwjgl.vulkan.VkDisplayPlaneInfo2KHR

actual class DisplayModeKHR(
		override val ptr: Long,
		actual val physicalDevice: PhysicalDevice,
		actual val display: DisplayKHR
) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		TODO()
	}

	actual fun getDisplayPlaneCapabilities(planeIndex: UInt): DisplayPlaneCapabilitiesKHR {
		val mode = this
		val physicalDevice = mode.physicalDevice
		MemoryStack.stackPush()
		try {
			val outputPtr = VkDisplayPlaneCapabilitiesKHR.mallocStack()
			val result = vkGetDisplayPlaneCapabilitiesKHR(physicalDevice.toVkType(),
					mode.toVkType(), planeIndex.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DisplayPlaneCapabilitiesKHR.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getDisplayPlaneCapabilities2(block: DisplayPlaneInfo2KHRBuilder.() -> Unit): DisplayPlaneCapabilities2KHR {
		val mode = this
		val physicalDevice = mode.physicalDevice
		MemoryStack.stackPush()
		try {
			val target = VkDisplayPlaneInfo2KHR.callocStack()
			val builder = DisplayPlaneInfo2KHRBuilder(target)
			builder.init(mode)
			builder.apply(block)
			val outputPtr = VkDisplayPlaneCapabilities2KHR.mallocStack()
			val result = vkGetDisplayPlaneCapabilities2KHR(physicalDevice.toVkType(), target,
					outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DisplayPlaneCapabilities2KHR.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

