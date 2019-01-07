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
package com.kgl.glfw

import com.kgl.vulkan.handles.Instance
import com.kgl.vulkan.handles.PhysicalDevice
import com.kgl.vulkan.handles.SurfaceKHR
import com.kgl.vulkan.utils.handleVkResult
import org.lwjgl.glfw.GLFWVulkan.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.VK10.VK_SUCCESS

actual val isVulkanSupported: Boolean get() = glfwVulkanSupported()
actual val requiredInstanceExtensions: Array<String>?
	get() {
		val output = glfwGetRequiredInstanceExtensions() ?: return null
		return Array(output.limit()) { output.getStringUTF8(it) }
	}

actual fun PhysicalDevice.getPresentationSupport(queueFamilyIndex: UInt): Boolean {
	return glfwGetPhysicalDevicePresentationSupport(instance.ptr, ptr, queueFamilyIndex.toInt())
}

actual fun Instance.createWindowSurface(window: Window): SurfaceKHR = MemoryStack.stackPush().use {
	val handlePtr = it.mallocLong(1)
	val result = glfwCreateWindowSurface(ptr, window.ptr, null, handlePtr)
	if (result != VK_SUCCESS) handleVkResult(result)
	SurfaceKHR(handlePtr[0], this)
}
