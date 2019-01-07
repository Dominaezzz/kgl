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

import com.kgl.vulkan.enums.Format
import com.kgl.vulkan.enums.ImageAspect
import com.kgl.vulkan.enums.ImageViewType
import com.kgl.vulkan.handles.Image
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.VK11
import org.lwjgl.vulkan.VkImageSubresourceRange
import org.lwjgl.vulkan.VkImageViewCreateInfo

actual class ImageSubresourceRangeBuilder(internal val target: VkImageSubresourceRange) {
	actual var aspectMask: VkFlag<ImageAspect>?
		get() = ImageAspect.fromMultiple(target.aspectMask())
		set(value) {
			target.aspectMask(value.toVkType())
		}

	actual var baseMipLevel: UInt
		get() = target.baseMipLevel().toUInt()
		set(value) {
			target.baseMipLevel(value.toVkType())
		}

	actual var levelCount: UInt
		get() = target.levelCount().toUInt()
		set(value) {
			target.levelCount(value.toVkType())
		}

	actual var baseArrayLayer: UInt
		get() = target.baseArrayLayer().toUInt()
		set(value) {
			target.baseArrayLayer(value.toVkType())
		}

	actual var layerCount: UInt
		get() = target.layerCount().toUInt()
		set(value) {
			target.layerCount(value.toVkType())
		}

	internal fun init() {
	}
}

actual class ImageViewCreateInfoBuilder(internal val target: VkImageViewCreateInfo) {
	actual fun components(block: ComponentMappingBuilder.() -> Unit) {
		val subTarget = target.components()
		val builder = ComponentMappingBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun subresourceRange(block: ImageSubresourceRangeBuilder.() -> Unit) {
		val subTarget = target.subresourceRange()
		val builder = ImageSubresourceRangeBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init(image: Image, viewType: ImageViewType, format: Format) {
		target.sType(VK11.VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO)
		target.pNext(0)
		target.flags(0)
		target.image(image.toVkType())
		target.viewType(viewType.toVkType())
		target.format(format.toVkType())
	}
}

