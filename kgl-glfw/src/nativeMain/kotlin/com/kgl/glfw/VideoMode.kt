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

import cglfw.GLFWvidmode
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.pointed

actual class VideoMode(internal val ptr: CPointer<GLFWvidmode>) {
	actual val width: Int get() = ptr.pointed.width
	actual val height: Int get() = ptr.pointed.height
	actual val redBits: Int get() = ptr.pointed.redBits
	actual val greenBits: Int get() = ptr.pointed.greenBits
	actual val blueBits: Int get() = ptr.pointed.blueBits
	actual val refreshRate: Int get() = ptr.pointed.refreshRate
}
