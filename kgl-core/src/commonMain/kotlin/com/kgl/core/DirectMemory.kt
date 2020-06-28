package com.kgl.core

expect class DirectMemory {
	val size: Long

	operator fun get(offset: Long): Byte
	operator fun set(offset: Long, value: Byte)

	fun get(dst: ByteArray, srcOffset: Long = 0L, dstOffset: Long = 0L, length: Long = dst.size.toLong())
	fun set(src: ByteArray, srcOffset: Long = 0L, dstOffset: Long = 0L, length: Long = src.size.toLong())

	fun copyTo(dst: DirectMemory, srcOffset: Long = 0L, dstOffset: Long = 0L, length: Long = dst.size - dstOffset)
	fun slice(offset: Long, length: Long): DirectMemory
}

@Suppress("NOTHING_TO_INLINE")
internal inline fun DirectMemory.checkBounds(offset: Long, length: Long) {
	require(offset in 0 until this.size)
	require(length in 0 until (this.size - offset))
}
