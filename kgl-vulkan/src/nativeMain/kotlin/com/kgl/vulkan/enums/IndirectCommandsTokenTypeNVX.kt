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

actual enum class IndirectCommandsTokenTypeNVX(override val value: VkIndirectCommandsTokenTypeNVX) : VkEnum<IndirectCommandsTokenTypeNVX> {
	PIPELINE(VK_INDIRECT_COMMANDS_TOKEN_TYPE_PIPELINE_NVX),

	DESCRIPTOR_SET(VK_INDIRECT_COMMANDS_TOKEN_TYPE_DESCRIPTOR_SET_NVX),

	INDEX_BUFFER(VK_INDIRECT_COMMANDS_TOKEN_TYPE_INDEX_BUFFER_NVX),

	VERTEX_BUFFER(VK_INDIRECT_COMMANDS_TOKEN_TYPE_VERTEX_BUFFER_NVX),

	PUSH_CONSTANT(VK_INDIRECT_COMMANDS_TOKEN_TYPE_PUSH_CONSTANT_NVX),

	DRAW_INDEXED(VK_INDIRECT_COMMANDS_TOKEN_TYPE_DRAW_INDEXED_NVX),

	DRAW(VK_INDIRECT_COMMANDS_TOKEN_TYPE_DRAW_NVX),

	DISPATCH(VK_INDIRECT_COMMANDS_TOKEN_TYPE_DISPATCH_NVX);

	companion object {
		fun from(value: VkIndirectCommandsTokenTypeNVX): IndirectCommandsTokenTypeNVX =
				enumValues<IndirectCommandsTokenTypeNVX>()[value.toInt()]
	}
}

