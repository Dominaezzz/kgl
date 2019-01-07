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

actual enum class QueueFlag(override val value: Int) : VkFlag<QueueFlag> {
	GRAPHICS(VK11.VK_QUEUE_GRAPHICS_BIT),

	COMPUTE(VK11.VK_QUEUE_COMPUTE_BIT),

	TRANSFER(VK11.VK_QUEUE_TRANSFER_BIT),

	SPARSE_BINDING(VK11.VK_QUEUE_SPARSE_BINDING_BIT),

	PROTECTED(VK11.VK_QUEUE_PROTECTED_BIT);

	companion object {
		private val enumLookUpMap: Map<Int, QueueFlag> = enumValues<QueueFlag>().associateBy({
			it.value
		})

		fun fromMultiple(value: Int): VkFlag<QueueFlag> = VkFlag(value)

		fun from(value: Int): QueueFlag = enumLookUpMap[value]!!
	}
}

