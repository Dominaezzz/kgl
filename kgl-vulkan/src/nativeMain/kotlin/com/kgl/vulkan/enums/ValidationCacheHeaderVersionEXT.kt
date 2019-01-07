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
import cvulkan.VK_VALIDATION_CACHE_HEADER_VERSION_ONE_EXT
import cvulkan.VkValidationCacheHeaderVersionEXT

actual enum class ValidationCacheHeaderVersionEXT(override val value: VkValidationCacheHeaderVersionEXT) : VkEnum<ValidationCacheHeaderVersionEXT> {
	ONE(VK_VALIDATION_CACHE_HEADER_VERSION_ONE_EXT);

	companion object {
		private val enumLookUpMap: Map<UInt, ValidationCacheHeaderVersionEXT> =
				enumValues<ValidationCacheHeaderVersionEXT>().associateBy({ it.value })

		fun from(value: VkValidationCacheHeaderVersionEXT): ValidationCacheHeaderVersionEXT =
				enumLookUpMap[value]!!
	}
}

