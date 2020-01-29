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

import cglfw.GLFWgammaramp
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.pointed

actual class GammaRamp(val ptr: CPointer<GLFWgammaramp>) {
	var red: UShortArray
		get() = UShortArray(size.toInt()) { ptr.pointed.red!![it] }
		set(_) {
			TODO()
		}

	var green: UShortArray
		get() = UShortArray(size.toInt()) { ptr.pointed.green!![it] }
		set(_) {
			TODO()
		}

	var blue: UShortArray
		get() = UShortArray(size.toInt()) { ptr.pointed.blue!![it] }
		set(_) {
			TODO()
		}

	actual var size: UInt
		get() = ptr.pointed.size
		set(value) {
			ptr.pointed.size = value
		}
}
