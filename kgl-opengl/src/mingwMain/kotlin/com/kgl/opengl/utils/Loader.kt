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
