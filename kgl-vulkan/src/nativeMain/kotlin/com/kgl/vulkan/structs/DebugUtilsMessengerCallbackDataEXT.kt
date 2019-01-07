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
package com.kgl.vulkan.structs

import cvulkan.VkDebugUtilsMessengerCallbackDataEXT
import kotlinx.cinterop.get
import kotlinx.cinterop.toKString

fun DebugUtilsMessengerCallbackDataEXT.Companion.from(ptr: VkDebugUtilsMessengerCallbackDataEXT): DebugUtilsMessengerCallbackDataEXT = DebugUtilsMessengerCallbackDataEXT(
		ptr.pMessageIdName?.toKString(),
		ptr.messageIdNumber,
		ptr.pMessage!!.toKString(),
		ptr.pQueueLabels?.let { target -> List(ptr.queueLabelCount.toInt()) { DebugUtilsLabelEXT.from(target[it]) } },
		ptr.pCmdBufLabels?.let { target -> List(ptr.cmdBufLabelCount.toInt()) { DebugUtilsLabelEXT.from(target[it]) } },
		ptr.pObjects!!.let { target -> List(ptr.objectCount.toInt()) { DebugUtilsObjectNameInfoEXT.from(target[it]) } }
)
