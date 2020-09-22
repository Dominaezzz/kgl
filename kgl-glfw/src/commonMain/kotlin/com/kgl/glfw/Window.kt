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

import io.ktor.utils.io.core.*

expect class Window(
	width: Int,
	height: Int,
	title: String,
	monitor: Monitor? = null,
	share: Window? = null
) : Closeable {
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
	val hasTransparentFramebuffer: Boolean
	val clientApi: ClientApi
	val contextCreationApi: CreationApi
	val contextVersionMajor: Int
	val contextVersionMinor: Int
	val contextRevision: Int
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

	fun setPosCallback(callback: WindowPosCallback?): WindowPosCallback?
	fun setSizeCallback(callback: WindowSizeCallback?): WindowSizeCallback?
	fun setCloseCallback(callback: WindowCloseCallback?): WindowCloseCallback?
	fun setRefreshCallback(callback: WindowRefreshCallback?): WindowRefreshCallback?
	fun setFocusCallback(callback: WindowFocusCallback?): WindowFocusCallback?
	fun setIconifyCallback(callback: WindowIconifyCallback?): WindowIconifyCallback?
	fun setMaximizeCallback(callback: WindowMaximizeCallback?): WindowMaximizeCallback?
	fun setContentScaleCallback(callback: WindowContentScaleCallback?): WindowContentScaleCallback?
	fun setScrollCallback(callback: ScrollCallback?): ScrollCallback?
	fun setCursorEnterCallback(callback: CursorEnterCallback?): CursorEnterCallback?
	fun setCursorPosCallback(callback: CursorPosCallback?): CursorPosCallback?
	fun setFrameBufferCallback(callback: FrameBufferCallback?): FrameBufferCallback?
	fun setDropCallback(callback: DropCallback?): DropCallback?
	fun setKeyCallback(callback: KeyCallback?): KeyCallback?
	fun setMouseButtonCallback(callback: MouseButtonCallback?): MouseButtonCallback?
	fun setCharCallback(callback: CharCallback?): CharCallback?
	fun setCharModsCallback(callback: CharModsCallback?): CharModsCallback?
}
