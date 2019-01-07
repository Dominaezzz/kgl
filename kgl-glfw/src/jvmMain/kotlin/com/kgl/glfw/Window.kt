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
import kotlinx.io.core.Closeable
import org.lwjgl.PointerBuffer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

actual class Window @PublishedApi internal constructor(val ptr: Long) : Closeable {

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
		glfwSetWindowIcon(ptr, TODO())
	}

	actual fun setIcon(images: Collection<Image>) {
		glfwSetWindowIcon(ptr, TODO())
	}

	actual fun maximize() {
		glfwMaximizeWindow(ptr)
	}

	actual fun setCursor(cursor: Cursor) {
		glfwSetCursor(ptr, cursor.ptr)
	}

	actual fun getKey(key: KeyboardKey): Action = Action.from(glfwGetKey(ptr, key.value))

	actual fun getKeyName(key: KeyboardKey): String? = glfwGetKeyName(key.value, 0)

	actual fun getKeyName(scancode: Int): String? = glfwGetKeyName(GLFW_KEY_UNKNOWN, scancode)

	actual fun getMouseButton(button: MouseButton): Action = Action.from(glfwGetMouseButton(ptr, button.value))

	actual fun setPosCallback(callback: ((Window, Int, Int) -> Unit)?) {
		if (callback != null) {
			glfwSetWindowPosCallback(ptr) { _, x, y ->
				callback(this, x, y)
			}
		} else {
			glfwSetWindowPosCallback(ptr, null)
		}?.free()
	}

	actual fun setSizeCallback(callback: ((Window, Int, Int) -> Unit)?) {
		if (callback != null) {
			glfwSetWindowSizeCallback(ptr) { _, width, height ->
				callback(this, width, height)
			}
		} else {
			glfwSetWindowSizeCallback(ptr, null)
		}?.free()
	}

	actual fun setFrameBufferCallback(callback: ((Window, Int, Int) -> Unit)?) {
		if (callback != null) {
			glfwSetFramebufferSizeCallback(ptr) { _, width, height ->
				callback(this, width, height)
			}
		} else {
			glfwSetFramebufferSizeCallback(ptr, null)
		}?.free()
	}

	actual fun setCloseCallback(callback: ((Window) -> Unit)?) {
		if (callback != null) {
			glfwSetWindowCloseCallback(ptr) {
				callback(this)
			}
		} else {
			glfwSetWindowCloseCallback(ptr, null)
		}?.free()
	}

	actual fun setRefreshCallback(callback: ((Window) -> Unit)?) {
		if (callback != null) {
			glfwSetWindowRefreshCallback(ptr) {
				callback(this)
			}
		} else {
			glfwSetWindowRefreshCallback(ptr, null)
		}?.free()
	}

	actual fun setFocusCallback(callback: ((Window, Boolean) -> Unit)?) {
		if (callback != null) {
			glfwSetWindowFocusCallback(ptr) { _, focused ->
				callback(this, focused)
			}
		} else {
			glfwSetWindowFocusCallback(ptr, null)
		}?.free()
	}

	actual fun setIconifyCallback(callback: ((Window, Boolean) -> Unit)?) {
		if (callback != null) {
			glfwSetWindowIconifyCallback(ptr) { _, focused ->
				callback(this, focused)
			}
		} else {
			glfwSetWindowIconifyCallback(ptr, null)
		}?.free()
	}

	actual fun setCursorEnterCallback(callback: ((Window, Boolean) -> Unit)?) {
		if (callback != null) {
			glfwSetCursorEnterCallback(ptr) { _, focused ->
				callback(this, focused)
			}
		} else {
			glfwSetCursorEnterCallback(ptr, null)
		}?.free()
	}

	actual fun setScrollCallback(callback: ((Window, Double, Double) -> Unit)?) {
		if (callback != null) {
			glfwSetScrollCallback(ptr) { _, x, y ->
				callback(this, x, y)
			}
		} else {
			glfwSetScrollCallback(ptr, null)
		}?.free()
	}

	actual fun setCursorPosCallback(callback: ((Window, Double, Double) -> Unit)?) {
		if (callback != null) {
			glfwSetCursorPosCallback(ptr) { _, x, y ->
				callback(this, x, y)
			}
		} else {
			glfwSetCursorPosCallback(ptr, null)
		}?.free()
	}

	actual fun setDropCallback(callback: ((Window, Array<String>) -> Unit)?) {
		if (callback != null) {
			glfwSetDropCallback(ptr) { _, count, names ->
				val pNames = PointerBuffer.create(names, count)
				callback(this, Array(count) { MemoryUtil.memUTF8(pNames[it]) })
			}
		} else {
			glfwSetDropCallback(ptr, null)
		}?.free()
	}

	actual fun setKeyCallback(callback: ((Window, KeyboardKey, Int, Action, Flag<Mod>) -> Unit)?) {
		if (callback != null) {
			glfwSetKeyCallback(ptr) { _, key, scancode, action, mods ->
				callback(this, KeyboardKey.from(key), scancode, Action.from(action), Flag(mods))
			}
		} else {
			glfwSetKeyCallback(ptr, null)
		}?.free()
	}

	actual fun setMouseButtonCallback(callback: ((Window, MouseButton, Action, Flag<Mod>) -> Unit)?) {
		if (callback != null) {
			glfwSetMouseButtonCallback(ptr) { _, button, action, mods ->
				callback(this, MouseButton.from(button), Action.from(action), Flag(mods))
			}
		} else {
			glfwSetMouseButtonCallback(ptr, null)
		}?.free()
	}

	actual fun setCharCallback(callback: ((Window, UInt) -> Unit)?) {
		if (callback != null) {
			glfwSetCharCallback(ptr) { _, codepoint ->
				callback(this, codepoint.toUInt())
			}
		} else {
			glfwSetCharCallback(ptr, null)
		}?.free()
	}

	actual fun setCharModsCallback(callback: ((Window, UInt, Flag<Mod>) -> Unit)?) {
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
		glfwDestroyWindow(ptr)
		glfwTerminate()
	}

	actual companion object {
		actual var currentContext: Window?
			get() = TODO("Getting current context is not yet supported on JVM") // glfwGetCurrentContext()
			set(value) {
				glfwMakeContextCurrent(value?.ptr ?: 0L)
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
