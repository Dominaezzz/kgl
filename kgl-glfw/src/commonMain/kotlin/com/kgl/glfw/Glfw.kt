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

import kotlin.time.*

expect object Glfw {
	var time: Double
	val timerValue: ULong
	val timerFrequency: ULong

	val primaryMonitor: Monitor?
	val monitors: List<Monitor>

	val version: GlfwVersion
	val versionString: String
	val windowHints: WindowHints

	fun init(): Boolean
	fun terminate()

	fun setErrorCallback(callback: ErrorCallback? = null): ErrorCallback?
	fun setJoystickCallback(callback: JoystickCallback? = null): JoystickCallback?
	fun setMonitorCallback(callback: MonitorCallback? = null): MonitorCallback?

	fun pollEvents()
	fun waitEvents()
	fun waitEvents(timeout: Double)
	fun postEmptyEvent()

	fun updateGamepadMappings(mapping: String): Boolean

	var currentContext: Window?
	fun isExtensionSupported(extension: String): Boolean
	fun setSwapInterval(interval: Int)
	val isRawMouseMotionSupported: Boolean
	fun getKeyName(key: KeyboardKey): String?
	fun getKeyName(scancode: Int): String?
	fun getKeyScancode(key: KeyboardKey): Int
}

@ExperimentalTime
fun Glfw.waitEvents(timeout: Duration) {
	waitEvents(timeout.toDouble(DurationUnit.SECONDS))
}
