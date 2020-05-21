package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FloatVector4Tests {
	@Test
	fun constants() {
		assertEquals(FloatVector4(0f), FloatVector4.ZERO)
		assertEquals(FloatVector4(1f), FloatVector4.ONE)
		assertEquals(FloatVector4(Float.POSITIVE_INFINITY), FloatVector4.POSITIVE_INFINITY)
		assertEquals(FloatVector4(Float.NEGATIVE_INFINITY), FloatVector4.NEGATIVE_INFINITY)
	}

	@Test
	fun copy() {
		assertEquals(FloatVector4(1f, 2f, 3f, 4f), FloatVector4(1f, 2f, 3f, 4f).copy())
	}

	@Test
	fun operator_get() {
		val vector = FloatVector4(1f, 2f, 3f, 4f)
		assertEquals(vector.x, vector[0])
		assertEquals(vector.y, vector[1])
		assertEquals(vector.z, vector[2])
		assertEquals(vector.w, vector[3])
		assertFailsWith(IndexOutOfBoundsException::class) { vector[4] }
	}

	@Test
	fun operator_unaryPlus() {
		assertEquals(FloatVector4(1f), +FloatVector4(1f))
	}

	@Test
	fun operator_unaryMinus() {
		assertEquals(FloatVector4(-1f), -FloatVector4(1f))
	}

	@Test
	fun operator_plus_scalar() {
		assertEquals(FloatVector4(2f), FloatVector4(1f) + 1f)
	}

	@Test
	fun operator_minus_scalar() {
		assertEquals(FloatVector4(0f), FloatVector4(1f) - 1f)
	}

	@Test
	fun operator_times_scalar() {
		assertEquals(FloatVector4(1f), FloatVector4(1f) * 1f)
	}

	@Test
	fun operator_div_scalar() {
		assertEquals(FloatVector4(1f), FloatVector4(1f) / 1f)
	}

	@Test
	fun operator_rem_scalar() {
		assertEquals(FloatVector4(0f), FloatVector4(1f) % 1f)
	}

	@Test
	fun operator_plus_vector() {
		assertEquals(FloatVector4(2f), FloatVector4(1f) + FloatVector4(1f))
	}

	@Test
	fun operator_minus_vector() {
		assertEquals(FloatVector4(0f), FloatVector4(1f) - FloatVector4(1f))
	}

	@Test
	fun operator_times_vector() {
		assertEquals(FloatVector4(1f), FloatVector4(1f) * FloatVector4(1f))
	}

	@Test
	fun operator_div_vector() {
		assertEquals(FloatVector4(1f), FloatVector4(1f) / FloatVector4(1f))
	}

	@Test
	fun operator_rem_vector() {
		assertEquals(FloatVector4(0f), FloatVector4(1f) % FloatVector4(1f))
	}

	@Test
	fun squareMagnitude() {
		assertEquals(16f, FloatVector4(2f).squareMagnitude)
	}

	@Test
	fun magnitude() {
		assertEquals(4f, FloatVector4(2f).magnitude)
	}

	@Test
	fun dot() {
		assertEquals(8f, FloatVector4(1f) dot FloatVector4(2f))
	}

	@Test
	fun movedTowards() {
		assertEquals(FloatVector4(1f), FloatVector4().movedTowards(FloatVector4(1f), 2f))
	}

	@Test
	fun normalized() {
		assertEquals(FloatVector4(1f, 0f, 0f, 0f), FloatVector4(2f, 0f, 0f, 0f).normalized())
	}

	@Test
	fun projected() {
		assertEquals(FloatVector4(2f, 0f, 2f, 0f), FloatVector4(2f, 2f, 2f, 2f).projected(FloatVector4(1f, 0f, 1f, 0f)))
	}

	// Mutable
	@Test
	fun operator_set() {
		val vector = MutableFloatVector4()
		vector[0] = 1f
		assertEquals(1f, vector.x)
		vector[1] = 1f
		assertEquals(1f, vector.y)
		vector[2] = 1f
		assertEquals(1f, vector.z)
		vector[3] = 1f
		assertEquals(1f, vector.w)
		assertFailsWith(IndexOutOfBoundsException::class) { vector[4] = 1f }
	}

	@Test
	fun operator_plusAssign_scalar() {
		val vector = MutableFloatVector4(1f)
		vector += 1f
		assertEquals(FloatVector4(2f), vector)
	}

	@Test
	fun operator_minusAssign_scalar() {
		val vector = MutableFloatVector4(1f)
		vector -= 1f
		assertEquals(FloatVector4(0f), vector)
	}

	@Test
	fun operator_timesAssign_scalar() {
		val vector = MutableFloatVector4(1f)
		vector *= 1f
		assertEquals(FloatVector4(1f), vector)
	}

	@Test
	fun operator_divAssign_scalar() {
		val vector = MutableFloatVector4(1f)
		vector /= 1f
		assertEquals(FloatVector4(1f), vector)
	}

	@Test
	fun operator_remAssign_scalar() {
		val vector = MutableFloatVector4(1f)
		vector %= 1f
		assertEquals(FloatVector4(0f), vector)
	}

	@Test
	fun operator_plusAssign_vector() {
		val vector = MutableFloatVector4(1f)
		vector += vector
		assertEquals(FloatVector4(2f), vector)
	}

	@Test
	fun operator_minusAssign_vector() {
		val vector = MutableFloatVector4(1f)
		vector -= vector
		assertEquals(FloatVector4(0f), vector)
	}

	@Test
	fun operator_timesAssign_vector() {
		val vector = MutableFloatVector4(1f)
		vector *= vector
		assertEquals(FloatVector4(1f), vector)
	}

	@Test
	fun operator_divAssign_vector() {
		val vector = MutableFloatVector4(1f)
		vector /= vector
		assertEquals(FloatVector4(1f), vector)
	}

	@Test
	fun operator_remAssign_vector() {
		val vector = MutableFloatVector4(1f)
		vector %= vector
		assertEquals(FloatVector4(0f), vector)
	}

	@Test
	fun set1() {
		val vector = MutableFloatVector4()
		vector.set(1f)
		assertEquals(FloatVector4(1f), vector)
	}

	@Test
	fun set4() {
		val vector = MutableFloatVector4()
		vector.set(1f, 2f, 3f, 4f)
		assertEquals(FloatVector4(1f, 2f, 3f, 4f), vector)
	}

	@Test
	fun moveTowards() {
		val vector = MutableFloatVector4()
		vector.moveTowards(FloatVector4(1f), 2f)
		assertEquals(FloatVector4(1f), vector)
	}

	@Test
	fun normalize() {
		val vector = MutableFloatVector4(2f, 0f, 0f, 0f)
		vector.normalize()
		assertEquals(FloatVector4(1f, 0f, 0f, 0f), vector)
	}

	@Test
	fun project() {
		val vector = MutableFloatVector4(2f, 2f, 2f, 2f)
		vector.project(FloatVector4(1f, 0f, 1f, 0f))
		assertEquals(FloatVector4(2f, 0f, 2f, 0f), vector)
	}

	// top-level
	@Test
	fun distance() {
		assertEquals(2f, distance(FloatVector4(0f), FloatVector4(1f)))
	}

	@Test
	fun lerp() {
		assertEquals(FloatVector4(0.25f), lerp(FloatVector4(0.5f), FloatVector4(1f), -0.5f))
	}

	@Test
	fun max() {
		assertEquals(FloatVector4(2f, 2f, 2f, 2f), max(FloatVector4(1f, 2f, 1f, 2f), FloatVector4(2f, 1f, 2f, 1f)))
	}

	@Test
	fun min() {
		assertEquals(FloatVector4(1f, 1f, 1f, 1f), min(FloatVector4(1f, 2f, 1f, 2f), FloatVector4(2f, 1f, 2f, 1f)))
	}

	@Test
	fun operator_scalar_plus() {
		assertEquals(FloatVector4(2f), 1f + FloatVector4.ONE)
	}

	@Test
	fun operator_scalar_minus() {
		assertEquals(FloatVector4(0f), 1f - FloatVector4.ONE)
	}

	@Test
	fun operator_scalar_times() {
		assertEquals(FloatVector4(1f), 1f * FloatVector4.ONE)
	}

	@Test
	fun operator_scalar_div() {
		assertEquals(FloatVector4(1f), 1f / FloatVector4.ONE)
	}

	@Test
	fun operator_scalar_rem() {
		assertEquals(FloatVector4(0f), 1f % FloatVector4.ONE)
	}
}
