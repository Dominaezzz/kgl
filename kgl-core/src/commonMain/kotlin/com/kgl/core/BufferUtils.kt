package com.kgl.core


expect inline fun <T> withByteBuffer(length: Long, block: (ByteBuffer) -> T): T
