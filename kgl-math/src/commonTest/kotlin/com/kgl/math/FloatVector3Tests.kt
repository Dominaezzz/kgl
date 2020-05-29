package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FloatVector3Tests {
	private val vector = FloatVector3(1f, 2f, 3f)

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
		assertEquals(FloatVector3(1f, 2f, 3f), vector.copy())
	}

	@Test
	fun get() {
		assertFailsWith(IndexOutOfBoundsException::class) { vector[-1] }
		assertEquals(vector.x, vector[0])
		assertEquals(vector.y, vector[1])
		assertEquals(vector.z, vector[2])
		assertFailsWith(IndexOutOfBoundsException::class) { vector[3] }
	}

	@Test
	fun unaryPlus() {
		assertEquals(FloatVector3(1f, 2f, 3f), +vector)
	}

	@Test
	fun unaryMinus() {
		assertEquals(FloatVector3(-1f, -2f, -3f), -vector)
	}

	@Test
	fun plus_scalar() {
		assertEquals(FloatVector3(2f, 3f, 4f), vector + 1f)
	}

	@Test
	fun minus_scalar() {
		assertEquals(FloatVector3(0f, 1f, 2f), vector - 1f)
	}

	@Test
	fun times_scalar() {
		assertEquals(FloatVector3(2f, 4f, 6f), vector * 2f)
	}

	@Test
	fun div_scalar() {
		assertEquals(FloatVector3(0.5f, 1f, 1.5f), vector / 2f)
	}

	@Test
	fun rem_scalar() {
		assertEquals(FloatVector3(1f, 0f, 1f), vector % 2f)
	}

	@Test
	fun plus_vector() {
		assertEquals(FloatVector3(2f, 4f, 6f), vector + vector)
	}

	@Test
	fun minus_vector() {
		assertEquals(FloatVector3.ZERO, vector - vector)
	}

	@Test
	fun times_vector() {
		assertEquals(FloatVector3(1f, 4f, 9f), vector * vector)
	}

	@Test
	fun div_vector() {
		assertEquals(FloatVector3.ONE, vector / vector)
	}

	@Test
	fun rem_vector() {
		assertEquals(FloatVector3.ZERO, vector % vector)
	}

	@Test
	fun squareMagnitude() {
		assertEquals(14f, vector.squareMagnitude)
	}

	@Test
	fun magnitude() {
		assertEquals(sqrt(14f), vector.magnitude)
	}

	@Test
	fun cross() {
		assertEquals(FloatVector3.FORWARD, FloatVector3.RIGHT cross FloatVector3.UP)
	}

	@Test
	fun dot() {
		assertEquals(14f, vector dot vector)
	}

	@Test
	fun movedTowards() {
		assertEquals(FloatVector3.ONE, FloatVector3.ZERO.movedTowards(FloatVector3.ONE, 2f))
	}

	@Test
	fun normalized() {
		assertEquals(FloatVector3.RIGHT, FloatVector3(2f, 0f, 0f).normalized())
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
	fun asFloatArray() {
		val result = vector.toMutableFloatVector3()
		assertTrue(result.asFloatArray().contentEquals(floatArrayOf(1f, 2f, 3f)))
		result.asFloatArray()[1] = 20f
		assertEquals(20f, result.y)
	}

	@Test
	fun set() {
		val result = MutableFloatVector3()
		assertFailsWith(IndexOutOfBoundsException::class) { result[-1] = 1f }
		result[0] = 1f
		assertEquals(1f, result.x)
		result[1] = 1f
		assertEquals(1f, result.y)
		result[2] = 1f
		assertEquals(1f, result.z)
		assertFailsWith(IndexOutOfBoundsException::class) { result[3] = 1f }
	}

	@Test
	fun plusAssign_scalar() {
		val result = vector.toMutableFloatVector3()
		result += 1f
		assertEquals(FloatVector3(2f, 3f, 4f), result)
	}

	@Test
	fun minusAssign_scalar() {
		val result = vector.toMutableFloatVector3()
		result -= 1f
		assertEquals(FloatVector3(0f, 1f, 2f), result)
	}

	@Test
	fun timesAssign_scalar() {
		val result = vector.toMutableFloatVector3()
		result *= 2f
		assertEquals(FloatVector3(2f, 4f, 6f), result)
	}

	@Test
	fun divAssign_scalar() {
		val result = vector.toMutableFloatVector3()
		result /= 2f
		assertEquals(FloatVector3(0.5f, 1f, 1.5f), result)
	}

	@Test
	fun remAssign_scalar() {
		val result = vector.toMutableFloatVector3()
		result %= 2f
		assertEquals(FloatVector3(1f, 0f, 1f), result)
	}

	@Test
	fun plusAssign_vector() {
		val result = vector.toMutableFloatVector3()
		result += vector
		assertEquals(FloatVector3(2f, 4f, 6f), result)
	}

	@Test
	fun minusAssign_vector() {
		val result = vector.toMutableFloatVector3()
		result -= vector
		assertEquals(FloatVector3.ZERO, result)
	}

	@Test
	fun timesAssign_vector() {
		val result = vector.toMutableFloatVector3()
		result *= result
		assertEquals(FloatVector3(1f, 4f, 9f), result)
	}

	@Test
	fun divAssign_vector() {
		val result = vector.toMutableFloatVector3()
		result /= vector
		assertEquals(FloatVector3.ONE, result)
	}

	@Test
	fun remAssign_vector() {
		val result = vector.toMutableFloatVector3()
		result %= vector
		assertEquals(FloatVector3.ZERO, result)
	}

	@Test
	fun set1() {
		val result = MutableFloatVector3()
		result.set(1f)
		assertEquals(FloatVector3.ONE, result)
	}

	@Test
	fun set3() {
		val result = MutableFloatVector3()
		result.set(1f, 2f, 3f)
		assertEquals(FloatVector3(1f, 2f, 3f), result)
	}

	@Test
	fun moveTowards() {
		val result = MutableFloatVector3()
		result.moveTowards(FloatVector3.ONE, 2f)
		assertEquals(FloatVector3.ONE, result)
	}

	@Test
	fun normalize() {
		val result = MutableFloatVector3(2f, 0f, 0f)
		result.normalize()
		assertEquals(FloatVector3(1f, 0f, 0f), result)
	}

	@Test
	fun project() {
		val result = MutableFloatVector3(2f, 2f, 2f)
		result.project(FloatVector3(1f, 0f, 1f))
		assertEquals(FloatVector3(2f, 0f, 2f), result)
	}

	@Test
	fun reflect() {
		val result = MutableFloatVector3(1f, -2f, 1f)
		result.reflect(FloatVector3.UP)
		assertEquals(FloatVector3(1f, 2f, 1f), result)
	}

	// top-level
	@Test
	fun toFloatArray() {
		assertTrue(vector.toFloatArray().contentEquals(floatArrayOf(1f, 2f, 3f)))
	}

	@Test
	fun toFloatVector3() {
		assertEquals(vector, vector.toFloatVector3())
	}

	@Test
	fun toMutableFloatVector3() {
		val result: MutableFloatVector3 = vector.toMutableFloatVector3()
		assertEquals(vector, result)
	}

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
