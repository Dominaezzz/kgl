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

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import org.lwjgl.stb.*
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.*
import java.nio.*
import java.nio.Buffer

actual class STBImage(
	actual val buffer: Memory,
	actual val info: STBInfo
) : Closeable {

	override fun close() {
		stbi_image_free(buffer.buffer)
	}

	actual companion object {
		@OptIn(DangerousInternalIoApi::class)
		private inline fun <reified T : Buffer> genericLoad(
			buffer: Memory, desiredChannels: Channels?,
			crossinline block: (
				buffer: ByteBuffer,
				x: IntBuffer,
				y: IntBuffer,
				channels_in_file: IntBuffer,
				desired_channels: Int
			) -> T?,
			convert: (T) -> ByteBuffer
		): STBImage {
			MemoryStack.stackPush()
			try {
				val x = MemoryStack.stackMallocInt(1)
				val y = MemoryStack.stackMallocInt(1)
				val channels = MemoryStack.stackMallocInt(1)

				val result = block(buffer.buffer, x, y, channels, desiredChannels?.value ?: 0)

				val bytes = convert(result ?: throw STBException(failureReason))

				return STBImage(
					Memory(bytes),
					STBInfo(x[0], y[0], Channels.values()[channels[0] - 1])
				)
			} finally {
				MemoryStack.stackPop()
			}
		}

		@OptIn(DangerousInternalIoApi::class)
		private inline fun <T> usingNativeCallbacks(callbacks: STBIOCallbacks, block: (STBIIOCallbacks, Long) -> T): T {
			val nativeCallbacks = STBIIOCallbacks.mallocStack()
			nativeCallbacks.read { _, data, size -> callbacks.read(Memory(MemoryUtil.memByteBuffer(data, size))) }
			nativeCallbacks.skip { _, n -> callbacks.skip(n) }
			nativeCallbacks.eof { if (callbacks.eof) 1 else 0 }
			try {
				return block(nativeCallbacks, 0L)
			} finally {
				nativeCallbacks.read().free()
				nativeCallbacks.skip().free()
				nativeCallbacks.eof().free()
			}
		}

		@OptIn(DangerousInternalIoApi::class)
		private inline fun <reified T : Buffer> genericLoad(
			callbacks: STBIOCallbacks, desiredChannels: Channels?,
			crossinline block: (
				clbk: STBIIOCallbacks,
				user: Long,
				x: IntBuffer,
				y: IntBuffer,
				channels_in_file: IntBuffer,
				desired_channels: Int
			) -> T?,
			convert: (T) -> ByteBuffer
		): STBImage {
			MemoryStack.stackPush()
			try {
				val x = MemoryStack.stackMallocInt(1)
				val y = MemoryStack.stackMallocInt(1)
				val channels = MemoryStack.stackMallocInt(1)

				return usingNativeCallbacks(callbacks) { nativeCallbacks, userPtr ->
					val result = block(nativeCallbacks, userPtr, x, y, channels, desiredChannels?.value ?: 0)

					val bytes = convert(result ?: throw STBException(failureReason))

					STBImage(
						Memory(bytes),
						STBInfo(x[0], y[0], Channels.values()[channels[0] - 1])
					)
				}
			} finally {
				MemoryStack.stackPop()
			}
		}

		actual fun load(buffer: Memory, desiredChannels: Channels?): STBImage {
			return genericLoad(buffer, desiredChannels, ::stbi_load_from_memory) { it }
		}

		actual fun load(callbacks: STBIOCallbacks, desiredChannels: Channels?): STBImage {
			return genericLoad(callbacks, desiredChannels, ::stbi_load_from_callbacks) { it }
		}

		actual fun load16(buffer: Memory, desiredChannels: Channels?): STBImage {
			return genericLoad(buffer, desiredChannels, ::stbi_load_16_from_memory) {
				MemoryUtil.memByteBuffer(MemoryUtil.memAddress(it), it.capacity() * Short.SIZE_BYTES)
			}
		}

		actual fun load16(callbacks: STBIOCallbacks, desiredChannels: Channels?): STBImage {
			return genericLoad(callbacks, desiredChannels, ::stbi_load_16_from_callbacks) {
				MemoryUtil.memByteBuffer(MemoryUtil.memAddress(it), it.capacity() * Short.SIZE_BYTES)
			}
		}

		actual fun loadf(buffer: Memory, desiredChannels: Channels?): STBImage {
			return genericLoad(buffer, desiredChannels, ::stbi_loadf_from_memory) {
				MemoryUtil.memByteBuffer(MemoryUtil.memAddress(it), it.capacity() * Int.SIZE_BYTES)
			}
		}

		actual fun loadf(callbacks: STBIOCallbacks, desiredChannels: Channels?): STBImage {
			return genericLoad(callbacks, desiredChannels, ::stbi_loadf_from_callbacks) {
				MemoryUtil.memByteBuffer(MemoryUtil.memAddress(it), it.capacity() * Int.SIZE_BYTES)
			}
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
			MemoryStack.stackPush()
			try {
				return usingNativeCallbacks(callbacks, ::stbi_is_hdr_from_callbacks)
			} finally {
				MemoryStack.stackPop()
			}
		}

		actual fun isHdr(buffer: Memory): Boolean {
			return stbi_is_hdr_from_memory(buffer.buffer)
		}

		actual val failureReason: String? get() = stbi_failure_reason()

		actual fun loadInfo(buffer: Memory): STBInfo {
			MemoryStack.stackPush()
			try {
				val x = MemoryStack.stackMallocInt(1)
				val y = MemoryStack.stackMallocInt(1)
				val channels = MemoryStack.stackMallocInt(1)

				val result = stbi_info_from_memory(buffer.buffer, x, y, channels)

				return if (result) {
					STBInfo(x[0], y[0], Channels.values()[channels[0] - 1])
				} else {
					throw STBException(failureReason)
				}
			} finally {
				MemoryStack.stackPop()
			}
		}

		actual fun loadInfo(callbacks: STBIOCallbacks): STBInfo {
			MemoryStack.stackPush()
			try {
				val x = MemoryStack.stackMallocInt(1)
				val y = MemoryStack.stackMallocInt(1)
				val channels = MemoryStack.stackMallocInt(1)

				return usingNativeCallbacks(callbacks) { nativeCallbacks, userPtr ->
					val result = stbi_info_from_callbacks(nativeCallbacks, userPtr, x, y, channels)

					if (result) {
						STBInfo(x[0], y[0], Channels.values()[channels[0] - 1])
					} else {
						throw STBException(failureReason)
					}
				}
			} finally {
				MemoryStack.stackPop()
			}
		}

		actual fun is16Bit(callbacks: STBIOCallbacks): Boolean {
			MemoryStack.stackPush()
			try {
				return usingNativeCallbacks(callbacks, ::stbi_is_16_bit_from_callbacks)
			} finally {
				MemoryStack.stackPop()
			}
		}

		actual fun is16Bit(buffer: Memory): Boolean {
			return stbi_is_16_bit_from_memory(buffer.buffer)
		}

		actual fun setUnpremultiplyOnLoad(flag: Boolean) {
			stbi_set_unpremultiply_on_load(flag)
		}

		actual fun setConvertIphonePngToRgb(flag: Boolean) {
			stbi_convert_iphone_png_to_rgb(flag)
		}

		actual fun setFlipVerticallyOnLoad(value: Boolean) {
			stbi_set_flip_vertically_on_load(value)
		}
	}
}
