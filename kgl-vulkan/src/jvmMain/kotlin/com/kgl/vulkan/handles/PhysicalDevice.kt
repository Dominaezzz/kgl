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
import org.lwjgl.system.*
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.EXTCalibratedTimestamps.*
import org.lwjgl.vulkan.EXTDirectModeDisplay.*
import org.lwjgl.vulkan.EXTDisplaySurfaceCounter.*
import org.lwjgl.vulkan.EXTSampleLocations.*
import org.lwjgl.vulkan.KHRDisplay.*
import org.lwjgl.vulkan.KHRGetDisplayProperties2.*
import org.lwjgl.vulkan.KHRGetSurfaceCapabilities2.*
import org.lwjgl.vulkan.KHRSurface.*
import org.lwjgl.vulkan.KHRSwapchain.*
import org.lwjgl.vulkan.NVExternalMemoryCapabilities.*
import org.lwjgl.vulkan.VK11.*

actual class PhysicalDevice(
	override val ptr: VkPhysicalDevice,
	actual val instance: Instance
) : VkHandleJVM<VkPhysicalDevice>(), VkHandle {
	actual val properties: PhysicalDeviceProperties
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputPtr = VkPhysicalDeviceProperties.mallocStack()
				vkGetPhysicalDeviceProperties(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceProperties.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val queueFamilyProperties: List<QueueFamilyProperties>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				vkGetPhysicalDeviceQueueFamilyProperties(
					physicalDevice.toVkType(), outputCountPtr,
					null
				)
				val outputPtr = VkQueueFamilyProperties.mallocStack(outputCountPtr[0])
				vkGetPhysicalDeviceQueueFamilyProperties(
					physicalDevice.toVkType(), outputCountPtr,
					outputPtr
				)
				return List(outputCountPtr[0]) { QueueFamilyProperties.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val memoryProperties: PhysicalDeviceMemoryProperties
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputPtr = VkPhysicalDeviceMemoryProperties.mallocStack()
				vkGetPhysicalDeviceMemoryProperties(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceMemoryProperties.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val features: PhysicalDeviceFeatures
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputPtr = VkPhysicalDeviceFeatures.mallocStack()
				vkGetPhysicalDeviceFeatures(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceFeatures.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val deviceLayerProperties: List<LayerProperties>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkEnumerateDeviceLayerProperties(
					physicalDevice.toVkType(),
					outputCountPtr, null
				)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkLayerProperties.mallocStack(outputCountPtr[0])
				val result1 = vkEnumerateDeviceLayerProperties(
					physicalDevice.toVkType(),
					outputCountPtr, outputPtr
				)
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

	actual val displayPropertiesKHR: List<DisplayPropertiesKHR>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkGetPhysicalDeviceDisplayPropertiesKHR(
					physicalDevice.toVkType(),
					outputCountPtr, null
				)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkDisplayPropertiesKHR.mallocStack(outputCountPtr[0])
				val result1 = vkGetPhysicalDeviceDisplayPropertiesKHR(
					physicalDevice.toVkType(),
					outputCountPtr, outputPtr
				)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { DisplayPropertiesKHR.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val displayPlanePropertiesKHR: List<DisplayPlanePropertiesKHR>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkGetPhysicalDeviceDisplayPlanePropertiesKHR(
					physicalDevice.toVkType(),
					outputCountPtr, null
				)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkDisplayPlanePropertiesKHR.mallocStack(outputCountPtr[0])
				val result1 =
					vkGetPhysicalDeviceDisplayPlanePropertiesKHR(
						physicalDevice.toVkType(),
						outputCountPtr, outputPtr
					)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { DisplayPlanePropertiesKHR.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val features2: PhysicalDeviceFeatures2
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputPtr = VkPhysicalDeviceFeatures2.mallocStack()
				vkGetPhysicalDeviceFeatures2(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceFeatures2.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val properties2: PhysicalDeviceProperties2
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputPtr = VkPhysicalDeviceProperties2.mallocStack()
				vkGetPhysicalDeviceProperties2(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceProperties2.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val queueFamilyProperties2: List<QueueFamilyProperties2>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				vkGetPhysicalDeviceQueueFamilyProperties2(
					physicalDevice.toVkType(), outputCountPtr,
					null
				)
				val outputPtr = VkQueueFamilyProperties2.mallocStack(outputCountPtr[0])
				vkGetPhysicalDeviceQueueFamilyProperties2(
					physicalDevice.toVkType(), outputCountPtr,
					outputPtr
				)
				return List(outputCountPtr[0]) { QueueFamilyProperties2.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val memoryProperties2: PhysicalDeviceMemoryProperties2
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputPtr = VkPhysicalDeviceMemoryProperties2.mallocStack()
				vkGetPhysicalDeviceMemoryProperties2(physicalDevice.toVkType(), outputPtr)
				return PhysicalDeviceMemoryProperties2.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val displayProperties2KHR: List<DisplayProperties2KHR>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result = vkGetPhysicalDeviceDisplayProperties2KHR(
					physicalDevice.toVkType(),
					outputCountPtr, null
				)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkDisplayProperties2KHR.mallocStack(outputCountPtr[0])
				val result1 = vkGetPhysicalDeviceDisplayProperties2KHR(
					physicalDevice.toVkType(),
					outputCountPtr, outputPtr
				)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { DisplayProperties2KHR.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val displayPlaneProperties2KHR: List<DisplayPlaneProperties2KHR>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result =
					vkGetPhysicalDeviceDisplayPlaneProperties2KHR(
						physicalDevice.toVkType(),
						outputCountPtr, null
					)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = VkDisplayPlaneProperties2KHR.mallocStack(outputCountPtr[0])
				val result1 =
					vkGetPhysicalDeviceDisplayPlaneProperties2KHR(
						physicalDevice.toVkType(),
						outputCountPtr, outputPtr
					)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { DisplayPlaneProperties2KHR.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	actual val calibrateableTimeDomainsEXT: List<TimeDomainEXT>
		get() {
			val physicalDevice = this
			MemoryStack.stackPush()
			try {
				val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
				val result =
					vkGetPhysicalDeviceCalibrateableTimeDomainsEXT(
						physicalDevice.toVkType(),
						outputCountPtr, null
					)
				when (result) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result)
				}
				val outputPtr = MemoryStack.stackGet().mallocInt(outputCountPtr[0])
				val result1 = vkGetPhysicalDeviceCalibrateableTimeDomainsEXT(
					physicalDevice.toVkType(),
					outputCountPtr, outputPtr
				)
				when (result1) {
					VK_SUCCESS -> Unit
					VK_INCOMPLETE -> Unit
					else -> handleVkResult(result1)
				}
				return List(outputCountPtr[0]) { TimeDomainEXT.from(outputPtr[it]) }
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		TODO()
	}

	actual fun getFormatProperties(format: Format): FormatProperties {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputPtr = VkFormatProperties.mallocStack()
			vkGetPhysicalDeviceFormatProperties(
				physicalDevice.toVkType(), format.toVkType(),
				outputPtr
			)
			return FormatProperties.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
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
		MemoryStack.stackPush()
		try {
			val outputPtr = VkImageFormatProperties.mallocStack()
			val result = vkGetPhysicalDeviceImageFormatProperties(
				physicalDevice.toVkType(),
				format.toVkType(), type.toVkType(), tiling.toVkType(), usage.toVkType(),
				flags.toVkType(), outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ImageFormatProperties.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDevice(
		enabledLayerNames: Collection<String>?,
		enabledExtensionNames: Collection<String>?,
		block: DeviceCreateInfoBuilder.() -> Unit
	): Device {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkDeviceCreateInfo.callocStack()
			val builder = DeviceCreateInfoBuilder(target)
			builder.init(enabledLayerNames, enabledExtensionNames)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocPointer(1)
			val result = vkCreateDevice(physicalDevice.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Device(VkDevice(outputPtr[0], ptr, target), this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getDeviceExtensionProperties(layerName: String?): List<ExtensionProperties> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkEnumerateDeviceExtensionProperties(
				physicalDevice.toVkType(),
				layerName.toVkType(), outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VkExtensionProperties.mallocStack(outputCountPtr[0])
			val result1 = vkEnumerateDeviceExtensionProperties(
				physicalDevice.toVkType(),
				layerName.toVkType(), outputCountPtr, outputPtr
			)
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

	actual fun getSparseImageFormatProperties(
		format: Format,
		type: ImageType,
		samples: SampleCount,
		usage: VkFlag<ImageUsage>,
		tiling: ImageTiling
	): List<SparseImageFormatProperties> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			vkGetPhysicalDeviceSparseImageFormatProperties(
				physicalDevice.toVkType(),
				format.toVkType(), type.toVkType(), samples.toVkType(), usage.toVkType(),
				tiling.toVkType(), outputCountPtr, null
			)
			val outputPtr = VkSparseImageFormatProperties.mallocStack(outputCountPtr[0])
			vkGetPhysicalDeviceSparseImageFormatProperties(
				physicalDevice.toVkType(),
				format.toVkType(), type.toVkType(), samples.toVkType(), usage.toVkType(),
				tiling.toVkType(), outputCountPtr, outputPtr
			)
			return List(outputCountPtr[0]) { SparseImageFormatProperties.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getDisplayPlaneSupportedDisplaysKHR(planeIndex: UInt): List<DisplayKHR> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetDisplayPlaneSupportedDisplaysKHR(
				physicalDevice.toVkType(),
				planeIndex.toVkType(), outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = MemoryStack.stackGet().mallocLong(outputCountPtr[0])
			val result1 = vkGetDisplayPlaneSupportedDisplaysKHR(
				physicalDevice.toVkType(),
				planeIndex.toVkType(), outputCountPtr, outputPtr
			)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountPtr[0]) { DisplayKHR(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getDisplayModePropertiesKHR(display: DisplayKHR): List<DisplayModePropertiesKHR> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetDisplayModePropertiesKHR(
				physicalDevice.toVkType(),
				display.toVkType(), outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VkDisplayModePropertiesKHR.mallocStack(outputCountPtr[0])
			val result1 = vkGetDisplayModePropertiesKHR(
				physicalDevice.toVkType(),
				display.toVkType(), outputCountPtr, outputPtr
			)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountPtr[0]) { DisplayModePropertiesKHR.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDisplayModeKHR(
		display: DisplayKHR,
		block: DisplayModeCreateInfoKHRBuilder.() -> Unit
	): DisplayModeKHR {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkDisplayModeCreateInfoKHR.callocStack()
			val builder = DisplayModeCreateInfoKHRBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateDisplayModeKHR(
				physicalDevice.toVkType(), display.toVkType(),
				target, null, outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DisplayModeKHR(outputPtr[0], this, display)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSurfaceSupportKHR(queueFamilyIndex: UInt, surface: SurfaceKHR): Boolean {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetPhysicalDeviceSurfaceSupportKHR(
				physicalDevice.toVkType(),
				queueFamilyIndex.toVkType(), surface.toVkType(), outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return outputPtr[0].toBoolean()
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSurfaceCapabilitiesKHR(surface: SurfaceKHR): SurfaceCapabilitiesKHR {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputPtr = VkSurfaceCapabilitiesKHR.mallocStack()
			val result = vkGetPhysicalDeviceSurfaceCapabilitiesKHR(
				physicalDevice.toVkType(),
				surface.toVkType(), outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceCapabilitiesKHR.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSurfaceFormatsKHR(surface: SurfaceKHR): List<SurfaceFormatKHR> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetPhysicalDeviceSurfaceFormatsKHR(
				physicalDevice.toVkType(),
				surface.toVkType(), outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VkSurfaceFormatKHR.mallocStack(outputCountPtr[0])
			val result1 = vkGetPhysicalDeviceSurfaceFormatsKHR(
				physicalDevice.toVkType(),
				surface.toVkType(), outputCountPtr, outputPtr
			)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountPtr[0]) { SurfaceFormatKHR.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSurfacePresentModesKHR(surface: SurfaceKHR): List<PresentModeKHR> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetPhysicalDeviceSurfacePresentModesKHR(
				physicalDevice.toVkType(),
				surface.toVkType(), outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = MemoryStack.stackGet().mallocInt(outputCountPtr[0])
			val result1 = vkGetPhysicalDeviceSurfacePresentModesKHR(
				physicalDevice.toVkType(),
				surface.toVkType(), outputCountPtr, outputPtr
			)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountPtr[0]) { PresentModeKHR.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
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
		MemoryStack.stackPush()
		try {
			val outputPtr = VkExternalImageFormatPropertiesNV.mallocStack()
			val result =
				vkGetPhysicalDeviceExternalImageFormatPropertiesNV(
					physicalDevice.toVkType(),
					format.toVkType(), type.toVkType(), tiling.toVkType(), usage.toVkType(),
					flags.toVkType(), externalHandleType.toVkType(), outputPtr
				)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ExternalImageFormatPropertiesNV.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getFormatProperties2(format: Format): FormatProperties2 {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputPtr = VkFormatProperties2.mallocStack()
			vkGetPhysicalDeviceFormatProperties2(
				physicalDevice.toVkType(), format.toVkType(),
				outputPtr
			)
			return FormatProperties2.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getImageFormatProperties2(block: PhysicalDeviceImageFormatInfo2Builder.() -> Unit): ImageFormatProperties2 {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkPhysicalDeviceImageFormatInfo2.callocStack()
			val builder = PhysicalDeviceImageFormatInfo2Builder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = VkImageFormatProperties2.mallocStack()
			val result = vkGetPhysicalDeviceImageFormatProperties2(
				physicalDevice.toVkType(),
				target, outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ImageFormatProperties2.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSparseImageFormatProperties2(block: PhysicalDeviceSparseImageFormatInfo2Builder.() -> Unit): List<SparseImageFormatProperties2> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkPhysicalDeviceSparseImageFormatInfo2.callocStack()
			val builder = PhysicalDeviceSparseImageFormatInfo2Builder(target)
			builder.init()
			builder.apply(block)
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			vkGetPhysicalDeviceSparseImageFormatProperties2(
				physicalDevice.toVkType(), target,
				outputCountPtr, null
			)
			val outputPtr = VkSparseImageFormatProperties2.mallocStack(outputCountPtr[0])
			vkGetPhysicalDeviceSparseImageFormatProperties2(
				physicalDevice.toVkType(), target,
				outputCountPtr, outputPtr
			)
			return List(outputCountPtr[0]) { SparseImageFormatProperties2.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getExternalBufferProperties(block: PhysicalDeviceExternalBufferInfoBuilder.() -> Unit): ExternalBufferProperties {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkPhysicalDeviceExternalBufferInfo.callocStack()
			val builder = PhysicalDeviceExternalBufferInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = VkExternalBufferProperties.mallocStack()
			vkGetPhysicalDeviceExternalBufferProperties(
				physicalDevice.toVkType(), target,
				outputPtr
			)
			return ExternalBufferProperties.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getExternalSemaphoreProperties(block: PhysicalDeviceExternalSemaphoreInfoBuilder.() -> Unit): ExternalSemaphoreProperties {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkPhysicalDeviceExternalSemaphoreInfo.callocStack()
			val builder = PhysicalDeviceExternalSemaphoreInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = VkExternalSemaphoreProperties.mallocStack()
			vkGetPhysicalDeviceExternalSemaphoreProperties(
				physicalDevice.toVkType(), target,
				outputPtr
			)
			return ExternalSemaphoreProperties.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getExternalFenceProperties(block: PhysicalDeviceExternalFenceInfoBuilder.() -> Unit): ExternalFenceProperties {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkPhysicalDeviceExternalFenceInfo.callocStack()
			val builder = PhysicalDeviceExternalFenceInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = VkExternalFenceProperties.mallocStack()
			vkGetPhysicalDeviceExternalFenceProperties(physicalDevice.toVkType(), target, outputPtr)
			return ExternalFenceProperties.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun releaseDisplayEXT(display: DisplayKHR) {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val result = vkReleaseDisplayEXT(physicalDevice.toVkType(), display.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSurfaceCapabilities2EXT(surface: SurfaceKHR): SurfaceCapabilities2EXT {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputPtr = VkSurfaceCapabilities2EXT.mallocStack()
			val result = vkGetPhysicalDeviceSurfaceCapabilities2EXT(
				physicalDevice.toVkType(),
				surface.toVkType(), outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceCapabilities2EXT.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getPresentRectanglesKHR(surface: SurfaceKHR): List<Rect2D> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetPhysicalDevicePresentRectanglesKHR(
				physicalDevice.toVkType(),
				surface.toVkType(), outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VkRect2D.mallocStack(outputCountPtr[0])
			val result1 = vkGetPhysicalDevicePresentRectanglesKHR(
				physicalDevice.toVkType(),
				surface.toVkType(), outputCountPtr, outputPtr
			)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountPtr[0]) { Rect2D.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getMultisamplePropertiesEXT(samples: SampleCount): MultisamplePropertiesEXT {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputPtr = VkMultisamplePropertiesEXT.mallocStack()
			vkGetPhysicalDeviceMultisamplePropertiesEXT(
				physicalDevice.toVkType(),
				samples.toVkType(), outputPtr
			)
			return MultisamplePropertiesEXT.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSurfaceCapabilities2KHR(
		surface: SurfaceKHR,
		block: PhysicalDeviceSurfaceInfo2KHRBuilder.() -> Unit
	): SurfaceCapabilities2KHR {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkPhysicalDeviceSurfaceInfo2KHR.callocStack()
			val builder = PhysicalDeviceSurfaceInfo2KHRBuilder(target)
			builder.init(surface)
			builder.apply(block)
			val outputPtr = VkSurfaceCapabilities2KHR.mallocStack()
			val result = vkGetPhysicalDeviceSurfaceCapabilities2KHR(
				physicalDevice.toVkType(),
				target, outputPtr
			)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SurfaceCapabilities2KHR.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getSurfaceFormats2KHR(
		surface: SurfaceKHR,
		block: PhysicalDeviceSurfaceInfo2KHRBuilder.() -> Unit
	): List<SurfaceFormat2KHR> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val target = VkPhysicalDeviceSurfaceInfo2KHR.callocStack()
			val builder = PhysicalDeviceSurfaceInfo2KHRBuilder(target)
			builder.init(surface)
			builder.apply(block)
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetPhysicalDeviceSurfaceFormats2KHR(
				physicalDevice.toVkType(), target,
				outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VkSurfaceFormat2KHR.mallocStack(outputCountPtr[0])
			val result1 = vkGetPhysicalDeviceSurfaceFormats2KHR(
				physicalDevice.toVkType(), target,
				outputCountPtr, outputPtr
			)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountPtr[0]) { SurfaceFormat2KHR.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getDisplayModeProperties2KHR(display: DisplayKHR): List<DisplayModeProperties2KHR> {
		val physicalDevice = this
		MemoryStack.stackPush()
		try {
			val outputCountPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetDisplayModeProperties2KHR(
				physicalDevice.toVkType(),
				display.toVkType(), outputCountPtr, null
			)
			when (result) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result)
			}
			val outputPtr = VkDisplayModeProperties2KHR.mallocStack(outputCountPtr[0])
			val result1 = vkGetDisplayModeProperties2KHR(
				physicalDevice.toVkType(),
				display.toVkType(), outputCountPtr, outputPtr
			)
			when (result1) {
				VK_SUCCESS -> Unit
				VK_INCOMPLETE -> Unit
				else -> handleVkResult(result1)
			}
			return List(outputCountPtr[0]) { DisplayModeProperties2KHR.from(outputPtr[it]) }
		} finally {
			MemoryStack.stackPop()
		}
	}
}
