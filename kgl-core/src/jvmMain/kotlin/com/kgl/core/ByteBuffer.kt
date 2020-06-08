package com.kgl.core

import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.jni.JNINativeInterface
import java.nio.ByteBuffer as JvmByteBuffer

actual class ByteBuffer(private val ptr: Long, actual val length: Long) : Buffer {
	actual override val size: ULong get() = (length * Byte.SIZE_BYTES).toULong()

	constructor(buffer: JvmByteBuffer) : this(MemoryUtil.memAddress(buffer), buffer.capacity().toLong())

	actual operator fun get(index: Long): Byte {
		return MemoryUtil.memGetByte(ptr + (index * Byte.SIZE_BYTES))
	}

	actual operator fun set(index: Long, value: Byte) {
		MemoryUtil.memPutByte(ptr + (index * Byte.SIZE_BYTES), value)
	}

	actual fun get(dst: ByteArray, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(srcIndex, length)

		JNINativeInterface.nSetByteArrayRegion(dst, dstIndex.toInt(), length.toInt(), ptr + (srcIndex * Byte.SIZE_BYTES))
	}

	actual fun set(src: ByteArray, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(dstIndex, length)
		JNINativeInterface.nGetByteArrayRegion(src, srcIndex.toInt(), length.toInt(), ptr + (dstIndex * Byte.SIZE_BYTES))
	}

	actual fun copyTo(dst: ByteBuffer, srcIndex: Long, dstIndex: Long, length: Long) {
		checkBounds(srcIndex, length)
		dst.checkBounds(dstIndex, length)

		MemoryUtil.memCopy(
				ptr + (srcIndex * Byte.SIZE_BYTES),
				dst.ptr + (dstIndex * Byte.SIZE_BYTES),
				length
		)
	}

	actual fun slice(index: Long, length: Long): ByteBuffer {
		checkBounds(index, length)

		return ByteBuffer(ptr + (index * Byte.SIZE_BYTES), length)
	}

	fun asJvmByteBuffer(): JvmByteBuffer {
		return MemoryUtil.memByteBuffer(ptr, length.toInt())
	}
}
