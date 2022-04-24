package com.kgl.opengl.utils

import kotlinx.cinterop.*

expect object Loader {
	fun kglGetProcAddress(name: String): COpaquePointer?
}
