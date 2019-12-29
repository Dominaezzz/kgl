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

import com.kgl.core.Flag

expect enum class Joystick {
	_1,
	_2,
	_3,
	_4,
	_5,
	_6,
	_7,
	_8,
	_9,
	_10,
	_11,
	_12,
	_13,
	_14,
	_15,
	_16
}

expect val Joystick.deviceName: String?
expect val Joystick.isPresent: Boolean
expect val Joystick.axes: List<Float>?
expect val Joystick.buttons: List<Action>?
expect val Joystick.hats: List<Flag<Hat>?>?
expect val Joystick.guid: String?
expect val Joystick.isGamepad: Boolean
expect val Joystick.gamepadName: String?
expect val Joystick.gamepadState: GamepadState?
