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
package com.kgl.vulkan.utils

import cvulkan.*
import kotlinx.cinterop.reinterpret

class GlobalDispatchTable(getProcAddr: (String) -> PFN_vkVoidFunction?) {
	val vkGetInstanceProcAddr: PFN_vkGetInstanceProcAddr = getProcAddr("vkGetInstanceProcAddr")!!.reinterpret()

	val vkCreateInstance: PFN_vkCreateInstance = getProcAddr("vkCreateInstance")!!.reinterpret()
	val vkEnumerateInstanceVersion: PFN_vkEnumerateInstanceVersion? = getProcAddr("vkEnumerateInstanceVersion")?.reinterpret()
	val vkEnumerateInstanceLayerProperties: PFN_vkEnumerateInstanceLayerProperties = getProcAddr("vkEnumerateInstanceLayerProperties")!!.reinterpret()
	val vkEnumerateInstanceExtensionProperties: PFN_vkEnumerateInstanceExtensionProperties = getProcAddr("vkEnumerateInstanceExtensionProperties")!!.reinterpret()
}
