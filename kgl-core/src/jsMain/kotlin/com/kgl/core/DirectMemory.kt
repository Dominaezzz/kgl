package com.kgl.core

import org.khronos.webgl.DataView

actual class DirectMemory(private val ptr: DataView) {
	actual val size: Long get() = ptr.byteLength.toLong()

	actual operator fun get(offset: Long): Byte {
		return ptr.getInt8(offset.toInt())
	}

	actual operator fun set(offset: Long, value: Byte) {
		ptr.setInt8(offset.toInt(), value)
	}

	actual fun get(dst: ByteArray, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(srcOffset, length)
		for (i in 0 until length) {
			dst[(dstOffset + i).toInt()] = get(srcOffset + i)
		}
	}

	actual fun set(src: ByteArray, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(dstOffset, length)
		for (i in 0 until length) {
			this[dstOffset + i] = src[(srcOffset + i).toInt()]
		}
	}

	actual fun copyTo(dst: DirectMemory, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(srcOffset, length)
		dst.checkBounds(dstOffset, length)
		for (i in 0 until length) {
			dst[dstOffset + i] = get(srcOffset + i)
		}
	}

	actual fun slice(offset: Long, length: Long): DirectMemory {
		checkBounds(offset, length)
		return DirectMemory(DataView(ptr.buffer, ptr.byteOffset + offset.toInt(), length.toInt()))
	}
}
