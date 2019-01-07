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

actual enum class BorderColor(override val value: Int) : VkEnum<BorderColor> {
	FLOAT_TRANSPARENT_BLACK(VK11.VK_BORDER_COLOR_FLOAT_TRANSPARENT_BLACK),

	INT_TRANSPARENT_BLACK(VK11.VK_BORDER_COLOR_INT_TRANSPARENT_BLACK),

	FLOAT_OPAQUE_BLACK(VK11.VK_BORDER_COLOR_FLOAT_OPAQUE_BLACK),

	INT_OPAQUE_BLACK(VK11.VK_BORDER_COLOR_INT_OPAQUE_BLACK),

	FLOAT_OPAQUE_WHITE(VK11.VK_BORDER_COLOR_FLOAT_OPAQUE_WHITE),

	INT_OPAQUE_WHITE(VK11.VK_BORDER_COLOR_INT_OPAQUE_WHITE);

	companion object {
		fun from(value: Int): BorderColor = enumValues<BorderColor>()[value]
	}
}

