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

import com.kgl.vulkan.dsls.BindSparseInfosBuilder
import com.kgl.vulkan.dsls.DebugUtilsLabelEXTBuilder
import com.kgl.vulkan.dsls.PresentInfoKHRBuilder
import com.kgl.vulkan.dsls.SubmitInfosBuilder
import com.kgl.vulkan.structs.CheckpointDataNV
import com.kgl.vulkan.utils.VkHandle

expect class Queue : VkHandle {
	val device: Device
	val queueFamilyIndex: UInt

	val checkpointDataNV: List<CheckpointDataNV>

	fun submit(fence: Fence?, block: SubmitInfosBuilder.() -> Unit)

	fun presentKHR(swapchains: Collection<Pair<SwapchainKHR, UInt>>, waitSemaphores: Collection<Semaphore>? = null, block: PresentInfoKHRBuilder.() -> Unit = {}): Boolean

	fun waitIdle()

	fun bindSparse(fence: Fence?, block: BindSparseInfosBuilder.() -> Unit)

	fun beginDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit = {})

	fun endDebugUtilsLabelEXT()

	fun insertDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit = {})
}

