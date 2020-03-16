package com.kgl.glfw

import kotlin.time.AbstractDoubleTimeSource
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@ExperimentalTime
object GlfwClock : AbstractDoubleTimeSource(DurationUnit.SECONDS) {
	override fun read(): Double = Glfw.time
}
