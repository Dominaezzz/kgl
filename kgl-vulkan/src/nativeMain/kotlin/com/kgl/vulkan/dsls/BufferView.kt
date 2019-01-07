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

import com.kgl.vulkan.enums.Format
import com.kgl.vulkan.handles.Buffer
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkBufferViewCreateInfo

actual class BufferViewCreateInfoBuilder(internal val target: VkBufferViewCreateInfo) {
	internal fun init(
			buffer: Buffer,
			format: Format,
			offset: ULong,
			range: ULong
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_BUFFER_VIEW_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.buffer = buffer.toVkType()
		target.format = format.toVkType()
		target.offset = offset.toVkType()
		target.range = range.toVkType()
	}
}

