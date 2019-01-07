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
import org.lwjgl.vulkan.EXTDescriptorIndexing
import org.lwjgl.vulkan.VK11

actual enum class DescriptorPoolCreate(override val value: Int) : VkFlag<DescriptorPoolCreate> {
	FREE_DESCRIPTOR_SET(VK11.VK_DESCRIPTOR_POOL_CREATE_FREE_DESCRIPTOR_SET_BIT),

	UPDATE_AFTER_BIND_EXT(EXTDescriptorIndexing.VK_DESCRIPTOR_POOL_CREATE_UPDATE_AFTER_BIND_BIT_EXT);

	companion object {
		private val enumLookUpMap: Map<Int, DescriptorPoolCreate> =
				enumValues<DescriptorPoolCreate>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<DescriptorPoolCreate> = VkFlag(value)

		fun from(value: Int): DescriptorPoolCreate = enumLookUpMap[value]!!
	}
}

