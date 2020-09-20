package com.kgl.math

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

sealed class Vec4 {
	abstract val x: Float
	abstract val y: Float
	abstract val z: Float
	abstract val w: Float

	companion object {
		val ZERO = Vec4(0f)
		val ONE = Vec4(1f)
		val POSITIVE_INFINITY = Vec4(Float.POSITIVE_INFINITY)
		val NEGATIVE_INFINITY = Vec4(Float.NEGATIVE_INFINITY)
	}

	fun copy(
		x: Float = this.x,
		y: Float = this.y,
		z: Float = this.z,
		w: Float = this.w
	): Vec4 = Vec4(x, y, z, w)

	operator fun get(index: Int): Float = when (index) {
		0 -> x
		1 -> y
		2 -> z
		3 -> w
		else -> throw IndexOutOfBoundsException()
	}

	operator fun unaryPlus(): Vec4 = this
	operator fun unaryMinus(): Vec4 = Vec4(-x, -y, -z, -w)

	operator fun plus(scalar: Float): Vec4 = Vec4(x + scalar, y + scalar, z + scalar, w + scalar)
	operator fun minus(scalar: Float): Vec4 = Vec4(x - scalar, y - scalar, z - scalar, w - scalar)
	operator fun times(scalar: Float): Vec4 = Vec4(x * scalar, y * scalar, z * scalar, w * scalar)
	operator fun div(scalar: Float): Vec4 = Vec4(x / scalar, y / scalar, z / scalar, w / scalar)
	operator fun rem(scalar: Float): Vec4 = Vec4(x % scalar, y % scalar, z % scalar, w % scalar)

	operator fun plus(other: Vec4): Vec4 = Vec4(x + other.x, y + other.y, z + other.z, w + other.w)
	operator fun minus(other: Vec4): Vec4 = Vec4(x - other.x, y - other.y, z - other.z, w - other.w)
	operator fun times(other: Vec4): Vec4 = Vec4(x * other.x, y * other.y, z * other.z, w * other.w)
	operator fun div(other: Vec4): Vec4 = Vec4(x / other.x, y / other.y, z / other.z, w / other.w)
	operator fun rem(other: Vec4): Vec4 = Vec4(x % other.x, y % other.y, z % other.z, w % other.w)

	val squareMagnitude: Float get() = this dot this
	val magnitude: Float get() = sqrt(this dot this)

	/** Returns the dot product of this vector and [other]. */
	infix fun dot(other: Vec4): Float = x * other.x + y * other.y + z * other.z + w * other.w

	/**
	 * Returns a new vector equivalent to moving this vector a maximum distance of [maxDistance] towards [target].
	 *
	 * If `distance(this, target)` is less than [maxDistance], the returned vector is equal to [target].
	 */
	fun movedTowards(target: Vec4, maxDistance: Float): Vec4 {
		return MutableVec4(x, y, z, w).apply { moveTowards(target, maxDistance) }
	}

	/** Returns a new vector made with the normalized components of this vector. */
	fun normalized(): Vec4 {
		return MutableVec4(x, y, z, w).apply { normalize() }
	}

	/** Returns a new vector equivalent to projecting this vector onto [other]. */
	fun projected(other: Vec4): Vec4 {
		return MutableVec4(x, y, z, w).apply { project(other) }
	}


	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Vec4) return false

		if (x != other.x) return false
		if (y != other.y) return false
		if (z != other.z) return false
		if (w != other.w) return false

		return true
	}

	override fun hashCode(): Int = w.hashCode() + 31 * (z.hashCode() + 31 * (y.hashCode() + 31 * x.hashCode()))

	override fun toString(): String = "($x, $y, $z, $w)"
}

@Suppress("FunctionName")
fun Vec4(scalar: Float = 0f): Vec4 = MutableVec4(scalar)

@Suppress("FunctionName")
fun Vec4(x: Float, y: Float, z: Float, w: Float): Vec4 = MutableVec4(x, y, z, w)


class MutableVec4(x: Float, y: Float, z: Float, w: Float) : Vec4() {
	private val m = floatArrayOf(x, y, z, w)

	constructor(scalar: Float = 0f) : this(scalar, scalar, scalar, scalar)

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
	override var w: Float
		get() = m[3]
		set(value) {
			m[3] = value
		}

	fun asFloatArray(): FloatArray = m

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

	operator fun plusAssign(other: Vec4) = set(x + other.x, y + other.y, z + other.z, w + other.w)
	operator fun minusAssign(other: Vec4) = set(x - other.x, y - other.y, z - other.z, w - other.w)
	operator fun timesAssign(other: Vec4) = set(x * other.x, y * other.y, z * other.z, w * other.w)
	operator fun divAssign(other: Vec4) = set(x / other.x, y / other.y, z / other.z, w / other.w)
	operator fun remAssign(other: Vec4) = set(x % other.x, y % other.y, z % other.z, w % other.w)

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
	fun moveTowards(target: Vec4, maxDistance: Float) {
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
	fun project(other: Vec4) {
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

fun Vec4.toFloatArray(): FloatArray = floatArrayOf(x, y, z, w)

fun Vec4.toVec4(): Vec4 = toMutableVec4()

fun Vec4.toMutableVec4(): MutableVec4 = MutableVec4(x, y, z, w)

/** Returns the distance between [from] and [to]. */
fun distance(from: Vec4, to: Vec4): Float {
	val xDiff = from.x - to.x
	val yDiff = from.y - to.y
	val zDiff = from.z - to.z
	val wDiff = from.w - to.w
	return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff + wDiff * wDiff)
}

/** Linearly interpolates between vectors [from] and [to] by [ratio]. */
fun lerp(from: Vec4, to: Vec4, ratio: Float): Vec4 {
	return Vec4(
		from.x * (1 - ratio) + to.x * ratio,
		from.y * (1 - ratio) + to.y * ratio,
		from.z * (1 - ratio) + to.z * ratio,
		from.w * (1 - ratio) + to.w * ratio
	)
}

/** Returns a new vector made from the largest components of [a] and [b]. */
fun max(a: Vec4, b: Vec4): Vec4 {
	return Vec4(
		max(a.x, b.x),
		max(a.y, b.y),
		max(a.z, b.z),
		max(a.w, b.w)
	)
}

/** Returns a new vector made from the smallest components of [a] and [b]. */
fun min(a: Vec4, b: Vec4): Vec4 {
	return Vec4(
		min(a.x, b.x),
		min(a.y, b.y),
		min(a.z, b.z),
		min(a.w, b.w)
	)
}


operator fun Float.plus(vector: Vec4): Vec4 = Vec4(this + vector.x, this + vector.y, this + vector.z, this + vector.w)
operator fun Float.minus(vector: Vec4): Vec4 = Vec4(this - vector.x, this - vector.y, this - vector.z, this - vector.w)
operator fun Float.times(vector: Vec4): Vec4 = Vec4(this * vector.x, this * vector.y, this * vector.z, this * vector.w)
operator fun Float.div(vector: Vec4): Vec4 = Vec4(this / vector.x, this / vector.y, this / vector.z, this / vector.w)
operator fun Float.rem(vector: Vec4): Vec4 = Vec4(this % vector.x, this % vector.y, this % vector.z, this % vector.w)
