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
import cnames.structs.GLFWcursor
import kotlinx.cinterop.*
import kotlinx.io.core.Closeable

actual class Cursor : Closeable {
	val ptr: CPointer<GLFWcursor>?

	actual constructor(image: Image, xhot: Int, yhot: Int) {
		ptr = memScoped {
			glfwCreateCursor(
					alloc<GLFWimage> {
						width = image.width
						height = image.height
						image.pixels.readDirect {
							pixels = it.reinterpret()
							image.width * image.height * 4
						}
					}.ptr,
					xhot,
					yhot
			)
		}
	}

	actual constructor(shape: Standard) {
		ptr = glfwCreateStandardCursor(shape.value)
	}

	override fun close() {
		glfwDestroyCursor(ptr)
	}

	actual enum class Standard(internal val value: Int) {
		Arrow(GLFW_ARROW_CURSOR),
		IBeam(GLFW_IBEAM_CURSOR),
		CrossHair(GLFW_CROSSHAIR_CURSOR),
		Hand(GLFW_HAND_CURSOR),
		HResize(GLFW_HRESIZE_CURSOR),
		VResize(GLFW_VRESIZE_CURSOR);
	}
}
