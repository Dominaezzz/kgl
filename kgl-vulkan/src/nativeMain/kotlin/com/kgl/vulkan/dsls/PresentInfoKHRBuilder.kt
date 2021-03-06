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
package com.kgl.vulkan.dsls

import com.kgl.core.*
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

actual class PresentInfoKHRBuilder(internal val target: VkPresentInfoKHR) {
	actual fun next(block: Next<PresentInfoKHRBuilder>.() -> Unit) {
		Next(this).apply(block)
	}

	internal actual fun init(swapchains: Collection<Pair<SwapchainKHR, UInt>>, waitSemaphores: Collection<Semaphore>?) {
		target.sType = VK_STRUCTURE_TYPE_PRESENT_INFO_KHR
		target.pNext = null
		target.pResults = null

		target.pSwapchains = swapchains.map { it.first }.toVkType()
		target.pImageIndices = swapchains.mapToCArray(VirtualStack) { value = it.second }
		target.swapchainCount = swapchains.size.toUInt()

		if (waitSemaphores != null) {
			target.pWaitSemaphores = waitSemaphores.toVkType()
			target.waitSemaphoreCount = waitSemaphores.size.toUInt()
		} else {
			target.waitSemaphoreCount = 0U
		}
	}
}
