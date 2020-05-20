package com.kgl.math

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign

val Float.Companion.EPSILON: Float get() = Float.fromBits(0x3400_0000)

val Float.degrees: Float get() = (this * PI / 180).toFloat()

fun lerp(from: Float, to: Float, ratio: Float): Float {
	return from * (1 - ratio) + to * ratio
}

fun Float.moveTowards(target: Float, maxDistance: Float): Float {
	if (abs(this - target) < maxDistance) {
		return target
	}
	return this + sign(target - this) * maxDistance
}
