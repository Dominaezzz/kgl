package com.kgl.math

import kotlin.math.cos
import kotlin.math.sin

sealed class FloatMatrix4x4 {
	companion object {
		val IDENTITY = FloatMatrix4x4(
			1f, 0f, 0f, 0f,
			0f, 1f, 0f, 0f,
			0f, 0f, 1f, 0f,
			0f, 0f, 0f, 1f
		)
		val ZERO = FloatMatrix4x4(
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f
		)
	}

	fun copy(
		c00: Float = this[0, 0], c01: Float = this[0, 1], c02: Float = this[0, 2], c03: Float = this[0, 3],
		c10: Float = this[1, 0], c11: Float = this[1, 1], c12: Float = this[1, 2], c13: Float = this[1, 3],
		c20: Float = this[2, 0], c21: Float = this[2, 1], c22: Float = this[2, 2], c23: Float = this[2, 3],
		c30: Float = this[3, 0], c31: Float = this[3, 1], c32: Float = this[3, 2], c33: Float = this[3, 3]
	): FloatMatrix4x4 = FloatMatrix4x4(
		c00, c01, c02, c03,
		c10, c11, c12, c13,
		c20, c21, c22, c23,
		c30, c31, c32, c33
	)

	// FIXME: currently, this version is called of no parameters are provided
	fun copy(
		row0: FloatVector4 = getRow(0),
		row1: FloatVector4 = getRow(1),
		row2: FloatVector4 = getRow(2),
		row3: FloatVector4 = getRow(3)
	): FloatMatrix4x4 = FloatMatrix4x4(row0, row1, row2, row3)

	abstract operator fun get(row: Int, column: Int): Float

	fun getRow(row: Int): FloatVector4 {
		if (row !in 0..3) throw IndexOutOfBoundsException()
		return FloatVector4(this[row, 0], this[row, 1], this[row, 2], this[row, 3])
	}

	fun getColumn(column: Int): FloatVector4 {
		if (column !in 0..3) throw IndexOutOfBoundsException()
		return FloatVector4(this[0, column], this[1, column], this[2, column], this[3, column])
	}

	val determinant: Float
		get() {
			val a = this[0, 0]
			val b = this[0, 1]
			val c = this[0, 2]
			val d = this[0, 3]
			val e = this[1, 0]
			val f = this[1, 1]
			val g = this[1, 2]
			val h = this[1, 3]
			val i = this[2, 0]
			val j = this[2, 1]
			val k = this[2, 2]
			val l = this[2, 3]
			val m = this[3, 0]
			val n = this[3, 1]
			val o = this[3, 2]
			val p = this[3, 3]

			val ijmn = i * n - j * m
			val ikmo = i * o - k * m
			val ilmp = i * p - l * m
			val jkno = j * o - k * n
			val jlnp = j * p - l * n
			val klop = k * p - l * o

			val efgijkmno = e * jkno - f * ikmo + g * ijmn
			val efhijlmnp = e * jlnp - f * ilmp + h * ijmn
			val eghiklmop = e * klop - g * ilmp + h * ikmo
			val fghjklnop = f * klop - g * jlnp + h * jkno

			return a * fghjklnop - b * eghiklmop + c * efhijlmnp - d * efgijkmno
		}

