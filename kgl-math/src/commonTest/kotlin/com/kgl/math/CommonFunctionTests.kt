package com.kgl.math

import kotlin.test.Test
import kotlin.test.expect

internal val epsilon = Float.fromBits(0x3400_0000)

class CommonFunctionTests {
	@Test
	fun abs_vector() {
		expect(FloatVector4(1f)) {
			abs(FloatVector4(-1f))
		}
	}

	@Test
	fun absInPlace_vector() {
		expect(FloatVector4(1f)) {
			MutableFloatVector4(-1f).also { absInPlace(it) }
		}
	}

	@Test
	fun vector_sign() {
		expect(FloatVector4(-1f)) {
			FloatVector4(-1f).sign
		}
	}

	@Test
	fun floor_vector() {
		expect(FloatVector4(1f)) {
			floor(FloatVector4(1.9f))
		}
	}

	@Test
	fun floorInPlace_vector() {
		expect(FloatVector4(1f)) {
			MutableFloatVector4(1.9f).also { floorInPlace(it) }
		}
	}

	@Test
	fun truncate_vector() {
		expect(FloatVector4(1f)) {
			truncate(FloatVector4(1.5f))
		}
	}

	@Test
	fun truncateInPlace_vector() {
		expect(FloatVector4(1f)) {
			MutableFloatVector4(1.5f).also { truncateInPlace(it) }
		}
	}

	@Test
	fun vector_roundToInt() {
		expect(IntVector4(1)) {
			FloatVector4(1.1f).roundToInt()
		}
	}

	@Test
	fun vector_roundToLong() {
		expect(LongVector4(1)) {
			FloatVector4(1.1f).roundToLong()
		}
	}

	@Test
	fun round_vector() {
		expect(FloatVector4(1f)) {
			round(FloatVector4(1.1f))
		}
	}

	@Test
	fun roundInPlace_vector() {
		expect(FloatVector4(1f)) {
			MutableFloatVector4(1.1f).also { roundInPlace(it) }
		}
	}

	@Test
	fun ceil_vector() {
		expect(FloatVector4(2f)) {
			ceil(FloatVector4(1.1f))
		}
	}

	@Test
	fun ceilInPlace_vector() {
		expect(FloatVector4(2f)) {
			MutableFloatVector4(1.1f).also { ceilInPlace(it) }
		}
	}

	@Test
	fun vector_fraction() {
		expect(FloatVector4(0.5f)) {
			FloatVector4(1.5f).fraction
		}
	}

	@Test
	fun vector_rem_scalar() {
		expect(FloatVector4(0.5f)) {
			FloatVector4(1.5f) % 1f
		}
	}

	@Test
	fun vector_rem_vector() {
		expect(FloatVector4(1f)) {
			FloatVector4(3f) % FloatVector4(2f)
		}
	}

	@Test
	fun vector_remAssign_scalar() {
		expect(FloatVector4(1f)) {
			MutableFloatVector4(3f).also { it %= FloatVector4(2f) }
		}
	}

	@Test
	fun vector_remAssign_vector() {
		expect(FloatVector4(1f)) {
			MutableFloatVector4(3f).also { it %= FloatVector4(2f) }
		}
	}

	@Test
	fun vector_coerceAtMost_scalar() {
		expect(FloatVector4(0f)) {
			FloatVector4(1f).coerceAtMost(0f)
		}
	}

	@Test
	fun vector_coerceAtMost_vector() {
		expect(FloatVector4(0f)) {
			FloatVector4(1f).coerceAtMost(FloatVector4(0f))
		}
	}

	@Test
	fun vector_coerceAssignAtMost_scalar() {
		expect(FloatVector4(0f)) {
			MutableFloatVector4(1f).also { it.coerceAssignAtMost(0f) }
		}
	}

	@Test
	fun vector_coerceAssignAtMost_vector() {
		expect(FloatVector4(0f)) {
			MutableFloatVector4(1f).also { it.coerceAssignAtMost(FloatVector4(0f)) }
		}
	}

	@Test
	fun vector_coerceAtLeast_scalar() {
		expect(FloatVector4(0f)) {
			FloatVector4(-1f).coerceAtLeast(0f)
		}
	}

