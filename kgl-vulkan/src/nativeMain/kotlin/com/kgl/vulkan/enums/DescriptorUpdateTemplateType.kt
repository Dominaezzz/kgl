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
import cvulkan.VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_DESCRIPTOR_SET
import cvulkan.VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_PUSH_DESCRIPTORS_KHR
import cvulkan.VkDescriptorUpdateTemplateType

actual enum class DescriptorUpdateTemplateType(override val value: VkDescriptorUpdateTemplateType) : VkEnum<DescriptorUpdateTemplateType> {
	DESCRIPTOR_SET(VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_DESCRIPTOR_SET),

	PUSH_DESCRIPTORS_KHR(VK_DESCRIPTOR_UPDATE_TEMPLATE_TYPE_PUSH_DESCRIPTORS_KHR);

	companion object {
		fun from(value: VkDescriptorUpdateTemplateType): DescriptorUpdateTemplateType =
				enumValues<DescriptorUpdateTemplateType>()[value.toInt()]
	}
}

