@file:Suppress("NOTHING_TO_INLINE")

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
package com.kgl.vulkan.utils

import org.lwjgl.system.*

internal inline fun <TItem, reified T : Struct, reified TBuffer : StructBuffer<T, TBuffer>> Collection<TItem>.toCArray(
	stack: MemoryStack,
	mallocStack: (Int, MemoryStack) -> TBuffer,
	block: T.(TItem) -> Unit
): TBuffer {
	return mallocStack(size, stack).also {
		forEachIndexed { index, item ->
			it[index].block(item)
		}
	}
}

internal inline fun <reified T : Struct, reified TBuffer : StructBuffer<T, TBuffer>> Collection<T.(MemoryStack) -> Unit>.toCArray(
	stack: MemoryStack,
	mallocStack: (Int, MemoryStack) -> TBuffer
): TBuffer {
	return toCArray(stack, mallocStack) { it(stack) }
}

internal inline fun <TItem, reified T : Struct, reified TBuffer : StructBuffer<T, TBuffer>> Collection<TItem>.mapToCArray(
	stack: MemoryStack,
	mallocStack: (Int, MemoryStack) -> TBuffer,
	block: T.(TItem) -> Unit
): TBuffer {
	return toCArray(stack, mallocStack, block)
}

internal inline fun <reified T : Struct, reified TBuffer : StructBuffer<T, TBuffer>> Collection<T.(MemoryStack) -> Unit>.mapToCArray(
	stack: MemoryStack,
	mallocStack: (Int, MemoryStack) -> TBuffer
): TBuffer {
	return mapToCArray(stack, mallocStack) { it(stack) }
}
