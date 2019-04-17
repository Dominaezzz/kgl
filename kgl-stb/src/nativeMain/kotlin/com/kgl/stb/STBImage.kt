/**
 * Copyright [2019] [Dominic Fischer]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kgl.stb

import com.kgl.core.VirtualStack
import com.kgl.stb.STBImage.Companion.asStbChannels
import cstb.*
import kotlinx.cinterop.*
import kotlinx.io.core.Closeable
import kotlinx.io.core.IoBuffer

actual class STBImage(
		actual val buffer: IoBuffer,
		actual val info: STBInfo) : Closeable {

	override fun close() {
		buffer.readDirect {
			stbi_image_free(it)
			0
		}
	}

	actual companion object {
		@Suppress("NOTHING_TO_INLINE")
		private inline fun Boolean.toInt() = if (this) 1 else 0
		private val nativeCallbacks = cValue<stbi_io_callbacks> {
			read = staticCFunction { user, data, size ->
				val callbacks = user!!.asStableRef<STBIOCallbacks>().get()
				callbacks.read(IoBuffer(data!!, size))
			}
			skip = staticCFunction { user, n ->
				val callbacks = user!!.asStableRef<STBIOCallbacks>().get()
				callbacks.skip(n)
			}
			eof = staticCFunction { user ->
				val callbacks = user!!.asStableRef<STBIOCallbacks>().get()
				callbacks.eof.toByte().toInt()
			}
		}
		private val Channels?.asStbChannels: Int get() = this?.value ?: STBI_default.toInt()

		private inline fun <reified T : CVariable> genericLoad(
				buffer: IoBuffer, desiredChannels: Channels?,
				crossinline block: (
						buffer: CValuesRef<stbi_ucVar>?,
						len: Int,
						x: CValuesRef<IntVar>?,
						y: CValuesRef<IntVar>?,
						channels_in_file: CValuesRef<IntVar>?,
						desired_channels: Int
				) -> CPointer<T>?
		): STBImage {
			VirtualStack.push()
			try {
				val x = VirtualStack.alloc<IntVar>()
				val y = VirtualStack.alloc<IntVar>()
				val channels = VirtualStack.alloc<IntVar>()

				var result: CPointer<T>? = null
				buffer.readDirect {
					result = block(it.reinterpret(), buffer.readRemaining, x.ptr, y.ptr, channels.ptr,
							desiredChannels.asStbChannels)
					buffer.readRemaining
				}

				val bytes = (result ?: throw STBException(failureReason)).reinterpret<ByteVar>()

				return STBImage(
						IoBuffer(bytes, x.value * y.value * channels.value * sizeOf<T>().toInt()),
						STBInfo(x.value, y.value, Channels.values()[channels.value - 1])
				)
			} finally {
				VirtualStack.pop()
			}
		}
		private inline fun <reified T : CVariable> genericLoad(
				callbacks: STBIOCallbacks, desiredChannels: Channels?, 
				crossinline block: (
						clbk: CValuesRef<stbi_io_callbacks>?,
						user: COpaquePointer,
						x: CValuesRef<IntVar>?,
						y: CValuesRef<IntVar>?,
						channels_in_file: CValuesRef<IntVar>?,
						desired_channels: Int
				) -> CPointer<T>?
		): STBImage {
			VirtualStack.push()
			val userData = StableRef.create(callbacks)
			try {
				val x = VirtualStack.alloc<IntVar>()
				val y = VirtualStack.alloc<IntVar>()
				val channels = VirtualStack.alloc<IntVar>()

				val result = block(
						nativeCallbacks,
						userData.asCPointer(),
						x.ptr, y.ptr, channels.ptr, desiredChannels.asStbChannels
				)

				val bytes = (result ?: throw STBException(failureReason)).reinterpret<ByteVar>()

				return STBImage(
						IoBuffer(bytes, x.value * y.value * channels.value * sizeOf<T>().toInt()),
						STBInfo(x.value, y.value, Channels.values()[channels.value - 1])
				)
			} finally {
				userData.dispose()
				VirtualStack.pop()
			}
		}

		actual fun load(buffer: IoBuffer, desiredChannels: Channels?): STBImage {
			return genericLoad(buffer, desiredChannels, ::stbi_load_from_memory)
		}
		actual fun load(callbacks: STBIOCallbacks, desiredChannels: Channels?): STBImage {
			return genericLoad(callbacks, desiredChannels, ::stbi_load_from_callbacks)
		}

		actual fun load16(buffer: IoBuffer, desiredChannels: Channels?): STBImage {
			return genericLoad(buffer, desiredChannels, ::stbi_load_16_from_memory)
		}
		actual fun load16(callbacks: STBIOCallbacks, desiredChannels: Channels?): STBImage {
			return genericLoad(callbacks, desiredChannels, ::stbi_load_16_from_callbacks)
		}

		actual fun loadf(buffer: IoBuffer, desiredChannels: Channels?): STBImage {
			return genericLoad(buffer, desiredChannels, ::stbi_loadf_from_memory)
		}
		actual fun loadf(callbacks: STBIOCallbacks, desiredChannels: Channels?): STBImage {
			return genericLoad(callbacks, desiredChannels, ::stbi_loadf_from_callbacks)
		}

		actual fun setHdrToLdrGamma(gamma: Float) {
			stbi_hdr_to_ldr_gamma(gamma)
		}
		actual fun setHdrToLdrScale(scale: Float) {
			stbi_hdr_to_ldr_scale(scale)
		}

		actual fun setLdrToHdrGamma(gamma: Float) {
			stbi_ldr_to_hdr_gamma(gamma)
		}
		actual fun setLdrToHdrScale(scale: Float) {
			stbi_ldr_to_hdr_scale(scale)
		}

		actual fun isHdr(callbacks: STBIOCallbacks): Boolean {
			val userData = StableRef.create(callbacks)
			try {
				val result = stbi_is_hdr_from_callbacks(nativeCallbacks, userData.asCPointer())
				return result != 0
			} finally {
				userData.dispose()
			}
		}
		actual fun isHdr(buffer: IoBuffer): Boolean {
			var result: Int? = null
			buffer.readDirect {
				result = stbi_is_hdr_from_memory(it.reinterpret(), buffer.readRemaining)
				buffer.readRemaining
			}
			return result != null && result != 0
		}

		actual val failureReason: String? get() = stbi_failure_reason()?.toKStringFromUtf8()

		actual fun loadInfo(buffer: IoBuffer): STBInfo {
			VirtualStack.push()
			try {
				val x = VirtualStack.alloc<IntVar>()
				val y = VirtualStack.alloc<IntVar>()
				val channels = VirtualStack.alloc<IntVar>()

				var result = 0
				buffer.readDirect {
					result = stbi_info_from_memory(it.reinterpret(), buffer.readRemaining, x.ptr, y.ptr, channels.ptr)
					buffer.readRemaining
				}

				return if (result != 0) {
					STBInfo(x.value, y.value, Channels.values()[channels.value - 1])
				} else {
					throw STBException(failureReason)
				}
			} finally {
				VirtualStack.pop()
			}
		}
		actual fun loadInfo(callbacks: STBIOCallbacks): STBInfo {
			VirtualStack.push()
			val userData = StableRef.create(callbacks)
			try {
				val x = VirtualStack.alloc<IntVar>()
				val y = VirtualStack.alloc<IntVar>()
				val channels = VirtualStack.alloc<IntVar>()

				val result = stbi_info_from_callbacks(
						nativeCallbacks,
						userData.asCPointer(),
						x.ptr, y.ptr, channels.ptr
				)

				return if (result != 0) {
					STBInfo(x.value, y.value, Channels.values()[channels.value - 1])
				} else {
					throw STBException(failureReason)
				}
			} finally {
				userData.dispose()
				VirtualStack.pop()
			}
		}

		actual fun is16Bit(callbacks: STBIOCallbacks): Boolean {
			val userData = StableRef.create(callbacks)
			try {
				val result = stbi_is_16_bit_from_callbacks(nativeCallbacks, userData.asCPointer())
				return result != 0
			} finally {
				userData.dispose()
			}
		}
		actual fun is16Bit(buffer: IoBuffer): Boolean {
			var result: Int? = null
			buffer.readDirect {
				result = stbi_is_16_bit_from_memory(it.reinterpret(), buffer.readRemaining)
				buffer.readRemaining
			}
			return result != null && result != 0
		}

		actual fun setUnpremultiplyOnLoad(flag: Boolean) {
			stbi_set_unpremultiply_on_load(flag.toInt())
		}

		actual fun setConvertIphonePngToRgb(flag: Boolean) {
			stbi_convert_iphone_png_to_rgb(flag.toInt())
		}

		actual fun setFlipVerticallyOnLoad(value: Boolean) {
			stbi_set_flip_vertically_on_load(value.toByte().toInt())
		}
	}
}
