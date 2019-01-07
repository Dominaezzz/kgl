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
import cvulkan.VK_EXTERNAL_MEMORY_FEATURE_DEDICATED_ONLY_BIT_NV
import cvulkan.VK_EXTERNAL_MEMORY_FEATURE_EXPORTABLE_BIT_NV
import cvulkan.VK_EXTERNAL_MEMORY_FEATURE_IMPORTABLE_BIT_NV
import cvulkan.VkExternalMemoryFeatureFlagBitsNV

actual enum class ExternalMemoryFeatureNV(override val value: VkExternalMemoryFeatureFlagBitsNV) : VkFlag<ExternalMemoryFeatureNV> {
	DEDICATED_ONLY(VK_EXTERNAL_MEMORY_FEATURE_DEDICATED_ONLY_BIT_NV),

	EXPORTABLE(VK_EXTERNAL_MEMORY_FEATURE_EXPORTABLE_BIT_NV),

	IMPORTABLE(VK_EXTERNAL_MEMORY_FEATURE_IMPORTABLE_BIT_NV);

	companion object {
		private val enumLookUpMap: Map<UInt, ExternalMemoryFeatureNV> =
				enumValues<ExternalMemoryFeatureNV>().associateBy({ it.value })

		fun fromMultiple(value: VkExternalMemoryFeatureFlagBitsNV): VkFlag<ExternalMemoryFeatureNV> = VkFlag(value)

		fun from(value: VkExternalMemoryFeatureFlagBitsNV): ExternalMemoryFeatureNV =
				enumLookUpMap[value]!!
	}
}

