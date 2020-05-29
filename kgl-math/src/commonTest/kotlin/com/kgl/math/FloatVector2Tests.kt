package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FloatVector2Tests {
	private val vector = FloatVector2(1f, 2f)

	@Test
	fun constants() {
		assertEquals(FloatVector2(0f), FloatVector2.ZERO)
		assertEquals(FloatVector2(1f), FloatVector2.ONE)
		assertEquals(FloatVector2(0f, 1f), FloatVector2.UP)
		assertEquals(FloatVector2(0f, -1f), FloatVector2.DOWN)
		assertEquals(FloatVector2(-1f, 0f), FloatVector2.LEFT)
		assertEquals(FloatVector2(1f, 0f), FloatVector2.RIGHT)
		assertEquals(FloatVector2(Float.POSITIVE_INFINITY), FloatVector2.POSITIVE_INFINITY)
		assertEquals(FloatVector2(Float.NEGATIVE_INFINITY), FloatVector2.NEGATIVE_INFINITY)
	}

	@Test
	fun copy() {
		assertEquals(vector, vector.copy())
	}

	@Test
	fun get() {
		assertFailsWith(IndexOutOfBoundsException::class) { vector[-1] }
		assertEquals(vector.x, vector[0])
		assertEquals(vector.y, vector[1])
		assertFailsWith(IndexOutOfBoundsException::class) { vector[2] }
	}

	@Test
	fun unaryPlus() {
		assertEquals(FloatVector2(1f, 2f), +vector)
	}

	@Test
	fun unaryMinus() {
		assertEquals(FloatVector2(-1f, -2f), -vector)
	}

	@Test
	fun plus_scalar() {
		assertEquals(FloatVector2(2f, 3f), vector + 1f)
	}

	@Test
	fun minus_scalar() {
		assertEquals(FloatVector2(0f, 1f), vector - 1f)
	}

	@Test
	fun times_scalar() {
		assertEquals(FloatVector2(2f, 4f), vector * 2f)
	}

	@Test
	fun div_scalar() {
		assertEquals(FloatVector2(0.5f, 1f), vector / 2f)
	}

	@Test
	fun rem_scalar() {
		assertEquals(FloatVector2(1f, 0f), vector % 2f)
	}

	@Test
	fun plus_vector() {
		assertEquals(FloatVector2(2f, 4f), vector + vector)
	}

	@Test
	fun minus_vector() {
		assertEquals(FloatVector2.ZERO, vector - vector)
	}

	@Test
	fun times_vector() {
		assertEquals(FloatVector2(1f, 4f), vector * vector)
	}

	@Test
	fun div_vector() {
		assertEquals(FloatVector2.ONE, vector / vector)
	}

	@Test
	fun rem_vector() {
		assertEquals(FloatVector2.ZERO, vector % vector)
	}

	@Test
	fun squareMagnitude() {
		assertEquals(5f, vector.squareMagnitude)
	}

	@Test
	fun magnitude() {
		assertEquals(sqrt(5f), vector.magnitude)
	}

	@Test
	fun perpendicular() {
		assertEquals(FloatVector2.UP, FloatVector2.RIGHT.perpendicular)
	}

	@Test
	fun dot() {
		assertEquals(5f, vector dot vector)
	}

	@Test
	fun movedTowards() {
		assertEquals(FloatVector2.ONE, FloatVector2.ZERO.movedTowards(FloatVector2.ONE, 2f))
	}

	@Test
	fun normalized() {
		assertEquals(FloatVector2.RIGHT, FloatVector2(2f, 0f).normalized())
	}

	@Test
	fun reflected() {
		assertEquals(FloatVector2(1f, -2f), FloatVector2(1f, 2f).reflected(FloatVector2(0f, -1f)))
	}

	// Mutable
	@Test
	fun asFloatArray() {
		val result = vector.toMutableFloatVector2()
		assertTrue(result.asFloatArray().contentEquals(floatArrayOf(1f, 2f)))
		result.asFloatArray()[1] = 20f
		assertEquals(20f, result.y)
	}

	@Test
	fun set() {
		val result = MutableFloatVector2()
		assertFailsWith(IndexOutOfBoundsException::class) { result[-1] = 1f }
		result[0] = 1f
		assertEquals(1f, result.x)
		result[1] = 1f
		assertEquals(1f, result.y)
		assertFailsWith(IndexOutOfBoundsException::class) { result[2] = 1f }
	}

	@Test
	fun plusAssign_scalar() {
		val result = vector.toMutableFloatVector2()
		result += 1f
		assertEquals(FloatVector2(2f, 3f), result)
	}

	@Test
	fun minusAssign_scalar() {
		val result = vector.toMutableFloatVector2()
		result -= 1f
		assertEquals(FloatVector2(0f, 1f), result)
	}

	@Test
	fun timesAssign_scalar() {
		val result = vector.toMutableFloatVector2()
		result *= 2f
		assertEquals(FloatVector2(2f, 4f), result)
	}

	@Test
	fun divAssign_scalar() {
		val result = vector.toMutableFloatVector2()
		result /= 2f
		assertEquals(FloatVector2(0.5f, 1f), result)
	}

	@Test
	fun remAssign_scalar() {
		val result = vector.toMutableFloatVector2()
		result %= 2f
		assertEquals(FloatVector2(1f, 0f), result)
	}

	@Test
	fun plusAssign_vector() {
		val result = vector.toMutableFloatVector2()
		result += vector
		assertEquals(FloatVector2(2f, 4f), result)
	}

	@Test
	fun minusAssign_vector() {
		val result = vector.toMutableFloatVector2()
		result -= vector
		assertEquals(FloatVector2.ZERO, result)
	}

	@Test
	fun timesAssign_vector() {
		val result = vector.toMutableFloatVector2()
		result *= vector
		assertEquals(FloatVector2(1f, 4f), result)
	}

	@Test
	fun divAssign_vector() {
		val result = vector.toMutableFloatVector2()
		result /= vector
		assertEquals(FloatVector2.ONE, result)
	}

	@Test
	fun remAssign_vector() {
		val result = vector.toMutableFloatVector2()
		result %= vector
		assertEquals(FloatVector2(0f), result)
	}

	@Test
	fun set1() {
		val result = MutableFloatVector2()
		result.set(1f)
		assertEquals(FloatVector2.ONE, result)
	}

	@Test
	fun set2() {
		val result = MutableFloatVector2()
		result.set(1f, 2f)
		assertEquals(FloatVector2(1f, 2f), result)
	}

	@Test
	fun moveTowards() {
		val result = MutableFloatVector2()
		result.moveTowards(FloatVector2.ONE, 2f)
		assertEquals(FloatVector2.ONE, result)
	}

	@Test
	fun normalize() {
		val result = MutableFloatVector2(2f, 0f)
		result.normalize()
		assertEquals(FloatVector2(1f, 0f), result)
	}

	@Test
	fun reflect() {
		val result = MutableFloatVector2(1f, 2f)
		result.reflect(FloatVector2(0f, -1f))
		assertEquals(FloatVector2(1f, -2f), result)
	}

	// top-level
	@Test
	fun toFloatArray() {
		assertTrue(vector.toFloatArray().contentEquals(floatArrayOf(1f, 2f)))
	}

	@Test
	fun toFloatVector2() {
		assertEquals(vector, vector.toFloatVector2())
	}

	@Test
	fun toMutableFloatVector2() {
		val result: MutableFloatVector2 = vector.toMutableFloatVector2()
		assertEquals(vector, result)
	}

	@Test
	fun angle() {
		assertEquals(90f.degrees, angle(FloatVector2.UP, FloatVector2.RIGHT))
	}

	@Test
	fun signedAngle() {
		assertEquals((-90f).degrees, signedAngle(FloatVector2.UP, FloatVector2.RIGHT))
	}

	@Test
	fun distance() {
		assertEquals(sqrt(2f), distance(FloatVector2.ZERO, FloatVector2.ONE))
	}

	@Test
	fun lerp() {
		assertEquals(FloatVector2(0.25f), lerp(FloatVector2(0.5f), FloatVector2(1f), -0.5f))
	}

	@Test
	fun max() {
		assertEquals(FloatVector2(2f, 2f), max(FloatVector2(1f, 2f), FloatVector2(2f, 1f)))
	}

	@Test
	fun min() {
		assertEquals(FloatVector2(1f, 1f), min(FloatVector2(1f, 2f), FloatVector2(2f, 1f)))
	}

	@Test
	fun scalar_plus() {
		assertEquals(FloatVector2(2f, 3f), 1f + vector)
	}

	@Test
	fun scalar_minus() {
		assertEquals(FloatVector2(0f, -1f), 1f - vector)
	}

	@Test
	fun scalar_times() {
		assertEquals(FloatVector2(2f, 4f), 2f * vector)
	}

	@Test
	fun scalar_div() {
		assertEquals(FloatVector2(2f, 1f), 2f / vector)
	}

	@Test
	fun scalar_rem() {
		assertEquals(FloatVector2(0f, 1f), 1f % vector)
	}
}
