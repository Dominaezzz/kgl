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
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.VK11
import org.lwjgl.vulkan.VkSamplerCreateInfo

actual class SamplerCreateInfoBuilder(internal val target: VkSamplerCreateInfo) {
	actual var magFilter: Filter?
		get() = Filter.from(target.magFilter())
		set(value) {
			target.magFilter(value.toVkType())
		}

	actual var minFilter: Filter?
		get() = Filter.from(target.minFilter())
		set(value) {
			target.minFilter(value.toVkType())
		}

	actual var mipmapMode: SamplerMipmapMode?
		get() = SamplerMipmapMode.from(target.mipmapMode())
		set(value) {
			target.mipmapMode(value.toVkType())
		}

	actual var addressModeU: SamplerAddressMode?
		get() = SamplerAddressMode.from(target.addressModeU())
		set(value) {
			target.addressModeU(value.toVkType())
		}

	actual var addressModeV: SamplerAddressMode?
		get() = SamplerAddressMode.from(target.addressModeV())
		set(value) {
			target.addressModeV(value.toVkType())
		}

	actual var addressModeW: SamplerAddressMode?
		get() = SamplerAddressMode.from(target.addressModeW())
		set(value) {
			target.addressModeW(value.toVkType())
		}

	actual var mipLodBias: Float
		get() = target.mipLodBias()
		set(value) {
			target.mipLodBias(value.toVkType())
		}

	actual var anisotropyEnable: Boolean
		get() = target.anisotropyEnable()
		set(value) {
			target.anisotropyEnable(value.toVkType())
		}

	actual var maxAnisotropy: Float
		get() = target.maxAnisotropy()
		set(value) {
			target.maxAnisotropy(value.toVkType())
		}

	actual var compareEnable: Boolean
		get() = target.compareEnable()
		set(value) {
			target.compareEnable(value.toVkType())
		}

	actual var compareOp: CompareOp?
		get() = CompareOp.from(target.compareOp())
		set(value) {
			target.compareOp(value.toVkType())
		}

	actual var minLod: Float
		get() = target.minLod()
		set(value) {
			target.minLod(value.toVkType())
		}

	actual var maxLod: Float
		get() = target.maxLod()
		set(value) {
			target.maxLod(value.toVkType())
		}

	actual var borderColor: BorderColor?
		get() = BorderColor.from(target.borderColor())
		set(value) {
			target.borderColor(value.toVkType())
		}

	actual var unnormalizedCoordinates: Boolean
		get() = target.unnormalizedCoordinates()
		set(value) {
			target.unnormalizedCoordinates(value.toVkType())
		}

	internal fun init() {
		target.sType(VK11.VK_STRUCTURE_TYPE_SAMPLER_CREATE_INFO)
		target.pNext(0)
		target.flags(0)
	}
}

