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

import com.kgl.vulkan.structs.Extent2D
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.VK11.vkDestroyRenderPass
import org.lwjgl.vulkan.VK11.vkGetRenderAreaGranularity
import org.lwjgl.vulkan.VkExtent2D

actual class RenderPass(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	actual val renderAreaGranularity: Extent2D
		get() {
			val renderPass = this
			val device = renderPass.device
			MemoryStack.stackPush()
			try {
				val outputPtr = VkExtent2D.mallocStack()
				vkGetRenderAreaGranularity(device.toVkType(), renderPass.toVkType(), outputPtr)
				return Extent2D.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		val renderPass = this
		val device = renderPass.device
		MemoryStack.stackPush()
		try {
			vkDestroyRenderPass(device.toVkType(), renderPass.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