	val inverse: FloatMatrix4x4
		get() {
			val a = this[0, 0]
			val b = this[0, 1]
			val c = this[0, 2]
			val d = this[0, 3]
			val e = this[1, 0]
			val f = this[1, 1]
			val g = this[1, 2]
			val h = this[1, 3]
			val i = this[2, 0]
			val j = this[2, 1]
			val k = this[2, 2]
			val l = this[2, 3]
			val m = this[3, 0]
			val n = this[3, 1]
			val o = this[3, 2]
			val p = this[3, 3]

			val efij = e * j - f * i
			val efmn = e * n - f * m
			val egik = e * k - g * i
			val egmo = e * o - g * m
			val ehil = e * l - h * i
			val ehmp = e * p - h * m
			val fgjk = f * k - g * j
			val fgno = f * o - g * n
			val fhjl = f * l - h * j
			val fhnp = f * p - h * n
			val ghkl = g * l - h * k
			val ghop = g * p - h * o
			val ijmn = i * n - j * m
			val ikmo = i * o - k * m
			val ilmp = i * p - l * m
			val jkno = j * o - k * n
			val jlnp = j * p - l * n
			val klop = k * p - l * o

			val abcefgijk = a * fgjk - b * egik + c * efij
			val abcefgmno = a * fgno - b * egmo + c * efmn
			val abcijkmno = a * jkno - b * ikmo + c * ijmn
			val abdefhijl = a * fhjl - b * ehil + d * efij
			val abdefhmnp = a * fhnp - b * ehmp + d * efmn
			val abdijlmnp = a * jlnp - b * ilmp + d * ijmn
			val acdeghikl = a * ghkl - c * ehil + d * egik
			val acdeghmop = a * ghop - c * ehmp + d * egmo
			val acdiklmop = a * klop - c * ilmp + d * ikmo
			val bcdfghjkl = b * ghkl - c * fhjl + d * fgjk
			val bcdfghnop = b * ghop - c * fhnp + d * fgno
			val bcdjklnop = b * klop - c * jlnp + d * jkno
			val efgijkmno = e * jkno - f * ikmo + g * ijmn
			val efhijlmnp = e * jlnp - f * ilmp + h * ijmn
			val eghiklmop = e * klop - g * ilmp + h * ikmo
			val fghjklnop = f * klop - g * jlnp + h * jkno

			return when (val determinant = a * fghjklnop - b * eghiklmop + c * efhijlmnp - d * efgijkmno) {
				0f -> {
					FloatMatrix4x4(
						Float.NaN, Float.NaN, Float.NaN, Float.NaN,
						Float.NaN, Float.NaN, Float.NaN, Float.NaN,
						Float.NaN, Float.NaN, Float.NaN, Float.NaN,
						Float.NaN, Float.NaN, Float.NaN, Float.NaN
					)
				}
				else -> {
					val inv = 1 / determinant
					FloatMatrix4x4(
						+(inv * fghjklnop), -(inv * bcdjklnop), +(inv * bcdfghnop), -(inv * bcdfghjkl),
						-(inv * eghiklmop), +(inv * acdiklmop), -(inv * acdeghmop), +(inv * acdeghikl),
						+(inv * efhijlmnp), -(inv * abdijlmnp), +(inv * abdefhmnp), -(inv * abdefhijl),
						-(inv * efgijkmno), +(inv * abcijkmno), -(inv * abcefgmno), +(inv * abcefgijk)
					)
				}
			}
		}

