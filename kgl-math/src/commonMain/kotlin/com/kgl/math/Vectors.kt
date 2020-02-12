package com.kgl.math

/**
 * A generic two-component vector. Methods in this interface support only read-only access to the vector;
 * read/write access is supported through the [MutableVector2] interface.
 * @param T the type of component contained in the vector. The vector is invariant on its component type.
 */
interface Vector2<T> {

	val x: T
	val y: T

	operator fun component1(): T = x
	operator fun component2(): T = y

	operator fun get(index: Int): T = when (index) {
		0 -> x; 1 -> y; else -> throw IndexOutOfBoundsException()
	}

	fun copy(): Vector2<T>


	// geometric

	val length: T

	fun normalize(): Vector2<T>

	infix fun dot(other: Vector2<T>): T


	// arithmetic

	operator fun plus(scalar: T): Vector2<T>
	operator fun minus(scalar: T): Vector2<T>
	operator fun times(scalar: T): Vector2<T>
	operator fun div(scalar: T): Vector2<T>

	operator fun plus(other: Vector2<T>): Vector2<T>
	operator fun minus(other: Vector2<T>): Vector2<T>
	operator fun times(other: Vector2<T>): Vector2<T>
	operator fun div(other: Vector2<T>): Vector2<T>
}

/**
 * A generic two-component vector that supports mutating the components.
 * @param T the type of component contained in the vector. The mutable vector is invariant on its component type.
 */
interface MutableVector2<T> : Vector2<T> {

	override var x: T
	override var y: T

	operator fun set(index: Int, value: T) {
		when (index) {
			0 -> x = value
			1 -> y = value
			else -> throw IndexOutOfBoundsException()
		}
	}

	override fun copy(): MutableVector2<T>


	// geometric

	override fun normalize(): MutableVector2<T>


	// arithmetic

	operator fun plusAssign(scalar: T)
	operator fun minusAssign(scalar: T)
	operator fun timesAssign(scalar: T)
	operator fun divAssign(scalar: T)

	operator fun plusAssign(other: Vector2<T>)
	operator fun minusAssign(other: Vector2<T>)
	operator fun timesAssign(other: Vector2<T>)
	operator fun divAssign(other: Vector2<T>)
}

/**
 * A generic three-component vector. Methods in this interface support only read-only access to the vector;
 * read/write access is supported through the [MutableVector3] interface.
 * @param T the type of component contained in the vector. The vector is invariant on its component type.
 */
interface Vector3<T> {

	val x: T
	val y: T
	val z: T

	operator fun component1(): T = x
	operator fun component2(): T = y
	operator fun component3(): T = z

	operator fun get(index: Int): T = when (index) {
		0 -> x; 1 -> y; 2 -> z; else -> throw IndexOutOfBoundsException()
	}

	fun copy(): Vector3<T>


	// geometric

	val length: T

	fun normalize(): Vector3<T>

	infix fun dot(other: Vector3<T>): T


	// arithmetic

	operator fun plus(scalar: T): Vector3<T>
	operator fun minus(scalar: T): Vector3<T>
	operator fun times(scalar: T): Vector3<T>
	operator fun div(scalar: T): Vector3<T>

	operator fun plus(other: Vector3<T>): Vector3<T>
	operator fun minus(other: Vector3<T>): Vector3<T>
	operator fun times(other: Vector3<T>): Vector3<T>
	operator fun div(other: Vector3<T>): Vector3<T>
}

/**
 * A generic three-component vector that supports mutating the components.
 * @param T the type of component contained in the vector. The mutable vector is invariant on its component type.
 */
interface MutableVector3<T> : Vector3<T> {

	override var x: T
	override var y: T
	override var z: T

	operator fun set(index: Int, value: T) {
		when (index) {
			0 -> x = value
			1 -> y = value
			2 -> z = value
			else -> throw IndexOutOfBoundsException()
		}
	}

	override fun copy(): MutableVector3<T>


	// geometric

	override fun normalize(): MutableVector3<T>


	// arithmetic

