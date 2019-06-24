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
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.VK11
import org.lwjgl.vulkan.VkSubmitInfo

actual class SubmitInfoBuilder(internal val target: VkSubmitInfo) {
	internal actual fun init(
			waitSemaphores: Collection<Pair<Semaphore, VkFlag<PipelineStage>>>?,
			commandBuffers: Collection<CommandBuffer>?,
			signalSemaphores: Collection<Semaphore>?
	) {
		target.sType(VK11.VK_STRUCTURE_TYPE_SUBMIT_INFO)
		target.pNext(0)
		if (waitSemaphores != null) {
			target.pWaitSemaphores(waitSemaphores.map { it.first }.toVkType())
			target.pWaitDstStageMask(waitSemaphores.map { it.second }.toVkType())
			target.waitSemaphoreCount(waitSemaphores.size)
		} else {
			target.waitSemaphoreCount(0)
		}
		target.pCommandBuffers(commandBuffers?.toVkType())
		target.pSignalSemaphores(signalSemaphores?.toVkType())
	}
}
