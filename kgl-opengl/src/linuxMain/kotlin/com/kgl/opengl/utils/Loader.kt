package com.kgl.opengl.utils

import kotlinx.cinterop.COpaquePointer
import platform.linux.*

actual object Loader {
	fun kglGetProcAddress(name: String): COpaquePointer? = glXGetProcAddressARB(name)
}
