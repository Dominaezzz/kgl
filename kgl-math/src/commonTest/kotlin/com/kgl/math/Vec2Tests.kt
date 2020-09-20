package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class Vec2Tests {
	private val vector = Vec2(1f, 2f)

	@Test
	fun constants() {
		assertEquals(Vec2(0f), Vec2.ZERO)
		assertEquals(Vec2(1f), Vec2.ONE)
		assertEquals(Vec2(0f, 1f), Vec2.UP)
		assertEquals(Vec2(0f, -1f), Vec2.DOWN)
		assertEquals(Vec2(-1f, 0f), Vec2.LEFT)
		assertEquals(Vec2(1f, 0f), Vec2.RIGHT)
		assertEquals(Vec2(Float.POSITIVE_INFINITY), Vec2.POSITIVE_INFINITY)
		assertEquals(Vec2(Float.NEGATIVE_INFINITY), Vec2.NEGATIVE_INFINITY)
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
		assertEquals(Vec2(1f, 2f), +vector)
	}

	@Test
	fun unaryMinus() {
		assertEquals(Vec2(-1f, -2f), -vector)
	}

	@Test
	fun plus_scalar() {
		assertEquals(Vec2(2f, 3f), vector + 1f)
	}

	@Test
	fun minus_scalar() {
		assertEquals(Vec2(0f, 1f), vector - 1f)
	}

	@Test
	fun times_scalar() {
		assertEquals(Vec2(2f, 4f), vector * 2f)
	}

	@Test
	fun div_scalar() {
		assertEquals(Vec2(0.5f, 1f), vector / 2f)
	}

	@Test
	fun rem_scalar() {
		assertEquals(Vec2(1f, 0f), vector % 2f)
	}

	@Test
	fun plus_vector() {
		assertEquals(Vec2(2f, 4f), vector + vector)
	}

	@Test
	fun minus_vector() {
		assertEquals(Vec2.ZERO, vector - vector)
	}

	@Test
	fun times_vector() {
		assertEquals(Vec2(1f, 4f), vector * vector)
	}

	@Test
	fun div_vector() {
		assertEquals(Vec2.ONE, vector / vector)
	}

	@Test
	fun rem_vector() {
		assertEquals(Vec2.ZERO, vector % vector)
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
		assertEquals(Vec2.UP, Vec2.RIGHT.perpendicular)
	}

	@Test
	fun dot() {
		assertEquals(5f, vector dot vector)
	}

	@Test
	fun movedTowards() {
		assertEquals(Vec2.ONE, Vec2.ZERO.movedTowards(Vec2.ONE, 2f))
	}

	@Test
	fun normalized() {
		assertEquals(Vec2.RIGHT, Vec2(2f, 0f).normalized())
	}

	@Test
	fun reflected() {
		assertEquals(Vec2(1f, -2f), Vec2(1f, 2f).reflected(Vec2(0f, -1f)))
	}

	// Mutable
	@Test
	fun asFloatArray() {
		val result = vector.toMutableVec2()
		assertTrue(result.asFloatArray().contentEquals(floatArrayOf(1f, 2f)))
		result.asFloatArray()[1] = 20f
		assertEquals(20f, result.y)
	}

	@Test
	fun set() {
		val result = MutableVec2()
		assertFailsWith(IndexOutOfBoundsException::class) { result[-1] = 1f }
		result[0] = 1f
		assertEquals(1f, result.x)
		result[1] = 1f
		assertEquals(1f, result.y)
		assertFailsWith(IndexOutOfBoundsException::class) { result[2] = 1f }
	}

	@Test
	fun plusAssign_scalar() {
		val result = vector.toMutableVec2()
		result += 1f
		assertEquals(Vec2(2f, 3f), result)
	}

	@Test
	fun minusAssign_scalar() {
		val result = vector.toMutableVec2()
		result -= 1f
		assertEquals(Vec2(0f, 1f), result)
	}

	@Test
	fun timesAssign_scalar() {
		val result = vector.toMutableVec2()
		result *= 2f
		assertEquals(Vec2(2f, 4f), result)
	}

	@Test
	fun divAssign_scalar() {
		val result = vector.toMutableVec2()
		result /= 2f
		assertEquals(Vec2(0.5f, 1f), result)
	}

	@Test
	fun remAssign_scalar() {
		val result = vector.toMutableVec2()
		result %= 2f
		assertEquals(Vec2(1f, 0f), result)
	}

	@Test
	fun plusAssign_vector() {
		val result = vector.toMutableVec2()
		result += vector
		assertEquals(Vec2(2f, 4f), result)
	}

	@Test
	fun minusAssign_vector() {
		val result = vector.toMutableVec2()
		result -= vector
		assertEquals(Vec2.ZERO, result)
	}

	@Test
	fun timesAssign_vector() {
		val result = vector.toMutableVec2()
		result *= vector
		assertEquals(Vec2(1f, 4f), result)
	}

	@Test
	fun divAssign_vector() {
		val result = vector.toMutableVec2()
		result /= vector
		assertEquals(Vec2.ONE, result)
	}

	@Test
	fun remAssign_vector() {
		val result = vector.toMutableVec2()
		result %= vector
		assertEquals(Vec2(0f), result)
	}

	@Test
	fun set1() {
		val result = MutableVec2()
		result.set(1f)
		assertEquals(Vec2.ONE, result)
	}

	@Test
	fun set2() {
		val result = MutableVec2()
		result.set(1f, 2f)
		assertEquals(Vec2(1f, 2f), result)
	}

	@Test
	fun moveTowards() {
		val result = MutableVec2()
		result.moveTowards(Vec2.ONE, 2f)
		assertEquals(Vec2.ONE, result)
	}

	@Test
	fun normalize() {
		val result = MutableVec2(2f, 0f)
		result.normalize()
		assertEquals(Vec2(1f, 0f), result)
	}

	@Test
	fun reflect() {
		val result = MutableVec2(1f, 2f)
		result.reflect(Vec2(0f, -1f))
		assertEquals(Vec2(1f, -2f), result)
	}

	// top-level
	@Test
	fun toFloatArray() {
		assertTrue(vector.toFloatArray().contentEquals(floatArrayOf(1f, 2f)))
	}

	@Test
	fun toVec2() {
		assertEquals(vector, vector.toVec2())
	}

	@Test
	fun toMutableVec2() {
		val result: MutableVec2 = vector.toMutableVec2()
		assertEquals(vector, result)
	}

	@Test
	fun angle() {
		assertEquals(90f.degrees, angle(Vec2.UP, Vec2.RIGHT))
	}

	@Test
	fun signedAngle() {
		assertEquals((-90f).degrees, signedAngle(Vec2.UP, Vec2.RIGHT))
	}

	@Test
	fun distance() {
		assertEquals(sqrt(2f), distance(Vec2.ZERO, Vec2.ONE))
	}

	@Test
	fun lerp() {
		assertEquals(Vec2(0.25f), lerp(Vec2(0.5f), Vec2(1f), -0.5f))
	}

	@Test
	fun max() {
		assertEquals(Vec2(2f, 2f), max(Vec2(1f, 2f), Vec2(2f, 1f)))
	}

	@Test
	fun min() {
		assertEquals(Vec2(1f, 1f), min(Vec2(1f, 2f), Vec2(2f, 1f)))
	}

	@Test
	fun scalar_plus() {
		assertEquals(Vec2(2f, 3f), 1f + vector)
	}

	@Test
	fun scalar_minus() {
		assertEquals(Vec2(0f, -1f), 1f - vector)
	}

	@Test
	fun scalar_times() {
		assertEquals(Vec2(2f, 4f), 2f * vector)
	}

	@Test
	fun scalar_div() {
		assertEquals(Vec2(2f, 1f), 2f / vector)
	}

	@Test
	fun scalar_rem() {
		assertEquals(Vec2(0f, 1f), 1f % vector)
	}
}
