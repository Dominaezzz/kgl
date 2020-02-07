package com.kgl.math

import kotlin.math.sqrt

class FloatVector2(private val storage: FloatArray) : AbstractMutableVector2<Float>() {

    constructor(x: Float, y: Float) : this(floatArrayOf(x, y))
    constructor(f: Float) : this(f, f)
    constructor() : this(0f)


    override var x: Float
        get() = storage[0]
        set(value) = run { storage[0] = value }

    override var y: Float
        get() = storage[1]
        set(value) = run { storage[1] = value }

    override fun copy(): FloatVector2 = FloatVector2(x, y)


    // geometric

    override val length: Float get() = sqrt(this dot this)

    override fun normalize(): FloatVector2 {
        val length = length
        return FloatVector2(x / length, y / length)
    }

    override fun dot(other: Vector2<Float>): Float = x * other.x + y * other.y


    // arithmetic

    override fun plus(scalar: Float): FloatVector2 {
        return FloatVector2(x + scalar, y + scalar)
    }

    override fun minus(scalar: Float): FloatVector2 {
        return FloatVector2(x - scalar, y - scalar)
    }

    override fun times(scalar: Float): FloatVector2 {
        return FloatVector2(x * scalar, y * scalar)
    }

    override fun div(scalar: Float): FloatVector2 {
        return FloatVector2(x / scalar, y / scalar)
    }


    override fun plusAssign(scalar: Float) {
        x += scalar; y += scalar
    }

    override fun minusAssign(scalar: Float) {
        x -= scalar; y -= scalar
    }

    override fun timesAssign(scalar: Float) {
        x *= scalar; y *= scalar
    }

    override fun divAssign(scalar: Float) {
        x /= scalar; y /= scalar
    }


    override fun plus(other: Vector2<Float>): FloatVector2 {
        return FloatVector2(x + other.x, y + other.y)
    }

    override fun minus(other: Vector2<Float>): FloatVector2 {
        return FloatVector2(x - other.x, y - other.y)
    }

    override fun times(other: Vector2<Float>): FloatVector2 {
        return FloatVector2(x * other.x, y * other.y)
    }

    override fun div(other: Vector2<Float>): FloatVector2 {
        return FloatVector2(x / other.x, y / other.y)
    }


    override fun plusAssign(other: Vector2<Float>) {
        x += other.x; y += other.y
    }

    override fun minusAssign(other: Vector2<Float>) {
        x -= other.x; y -= other.y
    }

    override fun timesAssign(other: Vector2<Float>) {
        x *= other.x; y *= other.y
    }

    override fun divAssign(other: Vector2<Float>) {
        x /= other.x; y /= other.y
    }
}
