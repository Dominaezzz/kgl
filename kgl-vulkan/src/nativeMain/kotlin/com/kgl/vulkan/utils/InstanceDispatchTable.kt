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
package com.kgl.vulkan.utils

import cvulkan.*
import kotlinx.cinterop.reinterpret

internal class InstanceDispatchTable(getProcAddr: (String) -> PFN_vkVoidFunction?) {
	val vkGetDeviceProcAddr: PFN_vkGetDeviceProcAddr = getProcAddr("vkGetDeviceProcAddr")!!.reinterpret()

	val vkDestroyInstance: PFN_vkDestroyInstance = getProcAddr("vkDestroyInstance")!!.reinterpret()
	val vkEnumeratePhysicalDevices: PFN_vkEnumeratePhysicalDevices = getProcAddr("vkEnumeratePhysicalDevices")!!.reinterpret()
	val vkGetPhysicalDeviceProperties: PFN_vkGetPhysicalDeviceProperties = getProcAddr("vkGetPhysicalDeviceProperties")!!.reinterpret()
	val vkGetPhysicalDeviceQueueFamilyProperties: PFN_vkGetPhysicalDeviceQueueFamilyProperties = getProcAddr("vkGetPhysicalDeviceQueueFamilyProperties")!!.reinterpret()
	val vkGetPhysicalDeviceMemoryProperties: PFN_vkGetPhysicalDeviceMemoryProperties = getProcAddr("vkGetPhysicalDeviceMemoryProperties")!!.reinterpret()
	val vkGetPhysicalDeviceFeatures: PFN_vkGetPhysicalDeviceFeatures = getProcAddr("vkGetPhysicalDeviceFeatures")!!.reinterpret()
	val vkGetPhysicalDeviceFormatProperties: PFN_vkGetPhysicalDeviceFormatProperties = getProcAddr("vkGetPhysicalDeviceFormatProperties")!!.reinterpret()
	val vkGetPhysicalDeviceImageFormatProperties: PFN_vkGetPhysicalDeviceImageFormatProperties = getProcAddr("vkGetPhysicalDeviceImageFormatProperties")!!.reinterpret()
	val vkCreateDevice: PFN_vkCreateDevice = getProcAddr("vkCreateDevice")!!.reinterpret()
	val vkEnumerateDeviceLayerProperties: PFN_vkEnumerateDeviceLayerProperties = getProcAddr("vkEnumerateDeviceLayerProperties")!!.reinterpret()
	val vkEnumerateDeviceExtensionProperties: PFN_vkEnumerateDeviceExtensionProperties = getProcAddr("vkEnumerateDeviceExtensionProperties")!!.reinterpret()
	val vkGetPhysicalDeviceSparseImageFormatProperties: PFN_vkGetPhysicalDeviceSparseImageFormatProperties = getProcAddr("vkGetPhysicalDeviceSparseImageFormatProperties")!!.reinterpret()
	// val vkCreateAndroidSurfaceKHR: PFN_vkCreateAndroidSurfaceKHR? = getProcAddr("vkCreateAndroidSurfaceKHR")?.reinterpret()
	val vkGetPhysicalDeviceDisplayPropertiesKHR: PFN_vkGetPhysicalDeviceDisplayPropertiesKHR? = getProcAddr("vkGetPhysicalDeviceDisplayPropertiesKHR")?.reinterpret()
	val vkGetPhysicalDeviceDisplayPlanePropertiesKHR: PFN_vkGetPhysicalDeviceDisplayPlanePropertiesKHR? = getProcAddr("vkGetPhysicalDeviceDisplayPlanePropertiesKHR")?.reinterpret()
	val vkGetDisplayPlaneSupportedDisplaysKHR: PFN_vkGetDisplayPlaneSupportedDisplaysKHR? = getProcAddr("vkGetDisplayPlaneSupportedDisplaysKHR")?.reinterpret()
	val vkGetDisplayModePropertiesKHR: PFN_vkGetDisplayModePropertiesKHR? = getProcAddr("vkGetDisplayModePropertiesKHR")?.reinterpret()
	val vkCreateDisplayModeKHR: PFN_vkCreateDisplayModeKHR? = getProcAddr("vkCreateDisplayModeKHR")?.reinterpret()
	val vkGetDisplayPlaneCapabilitiesKHR: PFN_vkGetDisplayPlaneCapabilitiesKHR? = getProcAddr("vkGetDisplayPlaneCapabilitiesKHR")?.reinterpret()
	val vkCreateDisplayPlaneSurfaceKHR: PFN_vkCreateDisplayPlaneSurfaceKHR? = getProcAddr("vkCreateDisplayPlaneSurfaceKHR")?.reinterpret()
	val vkDestroySurfaceKHR: PFN_vkDestroySurfaceKHR? = getProcAddr("vkDestroySurfaceKHR")?.reinterpret()
	val vkGetPhysicalDeviceSurfaceSupportKHR: PFN_vkGetPhysicalDeviceSurfaceSupportKHR? = getProcAddr("vkGetPhysicalDeviceSurfaceSupportKHR")?.reinterpret()
	val vkGetPhysicalDeviceSurfaceCapabilitiesKHR: PFN_vkGetPhysicalDeviceSurfaceCapabilitiesKHR? = getProcAddr("vkGetPhysicalDeviceSurfaceCapabilitiesKHR")?.reinterpret()
	val vkGetPhysicalDeviceSurfaceFormatsKHR: PFN_vkGetPhysicalDeviceSurfaceFormatsKHR? = getProcAddr("vkGetPhysicalDeviceSurfaceFormatsKHR")?.reinterpret()
	val vkGetPhysicalDeviceSurfacePresentModesKHR: PFN_vkGetPhysicalDeviceSurfacePresentModesKHR? = getProcAddr("vkGetPhysicalDeviceSurfacePresentModesKHR")?.reinterpret()
	// val vkCreateViSurfaceNN: PFN_vkCreateViSurfaceNN? = getProcAddr("vkCreateViSurfaceNN")?.reinterpret()
	// val vkCreateWaylandSurfaceKHR: PFN_vkCreateWaylandSurfaceKHR? = getProcAddr("vkCreateWaylandSurfaceKHR")?.reinterpret()
	// val vkGetPhysicalDeviceWaylandPresentationSupportKHR: PFN_vkGetPhysicalDeviceWaylandPresentationSupportKHR? = getProcAddr("vkGetPhysicalDeviceWaylandPresentationSupportKHR")?.reinterpret()
	// val vkCreateWin32SurfaceKHR: PFN_vkCreateWin32SurfaceKHR? = getProcAddr("vkCreateWin32SurfaceKHR")?.reinterpret()
	// val vkGetPhysicalDeviceWin32PresentationSupportKHR: PFN_vkGetPhysicalDeviceWin32PresentationSupportKHR? = getProcAddr("vkGetPhysicalDeviceWin32PresentationSupportKHR")?.reinterpret()
	// val vkCreateXlibSurfaceKHR: PFN_vkCreateXlibSurfaceKHR? = getProcAddr("vkCreateXlibSurfaceKHR")?.reinterpret()
	// val vkGetPhysicalDeviceXlibPresentationSupportKHR: PFN_vkGetPhysicalDeviceXlibPresentationSupportKHR? = getProcAddr("vkGetPhysicalDeviceXlibPresentationSupportKHR")?.reinterpret()
	// val vkCreateXcbSurfaceKHR: PFN_vkCreateXcbSurfaceKHR? = getProcAddr("vkCreateXcbSurfaceKHR")?.reinterpret()
	// val vkGetPhysicalDeviceXcbPresentationSupportKHR: PFN_vkGetPhysicalDeviceXcbPresentationSupportKHR? = getProcAddr("vkGetPhysicalDeviceXcbPresentationSupportKHR")?.reinterpret()
	// val vkCreateImagePipeSurfaceFUCHSIA: PFN_vkCreateImagePipeSurfaceFUCHSIA? = getProcAddr("vkCreateImagePipeSurfaceFUCHSIA")?.reinterpret()
	val vkCreateDebugReportCallbackEXT: PFN_vkCreateDebugReportCallbackEXT? = getProcAddr("vkCreateDebugReportCallbackEXT")?.reinterpret()
	val vkDestroyDebugReportCallbackEXT: PFN_vkDestroyDebugReportCallbackEXT? = getProcAddr("vkDestroyDebugReportCallbackEXT")?.reinterpret()
	val vkDebugReportMessageEXT: PFN_vkDebugReportMessageEXT? = getProcAddr("vkDebugReportMessageEXT")?.reinterpret()
	val vkGetPhysicalDeviceExternalImageFormatPropertiesNV: PFN_vkGetPhysicalDeviceExternalImageFormatPropertiesNV? = getProcAddr("vkGetPhysicalDeviceExternalImageFormatPropertiesNV")?.reinterpret()
	val vkGetPhysicalDeviceGeneratedCommandsPropertiesNVX: PFN_vkGetPhysicalDeviceGeneratedCommandsPropertiesNVX? = getProcAddr("vkGetPhysicalDeviceGeneratedCommandsPropertiesNVX")?.reinterpret()
	val vkGetPhysicalDeviceFeatures2: PFN_vkGetPhysicalDeviceFeatures2? = getProcAddr("vkGetPhysicalDeviceFeatures2")?.reinterpret()
	val vkGetPhysicalDeviceProperties2: PFN_vkGetPhysicalDeviceProperties2? = getProcAddr("vkGetPhysicalDeviceProperties2")?.reinterpret()
	val vkGetPhysicalDeviceFormatProperties2: PFN_vkGetPhysicalDeviceFormatProperties2? = getProcAddr("vkGetPhysicalDeviceFormatProperties2")?.reinterpret()
	val vkGetPhysicalDeviceImageFormatProperties2: PFN_vkGetPhysicalDeviceImageFormatProperties2? = getProcAddr("vkGetPhysicalDeviceImageFormatProperties2")?.reinterpret()
	val vkGetPhysicalDeviceQueueFamilyProperties2: PFN_vkGetPhysicalDeviceQueueFamilyProperties2? = getProcAddr("vkGetPhysicalDeviceQueueFamilyProperties2")?.reinterpret()
	val vkGetPhysicalDeviceMemoryProperties2: PFN_vkGetPhysicalDeviceMemoryProperties2? = getProcAddr("vkGetPhysicalDeviceMemoryProperties2")?.reinterpret()
	val vkGetPhysicalDeviceSparseImageFormatProperties2: PFN_vkGetPhysicalDeviceSparseImageFormatProperties2? = getProcAddr("vkGetPhysicalDeviceSparseImageFormatProperties2")?.reinterpret()
	val vkGetPhysicalDeviceExternalBufferProperties: PFN_vkGetPhysicalDeviceExternalBufferProperties? = getProcAddr("vkGetPhysicalDeviceExternalBufferProperties")?.reinterpret()
	val vkGetPhysicalDeviceExternalSemaphoreProperties: PFN_vkGetPhysicalDeviceExternalSemaphoreProperties? = getProcAddr("vkGetPhysicalDeviceExternalSemaphoreProperties")?.reinterpret()
	val vkGetPhysicalDeviceExternalFenceProperties: PFN_vkGetPhysicalDeviceExternalFenceProperties? = getProcAddr("vkGetPhysicalDeviceExternalFenceProperties")?.reinterpret()
	val vkReleaseDisplayEXT: PFN_vkReleaseDisplayEXT? = getProcAddr("vkReleaseDisplayEXT")?.reinterpret()
	// val vkAcquireXlibDisplayEXT: PFN_vkAcquireXlibDisplayEXT? = getProcAddr("vkAcquireXlibDisplayEXT")?.reinterpret()
	// val vkGetRandROutputDisplayEXT: PFN_vkGetRandROutputDisplayEXT? = getProcAddr("vkGetRandROutputDisplayEXT")?.reinterpret()
	val vkGetPhysicalDeviceSurfaceCapabilities2EXT: PFN_vkGetPhysicalDeviceSurfaceCapabilities2EXT? = getProcAddr("vkGetPhysicalDeviceSurfaceCapabilities2EXT")?.reinterpret()
	val vkEnumeratePhysicalDeviceGroups: PFN_vkEnumeratePhysicalDeviceGroups? = getProcAddr("vkEnumeratePhysicalDeviceGroups")?.reinterpret()
	val vkGetPhysicalDevicePresentRectanglesKHR: PFN_vkGetPhysicalDevicePresentRectanglesKHR? = getProcAddr("vkGetPhysicalDevicePresentRectanglesKHR")?.reinterpret()
	// val vkCreateIOSSurfaceMVK: PFN_vkCreateIOSSurfaceMVK? = getProcAddr("vkCreateIOSSurfaceMVK")?.reinterpret()
	// val vkCreateMacOSSurfaceMVK: PFN_vkCreateMacOSSurfaceMVK? = getProcAddr("vkCreateMacOSSurfaceMVK")?.reinterpret()
	val vkGetPhysicalDeviceMultisamplePropertiesEXT: PFN_vkGetPhysicalDeviceMultisamplePropertiesEXT? = getProcAddr("vkGetPhysicalDeviceMultisamplePropertiesEXT")?.reinterpret()
	val vkGetPhysicalDeviceSurfaceCapabilities2KHR: PFN_vkGetPhysicalDeviceSurfaceCapabilities2KHR? = getProcAddr("vkGetPhysicalDeviceSurfaceCapabilities2KHR")?.reinterpret()
	val vkGetPhysicalDeviceSurfaceFormats2KHR: PFN_vkGetPhysicalDeviceSurfaceFormats2KHR? = getProcAddr("vkGetPhysicalDeviceSurfaceFormats2KHR")?.reinterpret()
	val vkGetPhysicalDeviceDisplayProperties2KHR: PFN_vkGetPhysicalDeviceDisplayProperties2KHR? = getProcAddr("vkGetPhysicalDeviceDisplayProperties2KHR")?.reinterpret()
	val vkGetPhysicalDeviceDisplayPlaneProperties2KHR: PFN_vkGetPhysicalDeviceDisplayPlaneProperties2KHR? = getProcAddr("vkGetPhysicalDeviceDisplayPlaneProperties2KHR")?.reinterpret()
	val vkGetDisplayModeProperties2KHR: PFN_vkGetDisplayModeProperties2KHR? = getProcAddr("vkGetDisplayModeProperties2KHR")?.reinterpret()
	val vkGetDisplayPlaneCapabilities2KHR: PFN_vkGetDisplayPlaneCapabilities2KHR? = getProcAddr("vkGetDisplayPlaneCapabilities2KHR")?.reinterpret()
	val vkGetPhysicalDeviceCalibrateableTimeDomainsEXT: PFN_vkGetPhysicalDeviceCalibrateableTimeDomainsEXT? = getProcAddr("vkGetPhysicalDeviceCalibrateableTimeDomainsEXT")?.reinterpret()
	val vkCreateDebugUtilsMessengerEXT: PFN_vkCreateDebugUtilsMessengerEXT? = getProcAddr("vkCreateDebugUtilsMessengerEXT")?.reinterpret()
	val vkDestroyDebugUtilsMessengerEXT: PFN_vkDestroyDebugUtilsMessengerEXT? = getProcAddr("vkDestroyDebugUtilsMessengerEXT")?.reinterpret()
	val vkSubmitDebugUtilsMessageEXT: PFN_vkSubmitDebugUtilsMessageEXT? = getProcAddr("vkSubmitDebugUtilsMessageEXT")?.reinterpret()
}
