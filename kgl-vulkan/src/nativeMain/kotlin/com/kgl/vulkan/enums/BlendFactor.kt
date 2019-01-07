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
import cvulkan.*

actual enum class BlendFactor(override val value: VkBlendFactor) : VkEnum<BlendFactor> {
	ZERO(VK_BLEND_FACTOR_ZERO),

	ONE(VK_BLEND_FACTOR_ONE),

	SRC_COLOR(VK_BLEND_FACTOR_SRC_COLOR),

	ONE_MINUS_SRC_COLOR(VK_BLEND_FACTOR_ONE_MINUS_SRC_COLOR),

	DST_COLOR(VK_BLEND_FACTOR_DST_COLOR),

	ONE_MINUS_DST_COLOR(VK_BLEND_FACTOR_ONE_MINUS_DST_COLOR),

	SRC_ALPHA(VK_BLEND_FACTOR_SRC_ALPHA),

	ONE_MINUS_SRC_ALPHA(VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA),

	DST_ALPHA(VK_BLEND_FACTOR_DST_ALPHA),

	ONE_MINUS_DST_ALPHA(VK_BLEND_FACTOR_ONE_MINUS_DST_ALPHA),

	CONSTANT_COLOR(VK_BLEND_FACTOR_CONSTANT_COLOR),

	ONE_MINUS_CONSTANT_COLOR(VK_BLEND_FACTOR_ONE_MINUS_CONSTANT_COLOR),

	CONSTANT_ALPHA(VK_BLEND_FACTOR_CONSTANT_ALPHA),

	ONE_MINUS_CONSTANT_ALPHA(VK_BLEND_FACTOR_ONE_MINUS_CONSTANT_ALPHA),

	SRC_ALPHA_SATURATE(VK_BLEND_FACTOR_SRC_ALPHA_SATURATE),

	SRC1_COLOR(VK_BLEND_FACTOR_SRC1_COLOR),

	ONE_MINUS_SRC1_COLOR(VK_BLEND_FACTOR_ONE_MINUS_SRC1_COLOR),

	SRC1_ALPHA(VK_BLEND_FACTOR_SRC1_ALPHA),

	ONE_MINUS_SRC1_ALPHA(VK_BLEND_FACTOR_ONE_MINUS_SRC1_ALPHA);

	companion object {
		fun from(value: VkBlendFactor): BlendFactor = enumValues<BlendFactor>()[value.toInt()]
	}
}

