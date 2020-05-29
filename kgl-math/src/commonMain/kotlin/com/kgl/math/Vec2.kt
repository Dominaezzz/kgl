package com.kgl.math

import kotlin.math.*

sealed class Vec2 {
	abstract val x: Float
	abstract val y: Float

	companion object {
		val ZERO = Vec2(0f)
		val ONE = Vec2(1f)
		val RIGHT = Vec2(1f, 0f)
		val LEFT = Vec2(-1f, 0f)
		val UP = Vec2(0f, 1f)
		val DOWN = Vec2(0f, -1f)
		val POSITIVE_INFINITY = Vec2(Float.POSITIVE_INFINITY)
		val NEGATIVE_INFINITY = Vec2(Float.NEGATIVE_INFINITY)
	}

	fun copy(x: Float = this.x, y: Float = this.y): Vec2 = Vec2(x, y)

	operator fun get(index: Int): Float = when (index) {
		0 -> x
		1 -> y
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): Vec2 = this
	operator fun unaryMinus(): Vec2 = Vec2(-x, -y)

	operator fun plus(scalar: Float): Vec2 = Vec2(x + scalar, y + scalar)
	operator fun minus(scalar: Float): Vec2 = Vec2(x - scalar, y - scalar)
	operator fun times(scalar: Float): Vec2 = Vec2(x * scalar, y * scalar)
	operator fun div(scalar: Float): Vec2 = Vec2(x / scalar, y / scalar)
	operator fun rem(scalar: Float): Vec2 = Vec2(x % scalar, y % scalar)

	operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
	operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)
	operator fun times(other: Vec2): Vec2 = Vec2(x * other.x, y * other.y)
	operator fun div(other: Vec2): Vec2 = Vec2(x / other.x, y / other.y)
	operator fun rem(other: Vec2): Vec2 = Vec2(x % other.x, y % other.y)

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	val perpendicular: Vec2 get() = Vec2(-y, x)


	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: Vec2): Float = x * other.x + y * other.y

	/**
	 * Returns a new vector equivalent to moving this vector a maximum distance of [maxDistance] towards [target].
	 *
	 * If `distance(this, target)` is less than [maxDistance], the returned vector is equal to [target].
	 */
	fun movedTowards(target: Vec2, maxDistance: Float): Vec2 {
		return MutableVec2(x, y).apply { moveTowards(target, maxDistance) }
	}

	/** Returns a new vector made with the normalized components of this vector. */
	fun normalized(): Vec2 {
		return MutableVec2(x, y).apply { normalize() }
	}

	/** Returns a new vector equivalent to reflecting this vector off [normal]. */
	fun reflected(normal: Vec2): Vec2 {
		return MutableVec2(x, y).apply { reflect(normal) }
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Vec2) return false

		if (x != other.x) return false
		if (y != other.y) return false

		return true
	}

	override fun hashCode(): Int = y.hashCode() + 31 * x.hashCode()

	override fun toString(): String = "($x, $y)"
}

@Suppress("FunctionName")
fun Vec2(x: Float, y: Float): Vec2 = MutableVec2(x, y)

@Suppress("FunctionName")
fun Vec2(scalar: Float = 0f): Vec2 = MutableVec2(scalar)


class MutableVec2(x: Float, y: Float) : Vec2() {
	private val m = floatArrayOf(x, y)

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

	constructor(scalar: Float = 0f) : this(scalar, scalar)

	/** changes to the array are reflected in the vector */
	fun asFloatArray(): FloatArray = m

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value
		1 -> y = value
		else -> throw IndexOutOfBoundsException()
	}

	operator fun plusAssign(scalar: Float) = set(x + scalar, y + scalar)
	operator fun minusAssign(scalar: Float) = set(x - scalar, y - scalar)
	operator fun timesAssign(scalar: Float) = set(x * scalar, y * scalar)
	operator fun divAssign(scalar: Float) = set(x / scalar, y / scalar)
	operator fun remAssign(scalar: Float) = set(x % scalar, y % scalar)

	operator fun plusAssign(other: Vec2) = set(x + other.x, y + other.y)
	operator fun minusAssign(other: Vec2) = set(x - other.x, y - other.y)
	operator fun timesAssign(other: Vec2) = set(x * other.x, y * other.y)
	operator fun divAssign(other: Vec2) = set(x / other.x, y / other.y)
	operator fun remAssign(other: Vec2) = set(x % other.x, y % other.y)

	fun set(scalar: Float) {
		x = scalar
		y = scalar
	}

	fun set(x: Float, y: Float) {
		this.x = x
		this.y = y
	}

	fun moveTowards(target: Vec2, maxDistance: Float) {
		val xDiff = target.x - x
		val yDiff = target.y - y

		val squareDistance = xDiff * xDiff + yDiff * yDiff
		if (squareDistance == 0f || (maxDistance >= 0 && squareDistance <= maxDistance * maxDistance)) {
			this.set(target.x, target.y)
		} else {
			val distance = sqrt(squareDistance)
			this.set(
				x + xDiff / distance * maxDistance,
				y + yDiff / distance * maxDistance
			)
		}
	}

	fun normalize() {
		val magnitude = magnitude
		if (magnitude > 0) set(x / magnitude, y / magnitude) else set(0f)
	}

	fun reflect(normal: Vec2) {
		val factor = -2f * (this dot normal)
		set(
			factor * normal.x + x,
			factor * normal.y + y
		)
	}
}

fun Vec2.toFloatArray(): FloatArray = floatArrayOf(x, y)

fun Vec2.toVec2(): Vec2 = Vec2(x, y)

fun Vec2.toMutableVec2(): MutableVec2 = MutableVec2(x, y)

/** Returns the unsigned angle in radians between [from] and [to]. */
fun angle(from: Vec2, to: Vec2): Float {
	val divisor = sqrt(from.squareMagnitude * to.squareMagnitude)
	if (divisor == 0f) return 0f

	val dot = ((from dot to) / divisor).coerceIn(-1f, 1f)
	return acos(dot)
}

/** Returns the signed angle in radians between [from] and [to]. */
fun signedAngle(from: Vec2, to: Vec2): Float {
	val angle = angle(from, to)
	val sign = sign(from.x * to.y - from.y * to.x)
	return angle * sign
}

/** Returns the distance between [from] and [to]. */
fun distance(from: Vec2, to: Vec2): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	return sqrt(xDiff * xDiff + yDiff * yDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: Vec2, to: Vec2, ratio: Float): Vec2 {
	return Vec2(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: Vec2, b: Vec2): Vec2 {
	return Vec2(
		max(a.x, b.x),
		max(a.y, b.y)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: Vec2, b: Vec2): Vec2 {
	return Vec2(
		min(a.x, b.x),
		min(a.y, b.y)
	)
}


operator fun Float.plus(vector: Vec2): Vec2 = Vec2(this + vector.x, this + vector.y)
operator fun Float.minus(vector: Vec2): Vec2 = Vec2(this - vector.x, this - vector.y)
operator fun Float.times(vector: Vec2): Vec2 = Vec2(this * vector.x, this * vector.y)
operator fun Float.div(vector: Vec2): Vec2 = Vec2(this / vector.x, this / vector.y)
operator fun Float.rem(vector: Vec2): Vec2 = Vec2(this % vector.x, this % vector.y)
