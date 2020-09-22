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

import com.kgl.vulkan.enums.*
import com.kgl.vulkan.utils.*
import org.lwjgl.system.*
import org.lwjgl.vulkan.*

actual class DebugReportCallbackCreateInfoEXTBuilder(internal val target: VkDebugReportCallbackCreateInfoEXT) {
	actual var flags: VkFlag<DebugReportEXT>?
		get() = DebugReportEXT.fromMultiple(target.flags())
		set(value) {
			target.flags(value?.value ?: 0)
		}

	actual fun callback(callback: DebugReportCallbackEXT) {
		target.pUserData(0)
		target.pfnCallback { flags, objectType, `object`, location, messageCode, pLayerPrefix, pMessage, _ ->
			callback(
				DebugReportEXT.fromMultiple(flags),
				DebugReportObjectTypeEXT.from(objectType),
				`object`.toULong(), location.toULong(), messageCode,
				MemoryUtil.memUTF8(pLayerPrefix),
				MemoryUtil.memUTF8(pMessage)
			)
			VK10.VK_FALSE
		}
	}

	actual fun next(block: Next<DebugReportCallbackCreateInfoEXTBuilder>.() -> Unit) {
		Next(this).apply(block)
	}

	internal actual fun init() {
		target.sType(EXTDebugReport.VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT)
		target.pNext(0)
	}
}
