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

expect enum class Access : VkFlag<Access> {
	INDIRECT_COMMAND_READ,

	INDEX_READ,

	VERTEX_ATTRIBUTE_READ,

	UNIFORM_READ,

	INPUT_ATTACHMENT_READ,

	SHADER_READ,

	SHADER_WRITE,

	COLOR_ATTACHMENT_READ,

	COLOR_ATTACHMENT_WRITE,

	DEPTH_STENCIL_ATTACHMENT_READ,

	DEPTH_STENCIL_ATTACHMENT_WRITE,

	TRANSFER_READ,

	TRANSFER_WRITE,

	HOST_READ,

	HOST_WRITE,

	MEMORY_READ,

	MEMORY_WRITE,

	COMMAND_PROCESS_READ_NVX,

	COMMAND_PROCESS_WRITE_NVX,

	COLOR_ATTACHMENT_READ_NONCOHERENT_EXT,

	CONDITIONAL_RENDERING_READ_EXT,

	ACCELERATION_STRUCTURE_READ_NV,

	ACCELERATION_STRUCTURE_WRITE_NV,

	SHADING_RATE_IMAGE_READ_NV,

	TRANSFORM_FEEDBACK_WRITE_EXT,

	TRANSFORM_FEEDBACK_COUNTER_READ_EXT,

	TRANSFORM_FEEDBACK_COUNTER_WRITE_EXT
}

