package com.kgl.glfw

import com.kgl.core.Flag
import org.lwjgl.glfw.GLFW.*

actual enum class Hat(override val value: Int) : Flag<Hat> {
	UP(GLFW_HAT_UP),
	RIGHT(GLFW_HAT_RIGHT),
	DOWN(GLFW_HAT_DOWN),
	LEFT(GLFW_HAT_LEFT);

	actual companion object
}
