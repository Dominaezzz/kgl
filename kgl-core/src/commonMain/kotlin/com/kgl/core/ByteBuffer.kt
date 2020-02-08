package com.kgl.core

expect class ByteBuffer : Buffer {
	override val size: ULong
	val length: Long

	operator fun get(index: Long): Byte
	operator fun set(index: Long, value: Byte)

	fun get(dst: ByteArray, srcIndex: Long = 0L, dstIndex: Long = 0L, length: Long = dst.size.toLong())
	fun set(src: ByteArray, srcIndex: Long = 0L, dstIndex: Long = 0L, length: Long = src.size.toLong())

	fun copyTo(dst: ByteBuffer, srcIndex: Long = 0L, dstIndex: Long = 0L, length: Long = dst.length - dstIndex)
	fun slice(index: Long, length: Long): ByteBuffer
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun ByteBuffer.checkBounds(index: Long, length: Long) {
	require(index in 0 until this.length)
	require(length in 0 until (this.length - index))
}
