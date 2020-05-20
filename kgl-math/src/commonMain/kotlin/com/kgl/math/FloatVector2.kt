package com.kgl.math

import kotlin.math.*

sealed class FloatVector2 {
	abstract val x: Float
	abstract val y: Float

	companion object {
		val ZERO = FloatVector2(0f)
		val ONE = FloatVector2(1f)
		val RIGHT = FloatVector2(1f, 0f)
		val LEFT = FloatVector2(-1f, 0f)
		val UP = FloatVector2(0f, 1f)
		val DOWN = FloatVector2(0f, -1f)
		val POSITIVE_INFINITY = FloatVector2(Float.POSITIVE_INFINITY)
		val NEGATIVE_INFINITY = FloatVector2(Float.NEGATIVE_INFINITY)
	}

	fun copy(x: Float = this.x, y: Float = this.y): FloatVector2 = FloatVector2(x, y)

	operator fun get(index: Int): Float = when (index) {
		0 -> x
		1 -> y
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): FloatVector2 = this
	operator fun unaryMinus(): FloatVector2 = FloatVector2(-x, -y)

	operator fun plus(scalar: Float): FloatVector2 = FloatVector2(x + scalar, y + scalar)
	operator fun minus(scalar: Float): FloatVector2 = FloatVector2(x - scalar, y - scalar)
	operator fun times(scalar: Float): FloatVector2 = FloatVector2(x * scalar, y * scalar)
	operator fun div(scalar: Float): FloatVector2 = FloatVector2(x / scalar, y / scalar)
	operator fun rem(scalar: Float): FloatVector2 = FloatVector2(x % scalar, y % scalar)

	operator fun plus(other: FloatVector2): FloatVector2 = FloatVector2(x + other.x, y + other.y)
	operator fun minus(other: FloatVector2): FloatVector2 = FloatVector2(x - other.x, y - other.y)
	operator fun times(other: FloatVector2): FloatVector2 = FloatVector2(x * other.x, y * other.y)
	operator fun div(other: FloatVector2): FloatVector2 = FloatVector2(x / other.x, y / other.y)
	operator fun rem(other: FloatVector2): FloatVector2 = FloatVector2(x % other.x, y % other.y)

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	val perpendicular: FloatVector2 get() = FloatVector2(-y, x)


	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: FloatVector2): Float = x * other.x + y * other.y

	/**
	 * Returns a new vector equivalent to moving this vector a maximum distance of [maxDistance] towards [target].
	 *
	 * If `distance(this, target)` is less than [maxDistance], the returned vector is equal to [target].
	 */
	fun movedTowards(target: FloatVector2, maxDistance: Float): FloatVector2 {
		return MutableFloatVector2(x, y).apply { moveTowards(target, maxDistance) }
	}

	/** Returns a new vector made with the normalized components of this vector. */
	fun normalized(): FloatVector2 {
		return MutableFloatVector2(x, y).apply { normalize() }
	}

	/** Returns a new vector equivalent to reflecting this vector off [normal]. */
	fun reflected(normal: FloatVector2): FloatVector2 {
		return MutableFloatVector2(x, y).apply { reflect(normal) }
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is FloatVector2) return false

		if (x != other.x) return false
		if (y != other.y) return false

		return true
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		return result
	}

	override fun toString(): String = "($x, $y)"
}

@Suppress("FunctionName")
fun FloatVector2(x: Float, y: Float): FloatVector2 = MutableFloatVector2(x, y)

@Suppress("FunctionName")
fun FloatVector2(scalar: Float = 0f): FloatVector2 = MutableFloatVector2(scalar)


class MutableFloatVector2(
	override var x: Float,
	override var y: Float
) : FloatVector2() {
	constructor(scalar: Float = 0f) : this(scalar, scalar)

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

	operator fun plusAssign(other: FloatVector2) = set(x + other.x, y + other.y)
	operator fun minusAssign(other: FloatVector2) = set(x - other.x, y - other.y)
	operator fun timesAssign(other: FloatVector2) = set(x * other.x, y * other.y)
	operator fun divAssign(other: FloatVector2) = set(x / other.x, y / other.y)
	operator fun remAssign(other: FloatVector2) = set(x % other.x, y % other.y)

	fun set(scalar: Float) {
		x = scalar
		y = scalar
	}

	fun set(x: Float, y: Float) {
		this.x = x
		this.y = y
	}

	fun moveTowards(target: FloatVector2, maxDistance: Float) {
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

	fun reflect(normal: FloatVector2) {
		val factor = 2f * (this dot normal)
		set(
			x - factor * normal.x,
			y - factor * normal.y
		)
	}
}


/** Returns the unsigned angle in radians between [from] and [to]. */
fun angle(from: FloatVector2, to: FloatVector2): Float {
	val divisor = sqrt(from.squareMagnitude * to.squareMagnitude)
	if (divisor == 0f) return 0f

	val dot = ((from dot to) / divisor).coerceIn(-1f, 1f)
	return acos(dot)
}

/** Returns the signed angle in radians between [from] and [to]. */
fun signedAngle(from: FloatVector2, to: FloatVector2): Float {
	val angle = angle(from, to)
	val sign = sign(from.x * to.y - from.y * to.x)
	return angle * sign
}

/** Returns the distance between [from] and [to]. */
fun distance(from: FloatVector2, to: FloatVector2): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	return sqrt(xDiff * xDiff + yDiff * yDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: FloatVector2, to: FloatVector2, ratio: Float): FloatVector2 {
	return FloatVector2(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: FloatVector2, b: FloatVector2): FloatVector2 {
	return FloatVector2(
		max(a.x, b.x),
		max(a.y, b.y)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: FloatVector2, b: FloatVector2): FloatVector2 {
	return FloatVector2(
		min(a.x, b.x),
		min(a.y, b.y)
	)
}


operator fun Float.plus(vector: FloatVector2): FloatVector2 = FloatVector2(this + vector.x, this + vector.y)
operator fun Float.minus(vector: FloatVector2): FloatVector2 = FloatVector2(this - vector.x, this - vector.y)
operator fun Float.times(vector: FloatVector2): FloatVector2 = FloatVector2(this * vector.x, this * vector.y)
operator fun Float.div(vector: FloatVector2): FloatVector2 = FloatVector2(this / vector.x, this / vector.y)
operator fun Float.rem(vector: FloatVector2): FloatVector2 = FloatVector2(this % vector.x, this % vector.y)
