package com.kgl.core

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

actual inline fun <T> withByteBuffer(length: Long, block: (ByteBuffer) -> T): T {
	val array = ByteArray(length.toInt())
	return array.usePinned {
		val buffer = ByteBuffer(it.addressOf(0), length)
		block(buffer)
	}
}
