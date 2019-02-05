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

import com.kgl.core.utils.VirtualStack
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkSurfaceKHR
import kotlinx.cinterop.invoke

actual class SurfaceKHR(override val ptr: VkSurfaceKHR, actual val instance: Instance) : VkHandleNative<VkSurfaceKHR>(), VkHandle {
	internal val dispatchTable = instance.dispatchTable

	override fun close() {
		val surface = this
		val instance = surface.instance
		VirtualStack.push()
		try {
			dispatchTable.vkDestroySurfaceKHR!!(instance.toVkType(), surface.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}
}

