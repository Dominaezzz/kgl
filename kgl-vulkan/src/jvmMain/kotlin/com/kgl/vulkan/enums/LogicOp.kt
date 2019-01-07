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
import org.lwjgl.vulkan.VK11

actual enum class LogicOp(override val value: Int) : VkEnum<LogicOp> {
	CLEAR(VK11.VK_LOGIC_OP_CLEAR),

	AND(VK11.VK_LOGIC_OP_AND),

	AND_REVERSE(VK11.VK_LOGIC_OP_AND_REVERSE),

	COPY(VK11.VK_LOGIC_OP_COPY),

	AND_INVERTED(VK11.VK_LOGIC_OP_AND_INVERTED),

	NO_OP(VK11.VK_LOGIC_OP_NO_OP),

	XOR(VK11.VK_LOGIC_OP_XOR),

	OR(VK11.VK_LOGIC_OP_OR),

	NOR(VK11.VK_LOGIC_OP_NOR),

	EQUIVALENT(VK11.VK_LOGIC_OP_EQUIVALENT),

	INVERT(VK11.VK_LOGIC_OP_INVERT),

	OR_REVERSE(VK11.VK_LOGIC_OP_OR_REVERSE),

	COPY_INVERTED(VK11.VK_LOGIC_OP_COPY_INVERTED),

	OR_INVERTED(VK11.VK_LOGIC_OP_OR_INVERTED),

	NAND(VK11.VK_LOGIC_OP_NAND),

	SET(VK11.VK_LOGIC_OP_SET);

	companion object {
		fun from(value: Int): LogicOp = enumValues<LogicOp>()[value]
	}
}

