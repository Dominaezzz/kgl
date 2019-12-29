package com.kgl.glfw

import cglfw.GLFW_HAT_DOWN
import cglfw.GLFW_HAT_LEFT
import cglfw.GLFW_HAT_RIGHT
import cglfw.GLFW_HAT_UP
import com.kgl.core.Flag

actual enum class Hat(override val value: Int) : Flag<Hat> {
	UP(GLFW_HAT_UP),
	RIGHT(GLFW_HAT_RIGHT),
	DOWN(GLFW_HAT_DOWN),
	LEFT(GLFW_HAT_LEFT);

	actual companion object
}
