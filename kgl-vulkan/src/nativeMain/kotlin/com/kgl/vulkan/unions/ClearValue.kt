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
package com.kgl.vulkan.unions

import cvulkan.VkClearValue
import kotlinx.cinterop.set

actual sealed class ClearValue {
	abstract fun write(ptr: VkClearValue)
}

actual data class ClearDepthStencil actual constructor(actual val depth: Float, actual val stencil: UInt) : ClearValue() {
	override fun write(ptr: VkClearValue) {
		ptr.depthStencil.depth = depth
		ptr.depthStencil.stencil = stencil
	}
}

actual data class ClearColorF actual constructor(actual val red: Float, actual val green: Float, actual val blue: Float, actual val alpha: Float) : ClearValue() {
	override fun write(ptr: VkClearValue) {
		ptr.color.float32[0] = red
		ptr.color.float32[1] = green
		ptr.color.float32[2] = blue
		ptr.color.float32[3] = alpha
	}
}
