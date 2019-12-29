package com.kgl.glfw

import cglfw.*

actual enum class CursorMode(internal val value: Int) {
	Normal(GLFW_CURSOR_NORMAL),
	Hidden(GLFW_CURSOR_HIDDEN),
	Disabled(GLFW_CURSOR_DISABLED);

	companion object {
		internal fun from(value: Int) = values()[value - GLFW_CURSOR_NORMAL]
	}
}
