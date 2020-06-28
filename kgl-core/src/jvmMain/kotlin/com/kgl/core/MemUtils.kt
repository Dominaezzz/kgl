package com.kgl.core

import org.lwjgl.system.MemoryUtil


actual inline fun <T> withMemory(length: Long, block: (DirectMemory) -> T): T {
	val jvmBuffer = MemoryUtil.memAlloc(length.toInt())
	try {
		return block(DirectMemory(jvmBuffer))
	} finally {
		MemoryUtil.memFree(jvmBuffer)
	}
}
