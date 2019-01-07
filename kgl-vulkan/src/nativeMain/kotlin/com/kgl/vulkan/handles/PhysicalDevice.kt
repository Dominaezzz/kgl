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
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class PhysicalDevice(override val ptr: VkPhysicalDevice, actual val instance: Instance) : VkHandleNative<VkPhysicalDevice>(), VkHandle {
	actual val properties: PhysicalDeviceProperties
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkPhysicalDeviceProperties>()
				val outputPtr = outputVar.ptr
				vkGetPhysicalDeviceProperties(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceProperties.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val queueFamilyProperties: List<QueueFamilyProperties>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.toVkType(), outputCountPtr, null)
				val outputPtr = VirtualStack.allocArray<VkQueueFamilyProperties>(outputCountVar.value.toInt())
				vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice.toVkType(), outputCountPtr, outputPtr)
				return List(outputCountVar.value.toInt()) { QueueFamilyProperties.from(outputPtr[it]) }
			} finally {
				VirtualStack.pop()
			}
		}

	actual val memoryProperties: PhysicalDeviceMemoryProperties
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkPhysicalDeviceMemoryProperties>()
				val outputPtr = outputVar.ptr
				vkGetPhysicalDeviceMemoryProperties(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceMemoryProperties.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val features: PhysicalDeviceFeatures
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkPhysicalDeviceFeatures>()
				val outputPtr = outputVar.ptr
				vkGetPhysicalDeviceFeatures(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceFeatures.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val deviceLayerProperties: List<LayerProperties>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = vkEnumerateDeviceLayerProperties(physicalDevice.toVkType(), outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VirtualStack.allocArray<VkLayerProperties>(outputCountVar.value.toInt())
				val result1 = vkEnumerateDeviceLayerProperties(physicalDevice.toVkType(), outputCountPtr, outputPtr)
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

	actual val displayPropertiesKHR: List<DisplayPropertiesKHR>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = vkGetPhysicalDeviceDisplayPropertiesKHR(physicalDevice.toVkType(),
						outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkDisplayPropertiesKHR>(outputCountVar.value.toInt())
				val result1 = vkGetPhysicalDeviceDisplayPropertiesKHR(physicalDevice.toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) {
					DisplayPropertiesKHR.from(outputPtr[it])
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual val displayPlanePropertiesKHR: List<DisplayPlanePropertiesKHR>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = vkGetPhysicalDeviceDisplayPlanePropertiesKHR(physicalDevice.toVkType(),
						outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkDisplayPlanePropertiesKHR>(outputCountVar.value.toInt())
				val result1 =
						vkGetPhysicalDeviceDisplayPlanePropertiesKHR(physicalDevice.toVkType(),
								outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) {
					DisplayPlanePropertiesKHR.from(outputPtr[it])
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual val features2: PhysicalDeviceFeatures2
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkPhysicalDeviceFeatures2>()
				val outputPtr = outputVar.ptr
				vkGetPhysicalDeviceFeatures2(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceFeatures2.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val properties2: PhysicalDeviceProperties2
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkPhysicalDeviceProperties2>()
				val outputPtr = outputVar.ptr
				vkGetPhysicalDeviceProperties2(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceProperties2.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val queueFamilyProperties2: List<QueueFamilyProperties2>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				vkGetPhysicalDeviceQueueFamilyProperties2(physicalDevice.toVkType(), outputCountPtr,
						null)
				val outputPtr =
						VirtualStack.allocArray<VkQueueFamilyProperties2>(outputCountVar.value.toInt())
				vkGetPhysicalDeviceQueueFamilyProperties2(physicalDevice.toVkType(), outputCountPtr,
						outputPtr)
				return List(outputCountVar.value.toInt()) {
					QueueFamilyProperties2.from(outputPtr[it])
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual val memoryProperties2: PhysicalDeviceMemoryProperties2
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkPhysicalDeviceMemoryProperties2>()
				val outputPtr = outputVar.ptr
				vkGetPhysicalDeviceMemoryProperties2(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceMemoryProperties2.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	actual val displayProperties2KHR: List<DisplayProperties2KHR>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result = vkGetPhysicalDeviceDisplayProperties2KHR(physicalDevice.toVkType(),
						outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkDisplayProperties2KHR>(outputCountVar.value.toInt())
				val result1 = vkGetPhysicalDeviceDisplayProperties2KHR(physicalDevice.toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) {
					DisplayProperties2KHR.from(outputPtr[it])
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual val displayPlaneProperties2KHR: List<DisplayPlaneProperties2KHR>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result =
						vkGetPhysicalDeviceDisplayPlaneProperties2KHR(physicalDevice.toVkType(),
								outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr =
						VirtualStack.allocArray<VkDisplayPlaneProperties2KHR>(outputCountVar.value.toInt())
				val result1 =
						vkGetPhysicalDeviceDisplayPlaneProperties2KHR(physicalDevice.toVkType(),
								outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) {
					DisplayPlaneProperties2KHR.from(outputPtr[it])
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual val calibrateableTimeDomainsEXT: List<TimeDomainEXT>
		get() {
			val physicalDevice = this
			VirtualStack.push()
			try {
				val outputCountVar = VirtualStack.alloc<UIntVar>()
				val outputCountPtr = outputCountVar.ptr
				val result =
						vkGetPhysicalDeviceCalibrateableTimeDomainsEXT(physicalDevice.toVkType(),
								outputCountPtr, null)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VirtualStack.allocArray<VkTimeDomainEXTVar>(outputCountVar.value.toInt())
				val result1 = vkGetPhysicalDeviceCalibrateableTimeDomainsEXT(physicalDevice.toVkType(),
						outputCountPtr, outputPtr)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountVar.value.toInt()) { TimeDomainEXT.from(outputPtr[it]) }
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		TODO()
	}

	actual fun getFormatProperties(format: Format): FormatProperties {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkFormatProperties>()
			val outputPtr = outputVar.ptr
			vkGetPhysicalDeviceFormatProperties(physicalDevice.toVkType(), format.toVkType(),
					outputPtr)
			return FormatProperties.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getImageFormatProperties(
			format: Format,
			type: ImageType,
			tiling: ImageTiling,
			usage: VkFlag<ImageUsage>,
			flags: VkFlag<ImageCreate>?
	): ImageFormatProperties {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkImageFormatProperties>()
			val outputPtr = outputVar.ptr
			val result = vkGetPhysicalDeviceImageFormatProperties(physicalDevice.toVkType(),
					format.toVkType(), type.toVkType(), tiling.toVkType(), usage.toVkType(),
					flags.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ImageFormatProperties.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDevice(
			enabledLayerNames: Collection<String>?,
			enabledExtensionNames: Collection<String>?,
			block: DeviceCreateInfoBuilder.() -> Unit
	): Device {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDeviceCreateInfo>().ptr
			val builder = DeviceCreateInfoBuilder(target.pointed)
			builder.init(enabledLayerNames, enabledExtensionNames)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDeviceVar>()
			val outputPtr = outputVar.ptr
			val result = vkCreateDevice(physicalDevice.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Device(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getDeviceExtensionProperties(layerName: String?): List<ExtensionProperties> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkEnumerateDeviceExtensionProperties(physicalDevice.toVkType(),
					layerName, outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr =
					VirtualStack.allocArray<VkExtensionProperties>(outputCountVar.value.toInt())
			val result1 = vkEnumerateDeviceExtensionProperties(physicalDevice.toVkType(),
					layerName, outputCountPtr, outputPtr)
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

	actual fun getSparseImageFormatProperties(
			format: Format,
			type: ImageType,
			samples: SampleCount,
			usage: VkFlag<ImageUsage>,
			tiling: ImageTiling
	): List<SparseImageFormatProperties> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			vkGetPhysicalDeviceSparseImageFormatProperties(physicalDevice.toVkType(),
					format.toVkType(), type.toVkType(), samples.toVkType(), usage.toVkType(),
					tiling.toVkType(), outputCountPtr, null)
			val outputPtr =
					VirtualStack.allocArray<VkSparseImageFormatProperties>(outputCountVar.value.toInt())
			vkGetPhysicalDeviceSparseImageFormatProperties(physicalDevice.toVkType(),
					format.toVkType(), type.toVkType(), samples.toVkType(), usage.toVkType(),
					tiling.toVkType(), outputCountPtr, outputPtr)
			return List(outputCountVar.value.toInt()) {
				SparseImageFormatProperties.from(outputPtr[it])
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getDisplayPlaneSupportedDisplaysKHR(planeIndex: UInt): List<DisplayKHR> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkGetDisplayPlaneSupportedDisplaysKHR(physicalDevice.toVkType(),
					planeIndex.toVkType(), outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VirtualStack.allocArray<VkDisplayKHRVar>(outputCountVar.value.toInt())
			val result1 = vkGetDisplayPlaneSupportedDisplaysKHR(physicalDevice.toVkType(),
					planeIndex.toVkType(), outputCountPtr, outputPtr)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountVar.value.toInt()) { DisplayKHR(outputPtr[it]!!) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getDisplayModePropertiesKHR(display: DisplayKHR): List<DisplayModePropertiesKHR> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkGetDisplayModePropertiesKHR(physicalDevice.toVkType(),
					display.toVkType(), outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr =
					VirtualStack.allocArray<VkDisplayModePropertiesKHR>(outputCountVar.value.toInt())
			val result1 = vkGetDisplayModePropertiesKHR(physicalDevice.toVkType(),
					display.toVkType(), outputCountPtr, outputPtr)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountVar.value.toInt()) {
				DisplayModePropertiesKHR.from(outputPtr[it])
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDisplayModeKHR(display: DisplayKHR, block: DisplayModeCreateInfoKHRBuilder.() -> Unit): DisplayModeKHR {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDisplayModeCreateInfoKHR>().ptr
			val builder = DisplayModeCreateInfoKHRBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDisplayModeKHRVar>()
			val outputPtr = outputVar.ptr
			val result = vkCreateDisplayModeKHR(physicalDevice.toVkType(), display.toVkType(),
					target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DisplayModeKHR(outputVar.value!!, this, display)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSurfaceSupportKHR(queueFamilyIndex: UInt, surface: SurfaceKHR): Boolean {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<UIntVar>()
			val outputPtr = outputVar.ptr
			val result = vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice.toVkType(),
					queueFamilyIndex.toVkType(), surface.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputVar.value.toBoolean()
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSurfaceCapabilitiesKHR(surface: SurfaceKHR): SurfaceCapabilitiesKHR {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkSurfaceCapabilitiesKHR>()
			val outputPtr = outputVar.ptr
			val result = vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice.toVkType(),
					surface.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceCapabilitiesKHR.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSurfaceFormatsKHR(surface: SurfaceKHR): List<SurfaceFormatKHR> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.toVkType(),
					surface.toVkType(), outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr =
					VirtualStack.allocArray<VkSurfaceFormatKHR>(outputCountVar.value.toInt())
			val result1 = vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.toVkType(),
					surface.toVkType(), outputCountPtr, outputPtr)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountVar.value.toInt()) { SurfaceFormatKHR.from(outputPtr[it]) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSurfacePresentModesKHR(surface: SurfaceKHR): List<PresentModeKHR> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.toVkType(),
					surface.toVkType(), outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VirtualStack.allocArray<VkPresentModeKHRVar>(outputCountVar.value.toInt())
			val result1 = vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice.toVkType(),
					surface.toVkType(), outputCountPtr, outputPtr)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountVar.value.toInt()) { PresentModeKHR.from(outputPtr[it]) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getExternalImageFormatPropertiesNV(
			format: Format,
			type: ImageType,
			tiling: ImageTiling,
			usage: VkFlag<ImageUsage>,
			flags: VkFlag<ImageCreate>?,
			externalHandleType: VkFlag<ExternalMemoryHandleTypeNV>?
	): ExternalImageFormatPropertiesNV {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkExternalImageFormatPropertiesNV>()
			val outputPtr = outputVar.ptr
			val result =
					vkGetPhysicalDeviceExternalImageFormatPropertiesNV(physicalDevice.toVkType(),
							format.toVkType(), type.toVkType(), tiling.toVkType(), usage.toVkType(),
							flags.toVkType(), externalHandleType.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ExternalImageFormatPropertiesNV.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getFormatProperties2(format: Format): FormatProperties2 {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkFormatProperties2>()
			val outputPtr = outputVar.ptr
			vkGetPhysicalDeviceFormatProperties2(physicalDevice.toVkType(), format.toVkType(),
					outputPtr)
			return FormatProperties2.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getImageFormatProperties2(block: PhysicalDeviceImageFormatInfo2Builder.() -> Unit): ImageFormatProperties2 {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPhysicalDeviceImageFormatInfo2>().ptr
			val builder = PhysicalDeviceImageFormatInfo2Builder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkImageFormatProperties2>()
			val outputPtr = outputVar.ptr
			val result = vkGetPhysicalDeviceImageFormatProperties2(physicalDevice.toVkType(), target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ImageFormatProperties2.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSparseImageFormatProperties2(block: PhysicalDeviceSparseImageFormatInfo2Builder.() -> Unit): List<SparseImageFormatProperties2> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPhysicalDeviceSparseImageFormatInfo2>().ptr
			val builder = PhysicalDeviceSparseImageFormatInfo2Builder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			vkGetPhysicalDeviceSparseImageFormatProperties2(physicalDevice.toVkType(), target,
					outputCountPtr, null)
			val outputPtr =
					VirtualStack.allocArray<VkSparseImageFormatProperties2>(outputCountVar.value.toInt())
			vkGetPhysicalDeviceSparseImageFormatProperties2(physicalDevice.toVkType(), target,
					outputCountPtr, outputPtr)
			return List(outputCountVar.value.toInt()) {
				SparseImageFormatProperties2.from(outputPtr[it])
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getExternalBufferProperties(block: PhysicalDeviceExternalBufferInfoBuilder.() -> Unit): ExternalBufferProperties {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPhysicalDeviceExternalBufferInfo>().ptr
			val builder = PhysicalDeviceExternalBufferInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkExternalBufferProperties>()
			val outputPtr = outputVar.ptr
			vkGetPhysicalDeviceExternalBufferProperties(physicalDevice.toVkType(), target,
					outputPtr)
			return ExternalBufferProperties.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getExternalSemaphoreProperties(block: PhysicalDeviceExternalSemaphoreInfoBuilder.() -> Unit): ExternalSemaphoreProperties {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPhysicalDeviceExternalSemaphoreInfo>().ptr
			val builder = PhysicalDeviceExternalSemaphoreInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkExternalSemaphoreProperties>()
			val outputPtr = outputVar.ptr
			vkGetPhysicalDeviceExternalSemaphoreProperties(physicalDevice.toVkType(), target,
					outputPtr)
			return ExternalSemaphoreProperties.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getExternalFenceProperties(block: PhysicalDeviceExternalFenceInfoBuilder.() -> Unit): ExternalFenceProperties {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPhysicalDeviceExternalFenceInfo>().ptr
			val builder = PhysicalDeviceExternalFenceInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkExternalFenceProperties>()
			val outputPtr = outputVar.ptr
			vkGetPhysicalDeviceExternalFenceProperties(physicalDevice.toVkType(), target, outputPtr)
			return ExternalFenceProperties.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun releaseDisplayEXT(display: DisplayKHR) {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val result = vkReleaseDisplayEXT(physicalDevice.toVkType(), display.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSurfaceCapabilities2EXT(surface: SurfaceKHR): SurfaceCapabilities2EXT {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkSurfaceCapabilities2EXT>()
			val outputPtr = outputVar.ptr
			val result = vkGetPhysicalDeviceSurfaceCapabilities2EXT(physicalDevice.toVkType(),
					surface.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceCapabilities2EXT.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getPresentRectanglesKHR(surface: SurfaceKHR): List<Rect2D> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkGetPhysicalDevicePresentRectanglesKHR(physicalDevice.toVkType(),
					surface.toVkType(), outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VirtualStack.allocArray<VkRect2D>(outputCountVar.value.toInt())
			val result1 = vkGetPhysicalDevicePresentRectanglesKHR(physicalDevice.toVkType(),
					surface.toVkType(), outputCountPtr, outputPtr)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountVar.value.toInt()) { Rect2D.from(outputPtr[it]) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getMultisamplePropertiesEXT(samples: SampleCount): MultisamplePropertiesEXT {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkMultisamplePropertiesEXT>()
			val outputPtr = outputVar.ptr
			vkGetPhysicalDeviceMultisamplePropertiesEXT(physicalDevice.toVkType(),
					samples.toVkType(), outputPtr)
			return MultisamplePropertiesEXT.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSurfaceCapabilities2KHR(surface: SurfaceKHR, block: PhysicalDeviceSurfaceInfo2KHRBuilder.() -> Unit): SurfaceCapabilities2KHR {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPhysicalDeviceSurfaceInfo2KHR>().ptr
			val builder = PhysicalDeviceSurfaceInfo2KHRBuilder(target.pointed)
			builder.init(surface)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkSurfaceCapabilities2KHR>()
			val outputPtr = outputVar.ptr
			val result = vkGetPhysicalDeviceSurfaceCapabilities2KHR(physicalDevice.toVkType(),
					target, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceCapabilities2KHR.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getSurfaceFormats2KHR(surface: SurfaceKHR, block: PhysicalDeviceSurfaceInfo2KHRBuilder.() -> Unit): List<SurfaceFormat2KHR> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPhysicalDeviceSurfaceInfo2KHR>().ptr
			val builder = PhysicalDeviceSurfaceInfo2KHRBuilder(target.pointed)
			builder.init(surface)
			builder.apply(block)
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkGetPhysicalDeviceSurfaceFormats2KHR(physicalDevice.toVkType(), target,
					outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr =
					VirtualStack.allocArray<VkSurfaceFormat2KHR>(outputCountVar.value.toInt())
			val result1 = vkGetPhysicalDeviceSurfaceFormats2KHR(physicalDevice.toVkType(), target,
					outputCountPtr, outputPtr)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountVar.value.toInt()) { SurfaceFormat2KHR.from(outputPtr[it]) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getDisplayModeProperties2KHR(display: DisplayKHR): List<DisplayModeProperties2KHR> {
		val physicalDevice = this
		VirtualStack.push()
		try {
			val outputCountVar = VirtualStack.alloc<UIntVar>()
			val outputCountPtr = outputCountVar.ptr
			val result = vkGetDisplayModeProperties2KHR(physicalDevice.toVkType(),
					display.toVkType(), outputCountPtr, null)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr =
					VirtualStack.allocArray<VkDisplayModeProperties2KHR>(outputCountVar.value.toInt())
			val result1 = vkGetDisplayModeProperties2KHR(physicalDevice.toVkType(),
					display.toVkType(), outputCountPtr, outputPtr)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountVar.value.toInt()) {
				DisplayModeProperties2KHR.from(outputPtr[it])
			}
		} finally {
			VirtualStack.pop()
		}
	}
}

