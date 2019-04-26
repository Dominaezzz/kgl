package com.kgl.opengl.utils

import org.khronos.webgl.WebGLRenderingContext

actual object Loader {
	internal lateinit var context: WebGLRenderingContext

	fun init() {
		val context: WebGLRenderingContext = TODO()
	}
}