	operator fun plus(scalar: Float): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0] + scalar, this[0, 1] + scalar, this[0, 2] + scalar, this[0, 3] + scalar,
		this[1, 0] + scalar, this[1, 1] + scalar, this[1, 2] + scalar, this[1, 3] + scalar,
		this[2, 0] + scalar, this[2, 1] + scalar, this[2, 2] + scalar, this[2, 3] + scalar,
		this[3, 0] + scalar, this[3, 1] + scalar, this[3, 2] + scalar, this[3, 3] + scalar
	)

	operator fun minus(scalar: Float): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0] - scalar, this[0, 1] - scalar, this[0, 2] - scalar, this[0, 3] - scalar,
		this[1, 0] - scalar, this[1, 1] - scalar, this[1, 2] - scalar, this[1, 3] - scalar,
		this[2, 0] - scalar, this[2, 1] - scalar, this[2, 2] - scalar, this[2, 3] - scalar,
		this[3, 0] - scalar, this[3, 1] - scalar, this[3, 2] - scalar, this[3, 3] - scalar
	)

	operator fun times(scalar: Float): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0] * scalar, this[0, 1] * scalar, this[0, 2] * scalar, this[0, 3] * scalar,
		this[1, 0] * scalar, this[1, 1] * scalar, this[1, 2] * scalar, this[1, 3] * scalar,
		this[2, 0] * scalar, this[2, 1] * scalar, this[2, 2] * scalar, this[2, 3] * scalar,
		this[3, 0] * scalar, this[3, 1] * scalar, this[3, 2] * scalar, this[3, 3] * scalar
	)

	operator fun div(scalar: Float): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0] / scalar, this[0, 1] / scalar, this[0, 2] / scalar, this[0, 3] / scalar,
		this[1, 0] / scalar, this[1, 1] / scalar, this[1, 2] / scalar, this[1, 3] / scalar,
		this[2, 0] / scalar, this[2, 1] / scalar, this[2, 2] / scalar, this[2, 3] / scalar,
		this[3, 0] / scalar, this[3, 1] / scalar, this[3, 2] / scalar, this[3, 3] / scalar
	)

	operator fun plus(other: FloatMatrix4x4): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0] + other[0, 0], this[0, 1] + other[0, 1], this[0, 2] + other[0, 2], this[0, 3] + other[0, 3],
		this[1, 0] + other[1, 0], this[1, 1] + other[1, 1], this[1, 2] + other[1, 2], this[1, 3] + other[1, 3],
		this[2, 0] + other[2, 0], this[2, 1] + other[2, 1], this[2, 2] + other[2, 2], this[2, 3] + other[2, 3],
		this[3, 0] + other[3, 0], this[3, 1] + other[3, 1], this[3, 2] + other[3, 2], this[3, 3] + other[3, 3]
	)

	operator fun minus(other: FloatMatrix4x4): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0] - other[0, 0], this[0, 1] - other[0, 1], this[0, 2] - other[0, 2], this[0, 3] - other[0, 3],
		this[1, 0] - other[1, 0], this[1, 1] - other[1, 1], this[1, 2] - other[1, 2], this[1, 3] - other[1, 3],
		this[2, 0] - other[2, 0], this[2, 1] - other[2, 1], this[2, 2] - other[2, 2], this[2, 3] - other[2, 3],
		this[3, 0] - other[3, 0], this[3, 1] - other[3, 1], this[3, 2] - other[3, 2], this[3, 3] - other[3, 3]
	)

	operator fun times(other: FloatMatrix4x4): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0] * other[0, 0] + this[1, 0] * other[0, 1] + this[2, 0] * other[0, 2] + this[3, 0] * other[0, 3],
		this[0, 1] * other[0, 0] + this[1, 1] * other[0, 1] + this[2, 1] * other[0, 2] + this[3, 1] * other[0, 3],
		this[0, 2] * other[0, 0] + this[1, 2] * other[0, 1] + this[2, 2] * other[0, 2] + this[3, 2] * other[0, 3],
		this[0, 3] * other[0, 0] + this[1, 3] * other[0, 1] + this[2, 3] * other[0, 2] + this[3, 3] * other[0, 3],

		this[0, 0] * other[1, 0] + this[1, 0] * other[1, 1] + this[2, 0] * other[1, 2] + this[3, 0] * other[1, 3],
		this[0, 1] * other[1, 0] + this[1, 1] * other[1, 1] + this[2, 1] * other[1, 2] + this[3, 1] * other[1, 3],
		this[0, 2] * other[1, 0] + this[1, 2] * other[1, 1] + this[2, 2] * other[1, 2] + this[3, 2] * other[1, 3],
		this[0, 3] * other[1, 0] + this[1, 3] * other[1, 1] + this[2, 3] * other[1, 2] + this[3, 3] * other[1, 3],

		this[0, 0] * other[2, 0] + this[1, 0] * other[2, 1] + this[2, 0] * other[2, 2] + this[3, 0] * other[2, 3],
		this[0, 1] * other[2, 0] + this[1, 1] * other[2, 1] + this[2, 1] * other[2, 2] + this[3, 1] * other[2, 3],
		this[0, 2] * other[2, 0] + this[1, 2] * other[2, 1] + this[2, 2] * other[2, 2] + this[3, 2] * other[2, 3],
		this[0, 3] * other[2, 0] + this[1, 3] * other[2, 1] + this[2, 3] * other[2, 2] + this[3, 3] * other[2, 3],

		this[0, 0] * other[3, 0] + this[1, 0] * other[3, 1] + this[2, 0] * other[3, 2] + this[3, 0] * other[3, 3],
		this[0, 1] * other[3, 0] + this[1, 1] * other[3, 1] + this[2, 1] * other[3, 2] + this[3, 1] * other[3, 3],
		this[0, 2] * other[3, 0] + this[1, 2] * other[3, 1] + this[2, 2] * other[3, 2] + this[3, 2] * other[3, 3],
		this[0, 3] * other[3, 0] + this[1, 3] * other[3, 1] + this[2, 3] * other[3, 2] + this[3, 3] * other[3, 3]
	)

	operator fun div(other: FloatMatrix4x4): FloatMatrix4x4 = this * other.inverse

	fun transposed(): FloatMatrix4x4 = FloatMatrix4x4(
		this[0, 0], this[1, 0], this[2, 0], this[3, 0],
		this[0, 1], this[1, 1], this[2, 1], this[3, 1],
		this[0, 2], this[1, 2], this[2, 2], this[3, 2],
		this[0, 3], this[1, 3], this[2, 3], this[3, 3]
	)

	fun translated(vector: FloatVector3): FloatMatrix4x4 = toMutableFloatMatrix4x4().also {
		it.setRow(3, getRow(0) * vector[0] + getRow(1) * vector[1] + getRow(2) * vector[2] + getRow(3))
	}

	fun rotated(angle: Float, vector: FloatVector3): FloatMatrix4x4 {
		val cosine = cos(angle)
		val sine = sin(angle)

		val axis = vector.normalized()
		val temp = axis * (1 - cosine)

		val rotate = IDENTITY.toMutableFloatMatrix4x4().also {
			it[0, 0] = cosine + temp[0] * axis[0]
			it[0, 1] = temp[0] * axis[1] + sine * axis[2]
			it[0, 2] = temp[0] * axis[2] - sine * axis[1]

			it[1, 0] = temp[1] * axis[0] - sine * axis[2]
			it[1, 1] = cosine + temp[1] * axis[1]
			it[1, 2] = temp[1] * axis[2] + sine * axis[0]

			it[2, 0] = temp[2] * axis[0] + sine * axis[1]
			it[2, 1] = temp[2] * axis[1] - sine * axis[0]
			it[2, 2] = cosine + temp[2] * axis[2]
		}

		return FloatMatrix4x4(
			getRow(0) * rotate[0, 0] + getRow(1) * rotate[0, 1] + getRow(2) * rotate[0, 2],
			getRow(0) * rotate[1, 0] + getRow(1) * rotate[1, 1] + getRow(2) * rotate[1, 2],
			getRow(0) * rotate[2, 0] + getRow(1) * rotate[2, 1] + getRow(2) * rotate[2, 2],
			getRow(3)
		)
	}

	fun scaled(vector: FloatVector3): FloatMatrix4x4 {
		return FloatMatrix4x4(
			getRow(0) * vector[0],
			getRow(1) * vector[1],
			getRow(2) * vector[2],
			getRow(3)
		)
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is FloatMatrix4x4) return false

		if (getRow(0) != other.getRow(0)) return false
		if (getRow(1) != other.getRow(1)) return false
		if (getRow(2) != other.getRow(2)) return false
		if (getRow(3) != other.getRow(3)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = 0

		result = 31 * result + getRow(0).hashCode()
		result = 31 * result + getRow(1).hashCode()
		result = 31 * result + getRow(2).hashCode()
		result = 31 * result + getRow(3).hashCode()

		return result
	}

	override fun toString(): String = "${getRow(0)}\n${getRow(1)}\n${getRow(2)}\n${getRow(3)}"
}

