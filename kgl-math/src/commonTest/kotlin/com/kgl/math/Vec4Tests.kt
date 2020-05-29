package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class Vec4Tests {
	private val vector = Vec4(1f, 2f, 3f, 4f)

	@Test
	fun constants() {
		assertEquals(Vec4(0f), Vec4.ZERO)
		assertEquals(Vec4(1f), Vec4.ONE)
		assertEquals(Vec4(Float.POSITIVE_INFINITY), Vec4.POSITIVE_INFINITY)
		assertEquals(Vec4(Float.NEGATIVE_INFINITY), Vec4.NEGATIVE_INFINITY)
	}

	@Test
	fun copy() {
		assertEquals(Vec4(1f, 2f, 3f, 4f), vector.copy())
	}

	@Test
	fun get() {
		assertFailsWith(IndexOutOfBoundsException::class) { vector[-1] }
		assertEquals(vector.x, vector[0])
		assertEquals(vector.y, vector[1])
		assertEquals(vector.z, vector[2])
		assertEquals(vector.w, vector[3])
		assertFailsWith(IndexOutOfBoundsException::class) { vector[4] }
	}

	@Test
	fun unaryPlus() {
		assertEquals(Vec4(1f, 2f, 3f, 4f), +vector)
	}

	@Test
	fun unaryMinus() {
		assertEquals(Vec4(-1f, -2f, -3f, -4f), -vector)
	}

	@Test
	fun plus_scalar() {
		assertEquals(Vec4(2f, 3f, 4f, 5f), vector + 1f)
	}

	@Test
	fun minus_scalar() {
		assertEquals(Vec4(0f, 1f, 2f, 3f), vector - 1f)
	}

	@Test
	fun times_scalar() {
		assertEquals(Vec4(2f, 4f, 6f, 8f), vector * 2f)
	}

	@Test
	fun div_scalar() {
		assertEquals(Vec4(0.5f, 1f, 1.5f, 2f), vector / 2f)
	}

	@Test
	fun rem_scalar() {
		assertEquals(Vec4(1f, 0f, 1f, 0f), vector % 2f)
	}

	@Test
	fun plus_result() {
		assertEquals(Vec4(2f, 4f, 6f, 8f), vector + vector)
	}

	@Test
	fun minus_result() {
		assertEquals(Vec4.ZERO, vector - vector)
	}

	@Test
	fun times_result() {
		assertEquals(Vec4(1f, 4f, 9f, 16f), vector * vector)
	}

	@Test
	fun div_result() {
		assertEquals(Vec4.ONE, vector / vector)
	}

	@Test
	fun rem_result() {
		assertEquals(Vec4.ZERO, vector % vector)
	}

	@Test
	fun squareMagnitude() {
		assertEquals(30f, vector.squareMagnitude)
	}

	@Test
	fun magnitude() {
		assertEquals(sqrt(30f), vector.magnitude)
	}

	@Test
	fun dot() {
		assertEquals(30f, vector dot vector)
	}

	@Test
	fun movedTowards() {
		assertEquals(Vec4.ONE, Vec4.ZERO.movedTowards(Vec4.ONE, 2f))
	}

	@Test
	fun normalized() {
		assertEquals(Vec4(1f, 0f, 0f, 0f), Vec4(2f, 0f, 0f, 0f).normalized())
	}

	@Test
	fun projected() {
		assertEquals(Vec4(2f, 0f, 2f, 0f), Vec4(2f, 2f, 2f, 2f).projected(Vec4(1f, 0f, 1f, 0f)))
	}

	// Mutable
	@Test
	fun asFloatArray() {
		val result = vector.toMutableVec4()
		assertTrue(result.asFloatArray().contentEquals(floatArrayOf(1f, 2f, 3f, 4f)))
		result.asFloatArray()[2] = 20f
		assertEquals(20f, result.z)
	}

	@Test
	fun set() {
		val result = MutableVec4()
		assertFailsWith(IndexOutOfBoundsException::class) { result[-1] = 1f }
		result[0] = 1f
		assertEquals(1f, result.x)
		result[1] = 1f
		assertEquals(1f, result.y)
		result[2] = 1f
		assertEquals(1f, result.z)
		result[3] = 1f
		assertEquals(1f, result.w)
		assertFailsWith(IndexOutOfBoundsException::class) { result[4] = 1f }
	}

	@Test
	fun plusAssign_scalar() {
		val result = vector.toMutableVec4()
		result += 1f
		assertEquals(Vec4(2f, 3f, 4f, 5f), result)
	}

	@Test
	fun minusAssign_scalar() {
		val result = vector.toMutableVec4()
		result -= 1f
		assertEquals(Vec4(0f, 1f, 2f, 3f), result)
	}

	@Test
	fun timesAssign_scalar() {
		val result = vector.toMutableVec4()
		result *= 2f
		assertEquals(Vec4(2f, 4f, 6f, 8f), result)
	}

	@Test
	fun divAssign_scalar() {
		val result = vector.toMutableVec4()
		result /= 2f
		assertEquals(Vec4(0.5f, 1f, 1.5f, 2f), result)
	}

	@Test
	fun remAssign_scalar() {
		val result = vector.toMutableVec4()
		result %= 2f
		assertEquals(Vec4(1f, 0f, 1f, 0f), result)
	}

	@Test
	fun plusAssign_result() {
		val result = vector.toMutableVec4()
		result += vector
		assertEquals(Vec4(2f, 4f, 6f, 8f), result)
	}

	@Test
	fun minusAssign_result() {
		val result = vector.toMutableVec4()
		result -= vector
		assertEquals(Vec4.ZERO, result)
	}

	@Test
	fun timesAssign_result() {
		val result = vector.toMutableVec4()
		result *= vector
		assertEquals(Vec4(1f, 4f, 9f, 16f), result)
	}

	@Test
	fun divAssign_result() {
		val result = vector.toMutableVec4()
		result /= vector
		assertEquals(Vec4.ONE, result)
	}

	@Test
	fun remAssign_result() {
		val result = vector.toMutableVec4()
		result %= vector
		assertEquals(Vec4.ZERO, result)
	}

	@Test
	fun set1() {
		val result = MutableVec4()
		result.set(1f)
		assertEquals(Vec4.ONE, result)
	}

	@Test
	fun set4() {
		val result = MutableVec4()
		result.set(1f, 2f, 3f, 4f)
		assertEquals(Vec4(1f, 2f, 3f, 4f), result)
	}

	@Test
	fun moveTowards() {
		val result = MutableVec4()
		result.moveTowards(Vec4.ONE, 2f)
		assertEquals(Vec4.ONE, result)
	}

	@Test
	fun normalize() {
		val result = MutableVec4(2f, 0f, 0f, 0f)
		result.normalize()
		assertEquals(Vec4(1f, 0f, 0f, 0f), result)
	}

	@Test
	fun project() {
		val result = MutableVec4(2f, 2f, 2f, 2f)
		result.project(Vec4(1f, 0f, 1f, 0f))
		assertEquals(Vec4(2f, 0f, 2f, 0f), result)
	}

	// top-level
	@Test
	fun toFloatArray() {
		assertTrue(vector.toFloatArray().contentEquals(floatArrayOf(1f, 2f, 3f, 4f)))
	}

	@Test
	fun toVec4() {
		assertEquals(vector, vector.toVec4())
	}

	@Test
	fun toMutableVec4() {
		val result: MutableVec4 = vector.toMutableVec4()
		assertEquals(vector, result)
	}

	@Test
	fun distance() {
		assertEquals(2f, distance(Vec4(0f), Vec4(1f)))
	}

	@Test
	fun lerp() {
		assertEquals(Vec4(0.25f), lerp(Vec4(0.5f), Vec4(1f), -0.5f))
	}

	@Test
	fun max() {
		assertEquals(Vec4(2f, 2f, 2f, 2f), max(Vec4(1f, 2f, 1f, 2f), Vec4(2f, 1f, 2f, 1f)))
	}

	@Test
	fun min() {
		assertEquals(Vec4(1f, 1f, 1f, 1f), min(Vec4(1f, 2f, 1f, 2f), Vec4(2f, 1f, 2f, 1f)))
	}

	@Test
	fun scalar_plus() {
		assertEquals(Vec4(2f), 1f + Vec4.ONE)
	}

	@Test
	fun scalar_minus() {
		assertEquals(Vec4(0f), 1f - Vec4.ONE)
	}

	@Test
	fun scalar_times() {
		assertEquals(Vec4(1f), 1f * Vec4.ONE)
	}

	@Test
	fun scalar_div() {
		assertEquals(Vec4(1f), 1f / Vec4.ONE)
	}

	@Test
	fun scalar_rem() {
		assertEquals(Vec4(0f), 1f % Vec4.ONE)
	}
}
