package com.kgl.math

import kotlin.math.E
import kotlin.test.Test
import kotlin.test.expect

class ExponentialFunctionTests {
	@Test
	fun vector_pow_int() {
		expect(FloatVector4(4f)) {
			FloatVector4(2f).pow(2)
		}
	}

	@Test
	fun vector_pow_scalar() {
		expect(FloatVector4(4f)) {
			FloatVector4(2f).pow(2f)
		}
	}

	@Test
	fun vector_pow_intVector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			FloatVector4(2f).pow(IntVector4(0, 1, 2, 3))
		}
	}

	@Test
	fun vector_pow_vector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			FloatVector4(2f).pow(FloatVector4(0f, 1f, 2f, 3f))
		}
	}

	@Test
	fun vector_powAssign_int() {
		expect(FloatVector4(4f)) {
			MutableFloatVector4(2f).also { it.powAssign(2) }
		}
	}

	@Test
	fun vector_powAssign_scalar() {
		expect(FloatVector4(4f)) {
			MutableFloatVector4(2f).also { it.powAssign(2f) }
		}
	}

	@Test
	fun vector_powAssign_intVector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			MutableFloatVector4(2f).also { it.powAssign(IntVector4(0, 1, 2, 3)) }
		}
	}

	@Test
	fun vector_powAssign_vector() {
		expect(FloatVector4(1f, 2f, 4f, 8f)) {
			MutableFloatVector4(2f).also { it.powAssign(FloatVector4(0f, 1f, 2f, 3f)) }
		}
	}

	@Test
	fun exp_vector() {
		expect(true) {
			exp(FloatVector4(2f)).equal(FloatVector4((E * E).toFloat()), epsilon).all()
		}
	}

	@Test
	fun expInPlace_vector() {
		expect(true) {
			MutableFloatVector4(2f).also { expInPlace(it) }.equal(FloatVector4((E * E).toFloat()), epsilon).all()
		}
	}

	@Test
	fun ln_vector() {
		expect(FloatVector4(2f)) {
			ln(FloatVector4((E * E).toFloat()))
		}
	}

	@Test
	fun lnInPlace_vector() {
		expect(FloatVector4(2f)) {
			MutableFloatVector4((E * E).toFloat()).also { lnInPlace(it) }
		}
	}

	@Test
	fun exp2_vector() {
		expect(FloatVector4(4f)) {
			exp2(FloatVector4(2f))
		}
	}

	@Test
	fun exp2InPlace_vector() {
		expect(FloatVector4(4f)) {
			MutableFloatVector4(2f).also { exp2InPlace(it) }
		}
	}

	@Test
	fun log2_vector() {
		expect(FloatVector4(2f)) {
			log2(FloatVector4(4f))
		}
	}

	@Test
	fun log2InPlace_vector() {
		expect(FloatVector4(0f, 1f, 2f, 3f)) {
			MutableFloatVector4(1f, 2f, 4f, 8f).also { log2InPlace(it) }
		}
	}

	@Test
	fun log10_vector() {
		expect(FloatVector4(2f)) {
			log10(FloatVector4(100f))
		}
	}

	@Test
	fun log10InPlace_vector() {
		expect(FloatVector4(2f)) {
			MutableFloatVector4(100f).also { log10InPlace(it) }
		}
	}

	@Test
	fun sqrt_vector() {
		expect(FloatVector4(2f)) {
			sqrt(FloatVector4(4f))
		}
	}

	@Test
	fun sqrtInPlace_vector() {
		expect(FloatVector4(2f)) {
			MutableFloatVector4(4f).also { sqrtInPlace(it) }
		}
	}

	@Test
	fun inverseSqrt_vector() {
		expect(FloatVector4(1 / 2f)) {
			inverseSqrt(FloatVector4(4f))
		}
	}

	@Test
	fun inverseSqrtInPlace_vector() {
		expect(FloatVector4(1 / 2f)) {
			MutableFloatVector4(4f).also { inverseSqrtInPlace(it) }
		}
	}
}
