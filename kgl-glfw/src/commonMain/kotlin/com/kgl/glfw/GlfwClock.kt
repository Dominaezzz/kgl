package com.kgl.glfw

import kotlin.time.AbstractDoubleClock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@ExperimentalTime
object GlfwClock : AbstractDoubleClock(DurationUnit.SECONDS) {
	override fun read(): Double = Glfw.time
}
