package com.kgl.opengl.utils

import kotlinx.cinterop.*
import platform.posix.*

actual object Loader {
	private val module: COpaquePointer = dlopen("/System/Library/Frameworks/OpenGL.framework/Versions/Current/OpenGL", RTLD_LAZY) ?:
	throw Exception("'OpenGL' not found!")

	fun kglGetProcAddress(name: String): COpaquePointer? = memScoped {
		dlsym(module, name.cstr.ptr)?.reinterpret()
	}
}
