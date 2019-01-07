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

expect enum class ObjectType : VkEnum<ObjectType> {
	UNKNOWN,

	SAMPLER_YCBCR_CONVERSION,

	DESCRIPTOR_UPDATE_TEMPLATE,

	SURFACE_KHR,

	SWAPCHAIN_KHR,

	DISPLAY_KHR,

	DEBUG_REPORT_CALLBACK_EXT,

	OBJECT_TABLE_NVX,

	DEBUG_UTILS_MESSENGER_EXT,

	VALIDATION_CACHE_EXT,

	ACCELERATION_STRUCTURE_NV,

	INSTANCE,

	DISPLAY_MODE_KHR,

	INDIRECT_COMMANDS_LAYOUT_NVX,

	PHYSICAL_DEVICE,

	DEVICE,

	QUEUE,

	SEMAPHORE,

	COMMAND_BUFFER,

	FENCE,

	DEVICE_MEMORY,

	BUFFER,

	IMAGE,

	EVENT,

	QUERY_POOL,

	BUFFER_VIEW,

	IMAGE_VIEW,

	SHADER_MODULE,

	PIPELINE_CACHE,

	PIPELINE_LAYOUT,

	RENDER_PASS,

	PIPELINE,

	DESCRIPTOR_SET_LAYOUT,

	SAMPLER,

	DESCRIPTOR_POOL,

	DESCRIPTOR_SET,

	FRAMEBUFFER,

	COMMAND_POOL
}

