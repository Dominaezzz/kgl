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

import org.lwjgl.vulkan.*

actual class ClearColorValueBuilder(internal val target: VkClearColorValue) {
	internal actual fun init(r: Float, g: Float, b: Float, a: Float) {
		target.float32(0, r)
		target.float32(1, g)
		target.float32(2, b)
		target.float32(3, a)
	}

	internal actual fun init(r: Int, g: Int, b: Int, a: Int) {
		target.int32(0, r)
		target.int32(1, g)
		target.int32(2, b)
		target.int32(3, a)
	}

	internal actual fun init(r: UInt, g: UInt, b: UInt, a: UInt) {
		target.uint32(0, r.toInt())
		target.uint32(1, g.toInt())
		target.uint32(2, b.toInt())
		target.uint32(3, a.toInt())
	}
}
