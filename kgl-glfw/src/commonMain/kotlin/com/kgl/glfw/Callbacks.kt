package com.kgl.glfw

import com.kgl.core.*


typealias ErrorCallback = (error: Int, description: String) -> Unit
typealias JoystickCallback = (jid: Joystick, isConnected: Boolean) -> Unit
typealias MonitorCallback = (monitor: Monitor, isConnected: Boolean) -> Unit

typealias WindowPosCallback = (window: Window, x: Int, y: Int) -> Unit
typealias WindowSizeCallback = (window: Window, width: Int, height: Int) -> Unit
typealias WindowCloseCallback = (window: Window) -> Unit
typealias WindowRefreshCallback = (window: Window) -> Unit
typealias WindowFocusCallback = (window: Window, focused: Boolean) -> Unit
typealias WindowIconifyCallback = (window: Window, iconified: Boolean) -> Unit
typealias WindowMaximizeCallback = (window: Window, maximized: Boolean) -> Unit
typealias WindowContentScaleCallback = (window: Window, xScale: Float, yScale: Float) -> Unit
typealias ScrollCallback = (window: Window, x: Double, y: Double) -> Unit
typealias CursorEnterCallback = (window: Window, entered: Boolean) -> Unit
typealias CursorPosCallback = (window: Window, x: Double, y: Double) -> Unit
typealias FrameBufferCallback = (window: Window, width: Int, height: Int) -> Unit
typealias DropCallback = (window: Window, names: Array<String>) -> Unit
typealias KeyCallback = (window: Window, key: KeyboardKey, scancode: Int, action: Action, mods: Flag<Mod>) -> Unit
typealias MouseButtonCallback = (window: Window, button: MouseButton, action: Action, mods: Flag<Mod>) -> Unit
typealias CharCallback = (window: Window, codepoint: UInt) -> Unit
typealias CharModsCallback = (window: Window, codepoint: UInt, mods: Flag<Mod>) -> Unit
