package com.kgl.math

import kotlin.math.*

sealed class Vec3 {
	abstract val x: Float
	abstract val y: Float
	abstract val z: Float

	companion object {
		val ZERO = Vec3(0f)
		val ONE = Vec3(1f)
		val RIGHT = Vec3(1f, 0f, 0f)
		val LEFT = Vec3(-1f, 0f, 0f)
		val UP = Vec3(0f, 1f, 0f)
		val DOWN = Vec3(0f, -1f, 0f)
		val FORWARD = Vec3(0f, 0f, 1f)
		val BACK = Vec3(0f, 0f, -1f)
		val POSITIVE_INFINITY = Vec3(Float.POSITIVE_INFINITY)
		val NEGATIVE_INFINITY = Vec3(Float.NEGATIVE_INFINITY)
	}

	fun copy(x: Float = this.x, y: Float = this.y, z: Float = this.z): Vec3 = Vec3(x, y, z)

	operator fun get(index: Int): Float = when (index) {
		0 -> x
		1 -> y
		2 -> z
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): Vec3 = this
	operator fun unaryMinus(): Vec3 = Vec3(-x, -y, -z)

	operator fun plus(scalar: Float): Vec3 = Vec3(x + scalar, y + scalar, z + scalar)
	operator fun minus(scalar: Float): Vec3 = Vec3(x - scalar, y - scalar, z - scalar)
	operator fun times(scalar: Float): Vec3 = Vec3(x * scalar, y * scalar, z * scalar)
	operator fun div(scalar: Float): Vec3 = Vec3(x / scalar, y / scalar, z / scalar)
	operator fun rem(scalar: Float): Vec3 = Vec3(x % scalar, y % scalar, z % scalar)

	operator fun plus(other: Vec3): Vec3 = Vec3(x + other.x, y + other.y, z + other.z)
	operator fun minus(other: Vec3): Vec3 = Vec3(x - other.x, y - other.y, z - other.z)
	operator fun times(other: Vec3): Vec3 = Vec3(x * other.x, y * other.y, z * other.z)
	operator fun div(other: Vec3): Vec3 = Vec3(x / other.x, y / other.y, z / other.z)
	operator fun rem(other: Vec3): Vec3 = Vec3(x % other.x, y % other.y, z % other.z)

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	/** Returns the cross product of this and [other]. */
	infix fun cross(other: Vec3): Vec3 = Vec3(
		y * other.z - z * other.y,
		z * other.x - x * other.z,
		x * other.y - y * other.x
	)

	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: Vec3): Float = x * other.x + y * other.y + z * other.z

	/**
	 * Returns a new vector equivalent to moving this vector a maximum distance of [maxDistance] towards [target].
	 *
	 * If `distance(this, target)` is less than [maxDistance], the returned vector is equal to [target]. Negative values
	 * of [maxDistance] result in a vector moved away from [target].
	 */
	fun movedTowards(target: Vec3, maxDistance: Float): Vec3 {
		return MutableVec3(x, y, z).apply { moveTowards(target, maxDistance) }
	}

	/** Returns a new vector equivalent to normalizing this vector. */
	fun normalized(): Vec3 {
		return MutableVec3(x, y, z).apply { normalize() }
	}

	/** Returns a new vector equivalent to projecting this vector onto [other]. */
	fun projected(other: Vec3): Vec3 {
		return MutableVec3(x, y, z).apply { project(other) }
	}

	/** Returns a new vector equivalent to reflecting this vector off [normal]. */
	fun reflected(normal: Vec3): Vec3 {
		return MutableVec3(x, y, z).apply { reflect(normal) }
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Vec3) return false

		if (x != other.x) return false
		if (y != other.y) return false
		if (z != other.z) return false

		return true
	}

	override fun hashCode(): Int = z.hashCode() + 31 * (y.hashCode() + 31 * x.hashCode())

	override fun toString(): String = "($x, $y, $z)"
}

@Suppress("FunctionName")
fun Vec3(x: Float, y: Float, z: Float): Vec3 = MutableVec3(x, y, z)

@Suppress("FunctionName")
fun Vec3(scalar: Float = 0f): Vec3 = MutableVec3(scalar)


class MutableVec3(x: Float, y: Float, z: Float) : Vec3() {
	private val m = floatArrayOf(x, y, z)

	override var x: Float
		get() = m[0]
		set(value) {
			m[0] = value
		}
	override var y: Float
		get() = m[1]
		set(value) {
			m[1] = value
		}
	override var z: Float
		get() = m[2]
		set(value) {
			m[2] = value
		}

	constructor(scalar: Float = 0f) : this(scalar, scalar, scalar)

