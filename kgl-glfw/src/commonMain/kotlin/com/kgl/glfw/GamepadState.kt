package com.kgl.glfw

data class GamepadState(
		val buttons: List<Action>,
		val axes: FloatArray
)
