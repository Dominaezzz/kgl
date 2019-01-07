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
import org.lwjgl.vulkan.EXTDebugReport

actual enum class DebugReportEXT(override val value: Int) : VkFlag<DebugReportEXT> {
	INFORMATION(EXTDebugReport.VK_DEBUG_REPORT_INFORMATION_BIT_EXT),

	WARNING(EXTDebugReport.VK_DEBUG_REPORT_WARNING_BIT_EXT),

	PERFORMANCE_WARNING(EXTDebugReport.VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT),

	ERROR(EXTDebugReport.VK_DEBUG_REPORT_ERROR_BIT_EXT),

	DEBUG(EXTDebugReport.VK_DEBUG_REPORT_DEBUG_BIT_EXT);

	companion object {
		private val enumLookUpMap: Map<Int, DebugReportEXT> =
				enumValues<DebugReportEXT>().associateBy({ it.value })

		fun fromMultiple(value: Int): VkFlag<DebugReportEXT> = VkFlag(value)

		fun from(value: Int): DebugReportEXT = enumLookUpMap[value]!!
	}
}

