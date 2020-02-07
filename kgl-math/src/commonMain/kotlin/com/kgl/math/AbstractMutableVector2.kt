package com.kgl.math

abstract class AbstractMutableVector2<T> : MutableVector2<T> {

	override fun equals(other: Any?): Boolean {
		if(this === other) return true
		if (other !is AbstractMutableVector2<*>) return false

		if (x != other.x) return false
		if (y != other.y) return false

		return true
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result += 31 * y.hashCode()
		return result
	}

	override fun toString(): String = "[$x, $y]"
}
