package com.kgl.math

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class MathTests {
	@Test
	fun degrees() {
		assertEquals((PI / 2).toFloat(), 90f.degrees)
	}

	@Test
	fun lerp() {
		assertEquals(0.25f, lerp(0.5f, 1f, -0.5f))
	}

	@Test
	fun moveTowards() {
		assertEquals(0f, 0.5f.moveTowards(1f, -0.5f))
	}
}