@Suppress("FunctionName")
fun FloatMatrix4x4(
	row0: FloatVector4,
	row1: FloatVector4,
	row2: FloatVector4,
	row3: FloatVector4
): FloatMatrix4x4 = MutableFloatMatrix4x4(row0, row1, row2, row3)

@Suppress("FunctionName")
fun FloatMatrix4x4(
	c00: Float, c01: Float, c02: Float, c03: Float,
	c10: Float, c11: Float, c12: Float, c13: Float,
	c20: Float, c21: Float, c22: Float, c23: Float,
	c30: Float, c31: Float, c32: Float, c33: Float
): FloatMatrix4x4 = MutableFloatMatrix4x4(
	c00, c01, c02, c03,
	c10, c11, c12, c13,
	c20, c21, c22, c23,
	c30, c31, c32, c33
)


class MutableFloatMatrix4x4 : FloatMatrix4x4 {
	private val m: FloatArray

	constructor(
		c00: Float, c01: Float, c02: Float, c03: Float,
		c10: Float, c11: Float, c12: Float, c13: Float,
		c20: Float, c21: Float, c22: Float, c23: Float,
		c30: Float, c31: Float, c32: Float, c33: Float
	) {
		m = floatArrayOf(c00, c01, c02, c03, c10, c11, c12, c13, c20, c21, c22, c23, c30, c31, c32, c33)
	}

