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

expect enum class DebugReportObjectTypeEXT : VkEnum<DebugReportObjectTypeEXT> {
	UNKNOWN,

	SAMPLER_YCBCR_CONVERSION_EXT,

	DESCRIPTOR_UPDATE_TEMPLATE_EXT,

	ACCELERATION_STRUCTURE_NV_EXT,

	INSTANCE,

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

	COMMAND_POOL,

	SURFACE_KHR,

	SWAPCHAIN_KHR,

	DEBUG_REPORT_CALLBACK_EXT,

	DISPLAY_KHR,

	DISPLAY_MODE_KHR,

	OBJECT_TABLE_NVX,

	INDIRECT_COMMANDS_LAYOUT_NVX,

	VALIDATION_CACHE_EXT
}

