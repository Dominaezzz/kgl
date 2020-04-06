package com.kgl.math

import kotlin.test.Test
import kotlin.test.expect

class ExperimentalExtensionTests {
	@Test
	fun vec4_log() {
		expect(FloatVector4(2f)) {
			log(FloatVector4(16f), FloatVector4(4f))
		}
	}

	@Test
	fun vec4_logInPlace() {
		expect(FloatVector4(2f)) {
			MutableFloatVector4(16f).also { logInPlace(it, FloatVector4(4f)) }
		}
	}
}