	constructor(row0: FloatVector4, row1: FloatVector4, row2: FloatVector4, row3: FloatVector4) {
		m = floatArrayOf(
			row0[0], row0[1], row0[2], row0[3],
			row1[0], row1[1], row1[2], row1[3],
			row2[0], row2[1], row2[2], row2[3],
			row3[0], row3[1], row3[2], row3[3]
		)
	}

	/** changes to the array are reflected in the matrix */
	fun asFloatArray(): FloatArray = m

	override operator fun get(row: Int, column: Int): Float {
		if (row !in 0..3 || column !in 0..3) {
			throw IndexOutOfBoundsException("row=$row, column=$column")
		}
		return m[row * 4 + column]
	}

	operator fun set(row: Int, column: Int, value: Float) {
		if (row !in 0..3 || column !in 0..3) {
			throw IndexOutOfBoundsException("row=$row, column=$column")
		}
		m[row * 4 + column] = value
	}

	fun setRow(row: Int, value: FloatVector4) {
		if (row !in 0..3) throw IndexOutOfBoundsException()
		this[row, 0] = value[0]
		this[row, 1] = value[1]
		this[row, 2] = value[2]
		this[row, 3] = value[3]
	}

	fun setColumn(column: Int, value: FloatVector4) {
		if (column !in 0..3) throw IndexOutOfBoundsException()
		this[0, column] = value[0]
		this[1, column] = value[1]
		this[2, column] = value[2]
		this[3, column] = value[3]
	}

	operator fun plusAssign(scalar: Float) {
		this[0, 0] += scalar; this[0, 1] += scalar; this[0, 2] += scalar; this[0, 3] += scalar
		this[1, 0] += scalar; this[1, 1] += scalar; this[1, 2] += scalar; this[1, 3] += scalar
		this[2, 0] += scalar; this[2, 1] += scalar; this[2, 2] += scalar; this[2, 3] += scalar
		this[3, 0] += scalar; this[3, 1] += scalar; this[3, 2] += scalar; this[3, 3] += scalar
	}

	operator fun minusAssign(scalar: Float) {
		this[0, 0] -= scalar; this[0, 1] -= scalar; this[0, 2] -= scalar; this[0, 3] -= scalar
		this[1, 0] -= scalar; this[1, 1] -= scalar; this[1, 2] -= scalar; this[1, 3] -= scalar
		this[2, 0] -= scalar; this[2, 1] -= scalar; this[2, 2] -= scalar; this[2, 3] -= scalar
		this[3, 0] -= scalar; this[3, 1] -= scalar; this[3, 2] -= scalar; this[3, 3] -= scalar
	}

