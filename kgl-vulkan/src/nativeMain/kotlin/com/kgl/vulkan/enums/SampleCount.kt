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

actual enum class SampleCount(override val value: VkSampleCountFlagBits) : VkFlag<SampleCount> {
	`1`(VK_SAMPLE_COUNT_1_BIT),

	`2`(VK_SAMPLE_COUNT_2_BIT),

	`4`(VK_SAMPLE_COUNT_4_BIT),

	`8`(VK_SAMPLE_COUNT_8_BIT),

	`16`(VK_SAMPLE_COUNT_16_BIT),

	`32`(VK_SAMPLE_COUNT_32_BIT),

	`64`(VK_SAMPLE_COUNT_64_BIT);

	companion object {
		private val enumLookUpMap: Map<UInt, SampleCount> = enumValues<SampleCount>().associateBy({
			it.value
		})

		fun fromMultiple(value: VkSampleCountFlagBits): VkFlag<SampleCount> = VkFlag(value)

		fun from(value: VkSampleCountFlagBits): SampleCount = enumLookUpMap[value]!!
	}
}

