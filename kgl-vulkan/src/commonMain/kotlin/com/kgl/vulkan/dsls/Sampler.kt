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

expect class SamplerCreateInfoBuilder {
	var magFilter: Filter?

	var minFilter: Filter?

	var mipmapMode: SamplerMipmapMode?

	var addressModeU: SamplerAddressMode?

	var addressModeV: SamplerAddressMode?

	var addressModeW: SamplerAddressMode?

	var mipLodBias: Float

	var anisotropyEnable: Boolean

	var maxAnisotropy: Float

	var compareEnable: Boolean

	var compareOp: CompareOp?

	var minLod: Float

	var maxLod: Float

	var borderColor: BorderColor?

	var unnormalizedCoordinates: Boolean
}

