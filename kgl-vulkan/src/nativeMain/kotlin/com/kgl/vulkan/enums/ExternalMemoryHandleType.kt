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
import cvulkan.*

actual enum class ExternalMemoryHandleType(override val value: VkExternalMemoryHandleTypeFlagBits) : VkFlag<ExternalMemoryHandleType> {
	OPAQUE_FD(VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_FD_BIT),

	OPAQUE_WIN32(VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_WIN32_BIT),

	OPAQUE_WIN32_KMT(VK_EXTERNAL_MEMORY_HANDLE_TYPE_OPAQUE_WIN32_KMT_BIT),

	D3D11_TEXTURE(VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D11_TEXTURE_BIT),

	D3D11_TEXTURE_KMT(VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D11_TEXTURE_KMT_BIT),

	D3D12_HEAP(VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D12_HEAP_BIT),

	D3D12_RESOURCE(VK_EXTERNAL_MEMORY_HANDLE_TYPE_D3D12_RESOURCE_BIT),

	HOST_ALLOCATION_EXT(VK_EXTERNAL_MEMORY_HANDLE_TYPE_HOST_ALLOCATION_BIT_EXT),

	HOST_MAPPED_FOREIGN_MEMORY_EXT(VK_EXTERNAL_MEMORY_HANDLE_TYPE_HOST_MAPPED_FOREIGN_MEMORY_BIT_EXT),

	DMA_BUF_EXT(VK_EXTERNAL_MEMORY_HANDLE_TYPE_DMA_BUF_BIT_EXT),

	ANDROID_HARDWARE_BUFFER_ANDROID(VK_EXTERNAL_MEMORY_HANDLE_TYPE_ANDROID_HARDWARE_BUFFER_BIT_ANDROID);

	companion object {
		private val enumLookUpMap: Map<UInt, ExternalMemoryHandleType> =
				enumValues<ExternalMemoryHandleType>().associateBy({ it.value })

		fun fromMultiple(value: VkExternalMemoryHandleTypeFlagBits): VkFlag<ExternalMemoryHandleType> = VkFlag(value)

		fun from(value: VkExternalMemoryHandleTypeFlagBits): ExternalMemoryHandleType =
				enumLookUpMap[value]!!
	}
}

