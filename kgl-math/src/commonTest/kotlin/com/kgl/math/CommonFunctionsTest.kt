package com.kgl.math

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class CommonFunctionsTest {

	private val epsilon = FloatVector4(1e-6f)

	@Test
	fun vec4_abs() {
		expect(FloatVector4(0.1f, 2.3f, 4.5f, 6.7f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).abs()
		}
	}

	@Test
	fun vec4_absAssign() {
		expect(FloatVector4(0.1f, 2.3f, 4.5f, 6.7f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.absAssign() }
		}
	}

	@Test
	fun vec4_sign() {
		expect(FloatVector4(1f, -1f, 1f, -1f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).sign
		}
	}

	@Test
	fun vec4_floor() {
		expect(FloatVector4(0f, -3f, 4f, -7f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).floor()
		}
	}

	@Test
	fun vec4_floorAssign() {
		expect(FloatVector4(0f, -3f, 4f, -7f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.floorAssign() }
		}
	}

	@Test
	fun vec4_truncate() {
		expect(FloatVector4(0f, -2f, 4f, -6f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).truncate()
		}
	}

	@Test
	fun vec4_truncateAssign() {
		expect(FloatVector4(0f, -2f, 4f, -6f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.truncateAssign() }
		}
	}

	@Test
	fun vec4_roundToInt() {
		expect(IntVector4(0, -2, 5, -7)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).roundToInt()
		}
	}

	@Test
	fun vec4_roundToLong() {
		expect(LongVector4(0, -2, 5, -7)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).roundToLong()
		}
	}

	@Test
	fun vec4_round() {
		expect(FloatVector4(0f, -2f, 4f, -7f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).round()
		}
	}

	@Test
	fun vec4_roundAssign() {
		expect(FloatVector4(0f, -2f, 4f, -7f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.roundAssign() }
		}
	}

	@Test
	fun vec4_ceil() {
		expect(FloatVector4(1f, -2f, 5f, -6f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).ceil()
		}
	}

	@Test
	fun vec4_ceilAssign() {
		expect(FloatVector4(1f, -2f, 5f, -6f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.ceilAssign() }
		}
	}

	@Test
	fun vec4_fraction() {
		val expected = FloatVector4(0.1f, -0.3f, 0.5f, -0.7f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).fraction
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_rem_scalar() {
		val expected = FloatVector4(0.1f, -0.8f, 0f, -0.7f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f) % 1.5f
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_rem_vector() {
		val expected = FloatVector4(0.1f, -0.3f, 0f, -0.7f)
		val actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f) % FloatVector4(0.5f, 1f, 1.5f, 2f)
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_remAssign_scalar() {
		val expected = FloatVector4(0.1f, -0.8f, 0f, -0.7f)
		val actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.remAssign(1.5f) }
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_remAssign_vector() {
		val expected = FloatVector4(0.1f, -0.3f, 0f, -0.7f)
		val actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.remAssign(FloatVector4(0.5f, 1f, 1.5f, 2f)) }
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_coerceAtMost_scalar() {
		expect(FloatVector4(0f, -2.3f, 0f, -6.7f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).coerceAtMost(0f)
		}
	}

	@Test
	fun vec4_coerceAtMost_vector() {
		expect(FloatVector4(0f, -2.3f, 2f, -6.7f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).coerceAtMost(FloatVector4(0f, 1f, 2f, 3f))
		}
	}

	@Test
	fun vec4_coerceAssignAtMost_scalar() {
		expect(FloatVector4(0f, -2.3f, 0f, -6.7f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.coerceAssignAtMost(0f) }
		}
	}

	@Test
	fun vec4_coerceAssignAtMost_vector() {
		expect(FloatVector4(0f, -2.3f, 2f, -6.7f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.coerceAssignAtMost(FloatVector4(0f, 1f, 2f, 3f)) }
		}
	}

	@Test
	fun vec4_coerceAtLeast_scalar() {
		expect(FloatVector4(0.1f, 0f, 4.5f, 0f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).coerceAtLeast(0f)
		}
	}

	@Test
	fun vec4_coerceAtLeast_vector() {
		expect(FloatVector4(0.1f, 1f, 4.5f, 3f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).coerceAtLeast(FloatVector4(0f, 1f, 2f, 3f))
		}
	}

	@Test
	fun vec4_coerceAssignAtLeast_scalar() {
		expect(FloatVector4(0.1f, 0f, 4.5f, 0f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.coerceAssignAtLeast(0f) }
		}
	}

	@Test
	fun vec4_coerceAssignAtLeast_vector() {
		expect(FloatVector4(0.1f, 1f, 4.5f, 3f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.coerceAssignAtLeast(FloatVector4(0f, 1f, 2f, 3f)) }
		}
	}

	@Test
	fun vec4_coerceIn_scalar() {
		expect(FloatVector4(0.1f, -1f, 1f, -1f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).coerceIn(-1f, 1f)
		}
	}

	@Test
	fun vec4_coerceIn_vector() {
		assertEquals(
			expected = FloatVector4(0.1f, -1f, 1f, -3f),
			actual = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
				.coerceIn(FloatVector4(-0f, -1f, -2f, -3f), FloatVector4(3f, 2f, 1f, 0f))
		)
	}

	@Test
	fun vec4_coerceIn_range() {
		expect(FloatVector4(0.1f, -1f, 1f, -1f)) {
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f).coerceIn(-1f..1f)
		}
	}

	@Test
	fun vec4_coerceAssignIn_scalar() {
		expect(FloatVector4(0.1f, -1f, 1f, -1f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.coerceAssignIn(-1f, 1f) }
		}
	}

	@Test
	fun vec4_coerceAssignIn_vector() {
		assertEquals(
			expected = FloatVector4(0.1f, -1f, 1f, -3f),
			actual = MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also {
				it.coerceAssignIn(FloatVector4(-0f, -1f, -2f, -3f), FloatVector4(3f, 2f, 1f, 0f))
			}
		)
	}

	@Test
	fun vec4_coerceAssignIn_range() {
		expect(FloatVector4(0.1f, -1f, 1f, -1f)) {
			MutableFloatVector4(0.1f, -2.3f, 4.5f, -6.7f).also { it.coerceAssignIn(-1f..1f) }
		}
	}

	@Test
	fun vec4_mix_boolean() {
		val x = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		val y = FloatVector4(8.9f, -10.11f, 12.13f, -14.15f)
		expect(FloatVector4(0.1f, -2.3f, 4.5f, -6.7f)) {
			mix(x, y, true)
		}
	}

	@Test
	fun vec4_mix_booleanVector() {
		val x = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		val y = FloatVector4(8.9f, -10.11f, 12.13f, -14.15f)
		val a = BooleanVector4(x = true, y = false, z = true, w = false)
		expect(FloatVector4(0.1f, -10.11f, 4.5f, -14.15f)) {
			mix(x, y, a)
		}
	}

	@Test
	fun vec4_mix_scalar() {
		val expected = FloatVector4(4.5f, -6.205f, 8.315f, -10.425f)
		val x = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		val y = FloatVector4(8.9f, -10.11f, 12.13f, -14.15f)
		val actual = mix(x, y, 0.5f)
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_mix_vector() {
		val expected = FloatVector4(4.5f, -6.205f, 8.315f, -10.425f)
		val x = FloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		val y = FloatVector4(8.9f, -10.11f, 12.13f, -14.15f)
		val a = FloatVector4(0.5f)
		val actual = mix(x, y, a)
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_step_scalar() {
		expect(FloatVector4(0f, 1f, 0f, 1f)) {
			step(0f, FloatVector4(0.1f, -2.3f, 4.5f, -6.7f))
		}
	}

	@Test
	fun vec4_step_vector() {
		expect(FloatVector4(0f, 1f, 1f, 1f)) {
			step(FloatVector4(0f, 2.5f, 5f, 7.5f), FloatVector4(0.1f, -2.3f, 4.5f, -6.7f))
		}
	}

	@Test
	fun vec4_smoothStep_scalar() {
		val expected = FloatVector4(0.57475f, 0f, 1f, 0f)
		val actual = smoothStep(-1f, 1f, FloatVector4(0.1f, -2.3f, 4.5f, -6.7f))
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_smoothStep_vector() {
		val expected = FloatVector4(0.5f)
		val actual = smoothStep(
			FloatVector4(0f),
			FloatVector4(0.2f, -4.6f, 9f, -13.4f),
			FloatVector4(0.1f, -2.3f, 4.5f, -6.7f)
		)
		expect(true, "Expected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			((expected - actual).abs() lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_isNan() {
		expect(BooleanVector4(x = true, y = false, z = true, w = false)) {
			FloatVector4(Float.NaN, 0f, Float.NaN, 0f).isNaN()
		}
	}

	@Test
	fun vec4_isInfinite() {
		expect(BooleanVector4(x = true, y = false, z = true, w = false)) {
			FloatVector4(Float.POSITIVE_INFINITY, 0f, Float.NEGATIVE_INFINITY, 0f).isInfinite()
		}
	}

	@Test
	fun vec4_isFinite() {
		expect(BooleanVector4(x = true, y = false, z = true, w = false)) {
			FloatVector4(0f, Float.POSITIVE_INFINITY, 0f, Float.NEGATIVE_INFINITY).isFinite()
		}
	}

	@Test
	fun vec4_toBits() {
		expect(IntVector4(0b1__1111_1111__0000000_00000000_00000000L.toInt())) {
			FloatVector4(Float.NEGATIVE_INFINITY).toBits()
		}
	}

	@Test
	fun vec4_fromBits() {
		expect(true) {
			FloatVector4.fromBits(UIntVector4(0xFFFFFFFFu)).isNaN().all()
		}
	}

	@Test
	fun vec4_toFractionAndExponent() {
		expect(FloatVector4(0.5f) to IntVector4(3)) {
			FloatVector4(4f).toFractionAndExponent()
		}
	}

	@Test
	fun vec4_fromFractionAndExponent() {
		expect(FloatVector4(4f)) {
			FloatVector4.fromFractionAndExponent(FloatVector4(0.5f), IntVector4(3))
		}
	}
}
