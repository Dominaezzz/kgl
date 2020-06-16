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
import org.lwjgl.system.MemoryStack
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
		get() = MemoryUtil.memGlobalRefToObject(glfwGetWindowUserPointer(glfwGetCurrentContext()))
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

	actual val version: GlfwVersion
		get() = MemoryStack.stackPush().use {
			val major = it.mallocInt(1)
			val minor = it.mallocInt(1)
			val rev = it.mallocInt(1)
			glfwGetVersion(major, minor, rev)
			GlfwVersion(major[0], minor[0], rev[0])
		}
	actual val versionString: String get() = glfwGetVersionString()

	actual fun init(): Boolean = glfwInit()
	actual fun terminate() {
		glfwTerminate()
	}

	private var errorCallback: ErrorCallback? = null
	private var joystickCallback: JoystickCallback? = null
	private var monitorCallback: MonitorCallback? = null

	actual fun setErrorCallback(callback: ErrorCallback?): ErrorCallback? {
		val previous = errorCallback
		errorCallback = callback

		if (callback != null) {
			if (previous == null) {
				glfwSetErrorCallback { error, description ->
					errorCallback?.invoke(error, MemoryUtil.memUTF8(description))
				}?.free()
			}
		} else {
			glfwSetErrorCallback(null)?.free()
		}

		return previous
	}

	actual fun setJoystickCallback(callback: JoystickCallback?): JoystickCallback? {
		val previous = joystickCallback
		joystickCallback = callback

		if (callback != null) {
			if (previous == null) {
				glfwSetJoystickCallback { jid, event ->
					joystickCallback?.invoke(Joystick.values()[jid], event == GLFW_CONNECTED)
				}?.free()
			}
		} else {
			glfwSetJoystickCallback(null)?.free()
		}

		return previous
	}

	actual fun setMonitorCallback(callback: MonitorCallback?): MonitorCallback? {
		val previous = monitorCallback
		monitorCallback = callback

		if (callback != null) {
			if (previous == null) {
				glfwSetMonitorCallback { monitor, event ->
					monitorCallback?.invoke(Monitor(monitor), event == GLFW_CONNECTED)
				}?.free()
			}
		} else {
			glfwSetMonitorCallback(null)?.free()
		}

		return previous
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
		return MemoryStack.stackPush().use {
			glfwUpdateGamepadMappings(it.ASCII(mapping))
		}
	}

	actual fun isExtensionSupported(extension: String): Boolean {
		return glfwExtensionSupported(extension)
	}

	actual fun setSwapInterval(interval: Int) {
		glfwSwapInterval(interval)
	}

	actual val isRawMouseMotionSupported: Boolean
		get() = glfwRawMouseMotionSupported()

	actual fun getKeyName(key: KeyboardKey): String? = glfwGetKeyName(key.value, 0)

	actual fun getKeyName(scancode: Int): String? = glfwGetKeyName(GLFW_KEY_UNKNOWN, scancode)

	actual fun getKeyScancode(key: KeyboardKey): Int {
		return glfwGetKeyScancode(key.value)
	}
}
