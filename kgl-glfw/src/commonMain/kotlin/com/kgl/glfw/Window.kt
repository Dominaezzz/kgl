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

import io.ktor.utils.io.core.Closeable

expect class Window : Closeable {
	var position: Pair<Int, Int>
	var size: Pair<Int, Int>
	val frameBufferSize: Pair<Int, Int>
	val frameSize: FrameSize
	val contentScale: Pair<Float, Float>
	var clipboardString: String?
	var shouldClose: Boolean
	var isIconified: Boolean
	var isVisible: Boolean
	var isDecorated: Boolean
	var isResizable: Boolean
	var isFloating: Boolean
	var isAutoIconify: Boolean
	var isFocusOnShow: Boolean
	val isFocused: Boolean
	val isMaximized: Boolean
	val isHovered: Boolean
	val isTransparentFramebuffer: Boolean
	val clientApi: ClientApi
	val contextCreationApi: CreationApi
	val openGLForwardCompat: Boolean
	val openGLDebugContext: Boolean
	val openGLProfile: OpenGLProfile
	val contextReleaseBehaviour: ReleaseBehaviour
	val contextNoError: Boolean
	val contextRobustness: Robustness

	val monitor: Monitor?
	var cursorPosition: Pair<Double, Double>
	var cursorMode: CursorMode
	var opacity: Float
	var stickyKeysEnabled: Boolean
	var stickyMouseButtonsEnabled: Boolean
	var lockKeyModsEnabled: Boolean
	var rawMouseButtonEnabled: Boolean

	fun swapBuffers()
	fun setMonitor(monitor: Monitor, xpos: Int, ypos: Int, width: Int, height: Int, refreshRate: Int)
	fun setTitle(title: String)
	fun setSizeLimits(minWidth: Int, minHeight: Int, maxWidth: Int, maxHeight: Int)
	fun setAspectRatio(number: Int, denom: Int)
	fun setIcon(images: Array<Image>)
	fun maximize()
	fun focus()
	fun requestAttention()

	fun setCursor(cursor: Cursor?)
	fun getKey(key: KeyboardKey): Action
	fun getMouseButton(button: MouseButton): Action

	fun setPosCallback(callback: WindowPosCallback?)
	fun setSizeCallback(callback: WindowSizeCallback?)
	fun setCloseCallback(callback: WindowCloseCallback?)
	fun setRefreshCallback(callback: WindowRefreshCallback?)
	fun setFocusCallback(callback: WindowFocusCallback?)
	fun setIconifyCallback(callback: WindowIconifyCallback?)
	fun setMaximizeCallback(callback: WindowMaximizeCallback?)
	fun setContentScaleCallback(callback: WindowContentScaleCallback?)
	fun setScrollCallback(callback: ScrollCallback?)
	fun setCursorEnterCallback(callback: CursorEnterCallback?)
	fun setCursorPosCallback(callback: CursorPosCallback?)
	fun setFrameBufferCallback(callback: FrameBufferCallback?)
	fun setDropCallback(callback: DropCallback?)
	fun setKeyCallback(callback: KeyCallback?)
	fun setMouseButtonCallback(callback: MouseButtonCallback?)
	fun setCharCallback(callback: CharCallback?)
	fun setCharModsCallback(callback: CharModsCallback?)

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
		var autoIconify: Boolean
		var floating: Boolean
		var maximized: Boolean
		var transparentFramebuffer: Boolean

		var refreshRate: Int
	}
}
