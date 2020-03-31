package com.kgl.math

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

open class CommonFunctionsTest {

	@Test
	fun vec4_abs() {
		val expected = FloatVector4(0.1f, 2.3f, 4.5f, 6.7f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).abs()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_absAssign() {
		val expected = FloatVector4(0.1f, 2.3f, 4.5f, 6.7f)
		val actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		actual.absAssign()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_sign() {
		val expected = FloatVector4(1f, -1f, 1f, -1f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).sign
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_floor() {
		val expected = FloatVector4(0f, -3f, 4f, -7f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).floor()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_floorAssign() {
		val expected = FloatVector4(0f, -3f, 4f, -7f)
		val actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		actual.floorAssign()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_truncate() {
		val expected = FloatVector4(0f, -2f, 4f, -6f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).truncate()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_truncateAssign() {
		val expected = FloatVector4(0f, -2f, 4f, -6f)
		val actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		actual.truncateAssign()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_roundToInt() {
		val expected = IntVector4(0, -2, 5, -7)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).roundToInt()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_roundToLong() {
		val expected = LongVector4(0, -2, 5, -7)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).roundToLong()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_round() {
		val expected = FloatVector4(0f, -2f, 4f, -7f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).round()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_roundAssign() {
		val expected = FloatVector4(0f, -2f, 4f, -7f)
		val actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		actual.roundAssign()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_ceil() {
		val expected = FloatVector4(1f, -2f, 5f, -6f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).ceil()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_ceilAssign() {
		val expected = FloatVector4(1f, -2f, 5f, -6f)
		val actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		actual.ceilAssign()
		assertEquals(expected, actual)
	}

	@Test
	fun vec4_fraction() {
		// FIXME test currently fails because of inherent rounding errors (result differs on the order of 10e-6)
		//  The rounding errors are a kotlin artifact; the same code in C yields the correct result on the same machine
		val expected = FloatVector4(.1f, -.3f, .5f, -.7f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).fraction
		assertSame(expected, actual)
	}

	@Test
	fun vec4_rem() {

	}
}
