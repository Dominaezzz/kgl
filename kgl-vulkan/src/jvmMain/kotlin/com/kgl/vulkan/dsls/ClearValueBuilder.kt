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

import org.lwjgl.vulkan.VkClearValue

actual class ClearValueBuilder(internal val target: VkClearValue) {
	actual fun color(r: Float, g: Float, b: Float, a: Float) {
		val subTarget = target.color()
		val builder = ClearColorValueBuilder(subTarget)
		builder.init(r, g, b, a)
	}

	actual fun color(r: Int, g: Int, b: Int, a: Int) {
		val subTarget = target.color()
		val builder = ClearColorValueBuilder(subTarget)
		builder.init(r, g, b, a)
	}

	actual fun color(r: UInt, g: UInt, b: UInt, a: UInt) {
		val subTarget = target.color()
		val builder = ClearColorValueBuilder(subTarget)
		builder.init(r, g, b, a)
	}

	actual fun depthStencil(depth: Float, stencil: UInt) {
		val subTarget = target.depthStencil()
		val builder = ClearDepthStencilValueBuilder(subTarget)
		builder.init(depth, stencil)
	}

	internal actual fun init() {
	}
}
