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
import com.kgl.vulkan.handles.AccelerationStructureNV
import com.kgl.vulkan.handles.Buffer
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.toVkType
import cvulkan.*

actual class GeometryTrianglesNVBuilder(internal val target: VkGeometryTrianglesNV) {
	actual var vertexOffset: ULong
		get() = target.vertexOffset
		set(value) {
			target.vertexOffset = value.toVkType()
		}

	actual var vertexCount: UInt
		get() = target.vertexCount
		set(value) {
			target.vertexCount = value.toVkType()
		}

	actual var vertexStride: ULong
		get() = target.vertexStride
		set(value) {
			target.vertexStride = value.toVkType()
		}

	actual var vertexFormat: Format?
		get() = Format.from(target.vertexFormat)
		set(value) {
			target.vertexFormat = value.toVkType()
		}

	actual var indexOffset: ULong
		get() = target.indexOffset
		set(value) {
			target.indexOffset = value.toVkType()
		}

	actual var indexCount: UInt
		get() = target.indexCount
		set(value) {
			target.indexCount = value.toVkType()
		}

	actual var indexType: IndexType?
		get() = IndexType.from(target.indexType)
		set(value) {
			target.indexType = value.toVkType()
		}

	actual var transformOffset: ULong
		get() = target.transformOffset
		set(value) {
			target.transformOffset = value.toVkType()
		}

	internal fun init(
			vertexData: Buffer?,
			indexData: Buffer?,
			transformData: Buffer?
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_GEOMETRY_TRIANGLES_NV
		target.pNext = null
		target.vertexData = vertexData?.toVkType()
		target.indexData = indexData?.toVkType()
		target.transformData = transformData?.toVkType()
	}
}

actual class GeometryAABBNVBuilder(internal val target: VkGeometryAABBNV) {
	actual var numAABBs: UInt
		get() = target.numAABBs
		set(value) {
			target.numAABBs = value.toVkType()
		}

	actual var stride: UInt
		get() = target.stride
		set(value) {
			target.stride = value.toVkType()
		}

	actual var offset: ULong
		get() = target.offset
		set(value) {
			target.offset = value.toVkType()
		}

	internal fun init(aabbData: Buffer?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_GEOMETRY_AABB_NV
		target.pNext = null
		target.aabbData = aabbData?.toVkType()
	}
}

actual class GeometryDataNVBuilder(internal val target: VkGeometryDataNV) {
	actual fun triangles(
			vertexData: Buffer?,
			indexData: Buffer?,
			transformData: Buffer?,
			block: GeometryTrianglesNVBuilder.() -> Unit
	) {
		val subTarget = target.triangles
		val builder = GeometryTrianglesNVBuilder(subTarget)
		builder.init(vertexData, indexData, transformData)
		builder.apply(block)
	}

	actual fun aabbs(aabbData: Buffer?, block: GeometryAABBNVBuilder.() -> Unit) {
		val subTarget = target.aabbs
		val builder = GeometryAABBNVBuilder(subTarget)
		builder.init(aabbData)
		builder.apply(block)
	}

	internal fun init() {
	}
}

actual class GeometryNVBuilder(internal val target: VkGeometryNV) {
	actual var geometryType: GeometryTypeNV?
		get() = GeometryTypeNV.from(target.geometryType)
		set(value) {
			target.geometryType = value.toVkType()
		}

	actual var flags: VkFlag<GeometryNV>?
		get() = GeometryNV.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual fun geometry(block: GeometryDataNVBuilder.() -> Unit) {
		val subTarget = target.geometry
		val builder = GeometryDataNVBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_GEOMETRY_NV
		target.pNext = null
	}
}

actual class GeometryNVsBuilder {
	val targets: MutableList<(VkGeometryNV) -> Unit> = mutableListOf()

	actual fun geometry(block: GeometryNVBuilder.() -> Unit) {
		targets += {
			val builder = GeometryNVBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class AccelerationStructureCreateInfoNVBuilder(internal val target: VkAccelerationStructureCreateInfoNV) {
	actual var compactedSize: ULong
		get() = target.compactedSize
		set(value) {
			target.compactedSize = value.toVkType()
		}

	actual fun info(block: AccelerationStructureInfoNVBuilder.() -> Unit) {
		val subTarget = target.info
		val builder = AccelerationStructureInfoNVBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_ACCELERATION_STRUCTURE_CREATE_INFO_NV
		target.pNext = null
	}
}

actual class AccelerationStructureMemoryRequirementsInfoNVBuilder(internal val target: VkAccelerationStructureMemoryRequirementsInfoNV) {
	internal fun init(type: AccelerationStructureMemoryRequirementsTypeNV, accelerationStructure: AccelerationStructureNV) {
		target.sType = VK_STRUCTURE_TYPE_ACCELERATION_STRUCTURE_MEMORY_REQUIREMENTS_INFO_NV
		target.pNext = null

		target.type = type.toVkType()
		target.accelerationStructure = accelerationStructure.ptr
	}
}

