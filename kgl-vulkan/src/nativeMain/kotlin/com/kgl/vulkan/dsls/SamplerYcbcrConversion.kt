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
import com.kgl.vulkan.utils.toBoolean
import com.kgl.vulkan.utils.toVkType
import cvulkan.VkSamplerYcbcrConversionCreateInfo

actual class SamplerYcbcrConversionCreateInfoBuilder(internal val target: VkSamplerYcbcrConversionCreateInfo) {
	actual var format: Format?
		get() = Format.from(target.format)
		set(value) {
			target.format = value.toVkType()
		}

	actual var ycbcrModel: SamplerYcbcrModelConversion?
		get() = SamplerYcbcrModelConversion.from(target.ycbcrModel)
		set(value) {
			target.ycbcrModel = value.toVkType()
		}

	actual var ycbcrRange: SamplerYcbcrRange?
		get() = SamplerYcbcrRange.from(target.ycbcrRange)
		set(value) {
			target.ycbcrRange = value.toVkType()
		}

	actual var xChromaOffset: ChromaLocation?
		get() = ChromaLocation.from(target.xChromaOffset)
		set(value) {
			target.xChromaOffset = value.toVkType()
		}

	actual var yChromaOffset: ChromaLocation?
		get() = ChromaLocation.from(target.yChromaOffset)
		set(value) {
			target.yChromaOffset = value.toVkType()
		}

	actual var chromaFilter: Filter?
		get() = Filter.from(target.chromaFilter)
		set(value) {
			target.chromaFilter = value.toVkType()
		}

	actual var forceExplicitReconstruction: Boolean
		get() = target.forceExplicitReconstruction.toBoolean()
		set(value) {
			target.forceExplicitReconstruction = value.toVkType()
		}

	actual fun components(block: ComponentMappingBuilder.() -> Unit) {
		val subTarget = target.components
		val builder = ComponentMappingBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SAMPLER_YCBCR_CONVERSION_CREATE_INFO
		target.pNext = null
	}
}

