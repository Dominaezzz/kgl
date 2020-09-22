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
import com.kgl.core.*
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.*
import kotlinx.cinterop.*

actual val Glfw.isVulkanSupported: Boolean get() = glfwVulkanSupported() == GLFW_TRUE

actual val Glfw.requiredInstanceExtensions: Array<String>?
	get() {
		VirtualStack.push()
		try {
			val count = VirtualStack.alloc<UIntVar>()
			val output = glfwGetRequiredInstanceExtensions(count.ptr) ?: return null

			return Array(count.value.toInt()) { output[it]!!.toKString() }
		} finally {
			VirtualStack.pop()
		}
	}

actual fun PhysicalDevice.getPresentationSupport(queueFamilyIndex: UInt): Boolean {
	return glfwGetPhysicalDevicePresentationSupport(instance.ptr, ptr, queueFamilyIndex) == GLFW_TRUE
}

actual fun Instance.createWindowSurface(window: Window): SurfaceKHR {
	VirtualStack.push()
	try {
		val handlePtr = VirtualStack.alloc<VkSurfaceKHRVar>()
		val result = glfwCreateWindowSurface(ptr, window.ptr, null, handlePtr.ptr)
		if (result != VK_SUCCESS) handleVkResult(result)
		return SurfaceKHR(handlePtr.value!!, this)
	} finally {
		VirtualStack.pop()
	}
}
