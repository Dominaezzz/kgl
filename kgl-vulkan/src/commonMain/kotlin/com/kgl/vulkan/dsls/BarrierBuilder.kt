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
package com.kgl.vulkan.dsls

import com.kgl.vulkan.handles.Buffer
import com.kgl.vulkan.handles.Image
import com.kgl.vulkan.utils.StructMarker

@StructMarker
class BarrierBuilder {
	val targets: MutableList<(MemoryBarrierBuilder) -> Unit> = mutableListOf()
	val targets1: MutableList<(BufferMemoryBarrierBuilder) -> Unit> = mutableListOf()
	val targets2: MutableList<(ImageMemoryBarrierBuilder) -> Unit> = mutableListOf()

	fun memoryBarrier(block: MemoryBarrierBuilder.() -> Unit = {}) {
		targets += {
			it.init()
			it.apply(block)
		}
	}

	fun bufferMemoryBarrier(
			buffer: Buffer,
			offset: ULong,
			size: ULong,
			block: BufferMemoryBarrierBuilder.() -> Unit = {}
	) {
		targets1 += {
			it.init(buffer, offset, size)
			it.apply(block)
		}
	}

	fun imageMemoryBarrier(image: Image, block: ImageMemoryBarrierBuilder.() -> Unit) {
		targets2 += {
			it.init(image)
			it.apply(block)
		}
	}
}
