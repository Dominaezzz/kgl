package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FloatVector2Tests {
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
		assertEquals(FloatVector2(1f, 2f), FloatVector2(1f, 2f).copy())
	}

	@Test
	fun operator_get() {
		val vector = FloatVector2(1f, 2f)
		assertEquals(vector.x, vector[0])
		assertEquals(vector.y, vector[1])
		assertFailsWith(IndexOutOfBoundsException::class) { vector[2] }
	}

	@Test
	fun operator_plus_scalar() {
		assertEquals(FloatVector2(2f), FloatVector2(1f) + 1f)
	}

	@Test
	fun operator_minus_scalar() {
		assertEquals(FloatVector2(0f), FloatVector2(1f) - 1f)
	}

	@Test
	fun operator_times_scalar() {
		assertEquals(FloatVector2(1f), FloatVector2(1f) * 1f)
	}

	@Test
	fun operator_div_scalar() {
		assertEquals(FloatVector2(1f), FloatVector2(1f) / 1f)
	}

	@Test
	fun operator_rem_scalar() {
		assertEquals(FloatVector2(0f), FloatVector2(1f) % 1f)
	}

	@Test
	fun operator_plus_vector() {
		assertEquals(FloatVector2(2f), FloatVector2(1f) + FloatVector2(1f))
	}

	@Test
	fun operator_minus_vector() {
		assertEquals(FloatVector2(0f), FloatVector2(1f) - FloatVector2(1f))
	}

	@Test
	fun operator_times_vector() {
		assertEquals(FloatVector2(1f), FloatVector2(1f) * FloatVector2(1f))
	}

	@Test
	fun operator_div_vector() {
		assertEquals(FloatVector2(1f), FloatVector2(1f) / FloatVector2(1f))
	}

	@Test
	fun operator_rem_vector() {
		assertEquals(FloatVector2(0f), FloatVector2(1f) % FloatVector2(1f))
	}

	@Test
	fun squareMagnitude() {
		assertEquals(8f, FloatVector2(2f, 2f).squareMagnitude)
	}

	@Test
	fun magnitude() {
		assertEquals(sqrt(8f), FloatVector2(2f, 2f).magnitude)
	}

	@Test
	fun perpendicular() {
		assertEquals(FloatVector2.UP, FloatVector2.RIGHT.perpendicular)
	}

	@Test
	fun dot() {
		assertEquals(4f, FloatVector2(1f) dot FloatVector2(2f))
	}

	@Test
	fun movedTowards() {
		assertEquals(FloatVector2(1f), FloatVector2().movedTowards(FloatVector2(1f), 2f))
	}

	@Test
	fun normalized() {
		assertEquals(FloatVector2(1f, 0f), FloatVector2(2f, 0f).normalized())
	}

	@Test
	fun reflected() {
		assertEquals(FloatVector2(1f, -2f), FloatVector2(1f, 2f).reflected(FloatVector2(0f, -1f)))
	}

	// Mutable
	@Test
	fun operator_set() {
		val vector = MutableFloatVector2()
		vector[0] = 1f
		assertEquals(1f, vector.x)
		vector[1] = 1f
		assertEquals(1f, vector.y)
		assertFailsWith(IndexOutOfBoundsException::class) { vector[2] = 1f }
	}

	@Test
	fun operator_plusAssign_scalar() {
		val vector = MutableFloatVector2(1f)
		vector += 1f
		assertEquals(FloatVector2(2f), vector)
	}

	@Test
	fun operator_minusAssign_scalar() {
		val vector = MutableFloatVector2(1f)
		vector -= 1f
		assertEquals(FloatVector2(0f), vector)
	}

	@Test
	fun operator_timesAssign_scalar() {
		val vector = MutableFloatVector2(1f)
		vector *= 1f
		assertEquals(FloatVector2(1f), vector)
	}

	@Test
	fun operator_divAssign_scalar() {
		val vector = MutableFloatVector2(1f)
		vector /= 1f
		assertEquals(FloatVector2(1f), vector)
	}

	@Test
	fun operator_remAssign_scalar() {
		val vector = MutableFloatVector2(1f)
		vector %= 1f
		assertEquals(FloatVector2(0f), vector)
	}

	@Test
	fun operator_plusAssign_vector() {
		val vector = MutableFloatVector2(1f)
		vector += vector
		assertEquals(FloatVector2(2f), vector)
	}

	@Test
	fun operator_minusAssign_vector() {
		val vector = MutableFloatVector2(1f)
		vector -= vector
		assertEquals(FloatVector2(0f), vector)
	}

	@Test
	fun operator_timesAssign_vector() {
		val vector = MutableFloatVector2(1f)
		vector *= vector
		assertEquals(FloatVector2(1f), vector)
	}

	@Test
	fun operator_divAssign_vector() {
		val vector = MutableFloatVector2(1f)
		vector /= vector
		assertEquals(FloatVector2(1f), vector)
	}

	@Test
	fun operator_remAssign_vector() {
		val vector = MutableFloatVector2(1f)
		vector %= vector
		assertEquals(FloatVector2(0f), vector)
	}

	@Test
	fun set1() {
		val vector = MutableFloatVector2()
		vector.set(1f)
		assertEquals(FloatVector2(1f), vector)
	}

	@Test
	fun set2() {
		val vector = MutableFloatVector2()
		vector.set(1f, 2f)
		assertEquals(FloatVector2(1f, 2f), vector)
	}

	@Test
	fun moveTowards() {
		val vector = MutableFloatVector2()
		vector.moveTowards(FloatVector2(1f), 2f)
		assertEquals(FloatVector2(1f), vector)
	}

	@Test
	fun normalize() {
		val vector = MutableFloatVector2(2f, 0f)
		vector.normalize()
		assertEquals(FloatVector2(1f, 0f), vector)
	}

	@Test
	fun reflect() {
		val vector = MutableFloatVector2(1f, 2f)
		vector.reflect(FloatVector2(0f, -1f))
		assertEquals(FloatVector2(1f, -2f), vector)
	}

	// top-level
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
		assertEquals(sqrt(2f), distance(FloatVector2(0f), FloatVector2(1f)))
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
	fun operator_scalar_plus() {
		assertEquals(FloatVector2(2f), 1f + FloatVector2.ONE)
	}

	@Test
	fun operator_scalar_minus() {
		assertEquals(FloatVector2(0f), 1f - FloatVector2.ONE)
	}

	@Test
	fun operator_scalar_times() {
		assertEquals(FloatVector2(1f), 1f * FloatVector2.ONE)
	}

	@Test
	fun operator_scalar_div() {
		assertEquals(FloatVector2(1f), 1f / FloatVector2.ONE)
	}

	@Test
	fun operator_scalar_rem() {
		assertEquals(FloatVector2(0f), 1f % FloatVector2.ONE)
	}
}
