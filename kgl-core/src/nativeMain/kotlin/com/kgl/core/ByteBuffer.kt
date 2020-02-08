package com.kgl.core

import kotlinx.cinterop.*
import platform.posix.memcpy

actual class ByteBuffer(private val ptr: CPointer<ByteVar>, actual val length: Long) : Buffer {
	actual override val size: ULong get() = (length * Byte.SIZE_BYTES).toULong()

	actual operator fun get(index: Long): Byte {
		return ptr[index]
	}

	actual operator fun set(index: Long, value: Byte) {
		ptr[index] = value
	}

	actual fun get(dst: ByteArray, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(srcIndex, length)

		dst.usePinned {
			memcpy(it.addressOf(dstIndex.toInt()), ptr + srcIndex, length.convert())
		}
	}

	actual fun set(src: ByteArray, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(dstIndex, length)

		src.usePinned {
			memcpy(ptr + dstIndex, it.addressOf(srcIndex.toInt()), length.convert())
		}
	}

	actual fun copyTo(dst: ByteBuffer, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(srcIndex, length)
		dst.checkBounds(dstIndex, length)

		memcpy(dst.ptr + dstIndex, ptr + srcIndex, length.convert())
	}

	actual fun slice(index: Long, length: Long): ByteBuffer {
		checkBounds(index, length)

		return ByteBuffer((ptr + index)!!, length)
	}
}
