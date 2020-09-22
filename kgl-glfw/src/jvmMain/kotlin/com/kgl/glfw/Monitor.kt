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

import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.*

actual class Monitor(val ptr: Long) {
	actual val name: String get() = glfwGetMonitorName(ptr)!!
	actual val position: Pair<Int, Int>
		get() = MemoryStack.stackPush().use {
			val x = it.mallocInt(1)
			val y = it.mallocInt(1)
			glfwGetMonitorPos(ptr, x, y)
			x[0] to y[0]
		}
	actual val physicalSize: Pair<Int, Int>
		get() = MemoryStack.stackPush().use {
			val width = it.mallocInt(1)
			val height = it.mallocInt(1)
			glfwGetMonitorPhysicalSize(ptr, width, height)
			width[0] to height[0]
		}
	actual val contentScale: Pair<Float, Float>
		get() = MemoryStack.stackPush().use {
			val width = it.mallocFloat(1)
			val height = it.mallocFloat(1)
			glfwGetMonitorContentScale(ptr, width, height)
			width[0] to height[0]
		}
	actual val workarea: Workarea
		get() = MemoryStack.stackPush().use {
			val xpos = it.mallocInt(1)
			val ypos = it.mallocInt(1)
			val width = it.mallocInt(1)
			val height = it.mallocInt(1)
			glfwGetMonitorWorkarea(ptr, xpos, ypos, width, height)
			Workarea(xpos[0], ypos[0], width[0], height[0])
		}
	actual val videoMode: VideoMode get() = VideoMode(glfwGetVideoMode(ptr)!!)
	actual val videoModes: List<VideoMode>
		get() {
			return object : AbstractList<VideoMode>() {
				val videoModes: GLFWVidMode.Buffer = glfwGetVideoModes(ptr)!!

				override fun get(index: Int) = VideoMode(videoModes[index])
				override val size: Int = videoModes.capacity()
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
