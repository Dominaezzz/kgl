/**
 * Copyright [2019] [Dominic Fischer]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kgl.glfw

import cglfw.*
import com.kgl.core.*
import kotlinx.cinterop.*

actual enum class Joystick(val value: Int) {
	_1(GLFW_JOYSTICK_1),
	_2(GLFW_JOYSTICK_2),
	_3(GLFW_JOYSTICK_3),
	_4(GLFW_JOYSTICK_4),
	_5(GLFW_JOYSTICK_5),
	_6(GLFW_JOYSTICK_6),
	_7(GLFW_JOYSTICK_7),
	_8(GLFW_JOYSTICK_8),
	_9(GLFW_JOYSTICK_9),
	_10(GLFW_JOYSTICK_10),
	_11(GLFW_JOYSTICK_11),
	_12(GLFW_JOYSTICK_12),
	_13(GLFW_JOYSTICK_13),
	_14(GLFW_JOYSTICK_14),
	_15(GLFW_JOYSTICK_15),
	_16(GLFW_JOYSTICK_16)
}

actual val Joystick.isPresent: Boolean get() = glfwJoystickPresent(value) == GLFW_TRUE

actual val Joystick.deviceName: String? get() = glfwGetJoystickName(value)?.toKString()

actual val Joystick.axes: List<Float>?
	get() {
		VirtualStack.push()
		return try {
			val count = VirtualStack.alloc<IntVar>()

			glfwGetJoystickAxes(value, count.ptr)?.let {
				object : AbstractList<Float>() {
					override val size: Int = count.value
					override fun get(index: Int): Float = it[index]
				}
			}
		} finally {
			VirtualStack.pop()
		}
	}

actual val Joystick.buttons: List<Action>?
	get() {
		VirtualStack.push()
		return try {
			val count = VirtualStack.alloc<IntVar>()
			glfwGetJoystickButtons(value, count.ptr)?.let {
				object : AbstractList<Action>() {
					override val size: Int = count.value
					override fun get(index: Int): Action = Action.from(it[index].toInt())
				}
			}
		} finally {
			VirtualStack.pop()
		}
	}

actual val Joystick.hats: List<Flag<Hat>?>?
	get() {
		VirtualStack.push()
		return try {
			val count = VirtualStack.alloc<IntVar>()
			glfwGetJoystickHats(value, count.ptr)?.let {
				object : AbstractList<Flag<Hat>?>() {
					override val size: Int = count.value
					override fun get(index: Int): Flag<Hat>? = Flag(it[index].toInt())
				}
			}
		} finally {
			VirtualStack.pop()
		}
	}

actual val Joystick.guid: String? get() = glfwGetJoystickGUID(value)?.toKString()

actual val Joystick.isGamepad: Boolean get() = glfwJoystickIsGamepad(value) == GLFW_TRUE

actual val Joystick.gamepadName: String? get() = glfwGetGamepadName(value)?.toKString()

actual val Joystick.gamepadState: GamepadState?
	get() {
		VirtualStack.push()
		try {
			val state = VirtualStack.alloc<GLFWgamepadstate>()
			if (glfwGetGamepadState(value, state.ptr) == GLFW_TRUE) {
				return GamepadState(
					List(15) { Action.from(state.buttons[it].toInt()) },
					FloatArray(6) { state.axes[it] }
				)
			}
			return null
		} finally {
			VirtualStack.pop()
		}
	}
