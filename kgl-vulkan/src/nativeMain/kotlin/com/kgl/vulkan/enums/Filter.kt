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
import cvulkan.VK_FILTER_CUBIC_IMG
import cvulkan.VK_FILTER_LINEAR
import cvulkan.VK_FILTER_NEAREST
import cvulkan.VkFilter

actual enum class Filter(override val value: VkFilter) : VkEnum<Filter> {
	NEAREST(VK_FILTER_NEAREST),

	CUBIC_IMG(VK_FILTER_CUBIC_IMG),

	LINEAR(VK_FILTER_LINEAR);

	companion object {
		private val enumLookUpMap: Map<UInt, Filter> = enumValues<Filter>().associateBy({
			it.value
		})

		fun from(value: VkFilter): Filter = enumLookUpMap[value]!!
	}
}

