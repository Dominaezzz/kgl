package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FloatVector3Tests {
	@Test
	fun constants() {
		assertEquals(FloatVector3(0f), FloatVector3.ZERO)
		assertEquals(FloatVector3(1f), FloatVector3.ONE)
		assertEquals(FloatVector3(1f, 0f, 0f), FloatVector3.RIGHT)
		assertEquals(FloatVector3(-1f, 0f, 0f), FloatVector3.LEFT)
		assertEquals(FloatVector3(0f, 1f, 0f), FloatVector3.UP)
		assertEquals(FloatVector3(0f, -1f, 0f), FloatVector3.DOWN)
		assertEquals(FloatVector3(0f, 0f, 1f), FloatVector3.FORWARD)
		assertEquals(FloatVector3(0f, 0f, -1f), FloatVector3.BACK)
		assertEquals(FloatVector3(Float.POSITIVE_INFINITY), FloatVector3.POSITIVE_INFINITY)
		assertEquals(FloatVector3(Float.NEGATIVE_INFINITY), FloatVector3.NEGATIVE_INFINITY)
	}

	@Test
	fun copy() {
		assertEquals(FloatVector3(1f, 2f, 3f), FloatVector3(1f, 2f, 3f).copy())
	}

	@Test
	fun get() {
		val vector = FloatVector3(1f, 2f, 3f)
		assertFailsWith(IndexOutOfBoundsException::class) { vector[-1] }
		assertEquals(vector.x, vector[0])
		assertEquals(vector.y, vector[1])
		assertEquals(vector.z, vector[2])
		assertFailsWith(IndexOutOfBoundsException::class) { vector[3] }
	}

	@Test
	fun unaryPlus() {
		assertEquals(FloatVector3(1f), +FloatVector3(1f))
	}

	@Test
	fun unaryMinus() {
		assertEquals(FloatVector3(-1f), -FloatVector3(1f))
	}

	@Test
	fun plus_scalar() {
		assertEquals(FloatVector3(2f), FloatVector3(1f) + 1f)
	}

	@Test
	fun minus_scalar() {
		assertEquals(FloatVector3(0f), FloatVector3(1f) - 1f)
	}

	@Test
	fun times_scalar() {
		assertEquals(FloatVector3(1f), FloatVector3(1f) * 1f)
	}

	@Test
	fun div_scalar() {
		assertEquals(FloatVector3(1f), FloatVector3(1f) / 1f)
	}

	@Test
	fun rem_scalar() {
		assertEquals(FloatVector3(0f), FloatVector3(1f) % 1f)
	}

	@Test
	fun plus_vector() {
		assertEquals(FloatVector3(2f), FloatVector3(1f) + FloatVector3(1f))
	}

	@Test
	fun minus_vector() {
		assertEquals(FloatVector3(0f), FloatVector3(1f) - FloatVector3(1f))
	}

	@Test
	fun times_vector() {
		assertEquals(FloatVector3(1f), FloatVector3(1f) * FloatVector3(1f))
	}

	@Test
	fun div_vector() {
		assertEquals(FloatVector3(1f), FloatVector3(1f) / FloatVector3(1f))
	}

	@Test
	fun rem_vector() {
		assertEquals(FloatVector3(0f), FloatVector3(1f) % FloatVector3(1f))
	}

	@Test
	fun squareMagnitude() {
		assertEquals(12f, FloatVector3(2f, 2f, 2f).squareMagnitude)
	}

	@Test
	fun magnitude() {
		assertEquals(sqrt(12f), FloatVector3(2f, 2f, 2f).magnitude)
	}

	@Test
	fun cross() {
		assertEquals(FloatVector3.FORWARD, FloatVector3.RIGHT cross FloatVector3.UP)
	}

	@Test
	fun dot() {
		assertEquals(6f, FloatVector3(1f) dot FloatVector3(2f))
	}

	@Test
	fun movedTowards() {
		assertEquals(FloatVector3(1f), FloatVector3().movedTowards(FloatVector3(1f), 2f))
	}

	@Test
	fun normalized() {
		assertEquals(FloatVector3(1f, 0f, 0f), FloatVector3(2f, 0f, 0f).normalized())
	}

	@Test
	fun projected() {
		assertEquals(FloatVector3(2f, 0f, 2f), FloatVector3(2f, 2f, 2f).projected(FloatVector3(1f, 0f, 1f)))
	}

	@Test
	fun reflected() {
		assertEquals(FloatVector3(1f, 2f, 1f), FloatVector3(1f, -2f, 1f).reflected(FloatVector3(0f, 1f, 0f)))
	}

	// Mutable
	@Test
	fun set() {
		val vector = MutableFloatVector3()
		assertFailsWith(IndexOutOfBoundsException::class) { vector[-1] = 1f }
		vector[0] = 1f
		assertEquals(1f, vector.x)
		vector[1] = 1f
		assertEquals(1f, vector.y)
		vector[2] = 1f
		assertEquals(1f, vector.z)
		assertFailsWith(IndexOutOfBoundsException::class) { vector[3] = 1f }
	}

	@Test
	fun plusAssign_scalar() {
		val vector = MutableFloatVector3(1f)
		vector += 1f
		assertEquals(FloatVector3(2f), vector)
	}

	@Test
	fun minusAssign_scalar() {
		val vector = MutableFloatVector3(1f)
		vector -= 1f
		assertEquals(FloatVector3(0f), vector)
	}

	@Test
	fun timesAssign_scalar() {
		val vector = MutableFloatVector3(1f)
		vector *= 1f
		assertEquals(FloatVector3(1f), vector)
	}

	@Test
	fun divAssign_scalar() {
		val vector = MutableFloatVector3(1f)
		vector /= 1f
		assertEquals(FloatVector3(1f), vector)
	}

	@Test
	fun remAssign_scalar() {
		val vector = MutableFloatVector3(1f)
		vector %= 1f
		assertEquals(FloatVector3(0f), vector)
	}

	@Test
	fun plusAssign_vector() {
		val vector = MutableFloatVector3(1f)
		vector += vector
		assertEquals(FloatVector3(2f), vector)
	}

	@Test
	fun minusAssign_vector() {
		val vector = MutableFloatVector3(1f)
		vector -= vector
		assertEquals(FloatVector3(0f), vector)
	}

	@Test
	fun timesAssign_vector() {
		val vector = MutableFloatVector3(1f)
		vector *= vector
		assertEquals(FloatVector3(1f), vector)
	}

	@Test
	fun divAssign_vector() {
		val vector = MutableFloatVector3(1f)
		vector /= vector
		assertEquals(FloatVector3(1f), vector)
	}

	@Test
	fun remAssign_vector() {
		val vector = MutableFloatVector3(1f)
		vector %= vector
		assertEquals(FloatVector3(0f), vector)
	}

	@Test
	fun set1() {
		val vector = MutableFloatVector3()
		vector.set(1f)
		assertEquals(FloatVector3(1f), vector)
	}

	@Test
	fun set3() {
		val vector = MutableFloatVector3()
		vector.set(1f, 2f, 3f)
		assertEquals(FloatVector3(1f, 2f, 3f), vector)
	}

	@Test
	fun moveTowards() {
		val vector = MutableFloatVector3()
		vector.moveTowards(FloatVector3(1f), 2f)
		assertEquals(FloatVector3(1f), vector)
	}

	@Test
	fun normalize() {
		val vector = MutableFloatVector3(2f, 0f, 0f)
		vector.normalize()
		assertEquals(FloatVector3(1f, 0f, 0f), vector)
	}

	@Test
	fun project() {
		val vector = MutableFloatVector3(2f, 2f, 2f)
		vector.project(FloatVector3(1f, 0f, 1f))
		assertEquals(FloatVector3(2f, 0f, 2f), vector)
	}

	@Test
	fun reflect() {
		val vector = MutableFloatVector3(1f, -2f, 1f)
		vector.reflect(FloatVector3.UP)
		assertEquals(FloatVector3(1f, 2f, 1f), vector)
	}

	// top-level
	@Test
	fun angle() {
		assertEquals(90f.degrees, angle(FloatVector3.UP, FloatVector3.RIGHT))
	}

	@Test
	fun signedAngle() {
		assertEquals((-90f).degrees, signedAngle(FloatVector3.UP, FloatVector3.RIGHT, FloatVector3.FORWARD))
	}

	@Test
	fun distance() {
		assertEquals(sqrt(3f), distance(FloatVector3(0f), FloatVector3(1f)))
	}

	@Test
	fun lerp() {
		assertEquals(FloatVector3(0.25f), lerp(FloatVector3(0.5f), FloatVector3(1f), -0.5f))
	}

	@Test
	fun max() {
		assertEquals(FloatVector3(2f, 2f, 2f), max(FloatVector3(1f, 2f, 1f), FloatVector3(2f, 1f, 2f)))
	}

	@Test
	fun min() {
		assertEquals(FloatVector3(1f, 1f, 1f), min(FloatVector3(1f, 2f, 1f), FloatVector3(2f, 1f, 2f)))
	}

	@Test
	fun scalar_plus() {
		assertEquals(FloatVector3(2f), 1f + FloatVector3.ONE)
	}

	@Test
	fun scalar_minus() {
		assertEquals(FloatVector3(0f), 1f - FloatVector3.ONE)
	}

	@Test
	fun scalar_times() {
		assertEquals(FloatVector3(1f), 1f * FloatVector3.ONE)
	}

	@Test
	fun scalar_div() {
		assertEquals(FloatVector3(1f), 1f / FloatVector3.ONE)
	}

	@Test
	fun scalar_rem() {
		assertEquals(FloatVector3(0f), 1f % FloatVector3.ONE)
	}
}
