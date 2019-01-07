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

actual enum class CullMode(override val value: Int) : VkFlag<CullMode> {
	NONE(VK11.VK_CULL_MODE_NONE),

	FRONT(VK11.VK_CULL_MODE_FRONT_BIT),

	BACK(VK11.VK_CULL_MODE_BACK_BIT);

	companion object {
		private val enumLookUpMap: Map<Int, CullMode> = enumValues<CullMode>().associateBy({
			it.value
		})

		fun fromMultiple(value: Int): VkFlag<CullMode> = VkFlag(value)

		fun from(value: Int): CullMode = enumLookUpMap[value]!!
	}
}