	/** changes to the array are reflected in the vector */
	fun asFloatArray(): FloatArray = m

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value
		1 -> y = value
		2 -> z = value
		else -> throw IndexOutOfBoundsException()
	}

	operator fun plusAssign(scalar: Float) = set(x + scalar, y + scalar, z + scalar)
	operator fun minusAssign(scalar: Float) = set(x - scalar, y - scalar, z - scalar)
	operator fun timesAssign(scalar: Float) = set(x * scalar, y * scalar, z * scalar)
	operator fun divAssign(scalar: Float) = set(x / scalar, y / scalar, z / scalar)
	operator fun remAssign(scalar: Float) = set(x % scalar, y % scalar, z % scalar)

	operator fun plusAssign(other: Vec3) = set(x + other.x, y + other.y, z + other.z)
	operator fun minusAssign(other: Vec3) = set(x - other.x, y - other.y, z - other.z)
	operator fun timesAssign(other: Vec3) = set(x * other.x, y * other.y, z * other.z)
	operator fun divAssign(other: Vec3) = set(x / other.x, y / other.y, z / other.z)
	operator fun remAssign(other: Vec3) = set(x % other.x, y % other.y, z % other.z)

	fun set(scalar: Float) {
		x = scalar
		y = scalar
		z = scalar
	}

	fun set(x: Float, y: Float, z: Float) {
		this.x = x
		this.y = y
		this.z = z
	}

	/**
	 * Move this vector towards the target vector a maximum distance of [maxDistance].
	 *
	 * If `distance(this, target)` is less than [maxDistance], the sets this vector equal to [target]. Negative values
	 * of [maxDistance] moves this vector away from [target].
	 */
	fun moveTowards(target: Vec3, maxDistance: Float) {
		val xDiff = target.x - x
		val yDiff = target.y - y
		val zDiff = target.z - z

		val squareDistance = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff
		if (squareDistance == 0f || (maxDistance >= 0 && squareDistance <= maxDistance * maxDistance)) {
			this.set(target.x, target.y, target.z)
		} else {
			val distance = sqrt(squareDistance)
			this.set(
				x + xDiff / distance * maxDistance,
				y + yDiff / distance * maxDistance,
				z + zDiff / distance * maxDistance
			)
		}
	}

	/** Normalizes this vector. */
	fun normalize() {
		val magnitude = magnitude
		if (magnitude > 0) divAssign(magnitude) else set(0f)
	}

	/** Projects this vector onto [other]. */
	fun project(other: Vec3) {
		val squareMagnitude = other.squareMagnitude
		if (squareMagnitude == 0f) {
			set(0f)
		} else {
			val dot = this dot other
			set(
				other.x * dot / squareMagnitude,
				other.y * dot / squareMagnitude,
				other.z * dot / squareMagnitude
			)
		}
	}

	/** Reflects this vector off normal. */
	fun reflect(normal: Vec3) {
		val factor = -2f * (this dot normal)
		set(
			factor * normal.x + x,
			factor * normal.y + y,
			factor * normal.z + z
		)
	}
}

fun Vec3.toFloatArray(): FloatArray = floatArrayOf(x, y, z)

fun Vec3.toVec3(): Vec3 = toMutableVec3()

fun Vec3.toMutableVec3(): MutableVec3 = MutableVec3(x, y, z)

/** Returns the unsigned angle in radians between [from] and [to]. */
fun angle(from: Vec3, to: Vec3): Float {
	val divisor = sqrt(from.squareMagnitude * to.squareMagnitude)
	if (divisor == 0f) return 0f

	val dot = ((from dot to) / divisor).coerceIn(-1f, 1f)
	return acos(dot)
}

/** Returns the signed angle in radians between [from] and [to]. */
fun signedAngle(from: Vec3, to: Vec3, axis: Vec3): Float {
	val angle = angle(from, to)
	val xCross = from.y * to.z - from.z * to.y
	val yCross = from.z * to.x - from.x * to.z
	val zCross = from.x * to.y - from.y * to.x
	val sign = sign(axis.x * xCross + axis.y * yCross + axis.z * zCross)
	return angle * sign
}

/** Returns the distance between [from] and [to]. */
fun distance(from: Vec3, to: Vec3): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	val zDiff = from.z - to.z
	return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: Vec3, to: Vec3, ratio: Float): Vec3 {
	return Vec3(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio,
		from.z * (1 - ratio) + to.z * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: Vec3, b: Vec3): Vec3 {
	return Vec3(
		max(a.x, b.x),
		max(a.y, b.y),
		max(a.z, b.z)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: Vec3, b: Vec3): Vec3 {
	return Vec3(
		min(a.x, b.x),
		min(a.y, b.y),
		min(a.z, b.z)
	)
}


operator fun Float.plus(vector: Vec3): Vec3 = Vec3(this + vector.x, this + vector.y, this + vector.z)
operator fun Float.minus(vector: Vec3): Vec3 = Vec3(this - vector.x, this - vector.y, this - vector.z)
operator fun Float.times(vector: Vec3): Vec3 = Vec3(this * vector.x, this * vector.y, this * vector.z)
operator fun Float.div(vector: Vec3): Vec3 = Vec3(this / vector.x, this / vector.y, this / vector.z)
operator fun Float.rem(vector: Vec3): Vec3 = Vec3(this % vector.x, this % vector.y, this % vector.z)
