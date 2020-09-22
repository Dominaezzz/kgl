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
package com.kgl.vulkan.handles

import com.kgl.core.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class DescriptorUpdateTemplate(
	override val ptr: VkDescriptorUpdateTemplate,
	actual val device: Device
) : VkHandleNative<VkDescriptorUpdateTemplate>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val descriptorUpdateTemplate = this
		val device = descriptorUpdateTemplate.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyDescriptorUpdateTemplate!!(
				device.toVkType(),
				descriptorUpdateTemplate.toVkType(), null
			)
		} finally {
			VirtualStack.pop()
		}
	}
}
