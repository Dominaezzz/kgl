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
import org.lwjgl.vulkan.VK11

actual enum class ColorComponent(override val value: Int) : VkFlag<ColorComponent> {
	R(VK11.VK_COLOR_COMPONENT_R_BIT),

	G(VK11.VK_COLOR_COMPONENT_G_BIT),

	B(VK11.VK_COLOR_COMPONENT_B_BIT),

	A(VK11.VK_COLOR_COMPONENT_A_BIT);

	companion object {
		private val enumLookUpMap: Map<Int, ColorComponent> =
				enumValues<ColorComponent>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<ColorComponent> = VkFlag(value)

		fun from(value: Int): ColorComponent = enumLookUpMap[value]!!
	}
}

