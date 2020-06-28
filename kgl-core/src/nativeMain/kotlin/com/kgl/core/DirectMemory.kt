package com.kgl.core

import kotlinx.cinterop.*
import platform.posix.memcpy

actual class DirectMemory(private val ptr: CPointer<ByteVar>, actual val size: Long) {

	actual operator fun get(offset: Long): Byte {
		return ptr[offset]
	}

	actual operator fun set(offset: Long, value: Byte) {
		ptr[offset] = value
	}

	actual fun get(dst: ByteArray, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(srcOffset, length)

		dst.usePinned {
			memcpy(it.addressOf(dstOffset.toInt()), ptr + srcOffset, length.convert())
		}
	}

	actual fun set(src: ByteArray, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(dstOffset, length)

		src.usePinned {
			memcpy(ptr + dstOffset, it.addressOf(srcOffset.toInt()), length.convert())
		}
	}

	actual fun copyTo(dst: DirectMemory, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(srcOffset, length)
		dst.checkBounds(dstOffset, length)

		memcpy(dst.ptr + dstOffset, ptr + srcOffset, length.convert())
	}

	actual fun slice(offset: Long, length: Long): DirectMemory {
		checkBounds(offset, length)

		return DirectMemory((ptr + offset)!!, length)
	}

	fun asCPointer(): CPointer<ByteVar> {
		return ptr
	}
}