	@Test
	fun vector_coerceAtLeast_vector() {
		expect(FloatVector4(0f)) {
			FloatVector4(-1f).coerceAtLeast(FloatVector4(0f))
		}
	}

	@Test
	fun vector_coerceAssignAtLeast_scalar() {
		expect(FloatVector4(0f)) {
			MutableFloatVector4(-1f).also { it.coerceAssignAtLeast(0f) }
		}
	}

	@Test
	fun vector_coerceAssignAtLeast_vector() {
		expect(FloatVector4(0f)) {
			MutableFloatVector4(-1f).also { it.coerceAssignAtLeast(FloatVector4(0f)) }
		}
	}

	@Test
	fun mix_scalar_scalar_boolean() {
		expect(0f) {
			mix(0f, 1f, true)
		}
	}

	@Test
	fun mix_scalar_scalar_scalar() {
		expect(0f) {
			mix(-1f, 1f, 0.5f)
		}
	}

	@Test
	fun mix_vector_vector_boolean() {
		expect(FloatVector4(0f)) {
			mix(FloatVector4(0f), FloatVector4(1f), true)
		}
	}

	@Test
	fun mix_vector_vector_booleanVector() {
		expect(FloatVector4(0f)) {
			mix(FloatVector4(0f), FloatVector4(1f), BooleanVector4(true))
		}
	}

	@Test
	fun mix_vector_vector_scalar() {
		expect(FloatVector4(0f)) {
			mix(FloatVector4(-1f), FloatVector4(1f), 0.5f)
		}
	}

	@Test
	fun mix_vector_vector_vector() {
		expect(FloatVector4(0f)) {
			mix(FloatVector4(-1f), FloatVector4(1f), FloatVector4(0.5f))
		}
	}

	@Test
	fun step_scalar_vector() {
		expect(FloatVector4(0f)) {
			step(0f, FloatVector4(1f))
		}
	}

	@Test
	fun step_vector_vector() {
		expect(FloatVector4(0f)) {
			step(FloatVector4(0f), FloatVector4(1f))
		}
	}

	@Test
	fun smoothStep_scalar_scalar_vector() {
		expect(FloatVector4(0.5f)) {
			smoothStep(-1f, 1f, FloatVector4(0f))
		}
	}

	@Test
	fun smoothStep_vector_vector_vector() {
		expect(FloatVector4(0.5f)) {
			smoothStep(FloatVector4(-1f), FloatVector4(1f), FloatVector4(0f))
		}
	}

	@Test
	fun vector_isNan() {
		expect(BooleanVector4(true)) {
			FloatVector4(Float.NaN).isNaN()
		}
	}

	@Test
	fun vector_isInfinite() {
		expect(BooleanVector4(true)) {
			FloatVector4(Float.POSITIVE_INFINITY).isInfinite()
		}
	}

	@Test
	fun vector_isFinite() {
		expect(BooleanVector4(true)) {
			FloatVector4(0f).isFinite()
		}
	}

	@Test
	fun vector_toBits() {
		expect(IntVector4(0x7FC0_0000L.toInt())) {
			FloatVector4(Float.fromBits(0xFFFF_FFFFL.toInt())).toBits()
		}
	}

	@Test
	fun vector_toRawBits() {
		expect(IntVector4(0xFFFF_FFFFL.toInt())) {
			FloatVector4(Float.fromBits(0xFFFF_FFFFL.toInt())).toRawBits()
		}
	}

	@Test
	fun vector_fromBits() {
		expect(BooleanVector4(true)) {
			FloatVector4.fromBits(UIntVector4(0xFFFF_FFFFu)).isNaN() // NaN != NaN
		}
	}

	@Test
	fun vector_toFractionAndExponent() {
		expect(FloatVector4(0.5f) to IntVector4(3)) {
			FloatVector4(4f).toFractionAndExponent()
		}
	}

	@Test
	fun vector_fromFractionAndExponent() {
		expect(FloatVector4(4f)) {
			FloatVector4.fromFractionAndExponent(FloatVector4(0.5f), IntVector4(3))
		}
	}
}
