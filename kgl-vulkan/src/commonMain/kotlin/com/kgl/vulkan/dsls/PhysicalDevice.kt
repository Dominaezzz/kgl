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
import com.kgl.vulkan.utils.VkFlag

expect class PhysicalDeviceImageFormatInfo2Builder {
	var format: Format?

	var type: ImageType?

	var tiling: ImageTiling?

	var usage: VkFlag<ImageUsage>?

	var flags: VkFlag<ImageCreate>?
}

expect class PhysicalDeviceSparseImageFormatInfo2Builder {
	var format: Format?

	var type: ImageType?

	var samples: SampleCount?

	var usage: VkFlag<ImageUsage>?

	var tiling: ImageTiling?
}

expect class PhysicalDeviceExternalBufferInfoBuilder {
	var flags: VkFlag<BufferCreate>?

	var usage: VkFlag<BufferUsage>?

	var handleType: ExternalMemoryHandleType?
}

expect class PhysicalDeviceExternalSemaphoreInfoBuilder {
	var handleType: ExternalSemaphoreHandleType?
}

expect class PhysicalDeviceExternalFenceInfoBuilder {
	var handleType: ExternalFenceHandleType?
}

expect class PhysicalDeviceSurfaceInfo2KHRBuilder

