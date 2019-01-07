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
import org.lwjgl.vulkan.KHRSurface

actual enum class CompositeAlphaKHR(override val value: Int) : VkFlag<CompositeAlphaKHR> {
	OPAQUE(KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR),

	PRE_MULTIPLIED(KHRSurface.VK_COMPOSITE_ALPHA_PRE_MULTIPLIED_BIT_KHR),

	POST_MULTIPLIED(KHRSurface.VK_COMPOSITE_ALPHA_POST_MULTIPLIED_BIT_KHR),

	INHERIT(KHRSurface.VK_COMPOSITE_ALPHA_INHERIT_BIT_KHR);

	companion object {
		private val enumLookUpMap: Map<Int, CompositeAlphaKHR> =
				enumValues<CompositeAlphaKHR>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<CompositeAlphaKHR> = VkFlag(value)

		fun from(value: Int): CompositeAlphaKHR = enumLookUpMap[value]!!
	}
}

