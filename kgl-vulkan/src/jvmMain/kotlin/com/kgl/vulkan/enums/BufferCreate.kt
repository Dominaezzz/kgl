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

actual enum class BufferCreate(override val value: Int) : VkFlag<BufferCreate> {
	SPARSE_BINDING(VK11.VK_BUFFER_CREATE_SPARSE_BINDING_BIT),

	SPARSE_RESIDENCY(VK11.VK_BUFFER_CREATE_SPARSE_RESIDENCY_BIT),

	SPARSE_ALIASED(VK11.VK_BUFFER_CREATE_SPARSE_ALIASED_BIT),

	PROTECTED(VK11.VK_BUFFER_CREATE_PROTECTED_BIT);

	companion object {
		private val enumLookUpMap: Map<Int, BufferCreate> = enumValues<BufferCreate>().associateBy({
			it.value
		})

		fun fromMultiple(value: Int): VkFlag<BufferCreate> = VkFlag(value)

		fun from(value: Int): BufferCreate = enumLookUpMap[value]!!
	}
}

