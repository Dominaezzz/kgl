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

actual enum class LogicOp(override val value: VkLogicOp) : VkEnum<LogicOp> {
	CLEAR(VK_LOGIC_OP_CLEAR),

	AND(VK_LOGIC_OP_AND),

	AND_REVERSE(VK_LOGIC_OP_AND_REVERSE),

	COPY(VK_LOGIC_OP_COPY),

	AND_INVERTED(VK_LOGIC_OP_AND_INVERTED),

	NO_OP(VK_LOGIC_OP_NO_OP),

	XOR(VK_LOGIC_OP_XOR),

	OR(VK_LOGIC_OP_OR),

	NOR(VK_LOGIC_OP_NOR),

	EQUIVALENT(VK_LOGIC_OP_EQUIVALENT),

	INVERT(VK_LOGIC_OP_INVERT),

	OR_REVERSE(VK_LOGIC_OP_OR_REVERSE),

	COPY_INVERTED(VK_LOGIC_OP_COPY_INVERTED),

	OR_INVERTED(VK_LOGIC_OP_OR_INVERTED),

	NAND(VK_LOGIC_OP_NAND),

	SET(VK_LOGIC_OP_SET);

	companion object {
		fun from(value: VkLogicOp): LogicOp = enumValues<LogicOp>()[value.toInt()]
	}
}