	operator fun timesAssign(scalar: Float) {
		this[0, 0] *= scalar; this[0, 1] *= scalar; this[0, 2] *= scalar; this[0, 3] *= scalar
		this[1, 0] *= scalar; this[1, 1] *= scalar; this[1, 2] *= scalar; this[1, 3] *= scalar
		this[2, 0] *= scalar; this[2, 1] *= scalar; this[2, 2] *= scalar; this[2, 3] *= scalar
		this[3, 0] *= scalar; this[3, 1] *= scalar; this[3, 2] *= scalar; this[3, 3] *= scalar
	}

	operator fun divAssign(scalar: Float) {
		this[0, 0] /= scalar; this[0, 1] /= scalar; this[0, 2] /= scalar; this[0, 3] /= scalar
		this[1, 0] /= scalar; this[1, 1] /= scalar; this[1, 2] /= scalar; this[1, 3] /= scalar
		this[2, 0] /= scalar; this[2, 1] /= scalar; this[2, 2] /= scalar; this[2, 3] /= scalar
		this[3, 0] /= scalar; this[3, 1] /= scalar; this[3, 2] /= scalar; this[3, 3] /= scalar
	}

	operator fun plusAssign(other: FloatMatrix4x4) {
		this[0, 0] += other[0, 0]; this[0, 1] += other[0, 1]; this[0, 2] += other[0, 2]; this[0, 3] += other[0, 3]
		this[1, 0] += other[1, 0]; this[1, 1] += other[1, 1]; this[1, 2] += other[1, 2]; this[1, 3] += other[1, 3]
		this[2, 0] += other[2, 0]; this[2, 1] += other[2, 1]; this[2, 2] += other[2, 2]; this[2, 3] += other[2, 3]
		this[3, 0] += other[3, 0]; this[3, 1] += other[3, 1]; this[3, 2] += other[3, 2]; this[3, 3] += other[3, 3]
	}

	operator fun minusAssign(other: FloatMatrix4x4) {
		this[0, 0] -= other[0, 0]; this[0, 1] -= other[0, 1]; this[0, 2] -= other[0, 2]; this[0, 3] -= other[0, 3]
		this[1, 0] -= other[1, 0]; this[1, 1] -= other[1, 1]; this[1, 2] -= other[1, 2]; this[1, 3] -= other[1, 3]
		this[2, 0] -= other[2, 0]; this[2, 1] -= other[2, 1]; this[2, 2] -= other[2, 2]; this[2, 3] -= other[2, 3]
		this[3, 0] -= other[3, 0]; this[3, 1] -= other[3, 1]; this[3, 2] -= other[3, 2]; this[3, 3] -= other[3, 3]
	}

