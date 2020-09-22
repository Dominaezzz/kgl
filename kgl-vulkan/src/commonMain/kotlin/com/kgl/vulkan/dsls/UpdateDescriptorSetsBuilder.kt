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

import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.*

@StructMarker
class UpdateDescriptorSetsBuilder {
	internal val targets0: MutableList<(WriteDescriptorSetBuilder) -> Unit> = mutableListOf()
	internal val targets1: MutableList<(CopyDescriptorSetBuilder) -> Unit> = mutableListOf()

	fun write(
		dstSet: DescriptorSet,
		texelBufferView: Collection<BufferView>? = null,
		block: WriteDescriptorSetBuilder.() -> Unit
	) {
		targets0 += {
			it.init(dstSet, texelBufferView)
			it.apply(block)
		}
	}

	fun copy(srcSet: DescriptorSet, dstSet: DescriptorSet, block: CopyDescriptorSetBuilder.() -> Unit = {}) {
		targets1 += {
			it.init(srcSet, dstSet)
			it.apply(block)
		}
	}
}
