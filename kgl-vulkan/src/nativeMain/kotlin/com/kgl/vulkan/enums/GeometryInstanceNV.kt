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

actual enum class GeometryInstanceNV(override val value: VkGeometryInstanceFlagBitsNV) : VkFlag<GeometryInstanceNV> {
	TRIANGLE_CULL_DISABLE(VK_GEOMETRY_INSTANCE_TRIANGLE_CULL_DISABLE_BIT_NV),

	TRIANGLE_FRONT_COUNTERCLOCKWISE(VK_GEOMETRY_INSTANCE_TRIANGLE_FRONT_COUNTERCLOCKWISE_BIT_NV),

	FORCE_OPAQUE(VK_GEOMETRY_INSTANCE_FORCE_OPAQUE_BIT_NV),

	FORCE_NO_OPAQUE(VK_GEOMETRY_INSTANCE_FORCE_NO_OPAQUE_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<UInt, GeometryInstanceNV> =
				enumValues<GeometryInstanceNV>().associateBy({ it.value })

		fun fromMultiple(value: VkGeometryInstanceFlagBitsNV): VkFlag<GeometryInstanceNV> =
				VkFlag(value)

		fun from(value: VkGeometryInstanceFlagBitsNV): GeometryInstanceNV =
				enumLookUpMap[value]!!
	}
}