	operator fun plusAssign(scalar: T)
	operator fun minusAssign(scalar: T)
	operator fun timesAssign(scalar: T)
	operator fun divAssign(scalar: T)

	operator fun plusAssign(other: Vector3<T>)
	operator fun minusAssign(other: Vector3<T>)
	operator fun timesAssign(other: Vector3<T>)
	operator fun divAssign(other: Vector3<T>)
}

/**
 * A generic four-component vector. Methods in this interface support only read-only access to the vector;
 * read/write access is supported through the [MutableVector4] interface.
 * @param T the type of component contained in the vector. The vector is invariant on its component type.
 */
interface Vector4<T> {

	val x: T
	val y: T
	val z: T

	operator fun component1(): T = x
	operator fun component2(): T = y
	operator fun component3(): T = z

	operator fun get(index: Int): T = when (index) {
		0 -> x; 1 -> y; 2 -> z; else -> throw IndexOutOfBoundsException()
	}

	fun copy(): Vector4<T>


	// geometric

	val length: T

	fun normalize(): Vector4<T>

	infix fun dot(other: Vector4<T>): T


	// arithmetic

	operator fun plus(scalar: T): Vector4<T>
	operator fun minus(scalar: T): Vector4<T>
	operator fun times(scalar: T): Vector4<T>
	operator fun div(scalar: T): Vector4<T>

	operator fun plus(other: Vector4<T>): Vector4<T>
	operator fun minus(other: Vector4<T>): Vector4<T>
	operator fun times(other: Vector4<T>): Vector4<T>
	operator fun div(other: Vector4<T>): Vector4<T>
}

/**
 * A generic four-component vector that supports mutating the components.
 * @param T the type of component contained in the vector. The mutable vector is invariant on its component type.
 */
interface MutableVector4<T> : Vector4<T> {

	override var x: T
	override var y: T
	override var z: T

	operator fun set(index: Int, value: T) {
		when (index) {
			0 -> x = value
			1 -> y = value
			2 -> z = value
			else -> throw IndexOutOfBoundsException()
		}
	}

	override fun copy(): MutableVector4<T>


	// geometric

	override fun normalize(): MutableVector4<T>


	// arithmetic

	operator fun plusAssign(scalar: T)
	operator fun minusAssign(scalar: T)
	operator fun timesAssign(scalar: T)
	operator fun divAssign(scalar: T)

	operator fun plusAssign(other: Vector4<T>)
	operator fun minusAssign(other: Vector4<T>)
	operator fun timesAssign(other: Vector4<T>)
	operator fun divAssign(other: Vector4<T>)
}

/**
 * Returns a new read-only two-component vector with the given [value] for x and y
 */
fun vector2Of(value: Float = 0f): Vector2<Float> = FloatVector2(value)

/**
 * Returns a new read-only two-component vector with the given [x] and [y] values.
 */
fun vector2Of(x: Float, y: Float): Vector2<Float> = FloatVector2(x, y)

/**
 * Returns a new mutable two-component vector with the given [value] for x and y.
 */
fun mutableVector2Of(value: Float): MutableVector2<Float> = FloatVector2(value)

/**
 * Returns a new mutable two-component vector with the given [x] and [y] values.
 */
fun mutableVector2Of(x: Float, y: Float): MutableVector2<Float> = FloatVector2(x, y)

/**
 * Returns a new read-only two-component vector with the given [value] for x, y, and z
 */
fun vector3Of(value: Float = 0f): Vector3<Float> = FloatVector3(value)

/**
 * Returns a new read-only two-component vector with the given [x], [y], and [z] values.
 */
fun vector3Of(x: Float, y: Float, z: Float): Vector3<Float> = FloatVector3(x, y, z)

/**
 * Returns a new mutable two-component vector with the given [value] for x, y, and z.
 */
fun mutableVector3Of(value: Float): MutableVector3<Float> = FloatVector3(value)

/**
 * Returns a new mutable two-component vector with the given [x], [y], and [z] values.
 */
fun mutableVector3Of(x: Float, y: Float, z: Float): MutableVector3<Float> = FloatVector3(x, y, z)
