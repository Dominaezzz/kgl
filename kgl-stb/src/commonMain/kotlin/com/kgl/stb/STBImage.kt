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

import com.kgl.core.DirectMemory
import com.kgl.core.Closeable

expect class STBImage : Closeable {
	val buffer: DirectMemory
	val info: STBInfo

	companion object {
		fun load(buffer: DirectMemory, desiredChannels: Channels? = null): STBImage
		fun load(callbacks: STBIOCallbacks, desiredChannels: Channels? = null): STBImage
		// fun loadGif(buffer: ByteBuffer, desiredChannels: Channels? = null): STBImage

		fun load16(buffer: DirectMemory, desiredChannels: Channels? = null): STBImage
		fun load16(callbacks: STBIOCallbacks, desiredChannels: Channels? = null): STBImage

		fun loadf(buffer: DirectMemory, desiredChannels: Channels? = null): STBImage
		fun loadf(callbacks: STBIOCallbacks, desiredChannels: Channels? = null): STBImage

		fun setHdrToLdrGamma(gamma: Float)
		fun setHdrToLdrScale(scale: Float)

		fun setLdrToHdrGamma(gamma: Float)
		fun setLdrToHdrScale(scale: Float)

		fun isHdr(callbacks: STBIOCallbacks): Boolean
		fun isHdr(buffer: DirectMemory): Boolean

		val failureReason: String?

		fun loadInfo(buffer: DirectMemory): STBInfo
		fun loadInfo(callbacks: STBIOCallbacks): STBInfo

		fun is16Bit(callbacks: STBIOCallbacks): Boolean
		fun is16Bit(buffer: DirectMemory): Boolean

		/**
		 * For image formats that explicitly notate that they have premultiplied alpha,
		 * we just return the colors as stored in the file. set this flag to force
		 * unpremultiplication. results are undefined if the unpremultiply overflow.
		 */
		fun setUnpremultiplyOnLoad(flag: Boolean)

		/**
		 * indicate whether we should process iphone images back to canonical format,
		 * or just pass them through "as-is"
		 */
		fun setConvertIphonePngToRgb(flag: Boolean)

		/**
		 * flip the image vertically, so the first pixel in the output array is the bottom left
		 */
		fun setFlipVerticallyOnLoad(value: Boolean)
	}
}
