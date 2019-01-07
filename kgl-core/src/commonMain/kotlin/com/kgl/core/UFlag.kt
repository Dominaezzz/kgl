/**
 * Copyright [2019] [Dominic Fischer]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kgl.core

interface UFlag<T> where T : Enum<T>, T : UFlag<T> {
	val value: UInt

	companion object {
		operator fun <T> invoke(value: UInt): UFlag<T> where T : Enum<T>, T : UFlag<T> {
			return UFlags(value)
		}
	}

	private class UFlags<T>(override val value: UInt) : UFlag<T> where T : Enum<T>, T : UFlag<T> {
//        override fun toString(): String {
//            return enumValues<T>().filter { it in this }.joinToString(" | ") { it.name }
//        }
	}
}

operator fun <T> UFlag<T>?.contains(other: UFlag<T>?): Boolean where T : Enum<T>, T : UFlag<T> {
	if (other == null) return true
	if (this == null) return false
	return value and other.value == other.value
}

infix fun <T> UFlag<T>.or(other: UFlag<T>): UFlag<T> where T : Enum<T>, T : UFlag<T> = UFlag(value or other.value)

infix fun <T> UFlag<T>?.and(other: UFlag<T>?): UFlag<T>? where T : Enum<T>, T : UFlag<T> {
	if (other == null || this == null) return null
	val result = value and other.value
	return if (result != 0U) UFlag(result) else null
}
