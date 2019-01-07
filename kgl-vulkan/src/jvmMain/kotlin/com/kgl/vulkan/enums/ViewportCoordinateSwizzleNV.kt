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

import com.kgl.vulkan.utils.VkEnum
import org.lwjgl.vulkan.NVViewportSwizzle

actual enum class ViewportCoordinateSwizzleNV(override val value: Int) : VkEnum<ViewportCoordinateSwizzleNV> {
	POSITIVE_X(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_X_NV),

	NEGATIVE_X(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_X_NV),

	POSITIVE_Y(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_Y_NV),

	NEGATIVE_Y(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_Y_NV),

	POSITIVE_Z(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_Z_NV),

	NEGATIVE_Z(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_Z_NV),

	POSITIVE_W(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_POSITIVE_W_NV),

	NEGATIVE_W(NVViewportSwizzle.VK_VIEWPORT_COORDINATE_SWIZZLE_NEGATIVE_W_NV);

	companion object {
		fun from(value: Int): ViewportCoordinateSwizzleNV =
				enumValues<ViewportCoordinateSwizzleNV>()[value]
	}
}

