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

expect class Window : Closeable {
	var position: Pair<Int, Int>
	var size: Pair<Int, Int>
	val frameBufferSize: Pair<Int, Int>
	var clipboardString: String?
	var shouldClose: Boolean
	var isIconified: Boolean
	var isVisible: Boolean
	val monitor: Monitor?
	var cursorPosition: Pair<Double, Double>

	fun swapBuffers()
	fun setMonitor(monitor: Monitor, xpos: Int, ypos: Int, width: Int, height: Int, refreshRate: Int)
	fun setTitle(title: String)
	fun setSizeLimits(minWidth: Int, minHeight: Int, maxWidth: Int, maxHeight: Int)
	fun setAspectRatio(number: Int, denom: Int)
	fun setIcon(images: Array<Image>)
	fun maximize()

	fun setCursor(cursor: Cursor?)
	fun getKey(key: KeyboardKey): Action
	fun getKeyName(key: KeyboardKey): String?
	fun getKeyName(scancode: Int): String?
	fun getMouseButton(button: MouseButton): Action

	fun setPosCallback(callback: ((Window, Int, Int) -> Unit)?)
	fun setSizeCallback(callback: ((Window, Int, Int) -> Unit)?)
	fun setCloseCallback(callback: ((Window) -> Unit)?)
	fun setRefreshCallback(callback: ((Window) -> Unit)?)
	fun setFocusCallback(callback: ((Window, Boolean) -> Unit)?)
	fun setIconifyCallback(callback: ((Window, Boolean) -> Unit)?)
	fun setScrollCallback(callback: ((Window, Double, Double) -> Unit)?)
	fun setCursorEnterCallback(callback: ((Window, Boolean) -> Unit)?)
	fun setCursorPosCallback(callback: ((Window, Double, Double) -> Unit)?)
	fun setFrameBufferCallback(callback: ((Window, Int, Int) -> Unit)?)
	fun setDropCallback(callback: ((Window, Array<String>) -> Unit)?)
	fun setKeyCallback(callback: ((Window, KeyboardKey, Int, Action, Flag<Mod>) -> Unit)?)
	fun setMouseButtonCallback(callback: ((Window, MouseButton, Action, Flag<Mod>) -> Unit)?)
	fun setCharCallback(callback: ((Window, UInt) -> Unit)?)
	fun setCharModsCallback(callback: ((Window, UInt, Flag<Mod>) -> Unit)?)

	companion object {
		inline operator fun invoke(
				width: Int,
				height: Int,
				title: String,
				monitor: Monitor? = null,
				share: Window? = null,
				block: Builder.() -> Unit
		): Window
	}

	class Builder {
		var clientApi: ClientApi
		var contextCreationApi: CreationApi
		var contextVersionMajor: Int
		var contextVersionMinor: Int
		var contextRobustness: Robustness
		var releaseBehaviour: ReleaseBehaviour
		var openGLForwardCompat: Boolean
		var openGLDebugContext: Boolean
		var openGLProfile: OpenGLProfile

		var redBits: Int
		var greenBits: Int
		var blueBits: Int
		var alphaBits: Int
		var depthBits: Int
		var stencilBits: Int
		var accumRedBits: Int
		var accumGreenBits: Int
		var accumBlueBits: Int
		var accumAlphaBits: Int
		var samples: Int
		var stereo: Boolean
		var srgbCapable: Boolean
		var doubleBuffer: Boolean

		var resizable: Boolean
		var visible: Boolean
		var decorated: Boolean
		var focused: Boolean
		var autoIconfiy: Boolean
		var floating: Boolean
		var maximized: Boolean

		var refreshRate: Int
	}
}
