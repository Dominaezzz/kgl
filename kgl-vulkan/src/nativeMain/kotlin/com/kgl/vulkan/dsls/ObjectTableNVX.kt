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
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkObjectTableCreateInfoNVX
import cvulkan.VkObjectTableEntryNVX

actual class ObjectTableCreateInfoNVXBuilder(internal val target: VkObjectTableCreateInfoNVX) {
	actual var maxUniformBuffersPerDescriptor: UInt
		get() = target.maxUniformBuffersPerDescriptor
		set(value) {
			target.maxUniformBuffersPerDescriptor = value.toVkType()
		}

	actual var maxStorageBuffersPerDescriptor: UInt
		get() = target.maxStorageBuffersPerDescriptor
		set(value) {
			target.maxStorageBuffersPerDescriptor = value.toVkType()
		}

	actual var maxStorageImagesPerDescriptor: UInt
		get() = target.maxStorageImagesPerDescriptor
		set(value) {
			target.maxStorageImagesPerDescriptor = value.toVkType()
		}

	actual var maxSampledImagesPerDescriptor: UInt
		get() = target.maxSampledImagesPerDescriptor
		set(value) {
			target.maxSampledImagesPerDescriptor = value.toVkType()
		}

	actual var maxPipelineLayouts: UInt
		get() = target.maxPipelineLayouts
		set(value) {
			target.maxPipelineLayouts = value.toVkType()
		}

	internal fun init(
			objectEntryTypes: Collection<ObjectEntryTypeNVX>,
			objectEntryCounts: UIntArray,
			objectEntryUsageFlags: Collection<VkFlag<ObjectEntryUsageNVX>>
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_OBJECT_TABLE_CREATE_INFO_NVX
		target.pNext = null
		target.pObjectEntryTypes = objectEntryTypes.toVkType()
		target.objectCount = objectEntryTypes.size.toUInt()
		target.pObjectEntryCounts = objectEntryCounts.toVkType()
		target.objectCount = objectEntryCounts.size.toUInt()
		target.pObjectEntryUsageFlags = objectEntryUsageFlags.toVkType()
		target.objectCount = objectEntryUsageFlags.size.toUInt()
	}
}

actual class ObjectTableEntryNVXBuilder(internal val target: VkObjectTableEntryNVX) {
	actual var type: ObjectEntryTypeNVX?
		get() = ObjectEntryTypeNVX.from(target.type)
		set(value) {
			target.type = value.toVkType()
		}

	actual var flags: VkFlag<ObjectEntryUsageNVX>?
		get() = ObjectEntryUsageNVX.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	internal fun init() {
	}
}

actual class RegisterObjectsNVXBuilder {
	val targets: MutableList<(VkObjectTableEntryNVX) -> Unit> = mutableListOf()

	actual fun entry(block: ObjectTableEntryNVXBuilder.() -> Unit) {
		targets += {
			val builder = ObjectTableEntryNVXBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

