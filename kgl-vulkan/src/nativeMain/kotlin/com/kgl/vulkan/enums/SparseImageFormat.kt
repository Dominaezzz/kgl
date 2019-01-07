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
import cvulkan.VK_SPARSE_IMAGE_FORMAT_ALIGNED_MIP_SIZE_BIT
import cvulkan.VK_SPARSE_IMAGE_FORMAT_NONSTANDARD_BLOCK_SIZE_BIT
import cvulkan.VK_SPARSE_IMAGE_FORMAT_SINGLE_MIPTAIL_BIT
import cvulkan.VkSparseImageFormatFlagBits

actual enum class SparseImageFormat(override val value: VkSparseImageFormatFlagBits) : VkFlag<SparseImageFormat> {
	SINGLE_MIPTAIL(VK_SPARSE_IMAGE_FORMAT_SINGLE_MIPTAIL_BIT),

	ALIGNED_MIP_SIZE(VK_SPARSE_IMAGE_FORMAT_ALIGNED_MIP_SIZE_BIT),

	NONSTANDARD_BLOCK_SIZE(VK_SPARSE_IMAGE_FORMAT_NONSTANDARD_BLOCK_SIZE_BIT);

	companion object {
		private val enumLookUpMap: Map<UInt, SparseImageFormat> =
				enumValues<SparseImageFormat>().associateBy({ it.value })

		fun fromMultiple(value: VkSparseImageFormatFlagBits): VkFlag<SparseImageFormat> =
				VkFlag(value)

		fun from(value: VkSparseImageFormatFlagBits): SparseImageFormat =
				enumLookUpMap[value]!!
	}
}

