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

import com.kgl.vulkan.utils.VkEnum
import cvulkan.*

actual enum class SystemAllocationScope(override val value: VkSystemAllocationScope) : VkEnum<SystemAllocationScope> {
	COMMAND(VK_SYSTEM_ALLOCATION_SCOPE_COMMAND),

	OBJECT(VK_SYSTEM_ALLOCATION_SCOPE_OBJECT),

	CACHE(VK_SYSTEM_ALLOCATION_SCOPE_CACHE),

	DEVICE(VK_SYSTEM_ALLOCATION_SCOPE_DEVICE),

	INSTANCE(VK_SYSTEM_ALLOCATION_SCOPE_INSTANCE);

	companion object {
		fun from(value: VkSystemAllocationScope): SystemAllocationScope =
				enumValues<SystemAllocationScope>()[value.toInt()]
	}
}

