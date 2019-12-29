package com.kgl.glfw

expect enum class GamepadButton {
	A,
	B,
	X,
	Y,
	LEFT_BUMPER,
	RIGHT_BUMPER,
	BACK,
	START,
	GUIDE,
	LEFT_THUMB,
	RIGHT_THUMB,
	DPAD_UP,
	DPAD_RIGHT,
	DPAD_DOWN,
	DPAD_LEFT;

	companion object
}

inline val GamepadButton.Companion.CROSS get() = GamepadButton.A
inline val GamepadButton.Companion.CIRCLE get() = GamepadButton.B
inline val GamepadButton.Companion.SQUARE get() = GamepadButton.X
inline val GamepadButton.Companion.TRIANGLE get() = GamepadButton.Y
