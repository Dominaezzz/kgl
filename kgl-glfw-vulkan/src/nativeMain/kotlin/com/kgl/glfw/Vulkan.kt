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

import cglfw.*
import com.kgl.vulkan.handles.Instance
import com.kgl.vulkan.handles.PhysicalDevice
import com.kgl.vulkan.handles.SurfaceKHR
import com.kgl.vulkan.utils.handleVkResult
import kotlinx.cinterop.*

actual val isVulkanSupported: Boolean get() = glfwVulkanSupported() == GLFW_TRUE

actual val requiredInstanceExtensions: Array<String>?
	get() = memScoped {
		val count = alloc<UIntVar>()
		val output = glfwGetRequiredInstanceExtensions(count.ptr) ?: return null

		Array(count.value.toInt()) { output[it]!!.toKString() }
	}

actual fun PhysicalDevice.getPresentationSupport(queueFamilyIndex: UInt): Boolean {
	return glfwGetPhysicalDevicePresentationSupport(instance.ptr, ptr, queueFamilyIndex) == GLFW_TRUE
}

actual fun Instance.createWindowSurface(window: Window): SurfaceKHR = memScoped {
	val handlePtr = alloc<VkSurfaceKHRVar>()
	val result = glfwCreateWindowSurface(ptr, window.ptr, null, handlePtr.ptr)
	if (result != VK_SUCCESS) handleVkResult(result)
	SurfaceKHR(handlePtr.value!!, this@createWindowSurface)
}
