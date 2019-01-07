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

sealed class VulkanError : Error()

class OutOfHostMemoryError : VulkanError()
class OutOfDeviceMemoryError : VulkanError()
class InitializationFailedError : VulkanError()
class DeviceLostError : VulkanError()
class MemoryMapFailedError : VulkanError()
class LayerNotPresentError : VulkanError()
class ExtensionNotPresentError : VulkanError()
class FeatureNotPresentError : VulkanError()
class IncompatibleDriverError : VulkanError()
class TooManyObjectsError : VulkanError()
class FormatNotSupportedError : VulkanError()
class FragmentedPoolError : VulkanError()
class SurfaceLostKhrError : VulkanError()
class NativeWindowInUseKhrError : VulkanError()
class OutOfDateKhrError : VulkanError()
class IncompatibleDisplayKhrError : VulkanError()
class ValidationFailedExtError : VulkanError()
class InvalidShaderNvError : VulkanError()
class OutOfPoolMemoryError : VulkanError()
class InvalidExternalHandleError : VulkanError()
class NotPermittedError : VulkanError()
class UnknownVulkanError(val resultCode: Int) : VulkanError()
