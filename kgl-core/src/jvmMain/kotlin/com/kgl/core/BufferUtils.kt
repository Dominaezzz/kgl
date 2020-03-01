package com.kgl.core

import org.lwjgl.system.MemoryUtil


actual inline fun <T> withByteBuffer(length: Long, block: (ByteBuffer) -> T): T {
	val jvmBuffer = MemoryUtil.memAlloc(length.toInt())
	try {
		return block(ByteBuffer(jvmBuffer))
	} finally {
		MemoryUtil.memFree(jvmBuffer)
	}
}
