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
import cvulkan.*

actual enum class QueueGlobalPriorityEXT(override val value: VkQueueGlobalPriorityEXT) : VkEnum<QueueGlobalPriorityEXT> {
	LOW(VK_QUEUE_GLOBAL_PRIORITY_LOW_EXT),

	MEDIUM(VK_QUEUE_GLOBAL_PRIORITY_MEDIUM_EXT),

	HIGH(VK_QUEUE_GLOBAL_PRIORITY_HIGH_EXT),

	REALTIME(VK_QUEUE_GLOBAL_PRIORITY_REALTIME_EXT);

	companion object {
		private val enumLookUpMap: Map<UInt, QueueGlobalPriorityEXT> =
				enumValues<QueueGlobalPriorityEXT>().associateBy({ it.value })

		fun from(value: VkQueueGlobalPriorityEXT): QueueGlobalPriorityEXT =
				enumLookUpMap[value]!!
	}
}

