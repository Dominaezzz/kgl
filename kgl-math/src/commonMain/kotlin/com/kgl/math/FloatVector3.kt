package com.kgl.math

import kotlin.math.sqrt

/**
 * Provides a [MutableVector3] implementation with Float components.
 */
class FloatVector3(private val storage: FloatArray) : AbstractMutableVector3<Float>() {

	constructor(x: Float, y: Float, z: Float) : this(floatArrayOf(x, y, z))
	constructor(f: Float = 0f) : this(f, f, f)


	override var x: Float
		get() = storage[0]
		set(value) = storage.set(0, value)

	override var y: Float
		get() = storage[1]
		set(value) = storage.set(1, value)

	override var z: Float
		get() = storage[2]
		set(value) = storage.set(2, value)

	override fun copy(): FloatVector3 = FloatVector3(x, y, z)


	// geometric

	override val length: Float get() = sqrt(this dot this)

	override fun normalize(): FloatVector3 {
		val length = length
		return FloatVector3(x / length, y / length, z / length)
	}

	override infix fun dot(other: Vector3<Float>): Float = x * other.x + y * other.y + z * other.z


	// arithmetic

	override fun plus(scalar: Float): FloatVector3 {
		return FloatVector3(x + scalar, y + scalar, z + scalar)
	}

	override fun minus(scalar: Float): FloatVector3 {
		return FloatVector3(x - scalar, y - scalar, z - scalar)
	}

	override fun times(scalar: Float): FloatVector3 {
		return FloatVector3(x * scalar, y * scalar, z * scalar)
	}

	override fun div(scalar: Float): FloatVector3 {
		return FloatVector3(x / scalar, y / scalar, z / scalar)
	}


	override fun plusAssign(scalar: Float) {
		x += scalar; y += scalar; z += scalar
	}

	override fun minusAssign(scalar: Float) {
		x -= scalar; y -= scalar; z -= scalar
	}

	override fun timesAssign(scalar: Float) {
		x *= scalar; y *= scalar; z *= scalar
	}

	override fun divAssign(scalar: Float) {
		x /= scalar; y /= scalar; z /= scalar
	}


	override fun plus(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x + other.x, y + other.y, z + other.z)
	}

	override fun minus(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x - other.x, y - other.y, z - other.z)
	}

	override fun times(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x * other.x, y * other.y, z * other.z)
	}

	override fun div(other: Vector3<Float>): FloatVector3 {
		return FloatVector3(x / other.x, y / other.y, z / other.z)
	}


	override fun plusAssign(other: Vector3<Float>) {
		x += other.x; y += other.y; z += other.z
	}

	override fun minusAssign(other: Vector3<Float>) {
		x -= other.x; y -= other.y; z -= other.z
	}

	override fun timesAssign(other: Vector3<Float>) {
		x *= other.x; y *= other.y; z *= other.z
	}

	override fun divAssign(other: Vector3<Float>) {
		x /= other.x; y /= other.y; z /= other.z
	}
}
