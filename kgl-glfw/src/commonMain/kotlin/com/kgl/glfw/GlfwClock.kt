package com.kgl.glfw

import kotlin.time.*

@ExperimentalTime
object GlfwClock : AbstractDoubleTimeSource(DurationUnit.SECONDS) {
	override fun read(): Double = Glfw.time
}
