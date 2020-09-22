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

import cvulkan.*
import kotlinx.cinterop.*

internal inline fun <TItem, reified T : CVariable> Collection<TItem>.mapToCArray(
	scope: NativePlacement,
	block: T.(TItem) -> Unit
): CArrayPointer<T> {
	return scope.allocArray<T>(size).also {
		forEachIndexed { index, item ->
			it[index].block(item)
		}
	}
}

internal inline fun <TItem, reified T : CVariable> Array<TItem>.mapToCArray(
	scope: NativePlacement,
	block: T.(TItem) -> Unit
): CArrayPointer<T> {
	return scope.allocArray(size) { block(get(it)) }
}

internal inline fun <reified T : CVariable, TPlacement : NativePlacement> List<T.(TPlacement) -> Unit>.mapToCArray(scope: TPlacement): CArrayPointer<T> {
	return mapToCArray(scope) { it(scope) }
}

internal fun Collection<String>.mapToCArray(scope: AutofreeScope): CArrayPointer<CPointerVar<ByteVar>> {
	return mapToCArray(scope) { value = it.cstr.getPointer(scope) }
}

internal inline fun <reified U : CPointed, reified T : CPointer<U>> Collection<VkHandleNative<T>>.mapToCArray(scope: NativePlacement): CArrayPointer<CPointerVar<U>> {
	return mapToCArray(scope) { value = it.ptr }
}

internal inline fun <reified U : CPointed, reified T : CPointer<U>> Array<VkHandleNative<T>>.mapToCArray(scope: NativePlacement): CArrayPointer<CPointerVar<U>> {
	return mapToCArray(scope) { value = it.ptr }
}

internal inline fun VkBool32.toBoolean(): Boolean = this == VK_TRUE.toUInt()
internal inline fun Boolean.toVkBool(): VkBool32 = (if (this) VK_TRUE else VK_FALSE).toUInt()
