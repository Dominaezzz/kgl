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

import org.lwjgl.glfw.GLFW.*

actual enum class MouseButton(val value: Int) {
	_1(GLFW_MOUSE_BUTTON_1),
	_2(GLFW_MOUSE_BUTTON_2),
	_3(GLFW_MOUSE_BUTTON_3),
	_4(GLFW_MOUSE_BUTTON_4),
	_5(GLFW_MOUSE_BUTTON_5),
	_6(GLFW_MOUSE_BUTTON_6),
	_7(GLFW_MOUSE_BUTTON_7),
	_8(GLFW_MOUSE_BUTTON_8);

	actual companion object {
		private val lookUp = enumValues<MouseButton>().associateBy { it.value }
		fun from(value: Int): MouseButton = lookUp.getValue(value)
	}
}
