package com.kgl.core

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

actual inline fun <T> withMemory(length: Long, block: (DirectMemory) -> T): T {
	val array = ByteArray(length.toInt())
	return array.usePinned {
		val buffer = DirectMemory(it.addressOf(0), length)
		block(buffer)
	}
}
