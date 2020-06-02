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
import cnames.structs.GLFWwindow
import com.kgl.core.Flag
import com.kgl.core.VirtualStack
import io.ktor.utils.io.core.Closeable
import kotlinx.cinterop.*
import kotlin.native.concurrent.ensureNeverFrozen

actual class Window @PublishedApi internal constructor(val ptr: CPointer<GLFWwindow>) : Closeable {

	init {
		ensureNeverFrozen()

		check(glfwGetWindowUserPointer(ptr) == null) {
			"Must not already have a userPointer"
		}

		glfwSetWindowUserPointer(ptr, StableRef.create(this).asCPointer())
	}

	actual var position: Pair<Int, Int>
		get() {
			VirtualStack.push()
			return try {
				val width = VirtualStack.alloc<IntVar>()
				val height = VirtualStack.alloc<IntVar>()
				glfwGetWindowPos(ptr, width.ptr, height.ptr)
				width.value to height.value
			} finally {
				VirtualStack.pop()
			}
		}
		set(value) {
			glfwSetWindowPos(ptr, value.first, value.second)
		}

	actual var size: Pair<Int, Int>
		get() {
			VirtualStack.push()
			return try {
				val width = VirtualStack.alloc<IntVar>()
				val height = VirtualStack.alloc<IntVar>()
				glfwGetWindowSize(ptr, width.ptr, height.ptr)
				width.value to height.value
			} finally {
				VirtualStack.pop()
			}
		}
		set(value) {
			glfwSetWindowSize(ptr, value.first, value.second)
		}

	actual val frameBufferSize: Pair<Int, Int>
		get() {
			VirtualStack.push()
			return try {
				val width = VirtualStack.alloc<IntVar>()
				val height = VirtualStack.alloc<IntVar>()
				glfwGetFramebufferSize(ptr, width.ptr, height.ptr)
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
				glfwGetWindowContentScale(ptr, width.ptr, height.ptr)
				width.value to height.value
			} finally {
				VirtualStack.pop()
			}
		}

	actual val frameSize: FrameSize
		get() {
			VirtualStack.push()
			return try {
				val left = VirtualStack.alloc<IntVar>()
				val top = VirtualStack.alloc<IntVar>()
				val right = VirtualStack.alloc<IntVar>()
				val bottom = VirtualStack.alloc<IntVar>()
				glfwGetWindowFrameSize(ptr, left.ptr, top.ptr, right.ptr, bottom.ptr)
				FrameSize(left.value, top.value, right.value, bottom.value)
			} finally {
				VirtualStack.pop()
			}
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

	actual var isDecorated: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_DECORATED) == GLFW_TRUE
		set(value) {
			glfwSetWindowAttrib(ptr, GLFW_DECORATED, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var isResizable: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_RESIZABLE) == GLFW_TRUE
		set(value) {
			glfwSetWindowAttrib(ptr, GLFW_RESIZABLE, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var isFloating: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_FLOATING) == GLFW_TRUE
		set(value) {
			glfwSetWindowAttrib(ptr, GLFW_FLOATING, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var isAutoIconify: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_AUTO_ICONIFY) == GLFW_TRUE
		set(value) {
			glfwSetWindowAttrib(ptr, GLFW_AUTO_ICONIFY, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var isFocusOnShow: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_FOCUS_ON_SHOW) == GLFW_TRUE
		set(value) {
			glfwSetWindowAttrib(ptr, GLFW_FOCUS_ON_SHOW, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual val isFocused: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_FOCUSED) == GLFW_TRUE

	actual val isMaximized: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_MAXIMIZED) == GLFW_TRUE

	actual val isHovered: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_HOVERED) == GLFW_TRUE

	actual val isTransparentFramebuffer: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_TRANSPARENT_FRAMEBUFFER) == GLFW_TRUE

	actual val clientApi: ClientApi
		get() = ClientApi.from(glfwGetWindowAttrib(ptr, GLFW_CLIENT_API))

	actual val contextCreationApi: CreationApi
		get() = CreationApi.from(glfwGetWindowAttrib(ptr, GLFW_CONTEXT_CREATION_API))

	actual val openGLForwardCompat: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_OPENGL_FORWARD_COMPAT) == GLFW_TRUE

	actual val openGLDebugContext: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_OPENGL_DEBUG_CONTEXT) == GLFW_TRUE

	actual val openGLProfile: OpenGLProfile
		get() = OpenGLProfile.from(glfwGetWindowAttrib(ptr, GLFW_OPENGL_PROFILE))

	actual val contextReleaseBehaviour: ReleaseBehaviour
		get() = ReleaseBehaviour.from(glfwGetWindowAttrib(ptr, GLFW_CONTEXT_RELEASE_BEHAVIOR))

	actual val contextNoError: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_CONTEXT_NO_ERROR) == GLFW_TRUE

	actual val contextRobustness: Robustness
		get() = Robustness.from(glfwGetWindowAttrib(ptr, GLFW_CONTEXT_ROBUSTNESS))

	actual val monitor: Monitor? get() = glfwGetWindowMonitor(ptr)?.let { Monitor(it) }

	actual var cursorPosition: Pair<Double, Double>
		get() {
			VirtualStack.push()
			return try {
				val width = VirtualStack.alloc<DoubleVar>()
				val height = VirtualStack.alloc<DoubleVar>()
				glfwGetCursorPos(ptr, width.ptr, height.ptr)
				width.value to height.value
			} finally {
				VirtualStack.pop()
			}
		}
		set(value) {
			glfwSetCursorPos(ptr, value.first, value.second)
		}

	actual var opacity: Float
		get() = glfwGetWindowOpacity(ptr)
		set(value) {
			glfwSetWindowOpacity(ptr, value)
		}

	actual var cursorMode: CursorMode
		get() = CursorMode.from(glfwGetInputMode(ptr, GLFW_CURSOR))
		set(value) {
			glfwSetInputMode(ptr, GLFW_CURSOR, value.value)
		}

	actual var stickyKeysEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_STICKY_KEYS) == GLFW_TRUE
		set(value) {
			glfwSetInputMode(ptr, GLFW_STICKY_KEYS, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var stickyMouseButtonsEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_STICKY_MOUSE_BUTTONS) == GLFW_TRUE
		set(value) {
			glfwSetInputMode(ptr, GLFW_STICKY_MOUSE_BUTTONS, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var lockKeyModsEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_LOCK_KEY_MODS) == GLFW_TRUE
		set(value) {
			glfwSetInputMode(ptr, GLFW_LOCK_KEY_MODS, if (value) GLFW_TRUE else GLFW_FALSE)
		}

	actual var rawMouseButtonEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_RAW_MOUSE_MOTION) == GLFW_TRUE
		set(value) {
			glfwSetInputMode(ptr, GLFW_RAW_MOUSE_MOTION, if (value) GLFW_TRUE else GLFW_FALSE)
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
		glfwSetWindowIcon(ptr, images.size, createValues(images.size) { index ->
			val image = images[index]
			pixels = image.pixels.pointer.reinterpret()
			width = image.width
			height = image.height
		})
	}

	actual fun maximize() {
		glfwMaximizeWindow(ptr)
	}

	actual fun focus() {
		glfwFocusWindow(ptr)
	}

	actual fun requestAttention() {
		glfwRequestWindowAttention(ptr)
	}

	actual fun setCursor(cursor: Cursor?) {
		glfwSetCursor(ptr, cursor?.ptr)
	}

	actual fun getKey(key: KeyboardKey): Action = Action.from(glfwGetKey(ptr, key.value))

	actual fun getMouseButton(button: MouseButton): Action = Action.from(glfwGetMouseButton(ptr, button.value))

	actual fun swapBuffers() {
		glfwSwapBuffers(ptr)
	}

	private var windowPosCallback: WindowPosCallback? = null
	private var windowSizeCallback: WindowSizeCallback? = null
	private var frameBufferCallback: FrameBufferCallback? = null
	private var windowCloseCallback: WindowCloseCallback? = null
	private var windowRefreshCallback: WindowRefreshCallback? = null
	private var windowFocusCallback: WindowFocusCallback? = null
	private var windowMaximizeCallback: WindowMaximizeCallback? = null
	private var windowIconifyCallback: WindowIconifyCallback? = null
	private var cursorEnterCallback: CursorEnterCallback? = null
	private var windowContentScaleCallback: WindowContentScaleCallback? = null
	private var cursorPosCallback: CursorPosCallback? = null
	private var scrollCallback: ScrollCallback? = null
	private var dropCallback: DropCallback? = null
	private var keyCallback: KeyCallback? = null
	private var mouseButtonCallback: MouseButtonCallback? = null
	private var charCallback: CharCallback? = null
	private var charModsCallback: CharModsCallback? = null

	private inline fun <TCallback, TNativeCallback> setCallback(
			callback: TCallback?,
			propGetter: () -> TCallback?,
			propSetter: (TCallback?) -> Unit,
			realSetter: (CValuesRef<GLFWwindow>?, TNativeCallback?) -> TNativeCallback?,
			getCFunction: () -> TNativeCallback?
	): TCallback? {
		val previous = propGetter()
		propSetter(callback)

		if (callback != null) {
			realSetter(ptr, getCFunction())
		} else {
			realSetter(ptr, null)
		}

		return previous
	}

	actual fun setPosCallback(callback: WindowPosCallback?): WindowPosCallback? {
		return setCallback(callback, { windowPosCallback }, { windowPosCallback = it }, ::glfwSetWindowPosCallback) {
			staticCFunction { window, x, y ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowPosCallback?.invoke(context, x, y)
			}
		}
	}

	actual fun setSizeCallback(callback: WindowSizeCallback?): WindowSizeCallback? {
		return setCallback(callback, { windowSizeCallback }, { windowSizeCallback = it }, ::glfwSetWindowSizeCallback) {
			staticCFunction { window, width, height ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowSizeCallback?.invoke(context, width, height)
			}
		}
	}

	actual fun setCloseCallback(callback: WindowCloseCallback?): WindowCloseCallback? {
		return setCallback(callback, { windowCloseCallback }, { windowCloseCallback = it }, ::glfwSetWindowCloseCallback) {
			staticCFunction { window ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowCloseCallback?.invoke(context)
			}
		}
	}

	actual fun setRefreshCallback(callback: WindowRefreshCallback?): WindowRefreshCallback? {
		return setCallback(callback, { windowRefreshCallback }, { windowRefreshCallback = it }, ::glfwSetWindowRefreshCallback) {
			staticCFunction { window ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowRefreshCallback?.invoke(context)
			}
		}
	}

	actual fun setFocusCallback(callback: WindowFocusCallback?): WindowFocusCallback? {
		return setCallback(callback, { windowFocusCallback }, { windowFocusCallback = it }, ::glfwSetWindowFocusCallback) {
			staticCFunction { window, focused ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowFocusCallback?.invoke(context, focused == GLFW_TRUE)
			}
		}
	}

	actual fun setIconifyCallback(callback: WindowIconifyCallback?): WindowIconifyCallback? {
		return setCallback(callback, { windowIconifyCallback }, { windowIconifyCallback = it }, ::glfwSetWindowIconifyCallback) {
			staticCFunction { window, iconified ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowIconifyCallback?.invoke(context, iconified == GLFW_TRUE)
			}
		}
	}

	actual fun setMaximizeCallback(callback: WindowMaximizeCallback?): WindowMaximizeCallback? {
		return setCallback(callback, { windowMaximizeCallback }, { windowMaximizeCallback = it }, ::glfwSetWindowMaximizeCallback) {
			staticCFunction { window, maximized ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowMaximizeCallback?.invoke(context, maximized == GLFW_TRUE)
			}
		}
	}

	actual fun setContentScaleCallback(callback: WindowContentScaleCallback?): WindowContentScaleCallback? {
		return setCallback(callback, { windowContentScaleCallback }, { windowContentScaleCallback = it }, ::glfwSetWindowContentScaleCallback) {
			staticCFunction { window, x, y ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.windowContentScaleCallback?.invoke(context, x, y)
			}
		}
	}

	actual fun setCursorEnterCallback(callback: CursorEnterCallback?): CursorEnterCallback? {
		return setCallback(callback, { cursorEnterCallback }, { cursorEnterCallback = it }, ::glfwSetCursorEnterCallback) {
			staticCFunction { window, entered ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.cursorEnterCallback?.invoke(context, entered == GLFW_TRUE)
			}
		}
	}

	actual fun setScrollCallback(callback: ScrollCallback?): ScrollCallback? {
		return setCallback(callback, { scrollCallback }, { scrollCallback = it }, ::glfwSetScrollCallback) {
			staticCFunction { window, x, y ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.scrollCallback?.invoke(context, x, y)
			}
		}
	}

	actual fun setCursorPosCallback(callback: CursorPosCallback?): CursorPosCallback? {
		return setCallback(callback, { cursorPosCallback }, { cursorPosCallback = it }, ::glfwSetCursorPosCallback) {
			staticCFunction { window, width, height ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.cursorPosCallback?.invoke(context, width, height)
			}
		}
	}

	actual fun setFrameBufferCallback(callback: FrameBufferCallback?): FrameBufferCallback? {
		return setCallback(callback, { frameBufferCallback }, { frameBufferCallback = it }, ::glfwSetFramebufferSizeCallback) {
			staticCFunction { window, width, height ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.frameBufferCallback?.invoke(context, width, height)
			}
		}
	}

	actual fun setDropCallback(callback: DropCallback?): DropCallback? {
		return setCallback(callback, { dropCallback }, { dropCallback = it }, ::glfwSetDropCallback) {
			staticCFunction { window, count, names ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				checkNotNull(names)
				context.dropCallback?.invoke(context, Array(count) { names[it]!!.toKString() })
			}
		}
	}

	actual fun setKeyCallback(callback: KeyCallback?): KeyCallback? {
		return setCallback(callback, { keyCallback }, { keyCallback = it }, ::glfwSetKeyCallback) {
			staticCFunction { window, key, scancode, action, mods ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.keyCallback?.invoke(
						context, KeyboardKey.from(key), scancode, Action.from(action), Flag(mods)
				)
			}
		}
	}

	actual fun setMouseButtonCallback(callback: MouseButtonCallback?): MouseButtonCallback? {
		return setCallback(callback, { mouseButtonCallback }, { mouseButtonCallback = it }, ::glfwSetMouseButtonCallback) {
			staticCFunction { window, button, action, mods ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.mouseButtonCallback?.invoke(
						context, MouseButton.from(button), Action.from(action), Flag(mods)
				)
			}
		}
	}

	actual fun setCharCallback(callback: CharCallback?): CharCallback? {
		return setCallback(callback, { charCallback }, { charCallback = it }, ::glfwSetCharCallback) {
			staticCFunction { window, codepoint ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.charCallback?.invoke(context, codepoint)
			}
		}
	}

	actual fun setCharModsCallback(callback: CharModsCallback?): CharModsCallback? {
		return setCallback(callback, { charModsCallback }, { charModsCallback = it }, ::glfwSetCharModsCallback) {
			staticCFunction { window, codepoint, mods ->
				val context = glfwGetWindowUserPointer(window)!!.asStableRef<Window>().get()
				context.charModsCallback?.invoke(context, codepoint, Flag(mods))
			}
		}
	}

	override fun close() {
		glfwGetWindowUserPointer(ptr)!!.asStableRef<Window>().dispose()
		glfwDestroyWindow(ptr)
		glfwTerminate()
	}

	actual companion object {
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

		actual var autoIconify: Boolean
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

		actual var transparentFramebuffer: Boolean
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, if (value) GLFW_TRUE else GLFW_FALSE)
			}

		actual var refreshRate: Int
			get() = TODO("Querying window hints is not supported")
			set(value) {
				glfwWindowHint(GLFW_REFRESH_RATE, value)
			}
	}
}
