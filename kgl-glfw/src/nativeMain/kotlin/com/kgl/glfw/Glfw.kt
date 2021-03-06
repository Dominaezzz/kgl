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
import kotlin.native.concurrent.ThreadLocal

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
		get() = glfwGetCurrentContext()?.let { glfwGetWindowUserPointer(it) }?.asStableRef<Window>()?.get()
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

	actual val windowHints = object : WindowHints {
		override var resizable: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_RESIZABLE, if (value) GLFW_TRUE else GLFW_FALSE)

		override var visible: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_VISIBLE, if (value) GLFW_TRUE else GLFW_FALSE)

		override var decorated: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_DECORATED, if (value) GLFW_TRUE else GLFW_FALSE)

		override var focused: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_FOCUSED, if (value) GLFW_TRUE else GLFW_FALSE)

		override var autoIconify: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_AUTO_ICONIFY, if (value) GLFW_TRUE else GLFW_FALSE)

		override var floating: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_FLOATING, if (value) GLFW_TRUE else GLFW_FALSE)

		override var maximized: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_MAXIMIZED, if (value) GLFW_TRUE else GLFW_FALSE)

		override var centerCursor: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CENTER_CURSOR, if (value) GLFW_TRUE else GLFW_FALSE)

		override var transparentFrameBuffer: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, if (value) GLFW_TRUE else GLFW_FALSE)

		override var focusOnShow: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_FOCUS_ON_SHOW, if (value) GLFW_TRUE else GLFW_FALSE)

		override var scaleToMonitor: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_SCALE_TO_MONITOR, if (value) GLFW_TRUE else GLFW_FALSE)

		override var redBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_RED_BITS, value)

		override var greenBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_GREEN_BITS, value)

		override var blueBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_BLUE_BITS, value)

		override var alphaBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_ALPHA_BITS, value)

		override var depthBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_DEPTH_BITS, value)

		override var stencilBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_STENCIL_BITS, value)

		override var accumRedBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_ACCUM_RED_BITS, value)

		override var accumGreenBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_ACCUM_GREEN_BITS, value)

		override var accumBlueBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_ACCUM_BLUE_BITS, value)

		override var accumAlphaBits: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_ACCUM_ALPHA_BITS, value)

		override var auxBuffers: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_AUX_BUFFERS, value)

		override var stereo: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_STEREO, if (value) GLFW_TRUE else GLFW_FALSE)

		override var samples: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_SAMPLES, value)

		override var srgbCapable: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_SRGB_CAPABLE, if (value) GLFW_TRUE else GLFW_FALSE)

		override var doubleBuffer: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_DOUBLEBUFFER, if (value) GLFW_TRUE else GLFW_FALSE)

		override var refreshRate: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_REFRESH_RATE, value)

		override var clientApi: ClientApi
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CLIENT_API, value.value)

		override var contextCreationApi: CreationApi
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CONTEXT_CREATION_API, value.value)

		override var contextVersionMajor: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, value)

		override var contextVersionMinor: Int
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, value)

		override var openGLForwardCompat: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, if (value) GLFW_TRUE else GLFW_FALSE)

		override var openGLDebugContext: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, if (value) GLFW_TRUE else GLFW_FALSE)

		override var openGLProfile: OpenGLProfile
			get() = error("")
			set(value) = glfwWindowHint(GLFW_OPENGL_PROFILE, value.value)

		override var contextRobustness: Robustness
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CONTEXT_ROBUSTNESS, value.value)

		override var contextReleaseBehavior: ReleaseBehaviour
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CONTEXT_RELEASE_BEHAVIOR, value.value)

		override var contextNoError: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_CONTEXT_NO_ERROR, if (value) GLFW_TRUE else GLFW_FALSE)

		override var cocoaRetinaFrameBuffer: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, if (value) GLFW_TRUE else GLFW_FALSE)

		override var cocoaFrameName: String
			get() = error("")
			set(value) = glfwWindowHintString(GLFW_COCOA_FRAME_NAME, value)

		override var cocoaGraphicsSwitching: Boolean
			get() = error("")
			set(value) = glfwWindowHint(GLFW_COCOA_GRAPHICS_SWITCHING, if (value) GLFW_TRUE else GLFW_FALSE)

		override var x11ClassName: String
			get() = error("")
			set(value) = glfwWindowHintString(GLFW_X11_CLASS_NAME, value)

		override var x11InstanceName: String
			get() = error("")
			set(value) = glfwWindowHintString(GLFW_X11_INSTANCE_NAME, value)

		override fun restoreDefaults() = glfwDefaultWindowHints()
	}

	actual fun init(): Boolean = glfwInit() == GLFW_TRUE
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
				glfwSetErrorCallback(staticCFunction { error, description ->
					errorCallback?.invoke(error, description!!.toKString())
				})
			}
		} else {
			glfwSetErrorCallback(null)
		}

		return previous
	}

	actual fun setJoystickCallback(callback: JoystickCallback?): JoystickCallback? {
		val previous = joystickCallback
		joystickCallback = callback

		if (callback != null) {
			if (previous == null) {
				glfwSetJoystickCallback(staticCFunction { jid, event ->
					joystickCallback?.invoke(Joystick.values()[jid], event == GLFW_CONNECTED)
				})
			}
		} else {
			glfwSetJoystickCallback(null)
		}

		return previous
	}

	actual fun setMonitorCallback(callback: MonitorCallback?): MonitorCallback? {
		val previous = monitorCallback
		monitorCallback = callback

		if (callback != null) {
			if (previous == null) {
				glfwSetMonitorCallback(staticCFunction { monitor, event ->
					monitorCallback?.invoke(Monitor(monitor!!), event == GLFW_CONNECTED)
				})
			}
		} else {
			glfwSetMonitorCallback(null)
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
