package com.kgl.math

import kotlin.math.*

sealed class FloatVector4 {
	abstract val x: Float
	abstract val y: Float
	abstract val z: Float
	abstract val w: Float

	companion object {
		val ZERO = FloatVector4(0f)
		val ONE = FloatVector4(1f)
		val POSITIVE_INFINITY = FloatVector4(Float.POSITIVE_INFINITY)
		val NEGATIVE_INFINITY = FloatVector4(Float.NEGATIVE_INFINITY)
	}

	fun copy(
		x: Float = this.x,
		y: Float = this.y,
		z: Float = this.z,
		w: Float = this.w
	): FloatVector4 = FloatVector4(x, y, z, w)

	operator fun get(index: Int): Float = when (index) {
		0 -> x
		1 -> y
		2 -> z
		3 -> w
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): FloatVector4 = this
	operator fun unaryMinus(): FloatVector4 = FloatVector4(-x, -y, -z, -w)

	operator fun plus(scalar: Float): FloatVector4 = FloatVector4(x + scalar, y + scalar, z + scalar, w + scalar)
	operator fun minus(scalar: Float): FloatVector4 = FloatVector4(x - scalar, y - scalar, z - scalar, w - scalar)
	operator fun times(scalar: Float): FloatVector4 = FloatVector4(x * scalar, y * scalar, z * scalar, w * scalar)
	operator fun div(scalar: Float): FloatVector4 = FloatVector4(x / scalar, y / scalar, z / scalar, w / scalar)
	operator fun rem(scalar: Float): FloatVector4 = FloatVector4(x % scalar, y % scalar, z % scalar, w % scalar)

	operator fun plus(other: FloatVector4): FloatVector4 = FloatVector4(x + other.x, y + other.y, z + other.z, w + other.w)
	operator fun minus(other: FloatVector4): FloatVector4 = FloatVector4(x - other.x, y - other.y, z - other.z, w - other.w)
	operator fun times(other: FloatVector4): FloatVector4 = FloatVector4(x * other.x, y * other.y, z * other.z, w * other.w)
	operator fun div(other: FloatVector4): FloatVector4 = FloatVector4(x / other.x, y / other.y, z / other.z, w / other.w)
	operator fun rem(other: FloatVector4): FloatVector4 = FloatVector4(x % other.x, y % other.y, z % other.z, w % other.w)

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: FloatVector4): Float = x * other.x + y * other.y + z * other.z + w * other.w

	/**
	 * Returns a new vector equivalent to moving this vector a maximum distance of [maxDistance] towards [target].
	 *
	 * If `distance(this, target)` is less than [maxDistance], the returned vector is equal to [target].
	 */
	fun movedTowards(target: FloatVector4, maxDistance: Float): FloatVector4 {
		return MutableFloatVector4(x, y, z, w).apply { moveTowards(target, maxDistance) }
	}

	/** Returns a new vector made with the normalized components of this vector. */
	fun normalized(): FloatVector4 {
		return MutableFloatVector4(x, y, z, w).apply { normalize() }
	}

	/** Returns a new vector equivalent to projecting this vector onto [other]. */
	fun projected(other: FloatVector4): FloatVector4 {
		return MutableFloatVector4(x, y, z, w).apply { project(other) }
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is FloatVector4) return false

		if (x != other.x) return false
		if (y != other.y) return false
		if (z != other.z) return false
		if (w != other.w) return false

		return true
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		result = 31 * result + z.hashCode()
		result = 31 * result + w.hashCode()
		return result
	}

	override fun toString(): String = "($x, $y, $z, $w)"
}

@Suppress("FunctionName")
fun FloatVector4(scalar: Float = 0f): FloatVector4 = MutableFloatVector4(scalar)

@Suppress("FunctionName")
fun FloatVector4(x: Float, y: Float, z: Float, w: Float): FloatVector4 = MutableFloatVector4(x, y, z, w)


