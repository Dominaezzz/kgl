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
package com.kgl.vulkan.structs

import com.kgl.vulkan.enums.CompositeAlphaKHR
import com.kgl.vulkan.enums.ImageUsage
import com.kgl.vulkan.enums.SurfaceTransformKHR
import com.kgl.vulkan.utils.VkFlag

data class SurfaceCapabilitiesKHR(
		val minImageCount: UInt,
		val maxImageCount: UInt,
		val currentExtent: Extent2D,
		val minImageExtent: Extent2D,
		val maxImageExtent: Extent2D,
		val maxImageArrayLayers: UInt,
		val supportedTransforms: VkFlag<SurfaceTransformKHR>?,
		val currentTransform: SurfaceTransformKHR,
		val supportedCompositeAlpha: VkFlag<CompositeAlphaKHR>?,
		val supportedUsageFlags: VkFlag<ImageUsage>?
) {
	companion object
}

