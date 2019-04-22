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

import kotlinx.cinterop.Arena
import kotlinx.cinterop.AutofreeScope
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.NativePointed
import kotlin.native.concurrent.ThreadLocal

// TODO: Replace this with actual stack implementation.
@ThreadLocal
object VirtualStack : NativePlacement {
	private val scopes = mutableListOf<Arena>()

	val currentFrame: AutofreeScope? get() = scopes.lastOrNull()

	override fun alloc(size: Long, align: Int): NativePointed {
		check(scopes.size > 0) { "Call push() before allocation." }
		return scopes.last().alloc(size, align)
	}

	fun push() {
		scopes.add(Arena())
	}

	fun pop() {
		check(scopes.isNotEmpty()) { "pop() must only be called after push()." }

		scopes.removeAt(scopes.lastIndex).clear()
	}
}
