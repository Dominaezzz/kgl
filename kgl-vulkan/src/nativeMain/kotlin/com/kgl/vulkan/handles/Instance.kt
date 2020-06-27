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

import com.kgl.core.VirtualStack
import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.enums.DebugReportEXT
import com.kgl.vulkan.enums.DebugReportObjectTypeEXT
import com.kgl.vulkan.enums.DebugUtilsMessageSeverityEXT
import com.kgl.vulkan.enums.DebugUtilsMessageTypeEXT
import com.kgl.vulkan.structs.ExtensionProperties
import com.kgl.vulkan.structs.LayerProperties
import com.kgl.vulkan.structs.PhysicalDeviceGroupProperties
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class Instance(override val ptr: VkInstance, private val debugUtilsMessengerCallback: StableRef<*>?, private val debugReportCallback: StableRef<*>?) : VkHandleNative<VkInstance>(), VkHandle {
	internal val dispatchTable = InstanceDispatchTable {
		VirtualStack.push()
		try {
			Loader.vkGetInstanceProcAddr(ptr, it.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}
	
	actual val physicalDevices: List<PhysicalDevice>
		get() {
			val instance = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = dispatchTable.vkEnumeratePhysicalDevices(instance.toVkType(), outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkPhysicalDeviceVar>(outputCountVar.value.toInt())
				val result1 = dispatchTable.vkEnumeratePhysicalDevices(instance.toVkType(), outputCountPtr,
						outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) { PhysicalDevice(outputPtr[it]!!, this) }
			} finally {
				VirtualStack.pop()
			}
		}

	actual val physicalDeviceGroups: List<PhysicalDeviceGroupProperties>
		get() {
			val instance = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = dispatchTable.vkEnumeratePhysicalDeviceGroups!!(instance.toVkType(), outputCountPtr,
						null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkPhysicalDeviceGroupProperties>(outputCountVar.value.toInt())
				val result1 = dispatchTable.vkEnumeratePhysicalDeviceGroups!!(instance.toVkType(), outputCountPtr,
						outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) {
					PhysicalDeviceGroupProperties.from(outputPtr[it])
				}
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		val instance = this
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyInstance(instance.toVkType(), null)
		} finally {
			VirtualStack.pop()
			debugReportCallback?.dispose()
			debugUtilsMessengerCallback?.dispose()
		}
	}

	actual fun getProcAddr(name: String): Long {
		val instance = this
		VirtualStack.push()
		try {
			return Loader.vkGetInstanceProcAddr(instance.toVkType(), name.toVkType()).toLong()
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDisplayPlaneSurfaceKHR(displayMode: DisplayModeKHR, block: DisplaySurfaceCreateInfoKHRBuilder.() -> Unit): SurfaceKHR {
		val instance = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDisplaySurfaceCreateInfoKHR>().ptr
			val builder = DisplaySurfaceCreateInfoKHRBuilder(target.pointed)
			builder.init(displayMode)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkSurfaceKHRVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateDisplayPlaneSurfaceKHR!!(instance.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceKHR(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDebugUtilsMessengerEXT(block: DebugUtilsMessengerCreateInfoEXTBuilder.() -> Unit): DebugUtilsMessengerEXT {
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsMessengerCreateInfoEXT>()
			val builder = DebugUtilsMessengerCreateInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)

			val stablePtr = target.pUserData!!.asStableRef<Any>()

			val outputVar = VirtualStack.alloc<VkDebugUtilsMessengerEXTVar>()
			val result = dispatchTable.vkCreateDebugUtilsMessengerEXT!!(ptr, target.ptr, null, outputVar.ptr)
			if (result != VK_SUCCESS) {
				stablePtr.dispose()
				handleVkResult(result)
			}
			return DebugUtilsMessengerEXT(outputVar.value!!, this, stablePtr)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDebugReportCallbackEXT(block: DebugReportCallbackCreateInfoEXTBuilder.() -> Unit): DebugReportCallbackEXT {
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugReportCallbackCreateInfoEXT>()
			val builder = DebugReportCallbackCreateInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)

			val stablePtr = target.pUserData!!.asStableRef<Any>()

			val outputVar = VirtualStack.alloc<VkDebugReportCallbackEXTVar>()
			val result = dispatchTable.vkCreateDebugReportCallbackEXT!!(ptr, target.ptr, null, outputVar.ptr)
			if (result != VK_SUCCESS) {
				stablePtr.dispose()
				handleVkResult(result)
			}
			return DebugReportCallbackEXT(outputVar.value!!, this, stablePtr)
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkDebugReportMessageEXT!!(instance.toVkType(), flags.toVkType(), objectType.toVkType(),
					`object`.toVkType(), location.toVkType(), messageCode.toVkType(),
					layerPrefix.toVkType(), message.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun submitDebugUtilsMessageEXT(
			messageSeverity: DebugUtilsMessageSeverityEXT,
			messageTypes: VkFlag<DebugUtilsMessageTypeEXT>,
			block: DebugUtilsMessengerCallbackDataEXTBuilder.() -> Unit
	) {
		val instance = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsMessengerCallbackDataEXT>().ptr
			val builder = DebugUtilsMessengerCallbackDataEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkSubmitDebugUtilsMessageEXT!!(instance.toVkType(), messageSeverity.toVkType(),
					messageTypes.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual companion object {
		private val globalDispatchTable = GlobalDispatchTable {
			VirtualStack.push()
			try {
				Loader.vkGetInstanceProcAddr(null, it.toVkType())
			} finally {
				VirtualStack.pop()
			}
		}

		actual val version: VkVersion
			get() {
				VirtualStack.push()
				try {
					val outputVar = VirtualStack.alloc<UIntVar>()
					val outputPtr = outputVar.ptr
					val result = globalDispatchTable.vkEnumerateInstanceVersion!!(outputPtr)
					if (result != VK_SUCCESS) handleVkResult(result)
					return VkVersion(outputVar.value)
				} finally {
					VirtualStack.pop()
				}
			}

		actual val layerProperties: List<LayerProperties>
			get() {
				VirtualStack.push()
				try {
					val outputCountVar = VirtualStack.alloc<UIntVar>()
					val outputCountPtr = outputCountVar.ptr
					val result = globalDispatchTable.vkEnumerateInstanceLayerProperties(outputCountPtr, null)
					when (result) {
						VK_SUCCESS -> Unit
						VK_INCOMPLETE -> Unit
						else -> handleVkResult(result)
					}
					val outputPtr =
							VirtualStack.allocArray<VkLayerProperties>(outputCountVar.value.toInt())
					val result1 = globalDispatchTable.vkEnumerateInstanceLayerProperties(outputCountPtr, outputPtr)
					when (result1) {
						VK_SUCCESS -> Unit
						VK_INCOMPLETE -> Unit
						else -> handleVkResult(result1)
					}
					return List(outputCountVar.value.toInt()) { LayerProperties.from(outputPtr[it]) }
				} finally {
					VirtualStack.pop()
				}
			}

		actual fun getExtensionProperties(layerName: String?): List<ExtensionProperties> {
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = globalDispatchTable.vkEnumerateInstanceExtensionProperties(layerName?.toVkType(), outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkExtensionProperties>(outputCountVar.value.toInt())
				val result1 = globalDispatchTable.vkEnumerateInstanceExtensionProperties(layerName?.toVkType(), outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) { ExtensionProperties.from(outputPtr[it]) }
			} finally {
				VirtualStack.pop()
			}
		}

		actual fun create(
				enabledLayerNames: Collection<String>?,
				enabledExtensionNames: Collection<String>?,
				block: InstanceCreateInfoBuilder.() -> Unit
		): Instance {
			VirtualStack.push()
			try {
				val target = VirtualStack.alloc<VkInstanceCreateInfo>().ptr
				val builder = InstanceCreateInfoBuilder(target.pointed)
				builder.init(enabledLayerNames, enabledExtensionNames)
				builder.apply(block)
				val outputVar = VirtualStack.alloc<VkInstanceVar>()
				val outputPtr = outputVar.ptr
				val result = globalDispatchTable.vkCreateInstance(target, null, outputPtr)

				var debugUtilsMessengerCallback: StableRef<*>? = null
				var debugReportCallback: StableRef<*>? = null
				var node = target.pointed.pNext?.reinterpret<VkBaseInStructure>()
				while (node != null) {
					val struct = node.pointed
					if (struct.sType == VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT) {
						debugUtilsMessengerCallback = struct.reinterpret<VkDebugUtilsMessengerCreateInfoEXT>().pUserData?.asStableRef<Any>()
					} else if (struct.sType == VK_STRUCTURE_TYPE_DEBUG_REPORT_CREATE_INFO_EXT) {
						debugReportCallback = struct.reinterpret<VkDebugReportCallbackCreateInfoEXT>().pUserData?.asStableRef<Any>()
					}
					node = struct.pNext
				}

				if (result != VK_SUCCESS) {
					debugUtilsMessengerCallback?.dispose()
					debugReportCallback?.dispose()
					handleVkResult(result)
				}
				return Instance(outputVar.value!!, debugUtilsMessengerCallback, debugReportCallback)
			} finally {
				VirtualStack.pop()
			}
		}
	}
}

