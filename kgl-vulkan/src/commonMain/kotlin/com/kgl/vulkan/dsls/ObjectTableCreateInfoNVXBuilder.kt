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

import com.kgl.vulkan.enums.ObjectEntryTypeNVX
import com.kgl.vulkan.enums.ObjectEntryUsageNVX
import com.kgl.vulkan.utils.Next
import com.kgl.vulkan.utils.StructMarker
import com.kgl.vulkan.utils.VkFlag

@StructMarker
expect class ObjectTableCreateInfoNVXBuilder {
	var maxUniformBuffersPerDescriptor: UInt

	var maxStorageBuffersPerDescriptor: UInt

	var maxStorageImagesPerDescriptor: UInt

	var maxSampledImagesPerDescriptor: UInt

	var maxPipelineLayouts: UInt

	fun next(block: Next<ObjectTableCreateInfoNVXBuilder>.() -> Unit)

	internal fun init(
			objectEntryTypes: Collection<ObjectEntryTypeNVX>,
			objectEntryCounts: UIntArray,
			objectEntryUsageFlags: Collection<VkFlag<ObjectEntryUsageNVX>>
	)
}