	operator fun timesAssign(other: FloatMatrix4x4) {
		val temp00 = this[0, 0] * other[0, 0] + this[1, 0] * other[0, 1] + this[2, 0] * other[0, 2] + this[3, 0] * other[0, 3]
		val temp01 = this[0, 1] * other[0, 0] + this[1, 1] * other[0, 1] + this[2, 1] * other[0, 2] + this[3, 1] * other[0, 3]
		val temp02 = this[0, 2] * other[0, 0] + this[1, 2] * other[0, 1] + this[2, 2] * other[0, 2] + this[3, 2] * other[0, 3]
		val temp03 = this[0, 3] * other[0, 0] + this[1, 3] * other[0, 1] + this[2, 3] * other[0, 2] + this[3, 3] * other[0, 3]

		val temp10 = this[0, 0] * other[1, 0] + this[1, 0] * other[1, 1] + this[2, 0] * other[1, 2] + this[3, 0] * other[1, 3]
		val temp11 = this[0, 1] * other[1, 0] + this[1, 1] * other[1, 1] + this[2, 1] * other[1, 2] + this[3, 1] * other[1, 3]
		val temp12 = this[0, 2] * other[1, 0] + this[1, 2] * other[1, 1] + this[2, 2] * other[1, 2] + this[3, 2] * other[1, 3]
		val temp13 = this[0, 3] * other[1, 0] + this[1, 3] * other[1, 1] + this[2, 3] * other[1, 2] + this[3, 3] * other[1, 3]

		val temp20 = this[0, 0] * other[2, 0] + this[1, 0] * other[2, 1] + this[2, 0] * other[2, 2] + this[3, 0] * other[2, 3]
		val temp21 = this[0, 1] * other[2, 0] + this[1, 1] * other[2, 1] + this[2, 1] * other[2, 2] + this[3, 1] * other[2, 3]
		val temp22 = this[0, 2] * other[2, 0] + this[1, 2] * other[2, 1] + this[2, 2] * other[2, 2] + this[3, 2] * other[2, 3]
		val temp23 = this[0, 3] * other[2, 0] + this[1, 3] * other[2, 1] + this[2, 3] * other[2, 2] + this[3, 3] * other[2, 3]

		val temp30 = this[0, 0] * other[3, 0] + this[1, 0] * other[3, 1] + this[2, 0] * other[3, 2] + this[3, 0] * other[3, 3]
		val temp31 = this[0, 1] * other[3, 0] + this[1, 1] * other[3, 1] + this[2, 1] * other[3, 2] + this[3, 1] * other[3, 3]
		val temp32 = this[0, 2] * other[3, 0] + this[1, 2] * other[3, 1] + this[2, 2] * other[3, 2] + this[3, 2] * other[3, 3]
		val temp33 = this[0, 3] * other[3, 0] + this[1, 3] * other[3, 1] + this[2, 3] * other[3, 2] + this[3, 3] * other[3, 3]

		this[0, 0] = temp00
		this[0, 1] = temp01
		this[0, 2] = temp02
		this[0, 3] = temp03

		this[1, 0] = temp10
		this[1, 1] = temp11
		this[1, 2] = temp12
		this[1, 3] = temp13

		this[2, 0] = temp20
		this[2, 1] = temp21
		this[2, 2] = temp22
		this[2, 3] = temp23

		this[3, 0] = temp30
		this[3, 1] = temp31
		this[3, 2] = temp32
		this[3, 3] = temp33
	}

	operator fun divAssign(other: FloatMatrix4x4) {
		this *= other.inverse
	}

	fun transpose() {
		var temp = m[1]
		m[1] = m[4]
		m[4] = temp

		temp = m[2]
		m[2] = m[8]
		m[8] = temp

		temp = m[3]
		m[3] = m[12]
		m[12] = temp

		temp = m[6]
		m[6] = m[9]
		m[9] = temp

		temp = m[7]
		m[7] = m[13]
		m[13] = temp

		temp = m[11]
		m[11] = m[14]
		m[14] = temp
	}

	fun translate(vector: FloatVector3) {
		setRow(3, getRow(0) * vector[0] + getRow(1) * vector[1] + getRow(2) * vector[2] + getRow(3))
	}

	fun rotate(angle: Float, vector: FloatVector3) {
		val cosine = cos(angle)
		val sine = sin(angle)

		val axis = vector.normalized()
		val temp = axis * (1 - cosine)

		val rotate = IDENTITY.toMutableFloatMatrix4x4().also {
			it[0, 0] = cosine + temp[0] * axis[0]
			it[0, 1] = temp[0] * axis[1] + sine * axis[2]
			it[0, 2] = temp[0] * axis[2] - sine * axis[1]

			it[1, 0] = temp[1] * axis[0] - sine * axis[2]
			it[1, 1] = cosine + temp[1] * axis[1]
			it[1, 2] = temp[1] * axis[2] + sine * axis[0]

			it[2, 0] = temp[2] * axis[0] + sine * axis[1]
			it[2, 1] = temp[2] * axis[1] - sine * axis[0]
			it[2, 2] = cosine + temp[2] * axis[2]
		}

		val row0 = getRow(0) * rotate[0, 0] + getRow(1) * rotate[0, 1] + getRow(2) * rotate[0, 2]
		val row1 = getRow(0) * rotate[1, 0] + getRow(1) * rotate[1, 1] + getRow(2) * rotate[1, 2]
		val row2 = getRow(0) * rotate[2, 0] + getRow(1) * rotate[2, 1] + getRow(2) * rotate[2, 2]

		setRow(0, row0)
		setRow(1, row1)
		setRow(2, row2)
	}

