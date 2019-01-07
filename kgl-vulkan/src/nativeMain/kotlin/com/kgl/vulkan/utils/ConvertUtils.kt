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

import com.kgl.vulkan.unions.ClearValue
import cvulkan.VkBool32
import cvulkan.VkClearValue
import kotlinx.cinterop.*
import kotlinx.io.core.IoBuffer

// TODO: Replace this with actual stack implementation.
@ThreadLocal
internal object VirtualStack : AutofreeScope() {
	private val scopes = mutableListOf<Arena>()

	override fun alloc(size: Long, align: Int): NativePointed {
		check(scopes.size > 0) { "Call push() before allocation." }
		return scopes.last().alloc(size, align)
	}

	fun push() {
		scopes.add(Arena())
	}

	fun pop() {
		check(scopes.size > 0) { "pop() must only be called after push()." }

		scopes.removeAt(scopes.lastIndex).clear()
	}
}

internal inline fun Boolean.toVkType(): VkBool32 = this.toVkBool()
internal inline fun Float.toVkType(): Float = this
internal inline fun Byte.toVkType(): Byte = this
internal inline fun UByte.toVkType(): UByte = this
internal inline fun Short.toVkType(): Short = this
internal inline fun UShort.toVkType(): UShort = this
internal inline fun Int.toVkType(): Int = this
internal inline fun UInt.toVkType(): UInt = this
internal inline fun Long.toVkType(): Long = this
internal inline fun ULong.toVkType(): ULong = this

internal inline fun VkFlag<*>.toVkType(): UInt = this.value
internal inline fun VkFlag<*>?.toVkType(): UInt = this?.toVkType() ?: 0U

internal inline fun VkEnum<*>.toVkType(): UInt = this.value
internal inline fun VkEnum<*>?.toVkType(): UInt = this?.toVkType() ?: 0U

internal inline fun ByteArray.toVkType(): CPointer<ByteVar> = refTo(0).getPointer(VirtualStack)
internal inline fun ShortArray.toVkType(): CPointer<ShortVar> = refTo(0).getPointer(VirtualStack)
internal inline fun IntArray.toVkType(): CPointer<IntVar> = refTo(0).getPointer(VirtualStack)
internal inline fun LongArray.toVkType(): CPointer<LongVar> = refTo(0).getPointer(VirtualStack)
internal inline fun FloatArray.toVkType(): CPointer<FloatVar> = refTo(0).getPointer(VirtualStack)

internal inline fun UByteArray.toVkType(): CPointer<UByteVar> = refTo(0).getPointer(VirtualStack)
internal inline fun UShortArray.toVkType(): CPointer<UShortVar> = refTo(0).getPointer(VirtualStack)
internal inline fun UIntArray.toVkType(): CPointer<UIntVar> = refTo(0).getPointer(VirtualStack)
internal inline fun ULongArray.toVkType(): CPointer<ULongVar> = refTo(0).getPointer(VirtualStack)

internal inline fun IoBuffer.toVkType(): COpaquePointer {
	TODO("Needs to be redesigned")
	var buffer: COpaquePointer? = null
	readDirect {
		buffer = it
		readRemaining
	}
	return buffer ?: throw Error("Could not read directly from IOBuffer")
}

internal inline fun String.toVkType(): CPointer<ByteVar> = cstr.getPointer(VirtualStack)
internal inline fun String?.toVkType(): CPointer<ByteVar>? = this?.toVkType()
internal inline fun Array<String>.toVkType(): CPointer<CPointerVar<ByteVar>> {
	return mapToCArray(VirtualStack) { value = it.cstr.getPointer(VirtualStack) }
}

internal inline fun Collection<String>.toVkType(): CPointer<CPointerVar<ByteVar>> {
	return mapToCArray(VirtualStack) { value = it.cstr.getPointer(VirtualStack) }
}

internal inline fun Collection<ClearValue>.toVkType(): CPointer<VkClearValue> {
	return mapToCArray(VirtualStack) { it.write(this) }
}

internal inline fun Collection<VkEnum<*>>.toVkType(): CPointer<UIntVar> {
	return mapToCArray(VirtualStack) { value = it.value }
}

internal inline fun Collection<VkFlag<*>>.toVkType(): CPointer<UIntVar> {
	return mapToCArray(VirtualStack) { value = it.value }
}

//internal inline fun <reified U : CPointed, reified T : CPointer<U>> VkHandleNative<T>.toVkType(): CPointer<U> {
//	return ptr
//}
internal inline fun <reified U : CPointed, reified T : CPointer<U>> VkHandleNative<T>?.toVkType(): CPointer<U>? {
	return this?.ptr
}

internal inline fun <reified U : CPointed, reified T : CPointer<U>> Collection<VkHandleNative<T>>.toVkType(): CArrayPointer<CPointerVar<U>> {
	return mapToCArray(VirtualStack) { value = it.ptr }
}

internal inline fun <reified U : CPointed, reified T : CPointer<U>> Array<in VkHandleNative<T>>.toVkType(): CArrayPointer<CPointerVar<U>> {
	return mapToCArray(VirtualStack) { value = it.ptr }
}

internal inline fun <reified T : CVariable> List<T.() -> Unit>.mapToStackArray(): CArrayPointer<T> {
	return mapToCArray(VirtualStack) { it() }
}
