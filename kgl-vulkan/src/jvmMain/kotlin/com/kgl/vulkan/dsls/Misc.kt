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
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.*

actual class ImageSubresourceBuilder(internal val target: VkImageSubresource) {
	actual var aspectMask: VkFlag<ImageAspect>?
		get() = ImageAspect.fromMultiple(target.aspectMask())
		set(value) {
			target.aspectMask(value.toVkType())
		}

	actual var mipLevel: UInt
		get() = target.mipLevel().toUInt()
		set(value) {
			target.mipLevel(value.toVkType())
		}

	actual var arrayLayer: UInt
		get() = target.arrayLayer().toUInt()
		set(value) {
			target.arrayLayer(value.toVkType())
		}

	internal fun init() {
	}
}

actual class Offset3DBuilder(internal val target: VkOffset3D) {
	internal fun init(x: Int, y: Int, z: Int) {
		target.x(x.toVkType())
		target.y(y.toVkType())
		target.z(z.toVkType())
	}
}

actual class Extent3DBuilder(internal val target: VkExtent3D) {
	internal fun init(width: UInt, height: UInt, depth: UInt) {
		target.width(width.toVkType())
		target.height(height.toVkType())
		target.depth(depth.toVkType())
	}
}

actual class ComponentMappingBuilder(internal val target: VkComponentMapping) {
	actual var r: ComponentSwizzle?
		get() = ComponentSwizzle.from(target.r())
		set(value) {
			target.r(value.toVkType())
		}

	actual var g: ComponentSwizzle?
		get() = ComponentSwizzle.from(target.g())
		set(value) {
			target.g(value.toVkType())
		}

	actual var b: ComponentSwizzle?
		get() = ComponentSwizzle.from(target.b())
		set(value) {
			target.b(value.toVkType())
		}

	actual var a: ComponentSwizzle?
		get() = ComponentSwizzle.from(target.a())
		set(value) {
			target.a(value.toVkType())
		}

	internal fun init() {
	}
}

actual class ViewportBuilder(internal val target: VkViewport) {
	actual var x: Float
		get() = target.x()
		set(value) {
			target.x(value.toVkType())
		}

	actual var y: Float
		get() = target.y()
		set(value) {
			target.y(value.toVkType())
		}

	actual var width: Float
		get() = target.width()
		set(value) {
			target.width(value.toVkType())
		}

	actual var height: Float
		get() = target.height()
		set(value) {
			target.height(value.toVkType())
		}

	actual var minDepth: Float
		get() = target.minDepth()
		set(value) {
			target.minDepth(value.toVkType())
		}

	actual var maxDepth: Float
		get() = target.maxDepth()
		set(value) {
			target.maxDepth(value.toVkType())
		}

	internal fun init() {
	}
}

actual class Extent2DBuilder(internal val target: VkExtent2D) {
	internal fun init(width: UInt, height: UInt) {
		target.width(width.toVkType())
		target.height(height.toVkType())
	}
}

actual class Rect2DBuilder(internal val target: VkRect2D) {
	actual fun offset(x: Int, y: Int) {
		val subTarget = target.offset()
		val builder = Offset2DBuilder(subTarget)
		builder.init(x, y)
	}

	actual fun extent(width: UInt, height: UInt) {
		val subTarget = target.extent()
		val builder = Extent2DBuilder(subTarget)
		builder.init(width, height)
	}

	internal fun init() {
	}
}

actual class DescriptorSetLayoutCreateInfoBuilder(internal val target: VkDescriptorSetLayoutCreateInfo) {
	actual var flags: VkFlag<DescriptorSetLayoutCreate>?
		get() = DescriptorSetLayoutCreate.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual fun bindings(block: DescriptorSetLayoutBindingsBuilder.() -> Unit) {
		val targets = DescriptorSetLayoutBindingsBuilder().apply(block).targets
		target.pBindings(targets.mapToStackArray(VkDescriptorSetLayoutBinding::callocStack))
	}

	internal fun init() {
		target.sType(VK11.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
		target.pNext(0)
	}
}

actual class DebugUtilsObjectNameInfoEXTBuilder(internal val target: VkDebugUtilsObjectNameInfoEXT) {
	actual var objectType: ObjectType?
		get() = ObjectType.from(target.objectType())
		set(value) {
			target.objectType(value.toVkType())
		}

	actual var objectHandle: ULong
		get() = target.objectHandle().toULong()
		set(value) {
			target.objectHandle(value.toVkType())
		}

	actual var objectName: String?
		get() = target.pObjectNameString()
		set(value) {
			target.pObjectName(value.toVkType())
		}

	internal fun init() {
		target.sType(EXTDebugUtils.VK_STRUCTURE_TYPE_DEBUG_UTILS_OBJECT_NAME_INFO_EXT)
		target.pNext(0)
	}
}

actual class DebugUtilsLabelEXTBuilder(internal val target: VkDebugUtilsLabelEXT) {
	actual var labelName: String?
		get() = target.pLabelNameString()
		set(value) {
			target.pLabelName(value.toVkType())
		}

	actual fun color(arg0: Float, arg1: Float, arg2: Float, arg3: Float) {
		target.color(0, arg0.toVkType())
		target.color(1, arg1.toVkType())
		target.color(2, arg2.toVkType())
		target.color(3, arg3.toVkType())
	}

	internal fun init() {
		target.sType(EXTDebugUtils.VK_STRUCTURE_TYPE_DEBUG_UTILS_LABEL_EXT)
		target.pNext(0)
	}
}

actual class AccelerationStructureInfoNVBuilder(internal val target: VkAccelerationStructureInfoNV) {
	actual var type: AccelerationStructureTypeNV?
		get() = AccelerationStructureTypeNV.from(target.type())
		set(value) {
			target.type(value.toVkType())
		}

	actual var flags: VkFlag<BuildAccelerationStructureNV>?
		get() = BuildAccelerationStructureNV.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual var instanceCount: UInt
		get() = target.instanceCount().toUInt()
		set(value) {
			target.instanceCount(value.toVkType())
		}

	actual fun geometries(block: GeometryNVsBuilder.() -> Unit) {
		val targets = GeometryNVsBuilder().apply(block).targets
		target.pGeometries(targets.mapToStackArray(VkGeometryNV::callocStack))
	}

	internal fun init() {
		target.sType(NVRayTracing.VK_STRUCTURE_TYPE_ACCELERATION_STRUCTURE_INFO_NV)
		target.pNext(0)
	}
}

