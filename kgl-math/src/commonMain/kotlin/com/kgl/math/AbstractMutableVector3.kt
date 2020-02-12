package com.kgl.math

/**
 * Provides a skeletal implementations of the [MutableVector3] interface.
 *
 * @param T the type of component contained in the vector. The vector is invariant on its component type.
 */
abstract class AbstractMutableVector3<T> : MutableVector3<T> {

	override fun equals(other: Any?): Boolean {
		if(this === other) return true
		if (other !is AbstractMutableVector3<*>) return false

		if (x != other.x) return false
		if (y != other.y) return false
		if (z != other.z) return false

		return true
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result += 31 * y.hashCode()
		result += 31 * z.hashCode()
		return result
	}

	override fun toString(): String = "[$x, $y, $z]"
}
