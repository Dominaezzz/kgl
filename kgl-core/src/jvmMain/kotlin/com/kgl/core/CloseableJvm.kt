package com.kgl.core

actual typealias Closeable = java.io.Closeable

@PublishedApi
internal actual fun Throwable.addSuppressedInternal(other: Throwable) {
	addSuppressed(other)
}
