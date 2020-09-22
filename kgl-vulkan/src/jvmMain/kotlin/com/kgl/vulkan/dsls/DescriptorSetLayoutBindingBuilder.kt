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

import com.kgl.vulkan.enums.*
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.*
import org.lwjgl.vulkan.*

actual class DescriptorSetLayoutBindingBuilder(internal val target: VkDescriptorSetLayoutBinding) {
	actual var binding: UInt
		get() = target.binding().toUInt()
		set(value) {
			target.binding(value.toVkType())
		}

	actual var descriptorType: DescriptorType?
		get() = DescriptorType.from(target.descriptorType())
		set(value) {
			target.descriptorType(value.toVkType())
		}

	actual var stageFlags: VkFlag<ShaderStage>?
		get() = ShaderStage.fromMultiple(target.stageFlags())
		set(value) {
			target.stageFlags(value.toVkType())
		}

	actual fun next(block: Next<DescriptorSetLayoutBindingBuilder>.() -> Unit) {
		Next(this).apply(block)
	}

	internal actual fun init(immutableSamplers: Collection<Sampler>) {
		target.pImmutableSamplers(immutableSamplers.toVkType())
	}

	internal actual fun init(descriptorCount: UInt) {
		target.pImmutableSamplers(null)
		target.descriptorCount(descriptorCount.toInt())
	}
}
