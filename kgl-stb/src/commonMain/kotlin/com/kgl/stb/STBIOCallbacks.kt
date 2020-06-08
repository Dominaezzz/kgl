package com.kgl.stb

import com.kgl.core.ByteBuffer

interface STBIOCallbacks {
	/**
	 * Fill 'data' with 'size' bytes.
	 * @return number of bytes actually read
 	 */
	fun read(data: ByteBuffer): Int

	/**
	 * Skip the next 'n' bytes, or 'unget' the last -n bytes if negative
	 */
	fun skip(n: Int)

	/**
	 * `true` if we are at end of file/data
	 */
	val eof: Boolean
}
