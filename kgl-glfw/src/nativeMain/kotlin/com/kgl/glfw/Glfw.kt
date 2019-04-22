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

actual object Glfw {
	actual var time: Double
		get() = glfwGetTime()
		set(value) {
			glfwSetTime(value)
		}

	actual val timerValue: ULong get() = glfwGetTimerValue()
	actual val timerFrequency: ULong get() = glfwGetTimerFrequency()

	actual var currentContext: Window?
		get() = glfwGetWindowUserPointer(glfwGetCurrentContext())?.asStableRef<Window>()?.get()
		set(value) {
			glfwMakeContextCurrent(value?.ptr)
		}

	actual val primaryMonitor: Monitor? get() = glfwGetPrimaryMonitor()?.let { Monitor(it) }

	actual val monitors: List<Monitor>
		get() {
			VirtualStack.push()
			try {
				val count = VirtualStack.alloc<IntVar>()

				return object : AbstractList<Monitor>() {
					val monitors: CPointer<CPointerVar<GLFWmonitor>> = glfwGetMonitors(count.ptr)!!

					override val size: Int = count.value
					override fun get(index: Int) = Monitor(monitors[index]!!)
				}
			} finally {
				VirtualStack.pop()
			}
		}

	actual fun init(): Boolean = glfwInit() == GLFW_TRUE
	actual fun terminate() {
		glfwTerminate()
	}

	private var errorCallback: ((Int, String) -> Unit)? = null

	actual fun setErrorCallback(callback: ((Int, String) -> Unit)?) {
		val wasNotPreviouslySet = errorCallback == null
		errorCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetErrorCallback(staticCFunction { error, description ->
					Glfw.errorCallback?.invoke(error, description!!.toKString())
				})
			}
		} else {
			glfwSetErrorCallback(null)
		}
	}

	actual fun setJoystickCallback(callback: (Joystick, Boolean) -> Unit) {
		TODO()
	}

	actual fun setMonitorCallback(callback: (Monitor, Boolean) -> Unit) {
		TODO()
	}
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
