package com.kgl.glfw

import org.lwjgl.glfw.GLFW.*

actual enum class CursorMode(val value: Int) {
	Normal(GLFW_CURSOR_NORMAL),
	Hidden(GLFW_CURSOR_HIDDEN),
	Disabled(GLFW_CURSOR_DISABLED);

	companion object {
		fun from(value: Int) = values()[value - GLFW_CURSOR_NORMAL]
	}
}
