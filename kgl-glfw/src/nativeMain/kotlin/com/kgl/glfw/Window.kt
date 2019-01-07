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

//import kotlinx.io.core.Closeable
import cglfw.*
import cnames.structs.GLFWwindow
import com.kgl.core.Flag
import kotlinx.cinterop.*
import kotlinx.io.core.Closeable
import kotlin.native.concurrent.ensureNeverFrozen

actual class Window @PublishedApi internal constructor(val ptr: CPointer<GLFWwindow>) : Closeable {

	init {
		ensureNeverFrozen()

		glfwSetWindowUserPointer(ptr, StableRef.create(this).asCPointer())
	}

	actual var position: Pair<Int, Int>
		get() = memScoped {
			val width = alloc<IntVar>()
			val height = alloc<IntVar>()
			glfwGetWindowSize(ptr, width.ptr, height.ptr)
			width.value to height.value
		}
		set(value) {
			glfwSetWindowPos(ptr, value.first, value.second)
		}

	actual var size: Pair<Int, Int>
		get() = memScoped {
			val width = alloc<IntVar>()
			val height = alloc<IntVar>()
			glfwGetWindowSize(ptr, width.ptr, height.ptr)
			width.value to height.value
		}
		set(value) {
			glfwSetWindowSize(ptr, value.first, value.second)
		}

	actual val frameBufferSize: Pair<Int, Int>
		get() = memScoped {
			val width = alloc<IntVar>()
			val height = alloc<IntVar>()
			glfwGetFramebufferSize(ptr, width.ptr, height.ptr)
			width.value to height.value
		}

	actual var clipboardString: String?
		get() = glfwGetClipboardString(ptr)?.toKString()
		set(value) {
			glfwSetClipboardString(ptr, value)
		}

	actual var shouldClose: Boolean
		get() = glfwWindowShouldClose(ptr) == GLFW_TRUE
		set(value) {
			glfwSetWindowShouldClose(ptr, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var isIconified: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_ICONIFIED) == GLFW_TRUE
		set(value) {
			if (value) {
				glfwIconifyWindow(ptr)
			} else {
				glfwRestoreWindow(ptr)
			}
		}

	actual var isVisible: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_VISIBLE) == GLFW_TRUE
		set(value) {
			if (value) {
				glfwShowWindow(ptr)
			} else {
				glfwHideWindow(ptr)
			}
		}

	actual val monitor: Monitor? get() = glfwGetWindowMonitor(ptr)?.let { Monitor(it) }

	actual var cursorPosition: Pair<Double, Double>
		get() = memScoped {
			val width = alloc<DoubleVar>()
			val height = alloc<DoubleVar>()
			glfwGetCursorPos(ptr, width.ptr, height.ptr)
			width.value to height.value
		}
		set(value) {
			glfwSetCursorPos(ptr, value.first, value.second)
		}


	actual fun setMonitor(monitor: Monitor, xpos: Int, ypos: Int, width: Int, height: Int, refreshRate: Int) {
		glfwSetWindowMonitor(ptr, monitor.ptr, xpos, ypos, width, height, refreshRate)
	}

	actual fun setTitle(title: String) {
		glfwSetWindowTitle(ptr, title)
	}

	actual fun setSizeLimits(minWidth: Int, minHeight: Int, maxWidth: Int, maxHeight: Int) {
		glfwSetWindowSizeLimits(ptr, minWidth, minHeight, maxWidth, maxHeight)
	}

	actual fun setAspectRatio(number: Int, denom: Int) {
		glfwSetWindowAspectRatio(ptr, number, denom)
	}

	actual fun setIcon(images: Array<Image>) {
		@Suppress("UNREACHABLE_CODE")
		glfwSetWindowIcon(ptr, images.size, TODO())
	}

	actual fun setIcon(images: Collection<Image>) {
		@Suppress("UNREACHABLE_CODE")
		glfwSetWindowIcon(ptr, images.size, TODO())
	}

	actual fun maximize() {
		glfwMaximizeWindow(ptr)
	}

	actual fun setCursor(cursor: Cursor) {
		glfwSetCursor(ptr, cursor.ptr)
	}

	actual fun getKey(key: KeyboardKey): Action = Action.from(glfwGetKey(ptr, key.value))

	actual fun getKeyName(key: KeyboardKey): String? = glfwGetKeyName(key.value, 0)?.toKString()

	actual fun getKeyName(scancode: Int): String? = glfwGetKeyName(GLFW_KEY_UNKNOWN, scancode)?.toKString()

	actual fun getMouseButton(button: MouseButton): Action = Action.from(glfwGetMouseButton(ptr, button.value))

	actual fun swapBuffers() {
		glfwSwapBuffers(ptr)
	}

	private var windowPosCallback: ((Window, Int, Int) -> Unit)? = null
	private var windowSizeCallback: ((Window, Int, Int) -> Unit)? = null
	private var frameBufferCallback: ((Window, Int, Int) -> Unit)? = null
	private var windowCloseCallback: ((Window) -> Unit)? = null
	private var windowRefreshCallback: ((Window) -> Unit)? = null
	private var windowFocusCallback: ((Window, Boolean) -> Unit)? = null
	private var windowIconifyCallback: ((Window, Boolean) -> Unit)? = null
	private var cursorEnterCallback: ((Window, Boolean) -> Unit)? = null
	private var cursorPosCallback: ((Window, Double, Double) -> Unit)? = null
	private var scrollCallback: ((Window, Double, Double) -> Unit)? = null
	private var dropCallback: ((Window, Array<String>) -> Unit)? = null
	private var keyCallback: ((Window, KeyboardKey, Int, Action, Flag<Mod>) -> Unit)? = null
	private var mouseButtonCallback: ((Window, MouseButton, Action, Flag<Mod>) -> Unit)? = null
	private var charCallback: ((Window, UInt) -> Unit)? = null
	private var charModsCallback: ((Window, UInt, Flag<Mod>) -> Unit)? = null


	actual fun setPosCallback(callback: ((Window, Int, Int) -> Unit)?) {
		val wasNotPreviouslySet = windowPosCallback == null
		windowPosCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetWindowPosCallback(ptr, staticCFunction { window, width, height ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.windowPosCallback?.invoke(context, width, height)
				})
			}
		} else {
			glfwSetWindowPosCallback(ptr, null)
		}
	}

	actual fun setSizeCallback(callback: ((Window, Int, Int) -> Unit)?) {
		val wasNotPreviouslySet = windowSizeCallback == null
		windowSizeCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetWindowSizeCallback(ptr, staticCFunction { window, width, height ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.windowSizeCallback?.invoke(context, width, height)
				})
			}
		} else {
			glfwSetWindowSizeCallback(ptr, null)
		}
	}

	actual fun setCloseCallback(callback: ((Window) -> Unit)?) {
		val wasNotPreviouslySet = windowCloseCallback == null
		windowCloseCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetWindowCloseCallback(ptr, staticCFunction { window ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.windowCloseCallback?.invoke(context)
				})
			}
		} else {
			glfwSetWindowCloseCallback(ptr, null)
		}
	}

	actual fun setRefreshCallback(callback: ((Window) -> Unit)?) {
		val wasNotPreviouslySet = windowRefreshCallback == null
		windowRefreshCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetWindowRefreshCallback(ptr, staticCFunction { window ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.windowRefreshCallback?.invoke(context)
				})
			}
		} else {
			glfwSetWindowRefreshCallback(ptr, null)
		}
	}

	actual fun setFocusCallback(callback: ((Window, Boolean) -> Unit)?) {
		val wasNotPreviouslySet = windowFocusCallback == null
		windowFocusCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetWindowFocusCallback(ptr, staticCFunction { window, focused ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.windowFocusCallback?.invoke(context, focused == GLFW_TRUE)
				})
			}
		} else {
			glfwSetWindowFocusCallback(ptr, null)
		}
	}

	actual fun setIconifyCallback(callback: ((Window, Boolean) -> Unit)?) {
		val wasNotPreviouslySet = windowIconifyCallback == null
		windowIconifyCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetWindowIconifyCallback(ptr, staticCFunction { window, iconify ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.windowIconifyCallback?.invoke(context, iconify == GLFW_TRUE)
				})
			}
		} else {
			glfwSetWindowIconifyCallback(ptr, null)
		}
	}

	actual fun setCursorEnterCallback(callback: ((Window, Boolean) -> Unit)?) {
		val wasNotPreviouslySet = cursorEnterCallback == null
		cursorEnterCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetCursorEnterCallback(ptr, staticCFunction { window, entered ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.cursorEnterCallback?.invoke(context, entered == GLFW_TRUE)
				})
			}
		} else {
			glfwSetCursorEnterCallback(ptr, null)
		}
	}

	actual fun setScrollCallback(callback: ((Window, Double, Double) -> Unit)?) {
		val wasNotPreviouslySet = scrollCallback == null
		scrollCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetScrollCallback(ptr, staticCFunction { window, x, y ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.scrollCallback?.invoke(context, x, y)
				})
			}
		} else {
			glfwSetScrollCallback(ptr, null)
		}
	}

	actual fun setCursorPosCallback(callback: ((Window, Double, Double) -> Unit)?) {
		val wasNotPreviouslySet = cursorPosCallback == null
		cursorPosCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetCursorPosCallback(ptr, staticCFunction { window, width, height ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.cursorPosCallback?.invoke(context, width, height)
				})
			}
		} else {
			glfwSetCursorPosCallback(ptr, null)
		}
	}

	actual fun setFrameBufferCallback(callback: ((Window, Int, Int) -> Unit)?) {
		val wasNotPreviouslySet = frameBufferCallback == null
		frameBufferCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetFramebufferSizeCallback(ptr, staticCFunction { window, width, height ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.frameBufferCallback?.invoke(context, width, height)
				})
			}
		} else {
			glfwSetFramebufferSizeCallback(ptr, null)
		}
	}

	actual fun setDropCallback(callback: ((Window, Array<String>) -> Unit)?) {
		val wasNotPreviouslySet = dropCallback == null
		dropCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetDropCallback(ptr, staticCFunction { window, count, names ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.dropCallback?.invoke(context, Array(count) { names!![it]!!.toKString() })
				})
			}
		} else {
			glfwSetDropCallback(ptr, null)
		}
	}

	actual fun setKeyCallback(callback: ((Window, KeyboardKey, Int, Action, Flag<Mod>) -> Unit)?) {
		val wasNotPreviouslySet = keyCallback == null
		keyCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetKeyCallback(ptr, staticCFunction { window, key, scancode, action, mods ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.keyCallback?.invoke(
							context, KeyboardKey.from(key), scancode, Action.from(action), Flag(mods)
					)
				})
			}
		} else {
			glfwSetKeyCallback(ptr, null)
		}
	}

	actual fun setMouseButtonCallback(callback: ((Window, MouseButton, Action, Flag<Mod>) -> Unit)?) {
		val wasNotPreviouslySet = mouseButtonCallback == null
		mouseButtonCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetMouseButtonCallback(ptr, staticCFunction { window, button, action, mods ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.mouseButtonCallback?.invoke(
							context, MouseButton.from(button), Action.from(action), Flag(mods)
					)
				})
			}
		} else {
			glfwSetMouseButtonCallback(ptr, null)
		}
	}

	actual fun setCharCallback(callback: ((Window, UInt) -> Unit)?) {
		val wasNotPreviouslySet = charCallback == null
		charCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetCharCallback(ptr, staticCFunction { window, codepoint ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.charCallback?.invoke(context, codepoint)
				})
			}
		} else {
			glfwSetCharCallback(ptr, null)
		}
	}

	actual fun setCharModsCallback(callback: ((Window, UInt, Flag<Mod>) -> Unit)?) {
		val wasNotPreviouslySet = charModsCallback == null
		charModsCallback = callback

		if (callback != null) {
			if (wasNotPreviouslySet) {
				glfwSetCharModsCallback(ptr, staticCFunction { window, codepoint, mods ->
					val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
					context.charModsCallback?.invoke(context, codepoint, Flag(mods))
				})
			}
		} else {
			glfwSetCharModsCallback(ptr, null)
		}
	}

	override fun close() {
		glfwGetWindowUserPointer(ptr)!!.asStableRef<Window>().dispose()
		glfwDestroyWindow(ptr)
		glfwTerminate()
	}

	@ThreadLocal
	actual companion object {
		actual var currentContext: Window?
			get() = glfwGetCurrentContext()?.asStableRef<Window>()?.get()
			set(value) {
				glfwMakeContextCurrent(value?.ptr)
			}

		private var errorCallback: ((Int, String) -> Unit)? = null

		actual fun setErrorCallback(callback: ((Int, String) -> Unit)?) {
			val wasNotPreviouslySet = errorCallback == null
			errorCallback = callback

			if (callback != null) {
				if (wasNotPreviouslySet) {
					glfwSetErrorCallback(staticCFunction { error, description ->
						Window.errorCallback?.invoke(error, description!!.toKString())
					})
				}
			} else {
				glfwSetErrorCallback(null)
			}
		}

		actual inline operator fun invoke(
				width: Int,
				height: Int,
				title: String,
				monitor: Monitor?,
				share: Window?,
				block: Builder.() -> Unit
		): Window {
			check(glfwInit() == GLFW_TRUE) { "Could not init GLFW." }

			glfwDefaultWindowHints()
			Builder().block()

			return Window(glfwCreateWindow(width, height, title, monitor?.ptr, share?.ptr)
					?: throw Exception("Could not create window."))
		}
	}

	actual class Builder {
		actual var clientApi: ClientApi
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_CLIENT_API, value.value)
			}

		actual var contextCreationApi: CreationApi
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_CONTEXT_CREATION_API, value.value)
			}

		actual var contextVersionMajor: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, value)
			}

		actual var contextVersionMinor: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, value)
			}

		actual var contextRobustness: Robustness
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_CONTEXT_ROBUSTNESS, value.value)
			}

		actual var releaseBehaviour: ReleaseBehaviour
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_CONTEXT_RELEASE_BEHAVIOR, value.value)
			}

		actual var openGLForwardCompat: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var openGLDebugContext: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var openGLProfile: OpenGLProfile
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_OPENGL_PROFILE, value.value)
			}

		actual var redBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_RED_BITS, value)
			}

		actual var greenBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_GREEN_BITS, value)
			}

		actual var blueBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_BLUE_BITS, value)
			}

		actual var alphaBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_ALPHA_BITS, value)
			}

		actual var depthBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_DEPTH_BITS, value)
			}

		actual var stencilBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_STENCIL_BITS, value)
			}

		actual var accumRedBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_ACCUM_RED_BITS, value)
			}

		actual var accumGreenBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_ACCUM_GREEN_BITS, value)
			}

		actual var accumBlueBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_ACCUM_BLUE_BITS, value)
			}

		actual var accumAlphaBits: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_ACCUM_ALPHA_BITS, value)
			}

		actual var samples: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_SAMPLES, value)
			}

		actual var stereo: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_STEREO, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var srgbCapable: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_SRGB_CAPABLE, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var doubleBuffer: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_DOUBLEBUFFER, if (value) GLFW_TRUE else GLFW_FALSE)
			}


		actual var resizable: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_RESIZABLE, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var visible: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_VISIBLE, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var decorated: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_DECORATED, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var focused: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_FOCUSED, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var autoIconfiy: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_AUTO_ICONIFY, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var floating: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_FLOATING, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var maximized: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_MAXIMIZED, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var refreshRate: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_REFRESH_RATE, value)
			}
	}
}
