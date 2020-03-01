package com.kgl.core

import org.khronos.webgl.Int8Array

actual inline fun <T> withByteBuffer(length: Long, block: (ByteBuffer) -> T): T {
	val array = ByteArray(length.toInt())
	return block(ByteBuffer(array.unsafeCast<Int8Array>()))
}
