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
package com.kgl.vulkan.enums

import com.kgl.vulkan.utils.VkFlag
import cvulkan.VK_DEPENDENCY_BY_REGION_BIT
import cvulkan.VK_DEPENDENCY_DEVICE_GROUP_BIT
import cvulkan.VK_DEPENDENCY_VIEW_LOCAL_BIT
import cvulkan.VkDependencyFlagBits

actual enum class Dependency(override val value: VkDependencyFlagBits) : VkFlag<Dependency> {
	BY_REGION(VK_DEPENDENCY_BY_REGION_BIT),

	VIEW_LOCAL(VK_DEPENDENCY_VIEW_LOCAL_BIT),

	DEVICE_GROUP(VK_DEPENDENCY_DEVICE_GROUP_BIT);

	companion object {
		private val enumLookUpMap: Map<UInt, Dependency> = enumValues<Dependency>().associateBy({
			it.value
		})

		fun fromMultiple(value: VkDependencyFlagBits): VkFlag<Dependency> = VkFlag(value)

		fun from(value: VkDependencyFlagBits): Dependency = enumLookUpMap[value]!!
	}
}

