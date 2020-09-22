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
import kotlinx.cinterop.COpaquePointer
import platform.posix.*

object Loader {
	private val module: COpaquePointer =
		dlopen("libvulkan.dylib", RTLD_NOW or RTLD_LOCAL) ?: dlopen("libvulkan.dylib.1", RTLD_NOW or RTLD_LOCAL)
		?: dlopen("libMoltenVK.dylib", RTLD_NOW or RTLD_LOCAL) ?: throw Exception("'libvulkan.dylib' not found!")

	internal val vkGetInstanceProcAddr: PFN_vkGetInstanceProcAddr =
		dlsym(module, "vkGetInstanceProcAddr")!!.reinterpret()
}
