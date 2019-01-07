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

import com.kgl.vulkan.utils.VirtualStack
import com.kgl.vulkan.utils.VkVersion
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import cvulkan.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString

actual class ApplicationInfoBuilder(internal val target: VkApplicationInfo) {
	actual var applicationName: String?
		get() = target.pApplicationName?.toKString()
		set(value) {
			target.pApplicationName = value.toVkType()
		}

	actual var applicationVersion: VkVersion
		get() = VkVersion(target.applicationVersion)
		set(value) {
			target.applicationVersion = value.value.toVkType()
		}

	actual var engineName: String?
		get() = target.pEngineName?.toKString()
		set(value) {
			target.pEngineName = value.toVkType()
		}

	actual var engineVersion: VkVersion
		get() = VkVersion(target.engineVersion)
		set(value) {
			target.engineVersion = value.value.toVkType()
		}

	actual var apiVersion: VkVersion
		get() = VkVersion(target.apiVersion)
		set(value) {
			target.apiVersion = value.value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_APPLICATION_INFO
		target.pNext = null
	}
}

actual class InstanceCreateInfoBuilder(internal val target: VkInstanceCreateInfo) {
	actual fun applicationInfo(block: ApplicationInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkApplicationInfo>()
		target.pApplicationInfo = subTarget.ptr
		val builder = ApplicationInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init(enabledLayerNames: Collection<String>?, enabledExtensionNames: Collection<String>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.ppEnabledLayerNames = enabledLayerNames?.toVkType()
		target.enabledLayerCount = enabledLayerNames?.size?.toUInt() ?: 0U
		target.ppEnabledExtensionNames = enabledExtensionNames?.toVkType()
		target.enabledExtensionCount = enabledExtensionNames?.size?.toUInt() ?: 0U
	}
}

actual class DebugUtilsLabelsEXTBuilder {
	val targets: MutableList<(VkDebugUtilsLabelEXT) -> Unit> = mutableListOf()

	actual fun label(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		targets += {
			val builder = DebugUtilsLabelEXTBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class DebugUtilsObjectNameInfoEXTsBuilder {
	val targets: MutableList<(VkDebugUtilsObjectNameInfoEXT) -> Unit> = mutableListOf()

	actual fun info(block: DebugUtilsObjectNameInfoEXTBuilder.() -> Unit) {
		targets += {
			val builder = DebugUtilsObjectNameInfoEXTBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class DebugUtilsMessengerCallbackDataEXTBuilder(internal val target: VkDebugUtilsMessengerCallbackDataEXT) {
	actual var messageIdName: String?
		get() = target.pMessageIdName?.toKString()
		set(value) {
			target.pMessageIdName = value.toVkType()
		}

	actual var messageIdNumber: Int
		get() = target.messageIdNumber
		set(value) {
			target.messageIdNumber = value.toVkType()
		}

	actual var message: String?
		get() = target.pMessage?.toKString()
		set(value) {
			target.pMessage = value.toVkType()
		}

	actual fun queueLabels(block: DebugUtilsLabelsEXTBuilder.() -> Unit) {
		val targets = DebugUtilsLabelsEXTBuilder().apply(block).targets
		target.pQueueLabels = targets.mapToStackArray()
		target.queueLabelCount = targets.size.toUInt()
	}

	actual fun cmdBufLabels(block: DebugUtilsLabelsEXTBuilder.() -> Unit) {
		val targets = DebugUtilsLabelsEXTBuilder().apply(block).targets
		target.pCmdBufLabels = targets.mapToStackArray()
		target.cmdBufLabelCount = targets.size.toUInt()
	}

	actual fun objects(block: DebugUtilsObjectNameInfoEXTsBuilder.() -> Unit) {
		val targets = DebugUtilsObjectNameInfoEXTsBuilder().apply(block).targets
		target.pObjects = targets.mapToStackArray()
		target.objectCount = targets.size.toUInt()
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CALLBACK_DATA_EXT
		target.pNext = null
		target.flags = 0U
	}
}

