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

import org.lwjgl.PointerBuffer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil

actual object Glfw {
	actual var time: Double
		get() = glfwGetTime()
		set(value) {
			glfwSetTime(value)
		}

	actual val timerValue: ULong get() = glfwGetTimerValue().toULong()
	actual val timerFrequency: ULong get() = glfwGetTimerFrequency().toULong()

	actual var currentContext: Window?
		get() = TODO("Getting current context is not yet supported on JVM") // glfwGetCurrentContext()
		set(value) {
			glfwMakeContextCurrent(value?.ptr ?: 0L)
		}

	actual val primaryMonitor: Monitor? get() = glfwGetPrimaryMonitor().takeIf { it != 0L }?.let { Monitor(it) }

	actual val monitors: List<Monitor>
		get() {
			return object : AbstractList<Monitor>() {
				val monitors: PointerBuffer = glfwGetMonitors()!!

				override fun get(index: Int) = Monitor(monitors[index])
				override val size: Int = monitors.capacity()
			}
		}

	actual fun init(): Boolean = glfwInit()
	actual fun terminate() {
		glfwTerminate()
	}

	actual fun setErrorCallback(callback: ((Int, String) -> Unit)?) {
		if (callback != null) {
			glfwSetErrorCallback { error, description ->
				callback(error, MemoryUtil.memUTF8(description))
			}
		} else {
			glfwSetErrorCallback(null)
		}?.free()
	}

	actual fun setJoystickCallback(callback: (Joystick, Boolean) -> Unit) {
		TODO()
	}

	actual fun setMonitorCallback(callback: (Monitor, Boolean) -> Unit) {
		TODO()
	}

	actual fun pollEvents() {
		glfwPollEvents()
	}

	actual fun waitEvents() {
		glfwWaitEvents()
	}

	actual fun waitEvents(timeout: Double) {
		glfwWaitEventsTimeout(timeout)
	}

	actual fun postEmptyEvent() {
		glfwPostEmptyEvent()
	}

	actual fun setSwapInterval(interval: Int) {
		glfwSwapInterval(interval)
	}
}
