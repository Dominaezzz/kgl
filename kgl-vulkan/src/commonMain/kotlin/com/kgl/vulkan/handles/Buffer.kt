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

import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*

expect class Buffer : VkHandle {
	val device: Device
	val size: ULong

	val memory: DeviceMemory?
	val memoryOffset: ULong

	val memoryRequirements: MemoryRequirements

	fun bindMemory(memory: DeviceMemory, memoryOffset: ULong)

	fun createView(
		format: Format,
		offset: ULong,
		range: ULong,
		block: BufferViewCreateInfoBuilder.() -> Unit = {}
	): BufferView

	fun getMemoryRequirements2(block: BufferMemoryRequirementsInfo2Builder.() -> Unit = {}): MemoryRequirements2
}
