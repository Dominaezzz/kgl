package com.kgl.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class Vec3Tests {
	private val vector = Vec3(1f, 2f, 3f)

	@Test
	fun constants() {
		assertEquals(Vec3(0f), Vec3.ZERO)
		assertEquals(Vec3(1f), Vec3.ONE)
		assertEquals(Vec3(1f, 0f, 0f), Vec3.RIGHT)
		assertEquals(Vec3(-1f, 0f, 0f), Vec3.LEFT)
		assertEquals(Vec3(0f, 1f, 0f), Vec3.UP)
		assertEquals(Vec3(0f, -1f, 0f), Vec3.DOWN)
		assertEquals(Vec3(0f, 0f, 1f), Vec3.FORWARD)
		assertEquals(Vec3(0f, 0f, -1f), Vec3.BACK)
		assertEquals(Vec3(Float.POSITIVE_INFINITY), Vec3.POSITIVE_INFINITY)
		assertEquals(Vec3(Float.NEGATIVE_INFINITY), Vec3.NEGATIVE_INFINITY)
	}

	@Test
	fun copy() {
		assertEquals(Vec3(1f, 2f, 3f), vector.copy())
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
		assertEquals(Vec3(1f, 2f, 3f), +vector)
	}

	@Test
	fun unaryMinus() {
		assertEquals(Vec3(-1f, -2f, -3f), -vector)
	}

	@Test
	fun plus_scalar() {
		assertEquals(Vec3(2f, 3f, 4f), vector + 1f)
	}

	@Test
	fun minus_scalar() {
		assertEquals(Vec3(0f, 1f, 2f), vector - 1f)
	}

	@Test
	fun times_scalar() {
		assertEquals(Vec3(2f, 4f, 6f), vector * 2f)
	}

	@Test
	fun div_scalar() {
		assertEquals(Vec3(0.5f, 1f, 1.5f), vector / 2f)
	}

	@Test
	fun rem_scalar() {
		assertEquals(Vec3(1f, 0f, 1f), vector % 2f)
	}

	@Test
	fun plus_vector() {
		assertEquals(Vec3(2f, 4f, 6f), vector + vector)
	}

	@Test
	fun minus_vector() {
		assertEquals(Vec3.ZERO, vector - vector)
	}

	@Test
	fun times_vector() {
		assertEquals(Vec3(1f, 4f, 9f), vector * vector)
	}

	@Test
	fun div_vector() {
		assertEquals(Vec3.ONE, vector / vector)
	}

	@Test
	fun rem_vector() {
		assertEquals(Vec3.ZERO, vector % vector)
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
		assertEquals(Vec3.FORWARD, Vec3.RIGHT cross Vec3.UP)
	}

	@Test
	fun dot() {
		assertEquals(14f, vector dot vector)
	}

	@Test
	fun movedTowards() {
		assertEquals(Vec3.ONE, Vec3.ZERO.movedTowards(Vec3.ONE, 2f))
	}

	@Test
	fun normalized() {
		assertEquals(Vec3.RIGHT, Vec3(2f, 0f, 0f).normalized())
	}

	@Test
	fun projected() {
		assertEquals(Vec3(2f, 0f, 2f), Vec3(2f, 2f, 2f).projected(Vec3(1f, 0f, 1f)))
	}

	@Test
	fun reflected() {
		assertEquals(Vec3(1f, 2f, 1f), Vec3(1f, -2f, 1f).reflected(Vec3(0f, 1f, 0f)))
	}

	// Mutable
	@Test
	fun asFloatArray() {
		val result = vector.toMutableVec3()
		assertTrue(result.asFloatArray().contentEquals(floatArrayOf(1f, 2f, 3f)))
		result.asFloatArray()[1] = 20f
		assertEquals(20f, result.y)
	}

	@Test
	fun set() {
		val result = MutableVec3()
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
		val result = vector.toMutableVec3()
		result += 1f
		assertEquals(Vec3(2f, 3f, 4f), result)
	}

	@Test
	fun minusAssign_scalar() {
		val result = vector.toMutableVec3()
		result -= 1f
		assertEquals(Vec3(0f, 1f, 2f), result)
	}

	@Test
	fun timesAssign_scalar() {
		val result = vector.toMutableVec3()
		result *= 2f
		assertEquals(Vec3(2f, 4f, 6f), result)
	}

	@Test
	fun divAssign_scalar() {
		val result = vector.toMutableVec3()
		result /= 2f
		assertEquals(Vec3(0.5f, 1f, 1.5f), result)
	}

	@Test
	fun remAssign_scalar() {
		val result = vector.toMutableVec3()
		result %= 2f
		assertEquals(Vec3(1f, 0f, 1f), result)
	}

	@Test
	fun plusAssign_vector() {
		val result = vector.toMutableVec3()
		result += vector
		assertEquals(Vec3(2f, 4f, 6f), result)
	}

	@Test
	fun minusAssign_vector() {
		val result = vector.toMutableVec3()
		result -= vector
		assertEquals(Vec3.ZERO, result)
	}

	@Test
	fun timesAssign_vector() {
		val result = vector.toMutableVec3()
		result *= result
		assertEquals(Vec3(1f, 4f, 9f), result)
	}

	@Test
	fun divAssign_vector() {
		val result = vector.toMutableVec3()
		result /= vector
		assertEquals(Vec3.ONE, result)
	}

	@Test
	fun remAssign_vector() {
		val result = vector.toMutableVec3()
		result %= vector
		assertEquals(Vec3.ZERO, result)
	}

	@Test
	fun set1() {
		val result = MutableVec3()
		result.set(1f)
		assertEquals(Vec3.ONE, result)
	}

	@Test
	fun set3() {
		val result = MutableVec3()
		result.set(1f, 2f, 3f)
		assertEquals(Vec3(1f, 2f, 3f), result)
	}

	@Test
	fun moveTowards() {
		val result = MutableVec3()
		result.moveTowards(Vec3.ONE, 2f)
		assertEquals(Vec3.ONE, result)
	}

	@Test
	fun normalize() {
		val result = MutableVec3(2f, 0f, 0f)
		result.normalize()
		assertEquals(Vec3(1f, 0f, 0f), result)
	}

	@Test
	fun project() {
		val result = MutableVec3(2f, 2f, 2f)
		result.project(Vec3(1f, 0f, 1f))
		assertEquals(Vec3(2f, 0f, 2f), result)
	}

	@Test
	fun reflect() {
		val result = MutableVec3(1f, -2f, 1f)
		result.reflect(Vec3.UP)
		assertEquals(Vec3(1f, 2f, 1f), result)
	}

	// top-level
	@Test
	fun toFloatArray() {
		assertTrue(vector.toFloatArray().contentEquals(floatArrayOf(1f, 2f, 3f)))
	}

	@Test
	fun toVec3() {
		assertEquals(vector, vector.toVec3())
	}

	@Test
	fun toMutableVec3() {
		val result: MutableVec3 = vector.toMutableVec3()
		assertEquals(vector, result)
	}

	@Test
	fun angle() {
		assertEquals(90f.degrees, angle(Vec3.UP, Vec3.RIGHT))
	}

	@Test
	fun signedAngle() {
		assertEquals((-90f).degrees, signedAngle(Vec3.UP, Vec3.RIGHT, Vec3.FORWARD))
	}

	@Test
	fun distance() {
		assertEquals(sqrt(3f), distance(Vec3(0f), Vec3(1f)))
	}

	@Test
	fun lerp() {
		assertEquals(Vec3(0.25f), lerp(Vec3(0.5f), Vec3(1f), -0.5f))
	}

	@Test
	fun max() {
		assertEquals(Vec3(2f, 2f, 2f), max(Vec3(1f, 2f, 1f), Vec3(2f, 1f, 2f)))
	}

	@Test
	fun min() {
		assertEquals(Vec3(1f, 1f, 1f), min(Vec3(1f, 2f, 1f), Vec3(2f, 1f, 2f)))
	}

	@Test
	fun scalar_plus() {
		assertEquals(Vec3(2f), 1f + Vec3.ONE)
	}

	@Test
	fun scalar_minus() {
		assertEquals(Vec3(0f), 1f - Vec3.ONE)
	}

	@Test
	fun scalar_times() {
		assertEquals(Vec3(1f), 1f * Vec3.ONE)
	}

	@Test
	fun scalar_div() {
		assertEquals(Vec3(1f), 1f / Vec3.ONE)
	}

	@Test
	fun scalar_rem() {
		assertEquals(Vec3(0f), 1f % Vec3.ONE)
	}
}
