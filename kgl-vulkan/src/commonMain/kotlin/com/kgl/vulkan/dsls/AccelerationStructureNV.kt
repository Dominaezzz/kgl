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
import com.kgl.vulkan.enums.GeometryNV
import com.kgl.vulkan.enums.GeometryTypeNV
import com.kgl.vulkan.enums.IndexType
import com.kgl.vulkan.handles.Buffer
import com.kgl.vulkan.utils.VkFlag

expect class GeometryTrianglesNVBuilder {
	var vertexOffset: ULong

	var vertexCount: UInt

	var vertexStride: ULong

	var vertexFormat: Format?

	var indexOffset: ULong

	var indexCount: UInt

	var indexType: IndexType?

	var transformOffset: ULong
}

expect class GeometryAABBNVBuilder {
	var numAABBs: UInt

	var stride: UInt

	var offset: ULong
}

expect class GeometryDataNVBuilder {
	fun triangles(
			vertexData: Buffer?,
			indexData: Buffer?,
			transformData: Buffer?,
			block: GeometryTrianglesNVBuilder.() -> Unit = {}
	)

	fun aabbs(aabbData: Buffer?, block: GeometryAABBNVBuilder.() -> Unit = {})
}

expect class GeometryNVBuilder {
	var geometryType: GeometryTypeNV?

	var flags: VkFlag<GeometryNV>?

	fun geometry(block: GeometryDataNVBuilder.() -> Unit)
}

expect class GeometryNVsBuilder {
	fun geometry(block: GeometryNVBuilder.() -> Unit)
}

expect class AccelerationStructureCreateInfoNVBuilder {
	var compactedSize: ULong

	fun info(block: AccelerationStructureInfoNVBuilder.() -> Unit)
}

expect class AccelerationStructureMemoryRequirementsInfoNVBuilder

