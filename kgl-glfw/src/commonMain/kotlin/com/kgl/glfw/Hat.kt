package com.kgl.glfw

import com.kgl.core.*

expect enum class Hat : Flag<Hat> {
	UP,
	RIGHT,
	DOWN,
	LEFT;

	companion object
}

inline val Hat.Companion.RIGHT_UP get() = Hat.RIGHT or Hat.UP
inline val Hat.Companion.RIGHT_DOWN get() = Hat.RIGHT or Hat.DOWN
inline val Hat.Companion.LEFT_UP get() = Hat.LEFT or Hat.UP
inline val Hat.Companion.LEFT_DOWN get() = Hat.LEFT or Hat.DOWN
