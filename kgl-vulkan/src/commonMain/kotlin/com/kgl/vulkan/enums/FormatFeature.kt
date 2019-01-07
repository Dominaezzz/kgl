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

expect enum class FormatFeature : VkFlag<FormatFeature> {
	SAMPLED_IMAGE,

	STORAGE_IMAGE,

	STORAGE_IMAGE_ATOMIC,

	UNIFORM_TEXEL_BUFFER,

	STORAGE_TEXEL_BUFFER,

	STORAGE_TEXEL_BUFFER_ATOMIC,

	VERTEX_BUFFER,

	COLOR_ATTACHMENT,

	COLOR_ATTACHMENT_BLEND,

	DEPTH_STENCIL_ATTACHMENT,

	BLIT_SRC,

	BLIT_DST,

	SAMPLED_IMAGE_FILTER_LINEAR,

	SAMPLED_IMAGE_FILTER_CUBIC_IMG,

	TRANSFER_SRC,

	TRANSFER_DST,

	SAMPLED_IMAGE_FILTER_MINMAX_EXT,

	MIDPOINT_CHROMA_SAMPLES,

	SAMPLED_IMAGE_YCBCR_CONVERSION_LINEAR_FILTER,

	SAMPLED_IMAGE_YCBCR_CONVERSION_SEPARATE_RECONSTRUCTION_FILTER,

	SAMPLED_IMAGE_YCBCR_CONVERSION_CHROMA_RECONSTRUCTION_EXPLICIT,

	SAMPLED_IMAGE_YCBCR_CONVERSION_CHROMA_RECONSTRUCTION_EXPLICIT_FORCEABLE,

	DISJOINT,

	COSITED_CHROMA_SAMPLES
}

