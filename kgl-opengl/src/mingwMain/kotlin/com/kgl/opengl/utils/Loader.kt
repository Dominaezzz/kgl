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
package com.kgl.opengl.utils

import kotlinx.cinterop.*
import platform.windows.GetProcAddress
import platform.windows.LPCSTR
import platform.windows.LoadLibraryA
import platform.windows.PROC

actual object Loader {
	private val module = LoadLibraryA("opengl32.dll") ?: throw Exception("Could not find opengl32.dll")
	private val wglGetProcAddress = GetProcAddress(module, "wglGetProcAddress")!!
		.reinterpret<CFunction<(LPCSTR?) -> PROC?>>()

	fun kglGetProcAddress(name: String): COpaquePointer? = memScoped {
		wglGetProcAddress(name.cstr.ptr) ?: GetProcAddress(module, name)
	}
}
