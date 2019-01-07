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
package com.kgl.vulkan.dsls

import com.kgl.vulkan.enums.*
import com.kgl.vulkan.handles.SurfaceKHR
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import cvulkan.*

actual class PhysicalDeviceImageFormatInfo2Builder(internal val target: VkPhysicalDeviceImageFormatInfo2) {
	actual var format: Format?
		get() = Format.from(target.format)
		set(value) {
			target.format = value.toVkType()
		}

	actual var type: ImageType?
		get() = ImageType.from(target.type)
		set(value) {
			target.type = value.toVkType()
		}

	actual var tiling: ImageTiling?
		get() = ImageTiling.from(target.tiling)
		set(value) {
			target.tiling = value.toVkType()
		}

	actual var usage: VkFlag<ImageUsage>?
		get() = ImageUsage.fromMultiple(target.usage)
		set(value) {
			target.usage = value.toVkType()
		}

	actual var flags: VkFlag<ImageCreate>?
		get() = ImageCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_IMAGE_FORMAT_INFO_2
		target.pNext = null
	}
}

actual class PhysicalDeviceSparseImageFormatInfo2Builder(internal val target: VkPhysicalDeviceSparseImageFormatInfo2) {
	actual var format: Format?
		get() = Format.from(target.format)
		set(value) {
			target.format = value.toVkType()
		}

	actual var type: ImageType?
		get() = ImageType.from(target.type)
		set(value) {
			target.type = value.toVkType()
		}

	actual var samples: SampleCount?
		get() = SampleCount.from(target.samples)
		set(value) {
			target.samples = value.toVkType()
		}

	actual var usage: VkFlag<ImageUsage>?
		get() = ImageUsage.fromMultiple(target.usage)
		set(value) {
			target.usage = value.toVkType()
		}

	actual var tiling: ImageTiling?
		get() = ImageTiling.from(target.tiling)
		set(value) {
			target.tiling = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_SPARSE_IMAGE_FORMAT_INFO_2
		target.pNext = null
	}
}

actual class PhysicalDeviceExternalBufferInfoBuilder(internal val target: VkPhysicalDeviceExternalBufferInfo) {
	actual var flags: VkFlag<BufferCreate>?
		get() = BufferCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var usage: VkFlag<BufferUsage>?
		get() = BufferUsage.fromMultiple(target.usage)
		set(value) {
			target.usage = value.toVkType()
		}

	actual var handleType: ExternalMemoryHandleType?
		get() = ExternalMemoryHandleType.from(target.handleType)
		set(value) {
			target.handleType = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_EXTERNAL_BUFFER_INFO
		target.pNext = null
	}
}

actual class PhysicalDeviceExternalSemaphoreInfoBuilder(internal val target: VkPhysicalDeviceExternalSemaphoreInfo) {
	actual var handleType: ExternalSemaphoreHandleType?
		get() = ExternalSemaphoreHandleType.from(target.handleType)
		set(value) {
			target.handleType = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_EXTERNAL_SEMAPHORE_INFO
		target.pNext = null
	}
}

actual class PhysicalDeviceExternalFenceInfoBuilder(internal val target: VkPhysicalDeviceExternalFenceInfo) {
	actual var handleType: ExternalFenceHandleType?
		get() = ExternalFenceHandleType.from(target.handleType)
		set(value) {
			target.handleType = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_EXTERNAL_FENCE_INFO
		target.pNext = null
	}
}

actual class PhysicalDeviceSurfaceInfo2KHRBuilder(internal val target: VkPhysicalDeviceSurfaceInfo2KHR) {
	internal fun init(surface: SurfaceKHR) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_SURFACE_INFO_2_KHR
		target.pNext = null
		target.surface = surface.toVkType()
	}
}

