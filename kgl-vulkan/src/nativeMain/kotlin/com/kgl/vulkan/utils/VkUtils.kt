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

fun handleVkResult(resultCode: Int): Nothing {
	when (resultCode) {
		VK_ERROR_OUT_OF_HOST_MEMORY -> throw OutOfHostMemoryError()
		VK_ERROR_OUT_OF_DEVICE_MEMORY -> throw OutOfDeviceMemoryError()
		VK_ERROR_INITIALIZATION_FAILED -> throw InitializationFailedError()
		VK_ERROR_DEVICE_LOST -> throw DeviceLostError()
		VK_ERROR_MEMORY_MAP_FAILED -> throw MemoryMapFailedError()
		VK_ERROR_LAYER_NOT_PRESENT -> throw LayerNotPresentError()
		VK_ERROR_EXTENSION_NOT_PRESENT -> throw ExtensionNotPresentError()
		VK_ERROR_FEATURE_NOT_PRESENT -> throw FeatureNotPresentError()
		VK_ERROR_INCOMPATIBLE_DRIVER -> throw IncompatibleDriverError()
		VK_ERROR_TOO_MANY_OBJECTS -> throw TooManyObjectsError()
		VK_ERROR_FORMAT_NOT_SUPPORTED -> throw FormatNotSupportedError()
		VK_ERROR_FRAGMENTED_POOL -> throw FragmentedPoolError()
		VK_ERROR_SURFACE_LOST_KHR -> throw SurfaceLostKhrError()
		VK_ERROR_NATIVE_WINDOW_IN_USE_KHR -> throw NativeWindowInUseKhrError()
		VK_ERROR_OUT_OF_DATE_KHR -> throw OutOfDateKhrError()
		VK_ERROR_INCOMPATIBLE_DISPLAY_KHR -> throw IncompatibleDisplayKhrError()
		VK_ERROR_VALIDATION_FAILED_EXT -> throw ValidationFailedExtError()
		VK_ERROR_INVALID_SHADER_NV -> throw InvalidShaderNvError()
		VK_ERROR_OUT_OF_POOL_MEMORY -> throw OutOfPoolMemoryError()
		VK_ERROR_INVALID_EXTERNAL_HANDLE -> throw InvalidExternalHandleError()
		VK_ERROR_NOT_PERMITTED_EXT -> throw NotPermittedError()
		else -> throw UnknownVulkanError(resultCode)
	}
}
