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
import org.lwjgl.vulkan.KHRSharedPresentableImage
import org.lwjgl.vulkan.KHRSurface

actual enum class PresentModeKHR(override val value: Int) : VkEnum<PresentModeKHR> {
	IMMEDIATE(KHRSurface.VK_PRESENT_MODE_IMMEDIATE_KHR),

	SHARED_DEMAND_REFRESH_KHR(KHRSharedPresentableImage.VK_PRESENT_MODE_SHARED_DEMAND_REFRESH_KHR),

	MAILBOX(KHRSurface.VK_PRESENT_MODE_MAILBOX_KHR),

	SHARED_CONTINUOUS_REFRESH_KHR(KHRSharedPresentableImage.VK_PRESENT_MODE_SHARED_CONTINUOUS_REFRESH_KHR),

	FIFO(KHRSurface.VK_PRESENT_MODE_FIFO_KHR),

	FIFO_RELAXED(KHRSurface.VK_PRESENT_MODE_FIFO_RELAXED_KHR);

	companion object {
		private val enumLookUpMap: Map<Int, PresentModeKHR> =
				enumValues<PresentModeKHR>().associateBy({ it.value })

		fun from(value: Int): PresentModeKHR = enumLookUpMap[value]!!
	}
}

