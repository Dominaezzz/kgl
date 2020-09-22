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
import cnames.structs.GLFWmonitor
import com.kgl.core.VirtualStack
import kotlinx.cinterop.*
import kotlinx.cinterop.CPointer

actual class Monitor(val ptr: CPointer<GLFWmonitor>) {
	actual val name: String get() = glfwGetMonitorName(ptr)!!.toKString()
	actual val position: Pair<Int, Int>
		get() {
			VirtualStack.push()
			return try {
				val x = VirtualStack.alloc<IntVar>()
				val y = VirtualStack.alloc<IntVar>()
				glfwGetMonitorPos(ptr, x.ptr, y.ptr)
				x.value to y.value
			} finally {
				VirtualStack.pop()
			}
		}
	actual val physicalSize: Pair<Int, Int>
		get() {
			VirtualStack.push()
			return try {
				val width = VirtualStack.alloc<IntVar>()
				val height = VirtualStack.alloc<IntVar>()
				glfwGetMonitorPhysicalSize(ptr, width.ptr, height.ptr)
				width.value to height.value
			} finally {
				VirtualStack.pop()
			}
		}
	actual val contentScale: Pair<Float, Float>
		get() {
			VirtualStack.push()
			return try {
				val width = VirtualStack.alloc<FloatVar>()
				val height = VirtualStack.alloc<FloatVar>()
				glfwGetMonitorContentScale(ptr, width.ptr, height.ptr)
				width.value to height.value
			} finally {
				VirtualStack.pop()
			}
		}
	actual val workarea: Workarea
		get() {
			VirtualStack.push()
			return try {
				val xpos = VirtualStack.alloc<IntVar>()
				val ypos = VirtualStack.alloc<IntVar>()
				val width = VirtualStack.alloc<IntVar>()
				val height = VirtualStack.alloc<IntVar>()
				glfwGetMonitorWorkarea(ptr, xpos.ptr, ypos.ptr, width.ptr, height.ptr)
				Workarea(xpos.value, ypos.value, width.value, height.value)
			} finally {
				VirtualStack.pop()
			}
		}
	actual val videoMode: VideoMode get() = VideoMode(glfwGetVideoMode(ptr)!!)
	actual val videoModes: List<VideoMode>
		get() {
			VirtualStack.push()
			return try {
				object : AbstractList<VideoMode>() {
					val videoModes: CPointer<GLFWvidmode>

					init {
						val count = VirtualStack.alloc<IntVar>()
						videoModes = glfwGetVideoModes(ptr, count.ptr)!!
						size = count.value
					}

					override val size: Int

					override fun get(index: Int) = VideoMode(videoModes[index].ptr)
				}
			} finally {
				VirtualStack.pop()
			}
		}
	actual var gammaRamp: GammaRamp
		get() = GammaRamp(glfwGetGammaRamp(ptr)!!)
		set(value) {
			glfwSetGammaRamp(ptr, value.ptr)
		}

	actual fun setGamma(gamma: Float) {
		glfwSetGamma(ptr, gamma)
	}
}
