package com.kgl.math

import kotlin.test.Test
import kotlin.test.expect

class GeometricFunctionTests {
	@Test
	fun vector_length() {
		expect(1f) {
			FloatVector4.X.length
		}
	}

	@Test
	fun distance_vector_vector() {
		expect(0f) {
			distance(FloatVector4.X, FloatVector4.X)
		}
	}

	@Test
	fun vector_dot_vector() {
		expect(4f) {
			FloatVector4(1f) dot FloatVector4(1f)
		}
	}

	@Test
	fun vector_cross_vector() {
		expect(FloatVector3.Z) {
			FloatVector3.X cross FloatVector3.Y
		}
	}

	@Test
	fun vector_normalized() {
		expect(FloatVector4(1f, 0f, 0f, 0f)) {
			FloatVector4(5f, 0f, 0f, 0f).normalized()
		}
	}

	@Test
	fun vector_normalize() {
		expect(FloatVector4(1f, 0f, 0f, 0f)) {
			MutableFloatVector4(5f, 0f, 0f, 0f).also { it.normalize() }
		}
	}

	@Test
	fun vector_facedForward_vector_vector() {
		expect(-FloatVector4.X) {
			FloatVector4.X.facedForward(FloatVector4(1f, 0f, 0f, 1f), FloatVector4.W)
		}
	}

	@Test
	fun vector_faceForward_vector_vector() {
		expect(-FloatVector4.X) {
			MutableFloatVector4(1f, 0f, 0f, 0f).also { it.faceForward(FloatVector4(1f, 0f, 0f, 1f), FloatVector4.W) }
		}
	}

	@Test
	fun vector_reflected_vector() {
		expect(FloatVector4(1f, 1f, 0f, 0f)) {
			FloatVector4(1f, -1f, 0f, 0f).reflected(FloatVector4.Y)
		}
	}

	@Test
	fun vector_reflect_vector() {
		expect(FloatVector4(1f, 1f, 0f, 0f)) {
			MutableFloatVector4(1f, -1f, 0f, 0f).also { it.reflect(FloatVector4.Y) }
		}
	}

	@Test
	fun vector_refracted_vector_scalar() {
		expect(FloatVector4(0f, -1f, 0f, 0f)) {
			FloatVector4(0f, -1f, 0f, 0f).refracted(FloatVector4(0f, 1f, 0f, 0f), 0.5f)
		}
	}

	@Test
	fun vector_refract_vector_scalar() {
		expect(FloatVector4(0f, -1f, 0f, 0f)) {
			MutableFloatVector4(0f, -1f, 0f, 0f).also { it.refract(FloatVector4(0f, 1f, 0f, 0f), 0.5f) }
		}
	}
}
