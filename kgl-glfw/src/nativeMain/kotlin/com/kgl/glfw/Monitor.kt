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
import kotlinx.cinterop.*

actual class Monitor(val ptr: CPointer<GLFWmonitor>) {
	actual val name: String get() = glfwGetMonitorName(ptr)!!.toKString()
	actual val position: Pair<Int, Int>
		get() = memScoped {
			val x = alloc<IntVar>()
			val y = alloc<IntVar>()
			glfwGetMonitorPos(ptr, x.ptr, y.ptr)
			x.value to y.value
		}
	actual val physicalSize: Pair<Int, Int>
		get() = memScoped {
			val width = alloc<IntVar>()
			val height = alloc<IntVar>()
			glfwGetMonitorPhysicalSize(ptr, width.ptr, height.ptr)
			width.value to height.value
		}
	actual val videoMode: VideoMode get() = VideoMode(glfwGetVideoMode(ptr)!!)
	actual val videoModes: List<VideoMode>
		get() = memScoped {
			object : AbstractList<VideoMode>() {
				val videoModes: CPointer<GLFWvidmode>

				init {
					val count = alloc<IntVar>()
					videoModes = glfwGetVideoModes(ptr, count.ptr)!!
					size = count.value
				}

				override val size: Int

				override fun get(index: Int) = VideoMode(videoModes[index].ptr)
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

	actual companion object {
		actual val primary: Monitor? get() = glfwGetPrimaryMonitor()?.let { Monitor(it) }

		actual fun getMonitors(): List<Monitor> = memScoped {
			val count = alloc<IntVar>()

			object : AbstractList<Monitor>() {
				val monitors: CPointer<CPointerVar<GLFWmonitor>> = glfwGetMonitors(count.ptr)!!

				override val size: Int = count.value
				override fun get(index: Int) = Monitor(monitors[index]!!)
			}
		}

		actual fun setCallback(callback: (Monitor, Boolean) -> Unit) {}
	}
}
