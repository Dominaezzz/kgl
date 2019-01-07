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
import com.kgl.vulkan.utils.VkFlag

expect class ImageSubresourceBuilder {
	var aspectMask: VkFlag<ImageAspect>?

	var mipLevel: UInt

	var arrayLayer: UInt
}

expect class Offset3DBuilder

expect class Extent3DBuilder

expect class ComponentMappingBuilder {
	var r: ComponentSwizzle?

	var g: ComponentSwizzle?

	var b: ComponentSwizzle?

	var a: ComponentSwizzle?
}

expect class ViewportBuilder {
	var x: Float

	var y: Float

	var width: Float

	var height: Float

	var minDepth: Float

	var maxDepth: Float
}

expect class Extent2DBuilder

expect class Rect2DBuilder {
	fun offset(x: Int, y: Int)

	fun extent(width: UInt, height: UInt)
}

expect class DescriptorSetLayoutCreateInfoBuilder {
	var flags: VkFlag<DescriptorSetLayoutCreate>?

	fun bindings(block: DescriptorSetLayoutBindingsBuilder.() -> Unit)
}

expect class DebugUtilsObjectNameInfoEXTBuilder {
	var objectType: ObjectType?

	var objectHandle: ULong

	var objectName: String?
}

expect class DebugUtilsLabelEXTBuilder {
	var labelName: String?

	fun color(arg0: Float, arg1: Float, arg2: Float, arg3: Float)
}

expect class AccelerationStructureInfoNVBuilder {
	var type: AccelerationStructureTypeNV?

	var flags: VkFlag<BuildAccelerationStructureNV>?

	var instanceCount: UInt

	fun geometries(block: GeometryNVsBuilder.() -> Unit)
}

