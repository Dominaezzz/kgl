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
import org.lwjgl.vulkan.EXTConditionalRendering
import org.lwjgl.vulkan.EXTTransformFeedback
import org.lwjgl.vulkan.NVRayTracing
import org.lwjgl.vulkan.VK11

actual enum class BufferUsage(override val value: Int) : VkFlag<BufferUsage> {
	TRANSFER_SRC(VK11.VK_BUFFER_USAGE_TRANSFER_SRC_BIT),

	TRANSFER_DST(VK11.VK_BUFFER_USAGE_TRANSFER_DST_BIT),

	UNIFORM_TEXEL_BUFFER(VK11.VK_BUFFER_USAGE_UNIFORM_TEXEL_BUFFER_BIT),

	STORAGE_TEXEL_BUFFER(VK11.VK_BUFFER_USAGE_STORAGE_TEXEL_BUFFER_BIT),

	UNIFORM_BUFFER(VK11.VK_BUFFER_USAGE_UNIFORM_BUFFER_BIT),

	STORAGE_BUFFER(VK11.VK_BUFFER_USAGE_STORAGE_BUFFER_BIT),

	INDEX_BUFFER(VK11.VK_BUFFER_USAGE_INDEX_BUFFER_BIT),

	VERTEX_BUFFER(VK11.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT),

	INDIRECT_BUFFER(VK11.VK_BUFFER_USAGE_INDIRECT_BUFFER_BIT),

	CONDITIONAL_RENDERING_EXT(EXTConditionalRendering.VK_BUFFER_USAGE_CONDITIONAL_RENDERING_BIT_EXT),

	RAY_TRACING_NV(NVRayTracing.VK_BUFFER_USAGE_RAY_TRACING_BIT_NV),

	TRANSFORM_FEEDBACK_BUFFER_EXT(EXTTransformFeedback.VK_BUFFER_USAGE_TRANSFORM_FEEDBACK_BUFFER_BIT_EXT),

	TRANSFORM_FEEDBACK_COUNTER_BUFFER_EXT(EXTTransformFeedback.VK_BUFFER_USAGE_TRANSFORM_FEEDBACK_COUNTER_BUFFER_BIT_EXT);

	companion object {
		private val enumLookUpMap: Map<Int, BufferUsage> = enumValues<BufferUsage>().associateBy({
			it.value
		})

		fun fromMultiple(value: Int): VkFlag<BufferUsage> = VkFlag(value)

		fun from(value: Int): BufferUsage = enumLookUpMap[value]!!
	}
}

