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

interface Flag<T> where T : Enum<T>, T : Flag<T> {
	val value: Int

	companion object {
		operator fun <T> invoke(value: Int): Flag<T> where T : Enum<T>, T : Flag<T> {
			return Flags(value)
		}
	}

	private class Flags<T>(override val value: Int) : Flag<T> where T : Enum<T>, T : Flag<T> {
//        override fun toString(): String {
//            return enumValues<T>().filter { it in this }.joinToString(" | ") { it.name }
//        }
	}
}

operator fun <T> Flag<T>?.contains(other: Flag<T>?): Boolean where T : Enum<T>, T : Flag<T> {
	if (other == null) return true
	if (this == null) return false
	return value and other.value == other.value
}

infix fun <T> Flag<T>.or(other: Flag<T>): Flag<T> where T : Enum<T>, T : Flag<T> = Flag(value or other.value)

infix fun <T> Flag<T>?.and(other: Flag<T>?): Flag<T>? where T : Enum<T>, T : Flag<T> {
	if (other == null || this == null) return null
	val result = value and other.value
	return if (result != 0) Flag(result) else null
}
