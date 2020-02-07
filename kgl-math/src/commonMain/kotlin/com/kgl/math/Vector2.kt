package com.kgl.math

/**
 * A generic two-component vector. Methods in this interface support only read-only access to the vector;
 * read/write access is supported through the [MutableVector2] interface.
 * @param T the type of component contained in the vector. The vector is invariant on its component type.
 */
interface Vector2<T> {

    val x: T
    val y: T

    operator fun get(index: Int): T = when (index) {
        0 -> x; 1 -> y; else -> throw IndexOutOfBoundsException()
    }

    operator fun component1(): T = x
    operator fun component2(): T = y

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

fun vector2Of(x: Float, y: Float): Vector2<Float> = FloatVector2(x, y)

/**
 * A generic two component vector that supports mutating the components.
 * @param T the type of component contained in the vector. The mutable vector is invariant on its component type.
 */
interface MutableVector2<T> : Vector2<T> {

    override var x: T
    override var y: T

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

fun mutableVector2Of(x: Float, y: Float): MutableVector2<Float> = FloatVector2(x, y)
