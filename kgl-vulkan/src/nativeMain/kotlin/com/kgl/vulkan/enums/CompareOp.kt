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

actual enum class CompareOp(override val value: VkCompareOp) : VkEnum<CompareOp> {
	NEVER(VK_COMPARE_OP_NEVER),

	LESS(VK_COMPARE_OP_LESS),

	EQUAL(VK_COMPARE_OP_EQUAL),

	LESS_OR_EQUAL(VK_COMPARE_OP_LESS_OR_EQUAL),

	GREATER(VK_COMPARE_OP_GREATER),

	NOT_EQUAL(VK_COMPARE_OP_NOT_EQUAL),

	GREATER_OR_EQUAL(VK_COMPARE_OP_GREATER_OR_EQUAL),

	ALWAYS(VK_COMPARE_OP_ALWAYS);

	companion object {
		fun from(value: VkCompareOp): CompareOp = enumValues<CompareOp>()[value.toInt()]
	}
}

