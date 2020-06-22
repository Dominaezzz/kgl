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

import com.kgl.vulkan.enums.PipelineStage
import com.kgl.vulkan.handles.CommandBuffer
import com.kgl.vulkan.handles.Semaphore
import com.kgl.vulkan.utils.Next
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkSubmitInfo

actual class SubmitInfoBuilder(internal val target: VkSubmitInfo) {
	actual fun next(block: Next<SubmitInfoBuilder>.() -> Unit) {
		Next(this).apply(block)
	}

	internal actual fun init(
			waitSemaphores: Collection<Pair<Semaphore, VkFlag<PipelineStage>>>?,
			commandBuffers: Collection<CommandBuffer>?,
			signalSemaphores: Collection<Semaphore>?
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SUBMIT_INFO
		target.pNext = null

		if (waitSemaphores != null) {
			target.pWaitSemaphores = waitSemaphores.map { it.first }.toVkType()
			target.pWaitDstStageMask = waitSemaphores.map { it.second }.toVkType()
			target.waitSemaphoreCount = waitSemaphores.size.toUInt()
		} else {
			target.waitSemaphoreCount = 0U
		}

		if (commandBuffers != null) {
			target.pCommandBuffers = commandBuffers.toVkType()
			target.commandBufferCount = commandBuffers.size.toUInt()
		} else {
			target.commandBufferCount = 0U
		}

		if (signalSemaphores != null) {
			target.pSignalSemaphores = signalSemaphores.toVkType()
			target.signalSemaphoreCount = signalSemaphores.size.toUInt()
		} else {
			target.signalSemaphoreCount = 0U
		}
	}
}
