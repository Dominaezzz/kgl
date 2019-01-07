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
package com.kgl.vulkan.dsls

import cvulkan.VkValidationCacheCreateInfoEXT
import kotlinx.io.core.IoBuffer

actual class ValidationCacheCreateInfoEXTBuilder(internal val target: VkValidationCacheCreateInfoEXT) {
	internal fun init(pInitialData: IoBuffer?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_VALIDATION_CACHE_CREATE_INFO_EXT
		target.pNext = null
		target.flags = 0U
		pInitialData?.readDirect {
			target.pInitialData = it
			target.initialDataSize = pInitialData.readRemaining.toULong()
			pInitialData.readRemaining
		}
	}
}

