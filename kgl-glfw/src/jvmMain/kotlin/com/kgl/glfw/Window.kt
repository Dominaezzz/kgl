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

import com.kgl.core.*
import io.ktor.utils.io.core.*
import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.*
import org.lwjgl.system.jni.*

actual class Window private constructor(val ptr: Long) : Closeable {
	actual constructor(
		width: Int,
		height: Int,
		title: String,
		monitor: Monitor?,
		share: Window?
	) : this(
		glfwCreateWindow(width, height, title, monitor?.ptr ?: 0L, share?.ptr ?: 0L)
			.takeIf { it != 0L }
			?: throw Exception("Could not create window.")
	)

	init {
		check(glfwGetWindowUserPointer(ptr) == 0L) {
			"Must not already have a userPointer"
		}

		val globalRef = JNINativeInterface.NewGlobalRef(this)
		glfwSetWindowUserPointer(ptr, globalRef)
	}

	actual var position: Pair<Int, Int>
		get() = MemoryStack.stackPush().use {
			val width = it.mallocInt(1)
			val height = it.mallocInt(1)
			glfwGetWindowPos(ptr, width, height)
			width[0] to height[0]
		}
		set(value) {
			glfwSetWindowPos(ptr, value.first, value.second)
		}

	actual var size: Pair<Int, Int>
		get() = MemoryStack.stackPush().use {
			val width = it.mallocInt(1)
			val height = it.mallocInt(1)
			glfwGetWindowSize(ptr, width, height)
			width[0] to height[0]
		}
		set(value) {
			glfwSetWindowSize(ptr, value.first, value.second)
		}

	actual val frameBufferSize: Pair<Int, Int>
		get() = MemoryStack.stackPush().use {
			val width = it.mallocInt(1)
			val height = it.mallocInt(1)
			glfwGetFramebufferSize(ptr, width, height)
			width[0] to height[0]
		}

	actual val contentScale: Pair<Float, Float>
		get() = MemoryStack.stackPush().use {
			val width = it.mallocFloat(1)
			val height = it.mallocFloat(1)
			glfwGetWindowContentScale(ptr, width, height)
			width[0] to height[0]
		}

	actual val frameSize: FrameSize
		get() = MemoryStack.stackPush().use {
			val left = it.mallocInt(1)
			val top = it.mallocInt(1)
			val right = it.mallocInt(1)
			val bottom = it.mallocInt(1)
			glfwGetWindowFrameSize(ptr, left, top, right, bottom)
			FrameSize(left[0], top[0], right[0], bottom[0])
		}

	actual var clipboardString: String?
		get() = glfwGetClipboardString(ptr)
		set(value) {
			glfwSetClipboardString(ptr, value ?: "")
		}

	actual var shouldClose: Boolean
		get() = glfwWindowShouldClose(ptr)
		set(value) {
			glfwSetWindowShouldClose(ptr, value)
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

	actual val hasTransparentFramebuffer: Boolean
		get() = glfwGetWindowAttrib(ptr, GLFW_TRANSPARENT_FRAMEBUFFER) == GLFW_TRUE

	actual val clientApi: ClientApi
		get() = ClientApi.from(glfwGetWindowAttrib(ptr, GLFW_CLIENT_API))

	actual val contextCreationApi: CreationApi
		get() = CreationApi.from(glfwGetWindowAttrib(ptr, GLFW_CONTEXT_CREATION_API))

	actual val contextVersionMajor: Int
		get() = glfwGetWindowAttrib(ptr, GLFW_CONTEXT_VERSION_MAJOR)

	actual val contextVersionMinor: Int
		get() = glfwGetWindowAttrib(ptr, GLFW_CONTEXT_VERSION_MINOR)

	actual val contextRevision: Int
		get() = glfwGetWindowAttrib(ptr, GLFW_CONTEXT_REVISION)

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

	actual val monitor: Monitor? get() = glfwGetWindowMonitor(ptr).takeIf { it != 0L }?.let { Monitor(it) }

	actual var cursorPosition: Pair<Double, Double>
		get() = MemoryStack.stackPush().use {
			val width = it.mallocDouble(1)
			val height = it.mallocDouble(1)
			glfwGetCursorPos(ptr, width, height)
			width[0] to height[0]
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

	actual fun setMonitor(monitor: Monitor?, xpos: Int, ypos: Int, width: Int, height: Int, refreshRate: Int) {
		glfwSetWindowMonitor(ptr, monitor?.ptr ?: 0L, xpos, ypos, width, height, refreshRate)
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
		MemoryStack.stackPush()
		try {
			val imageArray = GLFWImage.callocStack(images.size)
			images.forEachIndexed { index, image ->
				imageArray[index].set(image.width, image.height, image.pixels.buffer)
			}
			glfwSetWindowIcon(ptr, imageArray)
		} finally {
			MemoryStack.stackPop()
		}
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
		glfwSetCursor(ptr, cursor?.ptr ?: 0)
	}

	actual fun getKey(key: KeyboardKey): Action = Action.from(glfwGetKey(ptr, key.value))

	actual fun getMouseButton(button: MouseButton): Action = Action.from(glfwGetMouseButton(ptr, button.value))

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

	private inline fun <TCallback, TNativeCallback : Callback, TNativeCallbackI : CallbackI> setCallback(
		callback: TCallback?,
		propGetter: () -> TCallback?,
		propSetter: (TCallback?) -> Unit,
		realSetter: (Long, TNativeCallbackI?) -> TNativeCallback?,
		getNativeCallback: () -> TNativeCallbackI
	): TCallback? {
		val previous = propGetter()
		propSetter(callback)

		if (callback != null) {
			// only set the native callback if there was no callback set,
			// since the native callback has a reference to the property
			if (previous == null) {
				realSetter(ptr, getNativeCallback())?.free()
			}
		} else {
			realSetter(ptr, null)?.free()
		}

		return previous
	}

	actual fun setPosCallback(callback: WindowPosCallback?): WindowPosCallback? {
		return setCallback(callback, { windowPosCallback }, { windowPosCallback = it }, ::glfwSetWindowPosCallback) {
			GLFWWindowPosCallbackI { window, x, y ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowPosCallback?.invoke(context, x, y)
			}
		}
	}

	actual fun setSizeCallback(callback: WindowSizeCallback?): WindowSizeCallback? {
		return setCallback(callback, { windowSizeCallback }, { windowSizeCallback = it }, ::glfwSetWindowSizeCallback) {
			GLFWWindowSizeCallbackI { window, width, height ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowSizeCallback?.invoke(context, width, height)
			}
		}
	}

	actual fun setFrameBufferCallback(callback: FrameBufferCallback?): FrameBufferCallback? {
		return setCallback(
			callback,
			{ frameBufferCallback },
			{ frameBufferCallback = it },
			::glfwSetFramebufferSizeCallback
		) {
			GLFWFramebufferSizeCallbackI { window, width, height ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.frameBufferCallback?.invoke(context, width, height)
			}
		}
	}

	actual fun setCloseCallback(callback: WindowCloseCallback?): WindowCloseCallback? {
		return setCallback(
			callback,
			{ windowCloseCallback },
			{ windowCloseCallback = it },
			::glfwSetWindowCloseCallback
		) {
			GLFWWindowCloseCallbackI { window ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowCloseCallback?.invoke(context)
			}
		}
	}

	actual fun setRefreshCallback(callback: WindowRefreshCallback?): WindowRefreshCallback? {
		return setCallback(
			callback,
			{ windowRefreshCallback },
			{ windowRefreshCallback = it },
			::glfwSetWindowRefreshCallback
		) {
			GLFWWindowRefreshCallbackI { window ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowRefreshCallback?.invoke(context)
			}
		}
	}

	actual fun setFocusCallback(callback: WindowFocusCallback?): WindowFocusCallback? {
		return setCallback(
			callback,
			{ windowFocusCallback },
			{ windowFocusCallback = it },
			::glfwSetWindowFocusCallback
		) {
			GLFWWindowFocusCallbackI { window, focused ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowFocusCallback?.invoke(context, focused)
			}
		}
	}

	actual fun setIconifyCallback(callback: WindowIconifyCallback?): WindowIconifyCallback? {
		return setCallback(
			callback,
			{ windowIconifyCallback },
			{ windowIconifyCallback = it },
			::glfwSetWindowIconifyCallback
		) {
			GLFWWindowIconifyCallbackI { window, iconified ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowIconifyCallback?.invoke(context, iconified)
			}
		}
	}

	actual fun setMaximizeCallback(callback: WindowMaximizeCallback?): WindowMaximizeCallback? {
		return setCallback(
			callback,
			{ windowMaximizeCallback },
			{ windowMaximizeCallback = it },
			::glfwSetWindowMaximizeCallback
		) {
			GLFWWindowMaximizeCallbackI { window, maximized ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowMaximizeCallback?.invoke(context, maximized)
			}
		}
	}

	actual fun setContentScaleCallback(callback: WindowContentScaleCallback?): WindowContentScaleCallback? {
		return setCallback(
			callback,
			{ windowContentScaleCallback },
			{ windowContentScaleCallback = it },
			::glfwSetWindowContentScaleCallback
		) {
			GLFWWindowContentScaleCallbackI { window, x, y ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.windowContentScaleCallback?.invoke(context, x, y)
			}
		}
	}

	actual fun setCursorEnterCallback(callback: CursorEnterCallback?): CursorEnterCallback? {
		return setCallback(
			callback,
			{ cursorEnterCallback },
			{ cursorEnterCallback = it },
			::glfwSetCursorEnterCallback
		) {
			GLFWCursorEnterCallbackI { window, entered ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.cursorEnterCallback?.invoke(context, entered)
			}
		}
	}

	actual fun setScrollCallback(callback: ScrollCallback?): ScrollCallback? {
		return setCallback(callback, { scrollCallback }, { scrollCallback = it }, ::glfwSetScrollCallback) {
			GLFWScrollCallbackI { window, x, y ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.scrollCallback?.invoke(context, x, y)
			}
		}
	}

	actual fun setCursorPosCallback(callback: CursorPosCallback?): CursorPosCallback? {
		return setCallback(callback, { cursorPosCallback }, { cursorPosCallback = it }, ::glfwSetCursorPosCallback) {
			GLFWCursorPosCallbackI { window, x, y ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.cursorPosCallback?.invoke(context, x, y)
			}
		}
	}

	actual fun setDropCallback(callback: DropCallback?): DropCallback? {
		return setCallback(callback, { dropCallback }, { dropCallback = it }, ::glfwSetDropCallback) {
			GLFWDropCallbackI { window, count, names ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				val pNames = PointerBuffer.create(names, count)
				context.dropCallback?.invoke(context, Array(count) { MemoryUtil.memUTF8(pNames[it]) })
			}
		}
	}

	actual fun setKeyCallback(callback: KeyCallback?): KeyCallback? {
		return setCallback(callback, { keyCallback }, { keyCallback = it }, ::glfwSetKeyCallback) {
			GLFWKeyCallbackI { window, key, scancode, action, mods ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.keyCallback?.invoke(context, KeyboardKey.from(key), scancode, Action.from(action), Flag(mods))
			}
		}
	}

	actual fun setMouseButtonCallback(callback: MouseButtonCallback?): MouseButtonCallback? {
		return setCallback(
			callback,
			{ mouseButtonCallback },
			{ mouseButtonCallback = it },
			::glfwSetMouseButtonCallback
		) {
			GLFWMouseButtonCallbackI { window, button, action, mods ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.mouseButtonCallback?.invoke(context, MouseButton.from(button), Action.from(action), Flag(mods))
			}
		}
	}

	actual fun setCharCallback(callback: CharCallback?): CharCallback? {
		return setCallback(callback, { charCallback }, { charCallback = it }, ::glfwSetCharCallback) {
			GLFWCharCallbackI { window, codepoint ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.charCallback?.invoke(context, codepoint.toUInt())
			}
		}
	}

	actual fun setCharModsCallback(callback: CharModsCallback?): CharModsCallback? {
		return setCallback(callback, { charModsCallback }, { charModsCallback = it }, ::glfwSetCharModsCallback) {
			GLFWCharModsCallbackI { window, codepoint, mods ->
				val context = MemoryUtil.memGlobalRefToObject<Window>(glfwGetWindowUserPointer(window))
				context.charModsCallback?.invoke(context, codepoint.toUInt(), Flag(mods))
			}
		}
	}

	actual fun swapBuffers() {
		glfwSwapBuffers(ptr)
	}

	override fun close() {
		// Free potentially allocated callbacks.
		glfwSetWindowPosCallback(ptr, null)?.free()
		glfwSetWindowSizeCallback(ptr, null)?.free()
		glfwSetFramebufferSizeCallback(ptr, null)?.free()
		glfwSetWindowCloseCallback(ptr, null)?.free()
		glfwSetWindowRefreshCallback(ptr, null)?.free()
		glfwSetWindowFocusCallback(ptr, null)?.free()
		glfwSetWindowIconifyCallback(ptr, null)?.free()
		glfwSetWindowMaximizeCallback(ptr, null)?.free()
		glfwSetWindowContentScaleCallback(ptr, null)?.free()
		glfwSetCursorEnterCallback(ptr, null)?.free()
		glfwSetScrollCallback(ptr, null)?.free()
		glfwSetCursorPosCallback(ptr, null)?.free()
		glfwSetDropCallback(ptr, null)?.free()
		glfwSetKeyCallback(ptr, null)?.free()
		glfwSetMouseButtonCallback(ptr, null)?.free()
		glfwSetCharCallback(ptr, null)?.free()
		glfwSetCharModsCallback(ptr, null)?.free()

		val globalRef = glfwGetWindowUserPointer(ptr)
		JNINativeInterface.DeleteGlobalRef(globalRef)

		glfwDestroyWindow(ptr)
	}
}
