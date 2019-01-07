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
import org.lwjgl.vulkan.KHRSwapchain

actual enum class SwapchainCreateKHR(override val value: Int) : VkFlag<SwapchainCreateKHR> {
	SPLIT_INSTANCE_BIND_REGIONS_KHR(KHRSwapchain.VK_SWAPCHAIN_CREATE_SPLIT_INSTANCE_BIND_REGIONS_BIT_KHR),

	PROTECTED_KHR(KHRSwapchain.VK_SWAPCHAIN_CREATE_PROTECTED_BIT_KHR);

	companion object {
		private val enumLookUpMap: Map<Int, SwapchainCreateKHR> =
				enumValues<SwapchainCreateKHR>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<SwapchainCreateKHR> = VkFlag(value)

		fun from(value: Int): SwapchainCreateKHR = enumLookUpMap[value]!!
	}
}

