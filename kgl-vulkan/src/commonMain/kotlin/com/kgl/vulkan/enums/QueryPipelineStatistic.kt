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

expect enum class QueryPipelineStatistic : VkFlag<QueryPipelineStatistic> {
	INPUT_ASSEMBLY_VERTICES,

	INPUT_ASSEMBLY_PRIMITIVES,

	VERTEX_SHADER_INVOCATIONS,

	GEOMETRY_SHADER_INVOCATIONS,

	GEOMETRY_SHADER_PRIMITIVES,

	CLIPPING_INVOCATIONS,

	CLIPPING_PRIMITIVES,

	FRAGMENT_SHADER_INVOCATIONS,

	TESSELLATION_CONTROL_SHADER_PATCHES,

	TESSELLATION_EVALUATION_SHADER_INVOCATIONS,

	COMPUTE_SHADER_INVOCATIONS
}

