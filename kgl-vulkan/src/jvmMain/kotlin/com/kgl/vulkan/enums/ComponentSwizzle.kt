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
import org.lwjgl.vulkan.VK11

actual enum class ComponentSwizzle(override val value: Int) : VkEnum<ComponentSwizzle> {
	IDENTITY(VK11.VK_COMPONENT_SWIZZLE_IDENTITY),

	ZERO(VK11.VK_COMPONENT_SWIZZLE_ZERO),

	ONE(VK11.VK_COMPONENT_SWIZZLE_ONE),

	R(VK11.VK_COMPONENT_SWIZZLE_R),

	G(VK11.VK_COMPONENT_SWIZZLE_G),

	B(VK11.VK_COMPONENT_SWIZZLE_B),

	A(VK11.VK_COMPONENT_SWIZZLE_A);

	companion object {
		fun from(value: Int): ComponentSwizzle = enumValues<ComponentSwizzle>()[value]
	}
}

