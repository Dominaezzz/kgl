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

actual enum class MemoryProperty(override val value: Int) : VkFlag<MemoryProperty> {
	DEVICE_LOCAL(VK11.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT),

	HOST_VISIBLE(VK11.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT),

	HOST_COHERENT(VK11.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT),

	HOST_CACHED(VK11.VK_MEMORY_PROPERTY_HOST_CACHED_BIT),

	LAZILY_ALLOCATED(VK11.VK_MEMORY_PROPERTY_LAZILY_ALLOCATED_BIT),

	PROTECTED(VK11.VK_MEMORY_PROPERTY_PROTECTED_BIT);

	companion object {
		private val enumLookUpMap: Map<Int, MemoryProperty> =
				enumValues<MemoryProperty>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<MemoryProperty> = VkFlag(value)

		fun from(value: Int): MemoryProperty = enumLookUpMap[value]!!
	}
}

