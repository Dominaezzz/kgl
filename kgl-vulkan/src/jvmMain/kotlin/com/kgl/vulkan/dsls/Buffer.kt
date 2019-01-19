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

import com.kgl.vulkan.enums.BufferCreate
import com.kgl.vulkan.enums.BufferUsage
import com.kgl.vulkan.enums.SharingMode
import com.kgl.vulkan.handles.Buffer
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.VK11
import org.lwjgl.vulkan.VkBufferCreateInfo
import org.lwjgl.vulkan.VkBufferMemoryRequirementsInfo2

actual class BufferCreateInfoBuilder(internal val target: VkBufferCreateInfo) {
	actual var flags: VkFlag<BufferCreate>?
		get() = BufferCreate.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual var size: ULong
		get() = target.size().toULong()
		set(value) {
			target.size(value.toVkType())
		}

	actual var usage: VkFlag<BufferUsage>?
		get() = BufferUsage.fromMultiple(target.usage())
		set(value) {
			target.usage(value.toVkType())
		}

	actual var sharingMode: SharingMode?
		get() = SharingMode.from(target.sharingMode())
		set(value) {
			target.sharingMode(value.toVkType())
		}

	internal fun init(queueFamilyIndices: UIntArray?) {
		target.sType(VK11.VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO)
		target.pNext(0)
		target.pQueueFamilyIndices(queueFamilyIndices?.toVkType())
	}
}

actual class BufferMemoryRequirementsInfo2Builder(internal val target: VkBufferMemoryRequirementsInfo2) {
	internal fun init(buffer: Buffer) {
		target.sType(VK11.VK_STRUCTURE_TYPE_BUFFER_MEMORY_REQUIREMENTS_INFO_2)
		target.pNext(0)
		target.buffer(buffer.toVkType())
	}
}

