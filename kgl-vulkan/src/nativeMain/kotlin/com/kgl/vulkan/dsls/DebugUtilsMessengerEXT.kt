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

import com.kgl.vulkan.enums.DebugUtilsMessageSeverityEXT
import com.kgl.vulkan.enums.DebugUtilsMessageTypeEXT
import com.kgl.vulkan.structs.DebugUtilsMessengerCallbackDataEXT
import com.kgl.vulkan.structs.from
import com.kgl.vulkan.utils.VkFlag
import cvulkan.VK_FALSE
import cvulkan.VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT
import cvulkan.VkDebugUtilsMessengerCreateInfoEXT
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.pointed
import kotlinx.cinterop.staticCFunction

actual class DebugUtilsMessengerCreateInfoEXTBuilder(internal val target: VkDebugUtilsMessengerCreateInfoEXT) {
	actual var messageSeverity: VkFlag<DebugUtilsMessageSeverityEXT>?
		get() = DebugUtilsMessageSeverityEXT.fromMultiple(target.messageSeverity)
		set(value) {
			target.messageSeverity = value?.value ?: 0u
		}

	actual var messageType: VkFlag<DebugUtilsMessageTypeEXT>?
		get() = DebugUtilsMessageTypeEXT.fromMultiple(target.messageType)
		set(value) {
			target.messageType = value?.value ?: 0u
		}

	actual fun userCallback(callback: (VkFlag<DebugUtilsMessageSeverityEXT>, VkFlag<DebugUtilsMessageTypeEXT>, DebugUtilsMessengerCallbackDataEXT) -> Unit) {
		target.pUserData = StableRef.create(callback).asCPointer()
		target.pfnUserCallback = staticCFunction { severity, type, callbackData, userData ->
			val theCallback = userData!!.asStableRef<(VkFlag<DebugUtilsMessageSeverityEXT>, VkFlag<DebugUtilsMessageTypeEXT>, DebugUtilsMessengerCallbackDataEXT) -> Unit>().get()

			theCallback(
					DebugUtilsMessageSeverityEXT.from(severity),
					DebugUtilsMessageTypeEXT.from(type),
					DebugUtilsMessengerCallbackDataEXT.from(callbackData!!.pointed)
			)

			VK_FALSE.toUInt()
		}
	}

	internal fun init() {
		target.sType = VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT
		target.pNext = null
		target.flags = 0U
	}
}
