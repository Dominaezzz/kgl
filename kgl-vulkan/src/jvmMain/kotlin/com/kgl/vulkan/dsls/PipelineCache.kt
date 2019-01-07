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

import kotlinx.io.core.IoBuffer
import org.lwjgl.vulkan.VK11
import org.lwjgl.vulkan.VkPipelineCacheCreateInfo

actual class PipelineCacheCreateInfoBuilder(internal val target: VkPipelineCacheCreateInfo) {
	internal fun init(pInitialData: IoBuffer?) {
		target.sType(VK11.VK_STRUCTURE_TYPE_PIPELINE_CACHE_CREATE_INFO)
		target.pNext(0)
		target.flags(0)
		pInitialData?.readDirect { target.pInitialData(it) }
	}
}

