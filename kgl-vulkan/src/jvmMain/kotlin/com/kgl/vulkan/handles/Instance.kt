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
import com.kgl.vulkan.enums.DebugReportEXT
import com.kgl.vulkan.enums.DebugReportObjectTypeEXT
import com.kgl.vulkan.enums.DebugUtilsMessageSeverityEXT
import com.kgl.vulkan.enums.DebugUtilsMessageTypeEXT
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.EXTDebugReport.*
import org.lwjgl.vulkan.EXTDebugUtils.*
import org.lwjgl.vulkan.KHRDisplay.vkCreateDisplayPlaneSurfaceKHR
import org.lwjgl.vulkan.VK11.*

actual class Instance(
		override val ptr: VkInstance,
		private val debugUtilsMessengerCallback: VkDebugUtilsMessengerCallbackEXT?,
		private val debugReportCallback: VkDebugReportCallbackEXT?
) : VkHandleJVM<VkInstance>(), VkHandle {
	actual val physicalDevices: List<PhysicalDevice>
		get() {
			val instance = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkEnumeratePhysicalDevices(instance.toVkType(), outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = MemoryStack.stackGet().mallocPointer(outputCountPtr[0])
				val result1 = vkEnumeratePhysicalDevices(instance.toVkType(), outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { PhysicalDevice(VkPhysicalDevice(outputPtr[it], ptr), this) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val physicalDeviceGroups: List<PhysicalDeviceGroupProperties>
		get() {
			val instance = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkEnumeratePhysicalDeviceGroups(instance.toVkType(), outputCountPtr,
						null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkPhysicalDeviceGroupProperties.mallocStack(outputCountPtr[0])
				val result1 = vkEnumeratePhysicalDeviceGroups(instance.toVkType(), outputCountPtr,
						outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { PhysicalDeviceGroupProperties.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		val instance = this
		MemoryStack.stackPush()
		try {
			vkDestroyInstance(instance.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
			debugReportCallback?.free()
			debugUtilsMessengerCallback?.free()
		}
	}

	actual fun getProcAddr(name: String): Long {
		val instance = this
		MemoryStack.stackPush()
		try {
			return vkGetInstanceProcAddr(instance.toVkType(), name.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDisplayPlaneSurfaceKHR(displayMode: DisplayModeKHR, block: DisplaySurfaceCreateInfoKHRBuilder.() -> Unit): SurfaceKHR {
		val instance = this
		MemoryStack.stackPush()
		try {
			val target = VkDisplaySurfaceCreateInfoKHR.callocStack()
			val builder = DisplaySurfaceCreateInfoKHRBuilder(target)
			builder.init(displayMode)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateDisplayPlaneSurfaceKHR(instance.toVkType(), target, null,
					outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceKHR(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDebugUtilsMessengerEXT(block: DebugUtilsMessengerCreateInfoEXTBuilder.() -> Unit): DebugUtilsMessengerEXT {
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsMessengerCreateInfoEXT.callocStack()
			val builder = DebugUtilsMessengerCreateInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)

			val callback = target.pfnUserCallback()

			val outputVar = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateDebugUtilsMessengerEXT(ptr, target, null, outputVar)
			if (result != VK_SUCCESS) {
				callback.close()
				handleVkResult(result)
			}
			return DebugUtilsMessengerEXT(outputVar[0], this, callback)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDebugReportCallbackEXT(block: DebugReportCallbackCreateInfoEXTBuilder.() -> Unit): DebugReportCallbackEXT {
		MemoryStack.stackPush()
		try {
			val target = VkDebugReportCallbackCreateInfoEXT.callocStack()
			val builder = DebugReportCallbackCreateInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)

			val callback = target.pfnCallback()

			val outputVar = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateDebugReportCallbackEXT(ptr, target, null, outputVar)
			if (result != VK_SUCCESS) {
				callback.free()
				handleVkResult(result)
			}
			return DebugReportCallbackEXT(outputVar[0], this, callback)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun debugReportMessageEXT(
			flags: VkFlag<DebugReportEXT>,
			objectType: DebugReportObjectTypeEXT,
			`object`: ULong,
			location: ULong,
			messageCode: Int,
			layerPrefix: String,
			message: String
	) {
		val instance = this
		MemoryStack.stackPush()
		try {
			vkDebugReportMessageEXT(instance.toVkType(), flags.toVkType(), objectType.toVkType(),
					`object`.toVkType(), location.toVkType(), messageCode.toVkType(),
					layerPrefix.toVkType(), message.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun submitDebugUtilsMessageEXT(
			messageSeverity: DebugUtilsMessageSeverityEXT,
			messageTypes: VkFlag<DebugUtilsMessageTypeEXT>,
			block: DebugUtilsMessengerCallbackDataEXTBuilder.() -> Unit
	) {
		val instance = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsMessengerCallbackDataEXT.callocStack()
			val builder = DebugUtilsMessengerCallbackDataEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkSubmitDebugUtilsMessageEXT(instance.toVkType(), messageSeverity.toVkType(),
					messageTypes.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual companion object {
		actual val version: VkVersion
			get() {
				MemoryStack.stackPush()
				try {
					val outputPtr = MemoryStack.stackGet().mallocInt(1)
					val result = vkEnumerateInstanceVersion(outputPtr)
					if (result != VK_SUCCESS) handleVkResult(result)
					return VkVersion(outputPtr[0].toUInt())
				} finally {
					MemoryStack.stackPop()
				}
			}

		actual val layerProperties: List<LayerProperties>
			get() {
				MemoryStack.stackPush()
				try {
					val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
					val result = vkEnumerateInstanceLayerProperties(outputCountPtr, null)
					when (result) {
						VK_SUCCESS -> Unit
						VK_INCOMPLETE -> Unit
						else -> handleVkResult(result)
					}
					val outputPtr = VkLayerProperties.mallocStack(outputCountPtr[0])
					val result1 = vkEnumerateInstanceLayerProperties(outputCountPtr, outputPtr)
					when (result1) {
						VK_SUCCESS -> Unit
						VK_INCOMPLETE -> Unit
						else -> handleVkResult(result1)
					}
					return List(outputCountPtr[0]) { LayerProperties.from(outputPtr[it]) }
				} finally {
					MemoryStack.stackPop()
				}
			}

		actual fun getExtensionProperties(layerName: String?): List<ExtensionProperties> {
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkEnumerateInstanceExtensionProperties(layerName.toVkType(),
						outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkExtensionProperties.mallocStack(outputCountPtr[0])
				val result1 = vkEnumerateInstanceExtensionProperties(layerName.toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { ExtensionProperties.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

		actual fun create(
				enabledLayerNames: Collection<String>?,
				enabledExtensionNames: Collection<String>?,
				block: InstanceCreateInfoBuilder.() -> Unit
		): Instance {
			MemoryStack.stackPush()
			try {
				val target = VkInstanceCreateInfo.callocStack()
				val builder = InstanceCreateInfoBuilder(target)
				builder.init(enabledLayerNames, enabledExtensionNames)
				builder.apply(block)
				val outputPtr = MemoryStack.stackGet().mallocPointer(1)
				val result = vkCreateInstance(target, null, outputPtr)

				var debugUtilsMessengerCallback: VkDebugUtilsMessengerCallbackEXT? = null
				var debugReportCallback: VkDebugReportCallbackEXT? = null
				var node = VkBaseInStructure.createSafe(target.pNext())
				while (node != null) {
					if (node.sType() == VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT) {
						debugUtilsMessengerCallback = VkDebugUtilsMessengerCreateInfoEXT.create(node.address()).pfnUserCallback()
					} else if (node.sType() == VK_STRUCTURE_TYPE_DEBUG_REPORT_CREATE_INFO_EXT) {
						debugReportCallback = VkDebugReportCallbackCreateInfoEXT.create(node.address()).pfnCallback()
					}
					node = node.pNext()
				}

				if (result != VK_SUCCESS) {
					debugUtilsMessengerCallback?.free()
					debugReportCallback?.free()
					handleVkResult(result)
				}

				if (result != VK_SUCCESS) handleVkResult(result)
				return Instance(VkInstance(outputPtr[0], target), debugUtilsMessengerCallback, debugReportCallback)
			} finally {
				MemoryStack.stackPop()
			}
		}
	}
}

