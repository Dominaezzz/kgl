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

expect enum class DescriptorType : VkEnum<DescriptorType> {
	SAMPLER,

	INLINE_UNIFORM_BLOCK_EXT,

	ACCELERATION_STRUCTURE_NV,

	COMBINED_IMAGE_SAMPLER,

	SAMPLED_IMAGE,

	STORAGE_IMAGE,

	UNIFORM_TEXEL_BUFFER,

	STORAGE_TEXEL_BUFFER,

	UNIFORM_BUFFER,

	STORAGE_BUFFER,

	UNIFORM_BUFFER_DYNAMIC,

	STORAGE_BUFFER_DYNAMIC,

	INPUT_ATTACHMENT
}

