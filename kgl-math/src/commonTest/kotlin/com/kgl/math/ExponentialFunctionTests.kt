package com.kgl.math

import kotlin.math.E
import kotlin.test.Test
import kotlin.test.expect

class ExponentialFunctionTests {

	private val epsilon = FloatVector4(1e-6f)

	@Test
	fun vec4_pow_int() {
		expect(FloatVector4(4f)) {
			FloatVector4(2f).pow(2)
		}
	}

	@Test
	fun vec4_powAssign_int() {
		expect(FloatVector4(4f)) {
			MutableFloatVector4(2f).also { it.powAssign(2) }
		}
	}

	@Test
	fun vec4_pow_scalar() {
		expect(FloatVector4(4f)) {
			FloatVector4(2f).pow(2f)
		}
	}

	@Test
	fun vec4_powAssign_scalar() {
		expect(FloatVector4(4f)) {
			MutableFloatVector4(2f).also { it.powAssign(2f) }
		}
	}

	@Test
	fun vec4_pow_intVector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			FloatVector4(2f).pow(IntVector4(0, 1, 2, 3))
		}
	}

	@Test
	fun vec4_powAssign_intVector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			MutableFloatVector4(2f).also { it.powAssign(IntVector4(0, 1, 2, 3)) }
		}
	}

	@Test
	fun vec4_pow_vector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			FloatVector4(2f).pow(FloatVector4(0f, 1f, 2f, 3f))
		}
	}

	@Test
	fun vec4_powAssign_vector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			MutableFloatVector4(2f).also { it.powAssign(FloatVector4(0f, 1f, 2f, 3f)) }
		}
	}

	@Test
	fun vec4_exp() {
		val expected = FloatVector4(1f, E.toFloat(), (E * E).toFloat(), (E * E * E).toFloat())
		val actual = exp(FloatVector4(0f, 1f, 2f, 3f))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_expInPlace() {
		val expected = FloatVector4(1f, E.toFloat(), (E * E).toFloat(), (E * E * E).toFloat())
		val actual = MutableFloatVector4(0f, 1f, 2f, 3f).also { expInPlace(it) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_ln() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = ln(FloatVector4(1f, E.toFloat(), (E * E).toFloat(), (E * E * E).toFloat()))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_lnInPlace() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = MutableFloatVector4(1f, E.toFloat(), (E * E).toFloat(), (E * E * E).toFloat()).also { lnInPlace(it) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_exp2() {
		val expected = FloatVector4(1f, 2f, 4f, 8f)
		val actual = exp2(FloatVector4(0f, 1f, 2f, 3f))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_exp2InPlace() {
		val expected = FloatVector4(1f, 2f, 4f, 8f)
		val actual = MutableFloatVector4(0f, 1f, 2f, 3f).also { exp2InPlace(it) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_log2() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = log2(FloatVector4(1f, 2f, 4f, 8f))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_log2InPlace() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = MutableFloatVector4(1f, 2f, 4f, 8f).also { log2InPlace(it) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_log10() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = log10(FloatVector4(1f, 10f, 100f, 1000f))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_log10InPlace() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = MutableFloatVector4(1f, 10f, 100f, 1000f).also { log10InPlace(it) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_log() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = log(FloatVector4(1f, 3f, 16f, 125f), FloatVector4(2f, 3f, 4f, 5f))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_logInPlace() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = MutableFloatVector4(1f, 3f, 16f, 125f).also { logInPlace(it, FloatVector4(2f, 3f, 4f, 5f)) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_sqrt() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = sqrt(FloatVector4(0f, 1f, 4f, 9f))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_sqrtInPlace() {
		val expected = FloatVector4(0f, 1f, 2f, 3f)
		val actual = MutableFloatVector4(0f, 1f, 4f, 9f).also { sqrtInPlace(it) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_inverseSqrt() {
		val expected = FloatVector4(1f, 1 / 2f, 1 / 3f, 1 / 4f)
		val actual = inverseSqrt(FloatVector4(1f, 4f, 9f, 16f))
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}

	@Test
	fun vec4_inverseSqrtInPlace() {
		val expected = FloatVector4(1f, 1 / 2f, 1 / 3f, 1 / 4f)
		val actual = MutableFloatVector4(1f, 4f, 9f, 16f).also { inverseSqrtInPlace(it) }
		expect(true, "\nExpected :$expected\nActual   :$actual\nEpsilon  :$epsilon") {
			(abs(expected - actual) lessThan epsilon).all()
		}
	}
}
