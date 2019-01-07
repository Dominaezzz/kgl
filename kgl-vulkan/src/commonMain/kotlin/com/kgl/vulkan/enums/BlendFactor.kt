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

expect enum class BlendFactor : VkEnum<BlendFactor> {
	ZERO,

	ONE,

	SRC_COLOR,

	ONE_MINUS_SRC_COLOR,

	DST_COLOR,

	ONE_MINUS_DST_COLOR,

	SRC_ALPHA,

	ONE_MINUS_SRC_ALPHA,

	DST_ALPHA,

	ONE_MINUS_DST_ALPHA,

	CONSTANT_COLOR,

	ONE_MINUS_CONSTANT_COLOR,

	CONSTANT_ALPHA,

	ONE_MINUS_CONSTANT_ALPHA,

	SRC_ALPHA_SATURATE,

	SRC1_COLOR,

	ONE_MINUS_SRC1_COLOR,

	SRC1_ALPHA,

	ONE_MINUS_SRC1_ALPHA
}

