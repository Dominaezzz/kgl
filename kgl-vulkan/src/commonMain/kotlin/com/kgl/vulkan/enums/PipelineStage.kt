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

expect enum class PipelineStage : VkFlag<PipelineStage> {
	TOP_OF_PIPE,

	DRAW_INDIRECT,

	VERTEX_INPUT,

	VERTEX_SHADER,

	TESSELLATION_CONTROL_SHADER,

	TESSELLATION_EVALUATION_SHADER,

	GEOMETRY_SHADER,

	FRAGMENT_SHADER,

	EARLY_FRAGMENT_TESTS,

	LATE_FRAGMENT_TESTS,

	COLOR_ATTACHMENT_OUTPUT,

	COMPUTE_SHADER,

	TRANSFER,

	BOTTOM_OF_PIPE,

	HOST,

	ALL_GRAPHICS,

	ALL_COMMANDS,

	COMMAND_PROCESS_NVX,

	CONDITIONAL_RENDERING_EXT,

	TASK_SHADER_NV,

	MESH_SHADER_NV,

	RAY_TRACING_SHADER_NV,

	SHADING_RATE_IMAGE_NV,

	TRANSFORM_FEEDBACK_EXT,

	ACCELERATION_STRUCTURE_BUILD_NV
}

