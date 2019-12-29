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
import kotlin.native.concurrent.ThreadLocal
import kotlinx.cinterop.*

@ThreadLocal
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

	actual val version: GlfwVersion
		get() {
			VirtualStack.push()
			try {
				val major = VirtualStack.alloc<IntVar>()
				val minor = VirtualStack.alloc<IntVar>()
				val rev = VirtualStack.alloc<IntVar>()
				glfwGetVersion(major.ptr, minor.ptr, rev.ptr)
				return GlfwVersion(major.value, minor.value, rev.value)
			} finally {
				VirtualStack.pop()
			}
		}
	actual val versionString: String get() = glfwGetVersionString()!!.toKString()

	actual fun init(): Boolean = glfwInit() == GLFW_TRUE
	actual fun terminate() {
		glfwTerminate()
	}

	private var errorCallback: ErrorCallback? = null
	private var joystickCallback: JoystickCallback? = null
	private var monitorCallback: MonitorCallback? = null

	actual fun setErrorCallback(callback: ErrorCallback?) {
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

	actual fun setJoystickCallback(callback: JoystickCallback?) {
		val wasNotPreviouslySet = joystickCallback == null
		joystickCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetJoystickCallback(staticCFunction { jid, event ->
					Glfw.joystickCallback?.invoke(Joystick.values()[jid], event == GLFW_CONNECTED)
				})
			}
		} else {
			glfwSetJoystickCallback(null)
		}
	}

	actual fun setMonitorCallback(callback: MonitorCallback?) {
		val wasNotPreviouslySet = monitorCallback == null
		monitorCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetMonitorCallback(staticCFunction { monitor, event ->
					Glfw.monitorCallback?.invoke(Monitor(monitor!!), event == GLFW_CONNECTED)
				})
			}
		} else {
			glfwSetMonitorCallback(null)
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

	actual fun updateGamepadMappings(mapping: String): Boolean {
		return glfwUpdateGamepadMappings(mapping) == GLFW_TRUE
	}

	actual fun isExtensionSupported(extension: String): Boolean {
		return glfwExtensionSupported(extension) == GLFW_TRUE
	}

	actual fun setSwapInterval(interval: Int) {
		glfwSwapInterval(interval)
	}

	actual val isRawMouseMotionSupported: Boolean
		get() = glfwRawMouseMotionSupported() == GLFW_TRUE

	actual fun getKeyName(key: KeyboardKey): String? = glfwGetKeyName(key.value, 0)?.toKString()

	actual fun getKeyName(scancode: Int): String? = glfwGetKeyName(GLFW_KEY_UNKNOWN, scancode)?.toKString()

	actual fun getKeyScancode(key: KeyboardKey): Int {
		return glfwGetKeyScancode(key.value)
	}
}
