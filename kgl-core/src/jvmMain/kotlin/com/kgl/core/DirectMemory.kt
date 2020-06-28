package com.kgl.core

import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.jni.JNINativeInterface
import java.nio.ByteBuffer as JvmByteBuffer

actual class DirectMemory(private val ptr: Long, actual val size: Long) {

	constructor(buffer: JvmByteBuffer) : this(MemoryUtil.memAddress(buffer), buffer.capacity().toLong())

	actual operator fun get(offset: Long): Byte {
		return MemoryUtil.memGetByte(ptr + (offset * Byte.SIZE_BYTES))
	}

	actual operator fun set(offset: Long, value: Byte) {
		MemoryUtil.memPutByte(ptr + (offset * Byte.SIZE_BYTES), value)
	}

	actual fun get(dst: ByteArray, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(srcOffset, length)

		JNINativeInterface.nSetByteArrayRegion(dst, dstOffset.toInt(), length.toInt(), ptr + (srcOffset * Byte.SIZE_BYTES))
	}

	actual fun set(src: ByteArray, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(dstOffset, length)
		JNINativeInterface.nGetByteArrayRegion(src, srcOffset.toInt(), length.toInt(), ptr + (dstOffset * Byte.SIZE_BYTES))
	}

	actual fun copyTo(dst: DirectMemory, srcOffset: Long, dstOffset: Long, length: Long) {
		checkBounds(srcOffset, length)
		dst.checkBounds(dstOffset, length)

		MemoryUtil.memCopy(
				ptr + (srcOffset * Byte.SIZE_BYTES),
				dst.ptr + (dstOffset * Byte.SIZE_BYTES),
				length
		)
	}

	actual fun slice(offset: Long, length: Long): DirectMemory {
		checkBounds(offset, length)

		return DirectMemory(ptr + (offset * Byte.SIZE_BYTES), length)
	}

	fun asJvmByteBuffer(): JvmByteBuffer {
		return MemoryUtil.memByteBuffer(ptr, size.toInt())
	}
}
