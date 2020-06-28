package com.kgl.core


expect inline fun <T> withMemory(length: Long, block: (DirectMemory) -> T): T
