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

expect enum class ImageCreate : VkFlag<ImageCreate> {
	SPARSE_BINDING,

	SPARSE_RESIDENCY,

	SPARSE_ALIASED,

	MUTABLE_FORMAT,

	CUBE_COMPATIBLE,

	`2D_ARRAY_COMPATIBLE`,

	SPLIT_INSTANCE_BIND_REGIONS,

	BLOCK_TEXEL_VIEW_COMPATIBLE,

	EXTENDED_USAGE,

	DISJOINT,

	ALIAS,

	PROTECTED,

	SAMPLE_LOCATIONS_COMPATIBLE_DEPTH_EXT,

	CORNER_SAMPLED_NV
}