class MutableFloatVector4(
	override var x: Float,
	override var y: Float,
	override var z: Float,
	override var w: Float
) : FloatVector4() {
	constructor(scalar: Float = 0f) : this(scalar, scalar, scalar, scalar)

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value
		1 -> y = value
		2 -> z = value
		3 -> w = value
		else -> throw IndexOutOfBoundsException()
	}

	operator fun plusAssign(scalar: Float) = set(x + scalar, y + scalar, z + scalar, w + scalar)
	operator fun minusAssign(scalar: Float) = set(x - scalar, y - scalar, z - scalar, w - scalar)
	operator fun timesAssign(scalar: Float) = set(x * scalar, y * scalar, z * scalar, w * scalar)
	operator fun divAssign(scalar: Float) = set(x / scalar, y / scalar, z / scalar, w / scalar)
	operator fun remAssign(scalar: Float) = set(x % scalar, y % scalar, z % scalar, w % scalar)

	operator fun plusAssign(other: FloatVector4) = set(x + other.x, y + other.y, z + other.z, w + other.w)
	operator fun minusAssign(other: FloatVector4) = set(x - other.x, y - other.y, z - other.z, w - other.w)
	operator fun timesAssign(other: FloatVector4) = set(x * other.x, y * other.y, z * other.z, w * other.w)
	operator fun divAssign(other: FloatVector4) = set(x / other.x, y / other.y, z / other.z, w / other.w)
	operator fun remAssign(other: FloatVector4) = set(x % other.x, y % other.y, z % other.z, w % other.w)

	fun set(scalar: Float) {
		x = scalar
		y = scalar
		z = scalar
		w = scalar
	}

	fun set(x: Float, y: Float, z: Float, w: Float) {
		this.x = x
		this.y = y
		this.z = z
		this.w = w
	}

	/**
	 * Move this vector towards the target vector a maximum distance of [maxDistance].
	 *
	 * If `distance(this, target)` is less than [maxDistance], the sets this vector equal to [target]. Negative values
	 * of [maxDistance] moves this vector away from [target].
	 */
	fun moveTowards(target: FloatVector4, maxDistance: Float) {
		val xDiff = target.x - x
		val yDiff = target.y - y
		val zDiff = target.z - z
		val wDiff = target.w - w

		val squareDistance = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff + wDiff * wDiff
		if (squareDistance == 0f || (maxDistance >= 0 && squareDistance <= maxDistance * maxDistance)) {
			this.set(target.x, target.y, target.z, target.w)
		} else {
			val distance = sqrt(squareDistance)
			this.set(
				x + xDiff / distance * maxDistance,
				y + yDiff / distance * maxDistance,
				z + zDiff / distance * maxDistance,
				w + wDiff / distance * maxDistance
			)
		}
	}

	/** Normalizes this vector. */
	fun normalize() {
		val magnitude = magnitude
		if (magnitude > 0) divAssign(magnitude) else set(0f)
	}

	/** Projects this vector onto [other]. */
	fun project(other: FloatVector4) {
		val squareMagnitude = other.squareMagnitude
		if (squareMagnitude == 0f) {
			set(0f)
		} else {
			val dot = this dot other
			set(
				other.x * dot / squareMagnitude,
				other.y * dot / squareMagnitude,
				other.z * dot / squareMagnitude,
				other.w * dot / squareMagnitude
			)
		}
	}
}


/** Returns the distance between [from] and [to]. */
fun distance(from: FloatVector4, to: FloatVector4): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	val zDiff = from.z - to.z
	val wDiff = from.w - to.w
	return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff + wDiff * wDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: FloatVector4, to: FloatVector4, ratio: Float): FloatVector4 {
	return FloatVector4(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio,
		from.z * (1 - ratio) + to.z * ratio,
		from.w * (1 - ratio) + to.w * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: FloatVector4, b: FloatVector4): FloatVector4 {
	return FloatVector4(
		max(a.x, b.x),
		max(a.y, b.y),
		max(a.z, b.z),
		max(a.w, b.w)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: FloatVector4, b: FloatVector4): FloatVector4 {
	return FloatVector4(
		min(a.x, b.x),
		min(a.y, b.y),
		min(a.z, b.z),
		min(a.w, b.w)
	)
}


operator fun Float.plus(vector: FloatVector4): FloatVector4 = FloatVector4(this + vector.x, this + vector.y, this + vector.z, this + vector.w)
operator fun Float.minus(vector: FloatVector4): FloatVector4 = FloatVector4(this - vector.x, this - vector.y, this - vector.z, this - vector.w)
operator fun Float.times(vector: FloatVector4): FloatVector4 = FloatVector4(this * vector.x, this * vector.y, this * vector.z, this * vector.w)
operator fun Float.div(vector: FloatVector4): FloatVector4 = FloatVector4(this / vector.x, this / vector.y, this / vector.z, this / vector.w)
operator fun Float.rem(vector: FloatVector4): FloatVector4 = FloatVector4(this % vector.x, this % vector.y, this % vector.z, this % vector.w)
