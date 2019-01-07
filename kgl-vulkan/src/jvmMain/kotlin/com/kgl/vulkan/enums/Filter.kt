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
import org.lwjgl.vulkan.IMGFilterCubic
import org.lwjgl.vulkan.VK11

actual enum class Filter(override val value: Int) : VkEnum<Filter> {
	NEAREST(VK11.VK_FILTER_NEAREST),

	CUBIC_IMG(IMGFilterCubic.VK_FILTER_CUBIC_IMG),

	LINEAR(VK11.VK_FILTER_LINEAR);

	companion object {
		private val enumLookUpMap: Map<Int, Filter> = enumValues<Filter>().associateBy({
			it.value
		})

		fun from(value: Int): Filter = enumLookUpMap[value]!!
	}
}

