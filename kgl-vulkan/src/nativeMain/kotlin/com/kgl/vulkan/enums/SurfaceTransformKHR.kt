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
package com.kgl.vulkan.enums

import com.kgl.vulkan.utils.VkFlag
import cvulkan.*

actual enum class SurfaceTransformKHR(override val value: VkSurfaceTransformFlagBitsKHR) : VkFlag<SurfaceTransformKHR> {
	IDENTITY(VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR),

	ROTATE_90(VK_SURFACE_TRANSFORM_ROTATE_90_BIT_KHR),

	ROTATE_180(VK_SURFACE_TRANSFORM_ROTATE_180_BIT_KHR),

	ROTATE_270(VK_SURFACE_TRANSFORM_ROTATE_270_BIT_KHR),

	HORIZONTAL_MIRROR(VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_BIT_KHR),

	HORIZONTAL_MIRROR_ROTATE_90(VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_90_BIT_KHR),

	HORIZONTAL_MIRROR_ROTATE_180(VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_180_BIT_KHR),

	HORIZONTAL_MIRROR_ROTATE_270(VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_270_BIT_KHR),

	INHERIT(VK_SURFACE_TRANSFORM_INHERIT_BIT_KHR);

	companion object {
		private val enumLookUpMap: Map<UInt, SurfaceTransformKHR> =
				enumValues<SurfaceTransformKHR>().associateBy({ it.value })

		fun fromMultiple(value: VkSurfaceTransformFlagBitsKHR): VkFlag<SurfaceTransformKHR> =
				VkFlag(value)

		fun from(value: VkSurfaceTransformFlagBitsKHR): SurfaceTransformKHR =
				enumLookUpMap[value]!!
	}
}

