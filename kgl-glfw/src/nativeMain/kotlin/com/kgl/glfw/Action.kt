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

import cglfw.GLFW_PRESS
import cglfw.GLFW_RELEASE
import cglfw.GLFW_REPEAT

actual enum class Action(internal val value: Int) {
	Press(GLFW_PRESS),
	Release(GLFW_RELEASE),
	Repeat(GLFW_REPEAT);

	companion object {
		private val lookUp = enumValues<Action>().associateBy { it.value }
		internal fun from(value: Int): Action = lookUp[value]!!
	}
}
