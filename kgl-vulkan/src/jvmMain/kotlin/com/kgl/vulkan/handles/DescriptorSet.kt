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

import com.kgl.core.DirectMemory
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.VK10.vkFreeDescriptorSets
import org.lwjgl.vulkan.VK11

actual class DescriptorSet(override val ptr: Long, actual val descriptorPool: DescriptorPool) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		vkFreeDescriptorSets(descriptorPool.device.ptr, descriptorPool.ptr, ptr)
	}

	actual fun updateWithTemplate(descriptorUpdateTemplate: DescriptorUpdateTemplate, data: DirectMemory) {
		TODO()
		MemoryStack.stackPush()
		try {
			VK11.vkUpdateDescriptorSetWithTemplate(descriptorPool.device.toVkType(), toVkType(),
					descriptorUpdateTemplate.toVkType(), 0)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