	fun scale(vector: FloatVector3) {
		setRow(0, getRow(0) * vector[0])
		setRow(1, getRow(1) * vector[1])
		setRow(2, getRow(2) * vector[2])
	}
}

fun FloatMatrix4x4.toFloatArray(): FloatArray = floatArrayOf(
	this[0, 0], this[0, 1], this[0, 2], this[0, 3],
	this[1, 0], this[1, 1], this[1, 2], this[1, 3],
	this[2, 0], this[2, 1], this[2, 2], this[2, 3],
	this[3, 0], this[3, 1], this[3, 2], this[3, 3]
)

fun FloatMatrix4x4.toFloatMatrix4x4(): FloatMatrix4x4 = toMutableFloatMatrix4x4()

fun FloatMatrix4x4.toMutableFloatMatrix4x4(): MutableFloatMatrix4x4 = MutableFloatMatrix4x4(
	this[0, 0], this[0, 1], this[0, 2], this[0, 3],
	this[1, 0], this[1, 1], this[1, 2], this[1, 3],
	this[2, 0], this[2, 1], this[2, 2], this[2, 3],
	this[3, 0], this[3, 1], this[3, 2], this[3, 3]
)


operator fun Float.plus(matrix: FloatMatrix4x4): FloatMatrix4x4 = FloatMatrix4x4(
	this + matrix[0, 0], this + matrix[0, 1], this + matrix[0, 2], this + matrix[0, 3],
	this + matrix[1, 0], this + matrix[1, 1], this + matrix[1, 2], this + matrix[1, 3],
	this + matrix[2, 0], this + matrix[2, 1], this + matrix[2, 2], this + matrix[2, 3],
	this + matrix[3, 0], this + matrix[3, 1], this + matrix[3, 2], this + matrix[3, 3]
)

operator fun Float.minus(matrix: FloatMatrix4x4): FloatMatrix4x4 = FloatMatrix4x4(
	this - matrix[0, 0], this - matrix[0, 1], this - matrix[0, 2], this - matrix[0, 3],
	this - matrix[1, 0], this - matrix[1, 1], this - matrix[1, 2], this - matrix[1, 3],
	this - matrix[2, 0], this - matrix[2, 1], this - matrix[2, 2], this - matrix[2, 3],
	this - matrix[3, 0], this - matrix[3, 1], this - matrix[3, 2], this - matrix[3, 3]
)

operator fun Float.times(matrix: FloatMatrix4x4): FloatMatrix4x4 = FloatMatrix4x4(
	this * matrix[0, 0], this * matrix[0, 1], this * matrix[0, 2], this * matrix[0, 3],
	this * matrix[1, 0], this * matrix[1, 1], this * matrix[1, 2], this * matrix[1, 3],
	this * matrix[2, 0], this * matrix[2, 1], this * matrix[2, 2], this * matrix[2, 3],
	this * matrix[3, 0], this * matrix[3, 1], this * matrix[3, 2], this * matrix[3, 3]
)

operator fun Float.div(matrix: FloatMatrix4x4): FloatMatrix4x4 = FloatMatrix4x4(
	this / matrix[0, 0], this / matrix[0, 1], this / matrix[0, 2], this / matrix[0, 3],
	this / matrix[1, 0], this / matrix[1, 1], this / matrix[1, 2], this / matrix[1, 3],
	this / matrix[2, 0], this / matrix[2, 1], this / matrix[2, 2], this / matrix[2, 3],
	this / matrix[3, 0], this / matrix[3, 1], this / matrix[3, 2], this / matrix[3, 3]
)
