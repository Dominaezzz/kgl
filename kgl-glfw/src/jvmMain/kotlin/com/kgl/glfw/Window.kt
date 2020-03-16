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

import com.kgl.core.Flag
import io.ktor.utils.io.core.Closeable
import org.lwjgl.PointerBuffer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.jni.JNINativeInterface

actual class Window @PublishedApi internal constructor(val ptr: Long) : Closeable {

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
		set(value) { glfwSetWindowOpacity(ptr, value) }

	actual var cursorMode: CursorMode
		get() = CursorMode.from(glfwGetInputMode(ptr, GLFW_CURSOR))
		set(value) { glfwSetInputMode(ptr, GLFW_CURSOR, value.value) }

	actual var stickyKeysEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_STICKY_KEYS) == GLFW_TRUE
		set(value) { glfwSetInputMode(ptr, GLFW_STICKY_KEYS, if (value) GLFW_TRUE else GLFW_FALSE) }

	actual var stickyMouseButtonsEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_STICKY_MOUSE_BUTTONS) == GLFW_TRUE
		set(value) { glfwSetInputMode(ptr, GLFW_STICKY_MOUSE_BUTTONS, if (value) GLFW_TRUE else GLFW_FALSE) }

	actual var lockKeyModsEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_LOCK_KEY_MODS) == GLFW_TRUE
		set(value) { glfwSetInputMode(ptr, GLFW_LOCK_KEY_MODS, if (value) GLFW_TRUE else GLFW_FALSE) }

	actual var rawMouseButtonEnabled: Boolean
		get() = glfwGetInputMode(ptr, GLFW_RAW_MOUSE_MOTION) == GLFW_TRUE
		set(value) { glfwSetInputMode(ptr, GLFW_RAW_MOUSE_MOTION, if (value) GLFW_TRUE else GLFW_FALSE) }

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

	actual fun setPosCallback(callback: WindowPosCallback?) {
		if (callback != null) {
			glfwSetWindowPosCallback(ptr) { _, x, y ->
				callback(this, x, y)
			}
		} else {
			glfwSetWindowPosCallback(ptr, null)
		}?.free()
	}

	actual fun setSizeCallback(callback: WindowSizeCallback?) {
		if (callback != null) {
			glfwSetWindowSizeCallback(ptr) { _, width, height ->
				callback(this, width, height)
			}
		} else {
			glfwSetWindowSizeCallback(ptr, null)
		}?.free()
	}

	actual fun setFrameBufferCallback(callback: FrameBufferCallback?) {
		if (callback != null) {
			glfwSetFramebufferSizeCallback(ptr) { _, width, height ->
				callback(this, width, height)
			}
		} else {
			glfwSetFramebufferSizeCallback(ptr, null)
		}?.free()
	}

	actual fun setCloseCallback(callback: WindowCloseCallback?) {
		if (callback != null) {
			glfwSetWindowCloseCallback(ptr) {
				callback(this)
			}
		} else {
			glfwSetWindowCloseCallback(ptr, null)
		}?.free()
	}

	actual fun setRefreshCallback(callback: WindowRefreshCallback?) {
		if (callback != null) {
			glfwSetWindowRefreshCallback(ptr) {
				callback(this)
			}
		} else {
			glfwSetWindowRefreshCallback(ptr, null)
		}?.free()
	}

	actual fun setFocusCallback(callback: WindowFocusCallback?) {
		if (callback != null) {
			glfwSetWindowFocusCallback(ptr) { _, focused ->
				callback(this, focused)
			}
		} else {
			glfwSetWindowFocusCallback(ptr, null)
		}?.free()
	}

	actual fun setIconifyCallback(callback: WindowIconifyCallback?) {
		if (callback != null) {
			glfwSetWindowIconifyCallback(ptr) { _, focused ->
				callback(this, focused)
			}
		} else {
			glfwSetWindowIconifyCallback(ptr, null)
		}?.free()
	}

	actual fun setMaximizeCallback(callback: WindowMaximizeCallback?) {
		if (callback != null) {
			glfwSetWindowMaximizeCallback(ptr) { _, maximized ->
				callback(this, maximized)
			}
		} else {
			glfwSetWindowMaximizeCallback(ptr, null)
		}?.free()
	}

	actual fun setContentScaleCallback(callback: WindowContentScaleCallback?) {
		if (callback != null) {
			glfwSetWindowContentScaleCallback(ptr) { _, xscale, yscale ->
				callback(this, xscale, yscale)
			}
		} else {
			glfwSetWindowContentScaleCallback(ptr, null)
		}?.free()
	}

	actual fun setCursorEnterCallback(callback: CursorEnterCallback?) {
		if (callback != null) {
			glfwSetCursorEnterCallback(ptr) { _, focused ->
				callback(this, focused)
			}
		} else {
			glfwSetCursorEnterCallback(ptr, null)
		}?.free()
	}

	actual fun setScrollCallback(callback: ScrollCallback?) {
		if (callback != null) {
			glfwSetScrollCallback(ptr) { _, x, y ->
				callback(this, x, y)
			}
		} else {
			glfwSetScrollCallback(ptr, null)
		}?.free()
	}

	actual fun setCursorPosCallback(callback: CursorPosCallback?) {
		if (callback != null) {
			glfwSetCursorPosCallback(ptr) { _, x, y ->
				callback(this, x, y)
			}
		} else {
			glfwSetCursorPosCallback(ptr, null)
		}?.free()
	}

	actual fun setDropCallback(callback: DropCallback?) {
		if (callback != null) {
			glfwSetDropCallback(ptr) { _, count, names ->
				val pNames = PointerBuffer.create(names, count)
				callback(this, Array(count) { MemoryUtil.memUTF8(pNames[it]) })
			}
		} else {
			glfwSetDropCallback(ptr, null)
		}?.free()
	}

	actual fun setKeyCallback(callback: KeyCallback?) {
		if (callback != null) {
			glfwSetKeyCallback(ptr) { _, key, scancode, action, mods ->
				callback(this, KeyboardKey.from(key), scancode, Action.from(action), Flag(mods))
			}
		} else {
			glfwSetKeyCallback(ptr, null)
		}?.free()
	}

	actual fun setMouseButtonCallback(callback: MouseButtonCallback?) {
		if (callback != null) {
			glfwSetMouseButtonCallback(ptr) { _, button, action, mods ->
				callback(this, MouseButton.from(button), Action.from(action), Flag(mods))
			}
		} else {
			glfwSetMouseButtonCallback(ptr, null)
		}?.free()
	}

	actual fun setCharCallback(callback: CharCallback?) {
		if (callback != null) {
			glfwSetCharCallback(ptr) { _, codepoint ->
				callback(this, codepoint.toUInt())
			}
		} else {
			glfwSetCharCallback(ptr, null)
		}?.free()
	}

	actual fun setCharModsCallback(callback: CharModsCallback?) {
		if (callback != null) {
			glfwSetCharModsCallback(ptr) { _, codepoint, mods ->
				callback(this, codepoint.toUInt(), Flag(mods))
			}
		} else {
			glfwSetCharModsCallback(ptr, null)
		}?.free()
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
			check(glfwInit()) { "Could not init GLFW." }

			glfwDefaultWindowHints()
			Builder().block()

			return Window(glfwCreateWindow(
					width, height, title, monitor?.ptr ?: 0L, share?.ptr ?: 0L
			).takeIf { it != 0L } ?: throw Exception("Could not create window."))
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
