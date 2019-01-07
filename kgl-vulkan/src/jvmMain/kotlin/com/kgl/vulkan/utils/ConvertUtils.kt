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
import kotlinx.io.core.IoBuffer
import org.lwjgl.PointerBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.Pointer
import org.lwjgl.system.Struct
import org.lwjgl.system.StructBuffer
import org.lwjgl.vulkan.VK10.VK_TRUE
import org.lwjgl.vulkan.VkClearValue
import java.nio.*

internal inline fun Int.toBoolean(): Boolean = this == VK_TRUE
internal inline fun Boolean.toVkType(): Boolean = this
internal inline fun Float.toVkType(): Float = this
internal inline fun Double.toVkType(): Double = this
internal inline fun Byte.toVkType(): Byte = this
internal inline fun UByte.toVkType(): Byte = this.toByte()
internal inline fun Short.toVkType(): Short = this
internal inline fun UShort.toVkType(): Short = this.toShort()
internal inline fun Int.toVkType(): Int = this
internal inline fun UInt.toVkType(): Int = this.toInt()
internal inline fun Long.toVkType(): Long = this
internal inline fun ULong.toVkType(): Long = this.toLong()

internal inline fun VkFlag<*>.toVkType(): Int = this.value
@JvmName("toNativeType_")
internal inline fun VkFlag<*>?.toVkType(): Int = this?.value ?: 0

internal inline fun VkEnum<*>.toVkType(): Int = this.value
@JvmName("toNativeType_")
internal inline fun VkEnum<*>?.toVkType(): Int = this?.value ?: 0

internal inline fun FloatArray.toVkType(): FloatBuffer = MemoryStack.stackGet().floats(*this)
internal inline fun DoubleArray.toVkType(): DoubleBuffer = MemoryStack.stackGet().doubles(*this)

internal inline fun ByteArray.toVkType(): ByteBuffer = MemoryStack.stackGet().bytes(*this)
internal inline fun ShortArray.toVkType(): ShortBuffer = MemoryStack.stackGet().shorts(*this)
internal inline fun IntArray.toVkType(): IntBuffer = MemoryStack.stackGet().ints(*this)
internal inline fun LongArray.toVkType(): LongBuffer = MemoryStack.stackGet().longs(*this)

@JvmName("toNativeType_")
internal inline fun ByteArray?.toVkType(): ByteBuffer? = this?.toVkType()

@JvmName("toNativeType_")
internal inline fun ShortArray?.toVkType(): ShortBuffer? = this?.toVkType()

@JvmName("toNativeType_")
internal inline fun IntArray?.toVkType(): IntBuffer? = this?.toVkType()

@JvmName("toNativeType_")
internal inline fun LongArray?.toVkType(): LongBuffer? = this?.toVkType()

internal inline fun UByteArray.toVkType(): ByteBuffer = asByteArray().toVkType()
internal inline fun UShortArray.toVkType(): ShortBuffer = asShortArray().toVkType()
internal inline fun UIntArray.toVkType(): IntBuffer = asIntArray().toVkType()
internal inline fun ULongArray.toVkType(): LongBuffer = asLongArray().toVkType()

internal inline fun UByteArray?.toVkType(): ByteBuffer? = this?.toVkType()
internal inline fun UShortArray?.toVkType(): ShortBuffer? = this?.toVkType()
internal inline fun UIntArray?.toVkType(): IntBuffer? = this?.toVkType()
internal inline fun ULongArray?.toVkType(): LongBuffer? = this?.toVkType()

@JvmName("toNativeType_")
internal inline fun String?.toVkType(): ByteBuffer? = this?.toVkType()

internal inline fun String.toVkType(): ByteBuffer = MemoryStack.stackGet().UTF8(this)

@JvmName("toNativeType_")
internal inline fun Array<String>?.toVkType(): PointerBuffer? = this?.toVkType()

internal inline fun Array<String>.toVkType(): PointerBuffer {
	val stack = MemoryStack.stackGet()
	return stack.mallocPointer(size).also {
		forEach { item ->
			it.put(stack.UTF8(item))
		}
		it.flip()
	}
}

@JvmName("toNativeType_")
internal inline fun Collection<String>?.toVkType(): PointerBuffer? = this?.toVkType()

internal inline fun Collection<String>.toVkType(): PointerBuffer {
	val stack = MemoryStack.stackGet()
	return stack.mallocPointer(size).also {
		forEach { item ->
			it.put(stack.UTF8(item))
		}
		it.flip()
	}
}

@JvmName("toNativeType__")
internal inline fun Collection<ClearValue>.toVkType(): VkClearValue.Buffer {
	return VkClearValue.callocStack(size).also {
		forEachIndexed { index, item ->
			item.write(it[index])
		}
	}
}

internal inline fun Collection<VkEnum<*>>.toVkType(): IntBuffer {
	val stack = MemoryStack.stackGet()
	return stack.mallocInt(size).also {
		forEach { item ->
			it.put(item.value)
		}
		it.flip()
	}
}

@JvmName("toNativeType_")
internal inline fun Collection<VkFlag<*>>.toVkType(): IntBuffer {
	val stack = MemoryStack.stackGet()
	return stack.mallocInt(size).also {
		forEach { item ->
			it.put(item.value)
		}
		it.flip()
	}
}

@JvmName("toNativeType_")
internal inline fun <reified T : Pointer> VkHandleJVM<T>.toVkType(): T = ptr

internal inline fun VkHandleJVM<Long>?.toVkType(): Long = this?.ptr ?: 0

internal inline fun IoBuffer.toVkType(): ByteBuffer {
	TODO("Needs to be redesigned!")
	var buffer: ByteBuffer? = null
	readDirect {
		buffer = it
	}
	return buffer ?: throw Error("Could not read directly from IOBuffer")
}

@JvmName("toNativeType__")
internal inline fun <reified T : Pointer> Collection<VkHandleJVM<T>>.toVkType(): PointerBuffer {
	return MemoryStack.stackGet().mallocPointer(size).also {
		forEach { item -> it.put(item.ptr) }
		it.flip()
	}
}

@JvmName("toNativeType__")
internal inline fun Collection<VkHandleJVM<Long>>.toVkType(): LongBuffer {
	return MemoryStack.stackGet().mallocLong(size).also {
		forEach { item -> it.put(item.ptr) }
		it.flip()
	}
}

internal inline fun <reified T : Pointer> Array<out VkHandleJVM<T>>.toVkType(): PointerBuffer {
	return MemoryStack.stackGet().mallocPointer(size).also {
		forEach { item -> it.put(item.ptr) }
		it.flip()
	}
}

internal inline fun <reified T : Struct, reified TBuffer : StructBuffer<T, TBuffer>> Collection<(T) -> Unit>.mapToStackArray(
		mallocStack: (Int, MemoryStack) -> TBuffer
): TBuffer {
	return mallocStack(size, MemoryStack.stackGet()).also {
		forEachIndexed { index, item ->
			item(it[index])
		}
	}
}
