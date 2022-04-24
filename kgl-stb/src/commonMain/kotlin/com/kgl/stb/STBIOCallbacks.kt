package com.kgl.stb

import io.ktor.utils.io.bits.*

interface STBIOCallbacks {
	/**
	 * Fill 'data' with 'size' bytes.
	 * @return number of bytes actually read
	 */
	fun read(data: Memory): Int

	/**
	 * Skip the next 'n' bytes, or 'unget' the last -n bytes if negative
	 */
	fun skip(n: Int)

	/**
	 * `true` if we are at end of file/data
	 */
	val eof: Boolean
}
