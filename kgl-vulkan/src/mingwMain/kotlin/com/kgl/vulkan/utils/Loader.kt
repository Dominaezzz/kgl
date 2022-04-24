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

import cvulkan.PFN_vkGetInstanceProcAddr
import kotlinx.cinterop.reinterpret
import platform.windows.GetProcAddress
import platform.windows.HMODULE
import platform.windows.LoadLibraryA

actual object Loader {
	private val module: HMODULE = LoadLibraryA("vulkan-1.dll") ?: throw Exception("'vulkan-1.dll' not found!")
	internal actual val vkGetInstanceProcAddr: PFN_vkGetInstanceProcAddr =
		GetProcAddress(module, "vkGetInstanceProcAddr")!!.reinterpret()
}
