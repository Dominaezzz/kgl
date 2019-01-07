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

actual enum class FrontFace(override val value: Int) : VkEnum<FrontFace> {
	COUNTER_CLOCKWISE(VK11.VK_FRONT_FACE_COUNTER_CLOCKWISE),

	CLOCKWISE(VK11.VK_FRONT_FACE_CLOCKWISE);

	companion object {
		fun from(value: Int): FrontFace = enumValues<FrontFace>()[value]
	}
}

