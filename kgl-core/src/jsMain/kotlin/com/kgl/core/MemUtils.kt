package com.kgl.core

import org.khronos.webgl.DataView
import org.khronos.webgl.Int8Array

actual inline fun <T> withMemory(length: Long, block: (DirectMemory) -> T): T {
	val array = ByteArray(length.toInt())
	val jsArray = array.unsafeCast<Int8Array>()
	val view = DataView(jsArray.buffer, jsArray.byteOffset, jsArray.byteLength)
	return block(DirectMemory(view))
}
