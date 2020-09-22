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

import com.kgl.core.*
import cvulkan.*
import kotlinx.cinterop.*


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

internal inline fun ByteArray.toVkType(): CPointer<ByteVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)
internal inline fun ShortArray.toVkType(): CPointer<ShortVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)
internal inline fun IntArray.toVkType(): CPointer<IntVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)
internal inline fun LongArray.toVkType(): CPointer<LongVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)
internal inline fun FloatArray.toVkType(): CPointer<FloatVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)

internal inline fun UByteArray.toVkType(): CPointer<UByteVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)
internal inline fun UShortArray.toVkType(): CPointer<UShortVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)
internal inline fun UIntArray.toVkType(): CPointer<UIntVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)
internal inline fun ULongArray.toVkType(): CPointer<ULongVar> = refTo(0).getPointer(VirtualStack.currentFrame!!)

internal inline fun String.toVkType(): CPointer<ByteVar> = cstr.getPointer(VirtualStack.currentFrame!!)
internal inline fun String?.toVkType(): CPointer<ByteVar>? = this?.toVkType()
internal inline fun Array<String>.toVkType(): CPointer<CPointerVar<ByteVar>> {
	return mapToCArray(VirtualStack) { value = it.cstr.getPointer(VirtualStack.currentFrame!!) }
}

internal inline fun Collection<String>.toVkType(): CPointer<CPointerVar<ByteVar>> {
	return mapToCArray(VirtualStack) { value = it.cstr.getPointer(VirtualStack.currentFrame!!) }
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

internal inline fun <reified U : CPointed, reified T : CPointer<U>> Array<VkHandleNative<T>>.toVkType(): CArrayPointer<CPointerVar<U>> {
	return mapToCArray(VirtualStack) { value = it.ptr }
}

internal inline fun <reified TStruct : CVariable, TBuilder> List<(TBuilder) -> Unit>.mapToStackArray(createBuilder: (TStruct) -> TBuilder): CArrayPointer<TStruct> {
	return mapToCArray(VirtualStack) { it(createBuilder(this)) }
}

internal inline fun <reified TStruct : CVariable, TBuilder> List<(TBuilder) -> Unit>.mapToJaggedArray(createBuilder: (TStruct) -> TBuilder): CArrayPointer<CPointerVar<TStruct>> {
	val array = mapToStackArray(createBuilder)
	return VirtualStack.allocArray(size) { value = array[it].ptr }
}
