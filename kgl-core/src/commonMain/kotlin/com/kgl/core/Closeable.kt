package com.kgl.core

expect interface Closeable {
	fun close()
}

inline fun <C : Closeable, R> C.use(block: (C) -> R): R {
	var closed = false

	return try {
		block(this)
	} catch (first: Throwable) {
		try {
			closed = true
			close()
		} catch (second: Throwable) {
			first.addSuppressedInternal(second)
		}

		throw first
	} finally {
		if (!closed) {
			close()
		}
	}
}

// Work-around until https://youtrack.jetbrains.com/issue/KT-23737
@PublishedApi
internal expect fun Throwable.addSuppressedInternal(other: Throwable)
