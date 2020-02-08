package com.kgl.core

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

actual class ByteBuffer(private val ptr: Uint8Array) : Buffer {
	actual val length: Long get() = ptr.length.toLong()
	actual override val size: ULong get() = length.toULong() * Byte.SIZE_BYTES.toUInt()

	actual operator fun get(index: Long): Byte {
		return ptr[index.toInt()]
	}

	actual operator fun set(index: Long, value: Byte) {
		ptr[index.toInt()] = value
	}

	actual fun get(dst: ByteArray, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(srcIndex, length)
		for (i in 0 until length) {
			dst[(dstIndex + i).toInt()] = get(srcIndex + i)
		}
	}

	actual fun set(src: ByteArray, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(dstIndex, length)
		for (i in 0 until length) {
			this[dstIndex + i] = src[(srcIndex + i).toInt()]
		}
	}

	actual fun copyTo(dst: ByteBuffer, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(srcIndex, length)
		dst.checkBounds(dstIndex, length)
		for (i in 0 until length) {
			dst[dstIndex + i] = get(srcIndex + i)
		}
	}

	actual fun slice(index: Long, length: Long): ByteBuffer {
		checkBounds(index, length)
		return ByteBuffer(ptr.subarray(index.toInt(), (index + length).toInt()))
	}
}
