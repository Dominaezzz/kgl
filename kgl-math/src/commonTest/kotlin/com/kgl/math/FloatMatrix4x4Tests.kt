package com.kgl.math

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FloatMatrix4x4Tests {
	private val matrix = FloatMatrix4x4(
		1f, 2f, 3f, 4f,
		2f, 1f, 2f, 3f,
		3f, 2f, 1f, 2f,
		5f, 3f, 2f, 1f
	)

	@Test
	fun constants() {
		assertEquals(
			expected = FloatMatrix4x4(
				1f, 0f, 0f, 0f,
				0f, 1f, 0f, 0f,
				0f, 0f, 1f, 0f,
				0f, 0f, 0f, 1f
			),
			actual = FloatMatrix4x4.IDENTITY
		)
		assertEquals(
			expected = FloatMatrix4x4(FloatVector4(), FloatVector4(), FloatVector4(), FloatVector4()),
			actual = FloatMatrix4x4.ZERO
		)
	}

	@Test
	fun get() {
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[-1, 0] }
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[0, -1] }
		assertEquals(1f, matrix[0, 0])
		assertEquals(2f, matrix[0, 1])
		assertEquals(3f, matrix[0, 2])
		assertEquals(4f, matrix[0, 3])
		assertEquals(2f, matrix[1, 0])
		assertEquals(1f, matrix[1, 1])
		assertEquals(2f, matrix[1, 2])
		assertEquals(3f, matrix[1, 3])
		assertEquals(3f, matrix[2, 0])
		assertEquals(2f, matrix[2, 1])
		assertEquals(1f, matrix[2, 2])
		assertEquals(2f, matrix[2, 3])
		assertEquals(5f, matrix[3, 0])
		assertEquals(3f, matrix[3, 1])
		assertEquals(2f, matrix[3, 2])
		assertEquals(1f, matrix[3, 3])
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[4, 0] }
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[0, 4] }
	}

	@Test
	fun getRow() {
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.getRow(-1) }
		assertEquals(FloatVector4(1f, 2f, 3f, 4f), matrix.getRow(0))
		assertEquals(FloatVector4(2f, 1f, 2f, 3f), matrix.getRow(1))
		assertEquals(FloatVector4(3f, 2f, 1f, 2f), matrix.getRow(2))
		assertEquals(FloatVector4(5f, 3f, 2f, 1f), matrix.getRow(3))
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.getRow(4) }
	}

	@Test
	fun getColumn() {
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.getColumn(-1) }
		assertEquals(FloatVector4(1f, 2f, 3f, 5f), matrix.getColumn(0))
		assertEquals(FloatVector4(2f, 1f, 2f, 3f), matrix.getColumn(1))
		assertEquals(FloatVector4(3f, 2f, 1f, 2f), matrix.getColumn(2))
		assertEquals(FloatVector4(4f, 3f, 2f, 1f), matrix.getColumn(3))
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.getColumn(4) }
	}

	@Test
	fun determinant() {
		assertEquals(-22f, matrix.determinant)
	}

	@Test
	fun inverse() {
		assertEquals(
			expected = FloatMatrix4x4(
				-4 / 11f, 5 / 11f, 0f, 1 / 11f,
				1 / 2f, -1f, 1 / 2f, 0f,
				2 / 11f, 3 / 11f, -1f, 5 / 11f,
				-1 / 22f, 2 / 11f, 1 / 2f, -4 / 11f
			),
			actual = matrix.inverse
		)
	}

	@Test
	fun plus_scalar() {
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 3f, 4f, 5f,
				3f, 2f, 3f, 4f,
				4f, 3f, 2f, 3f,
				6f, 4f, 3f, 2f
			),
			actual = matrix + 1f
		)
	}

	@Test
	fun minus_scalar() {
		assertEquals(
			expected = FloatMatrix4x4(
				0f, 1f, 2f, 3f,
				1f, 0f, 1f, 2f,
				2f, 1f, 0f, 1f,
				4f, 2f, 1f, 0f
			),
			actual = matrix - 1f
		)
	}

	@Test
	fun times_scalar() {
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 4f, 6f, 8f,
				4f, 2f, 4f, 6f,
				6f, 4f, 2f, 4f,
				10f, 6f, 4f, 2f
			),
			actual = matrix * 2f
		)
	}

	@Test
	fun div_scalar() {
		assertEquals(
			expected = FloatMatrix4x4(
				0.5f, 1f, 1.5f, 2f,
				1f, 0.5f, 1f, 1.5f,
				1.5f, 1f, 0.5f, 1f,
				2.5f, 1.5f, 1f, 0.5f
			),
			actual = matrix / 2f
		)
	}

	@Test
	fun plus_matrix() {
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 3f, 4f, 5f,
				3f, 2f, 3f, 4f,
				4f, 3f, 2f, 3f,
				6f, 4f, 3f, 2f
			),
			actual = matrix + FloatMatrix4x4(
				FloatVector4(1f),
				FloatVector4(1f),
				FloatVector4(1f),
				FloatVector4(1f)
			)
		)
	}

	@Test
	fun minus_matrix() {
		assertEquals(
			expected = FloatMatrix4x4(
				0f, 1f, 2f, 3f,
				1f, 0f, 1f, 2f,
				2f, 1f, 0f, 1f,
				4f, 2f, 1f, 0f
			),
			actual = matrix - FloatMatrix4x4(
				FloatVector4(1f),
				FloatVector4(1f),
				FloatVector4(1f),
				FloatVector4(1f)
			)
		)
	}

	@Test
	fun times_matrix() {
		assertEquals(
			expected = FloatMatrix4x4(
				34f, 22f, 18f, 20f,
				25f, 18f, 16f, 18f,
				20f, 16f, 18f, 22f,
				22f, 20f, 25f, 34f
			),
			actual = matrix * matrix
		)
	}

	@Test
	fun div_matrix() {
		val expected = FloatMatrix4x4.IDENTITY.toFloatArray()
		val actual = (matrix / matrix).toFloatArray()
		for (i in 0..15) {
			assertTrue(abs(expected[i] - actual[i]) <= Float.EPSILON)
		}
	}

	@Test
	fun transposed() {
		assertEquals(
			expected = FloatMatrix4x4(
				1f, 2f, 3f, 5f,
				2f, 1f, 2f, 3f,
				3f, 2f, 1f, 2f,
				4f, 3f, 2f, 1f
			),
			actual = matrix.transposed()
		)
	}

	@Test
	fun translated() {
		assertEquals(
			expected = FloatMatrix4x4(
				1f, 0f, 0f, 0f,
				0f, 1f, 0f, 0f,
				0f, 0f, 1f, 0f,
				1f, 2f, 3f, 1f
			),
			actual = FloatMatrix4x4.IDENTITY.translated(FloatVector3(1f, 2f, 3f))
		)
	}

	@Test
	fun rotated() {
		assertEquals(
			expected = FloatMatrix4x4(
				sqrt(2f) / 2f, sqrt(2f) / 2f, 0f, 0f,
				-sqrt(2f) / 2f, sqrt(2f) / 2f, 0f, 0f,
				0f, 0f, 1f, 0f,
				0f, 0f, 0f, 1f
			),
			actual = FloatMatrix4x4.IDENTITY.rotated(45f.degrees, FloatVector3.FORWARD)
		)
	}

	@Test
	fun scaled() {
		assertEquals(
			expected = FloatMatrix4x4(
				1f, 0f, 0f, 0f,
				0f, 2f, 0f, 0f,
				0f, 0f, 3f, 0f,
				0f, 0f, 0f, 1f
			),
			actual = FloatMatrix4x4.IDENTITY.scaled(FloatVector3(1f, 2f, 3f))
		)
	}

	@Test
	fun translated_rotated_scaled() {
		assertEquals(
			expected = FloatMatrix4x4(
				sqrt(2f) / 2f, sqrt(2f) / 2f, 0f, 0f,
				-sqrt(2f), sqrt(2f), 0f, 0f,
				0f, 0f, 3f, 0f,
				1f, 2f, 3f, 1f
			),
			actual = FloatMatrix4x4.IDENTITY
				.translated(FloatVector3(1f, 2f, 3f))
				.rotated(45f.degrees, FloatVector3.FORWARD)
				.scaled(FloatVector3(1f, 2f, 3f))
		)
	}

	// mutable
	@Test
	fun asFloatArray() {
		val result = matrix.toMutableFloatMatrix4x4()
		assertTrue(
			result.asFloatArray().contentEquals(
				floatArrayOf(
					1f, 2f, 3f, 4f,
					2f, 1f, 2f, 3f,
					3f, 2f, 1f, 2f,
					5f, 3f, 2f, 1f
				)
			)
		)
		result.asFloatArray()[12] = 20f
		assertEquals(20f, result[3, 0])
	}

	@Test
	fun set() {
		val matrix = FloatMatrix4x4.ZERO.toMutableFloatMatrix4x4()
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[-1, 0] = 1f }
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[0, -1] = 1f }
		matrix[0, 0] = 1f
		assertEquals(1f, matrix[0, 0])
		matrix[0, 1] = 2f
		assertEquals(2f, matrix[0, 1])
		matrix[0, 2] = 3f
		assertEquals(3f, matrix[0, 2])
		matrix[0, 3] = 4f
		assertEquals(4f, matrix[0, 3])
		matrix[1, 0] = 2f
		assertEquals(2f, matrix[1, 0])
		matrix[1, 1] = 1f
		assertEquals(1f, matrix[1, 1])
		matrix[1, 2] = 2f
		assertEquals(2f, matrix[1, 2])
		matrix[1, 3] = 3f
		assertEquals(3f, matrix[1, 3])
		matrix[2, 0] = 3f
		assertEquals(3f, matrix[2, 0])
		matrix[2, 1] = 2f
		assertEquals(2f, matrix[2, 1])
		matrix[2, 2] = 1f
		assertEquals(1f, matrix[2, 2])
		matrix[2, 3] = 2f
		assertEquals(2f, matrix[2, 3])
		matrix[3, 0] = 5f
		assertEquals(5f, matrix[3, 0])
		matrix[3, 1] = 3f
		assertEquals(3f, matrix[3, 1])
		matrix[3, 2] = 2f
		assertEquals(2f, matrix[3, 2])
		matrix[3, 3] = 1f
		assertEquals(1f, matrix[3, 3])
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[4, 0] }
		assertFailsWith(IndexOutOfBoundsException::class) { matrix[0, 4] }
	}

	@Test
	fun setRow() {
		val matrix = FloatMatrix4x4.ZERO.toMutableFloatMatrix4x4()
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.setRow(-1, FloatVector4.ONE) }
		matrix.setRow(0, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getRow(0))
		matrix.setRow(1, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getRow(1))
		matrix.setRow(2, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getRow(2))
		matrix.setRow(3, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getRow(3))
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.setRow(4, FloatVector4.ONE) }
	}

	@Test
	fun setColumn() {
		val matrix = FloatMatrix4x4.ZERO.toMutableFloatMatrix4x4()
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.setColumn(-1, FloatVector4.ONE) }
		matrix.setColumn(0, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getColumn(0))
		matrix.setColumn(1, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getColumn(1))
		matrix.setColumn(2, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getColumn(2))
		matrix.setColumn(3, FloatVector4.ONE)
		assertEquals(FloatVector4.ONE, matrix.getColumn(3))
		assertFailsWith(IndexOutOfBoundsException::class) { matrix.setColumn(4, FloatVector4.ONE) }
	}

	@Test
	fun plusAssign_scalar() {
		val result = matrix.toMutableFloatMatrix4x4()
		result += 1f
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 3f, 4f, 5f,
				3f, 2f, 3f, 4f,
				4f, 3f, 2f, 3f,
				6f, 4f, 3f, 2f
			),
			actual = result
		)
	}

	@Test
	fun minusAssign_scalar() {
		val result = matrix.toMutableFloatMatrix4x4()
		result -= 1f
		assertEquals(
			expected = FloatMatrix4x4(
				0f, 1f, 2f, 3f,
				1f, 0f, 1f, 2f,
				2f, 1f, 0f, 1f,
				4f, 2f, 1f, 0f
			),
			actual = result
		)
	}

	@Test
	fun timesAssign_scalar() {
		val result = matrix.toMutableFloatMatrix4x4()
		result *= 2f
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 4f, 6f, 8f,
				4f, 2f, 4f, 6f,
				6f, 4f, 2f, 4f,
				10f, 6f, 4f, 2f
			),
			actual = result
		)
	}

	@Test
	fun divAssign_scalar() {
		val result = matrix.toMutableFloatMatrix4x4()
		result /= 2f
		assertEquals(
			expected = FloatMatrix4x4(
				0.5f, 1f, 1.5f, 2f,
				1f, 0.5f, 1f, 1.5f,
				1.5f, 1f, 0.5f, 1f,
				2.5f, 1.5f, 1f, 0.5f
			),
			actual = result
		)
	}

	@Test
	fun plusAssign_matrix() {
		val result = matrix.toMutableFloatMatrix4x4()
		result += FloatMatrix4x4(
			FloatVector4(1f),
			FloatVector4(1f),
			FloatVector4(1f),
			FloatVector4(1f)
		)
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 3f, 4f, 5f,
				3f, 2f, 3f, 4f,
				4f, 3f, 2f, 3f,
				6f, 4f, 3f, 2f
			),
			actual = result
		)
	}

	@Test
	fun minusAssign_matrix() {
		assertEquals(
			expected = FloatMatrix4x4(
				0f, 1f, 2f, 3f,
				1f, 0f, 1f, 2f,
				2f, 1f, 0f, 1f,
				4f, 2f, 1f, 0f
			),
			actual = matrix - FloatMatrix4x4(
				FloatVector4(1f),
				FloatVector4(1f),
				FloatVector4(1f),
				FloatVector4(1f)
			)
		)
	}

	@Test
	fun timesAssign_matrix() {
		assertEquals(
			expected = FloatMatrix4x4(
				34f, 22f, 18f, 20f,
				25f, 18f, 16f, 18f,
				20f, 16f, 18f, 22f,
				22f, 20f, 25f, 34f
			),
			actual = matrix * matrix
		)
	}

	@Test
	fun divAssign_matrix() {
		val expected = FloatMatrix4x4.IDENTITY.toFloatArray()
		val actual = (matrix / matrix).toFloatArray()
		for (i in 0..15) {
			assertTrue(abs(expected[i] - actual[i]) <= Float.EPSILON)
		}
	}

	@Test
	fun transpose() {
		val result = matrix.toMutableFloatMatrix4x4()
		result.transpose()
		assertEquals(
			expected = FloatMatrix4x4(
				1f, 2f, 3f, 5f,
				2f, 1f, 2f, 3f,
				3f, 2f, 1f, 2f,
				4f, 3f, 2f, 1f
			),
			actual = result
		)
	}

	@Test
	fun translate() {
		val result = FloatMatrix4x4.IDENTITY.toMutableFloatMatrix4x4()
		result.translate(FloatVector3(1f, 2f, 3f))
		assertEquals(
			expected = FloatMatrix4x4(
				1f, 0f, 0f, 0f,
				0f, 1f, 0f, 0f,
				0f, 0f, 1f, 0f,
				1f, 2f, 3f, 1f
			),
			actual = result
		)
	}

	@Test
	fun rotate() {
		val result = FloatMatrix4x4.IDENTITY.toMutableFloatMatrix4x4()
		result.rotate(45f.degrees, FloatVector3.FORWARD)
		assertEquals(
			expected = FloatMatrix4x4(
				sqrt(2f) / 2f, sqrt(2f) / 2f, 0f, 0f,
				-sqrt(2f) / 2f, sqrt(2f) / 2f, 0f, 0f,
				0f, 0f, 1f, 0f,
				0f, 0f, 0f, 1f
			),
			actual = result
		)
	}

	@Test
	fun scale() {
		val result = FloatMatrix4x4.IDENTITY.toMutableFloatMatrix4x4()
		result.scale(FloatVector3(1f, 2f, 3f))
		assertEquals(
			expected = FloatMatrix4x4(
				1f, 0f, 0f, 0f,
				0f, 2f, 0f, 0f,
				0f, 0f, 3f, 0f,
				0f, 0f, 0f, 1f
			),
			actual = result
		)
	}

	@Test
	fun translate_rotate_scale() {
		val result = FloatMatrix4x4.IDENTITY.toMutableFloatMatrix4x4().apply {
			translate(FloatVector3(1f, 2f, 3f))
			rotate(45f.degrees, FloatVector3.FORWARD)
			scale(FloatVector3(1f, 2f, 3f))
		}
		assertEquals(
			expected = FloatMatrix4x4(
				sqrt(2f) / 2f, sqrt(2f) / 2f, 0f, 0f,
				-sqrt(2f), sqrt(2f), 0f, 0f,
				0f, 0f, 3f, 0f,
				1f, 2f, 3f, 1f
			),
			actual = result
		)
	}

	// top level
	@Test
	fun toFloatArray() {
		assertTrue(
			matrix.toFloatArray().contentEquals(
				floatArrayOf(
					1f, 2f, 3f, 4f,
					2f, 1f, 2f, 3f,
					3f, 2f, 1f, 2f,
					5f, 3f, 2f, 1f
				)
			)
		)
	}

	@Test
	fun toFloatMatrix4x4() {
		assertEquals(matrix, matrix.toFloatMatrix4x4())
	}

	@Test
	fun toMutableFloatMatrix4x4() {
		assertEquals(matrix, matrix.toMutableFloatMatrix4x4())
	}

	@Test
	fun scalar_plus() {
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 3f, 4f, 5f,
				3f, 2f, 3f, 4f,
				4f, 3f, 2f, 3f,
				6f, 4f, 3f, 2f
			),
			actual = 1f + matrix
		)
	}

	@Test
	fun scalar_minus() {
		assertEquals(
			expected = FloatMatrix4x4(
				-0f, -1f, -2f, -3f,
				-1f, -0f, -1f, -2f,
				-2f, -1f, -0f, -1f,
				-4f, -2f, -1f, -0f
			),
			actual = 1f - matrix
		)
	}

	@Test
	fun scalar_times() {
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 4f, 6f, 8f,
				4f, 2f, 4f, 6f,
				6f, 4f, 2f, 4f,
				10f, 6f, 4f, 2f
			),
			actual = 2f * matrix
		)
	}

	@Test
	fun scalar_div() {
		assertEquals(
			expected = FloatMatrix4x4(
				2f, 1f, 2 / 3f, 1 / 2f,
				1f, 2f, 1f, 2 / 3f,
				2 / 3f, 1f, 2f, 1f,
				2 / 5f, 2 / 3f, 1f, 2f
			),
			actual = 2f / matrix
		)
	}
}
