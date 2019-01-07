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
import com.kgl.vulkan.utils.VkFlag

expect class DeviceQueueCreateInfoBuilder {
	var flags: VkFlag<DeviceQueueCreate>?
}

expect class DeviceQueueCreateInfosBuilder {
	fun queue(
			queueFamilyIndex: UInt,
			vararg queuePriorities: Float,
			block: DeviceQueueCreateInfoBuilder.() -> Unit = {}
	)
}

expect class PhysicalDeviceFeaturesBuilder {
	var robustBufferAccess: Boolean

	var fullDrawIndexUint32: Boolean

	var imageCubeArray: Boolean

	var independentBlend: Boolean

	var geometryShader: Boolean

	var tessellationShader: Boolean

	var sampleRateShading: Boolean

	var dualSrcBlend: Boolean

	var logicOp: Boolean

	var multiDrawIndirect: Boolean

	var drawIndirectFirstInstance: Boolean

	var depthClamp: Boolean

	var depthBiasClamp: Boolean

	var fillModeNonSolid: Boolean

	var depthBounds: Boolean

	var wideLines: Boolean

	var largePoints: Boolean

	var alphaToOne: Boolean

	var multiViewport: Boolean

	var samplerAnisotropy: Boolean

	var textureCompressionETC2: Boolean

	var textureCompressionASTC_LDR: Boolean

	var textureCompressionBC: Boolean

	var occlusionQueryPrecise: Boolean

	var pipelineStatisticsQuery: Boolean

	var vertexPipelineStoresAndAtomics: Boolean

	var fragmentStoresAndAtomics: Boolean

	var shaderTessellationAndGeometryPointSize: Boolean

	var shaderImageGatherExtended: Boolean

	var shaderStorageImageExtendedFormats: Boolean

	var shaderStorageImageMultisample: Boolean

	var shaderStorageImageReadWithoutFormat: Boolean

	var shaderStorageImageWriteWithoutFormat: Boolean

	var shaderUniformBufferArrayDynamicIndexing: Boolean

	var shaderSampledImageArrayDynamicIndexing: Boolean

	var shaderStorageBufferArrayDynamicIndexing: Boolean

	var shaderStorageImageArrayDynamicIndexing: Boolean

	var shaderClipDistance: Boolean

	var shaderCullDistance: Boolean

	var shaderFloat64: Boolean

	var shaderInt64: Boolean

	var shaderInt16: Boolean

	var shaderResourceResidency: Boolean

	var shaderResourceMinLod: Boolean

	var sparseBinding: Boolean

	var sparseResidencyBuffer: Boolean

	var sparseResidencyImage2D: Boolean

	var sparseResidencyImage3D: Boolean

	var sparseResidency2Samples: Boolean

	var sparseResidency4Samples: Boolean

	var sparseResidency8Samples: Boolean

	var sparseResidency16Samples: Boolean

	var sparseResidencyAliased: Boolean

	var variableMultisampleRate: Boolean

	var inheritedQueries: Boolean
}

expect class DeviceCreateInfoBuilder {
	fun queues(block: DeviceQueueCreateInfosBuilder.() -> Unit)

	fun enabledFeatures(block: PhysicalDeviceFeaturesBuilder.() -> Unit = {})
}

expect class MemoryAllocateInfoBuilder {
	var allocationSize: ULong

	var memoryTypeIndex: UInt
}

expect class MappedMemoryRangeBuilder

expect class FlushMappedMemoryRangesBuilder {
	fun range(
			memory: DeviceMemory,
			offset: ULong,
			size: ULong,
			block: MappedMemoryRangeBuilder.() -> Unit = {}
	)
}

expect class InvalidateMappedMemoryRangesBuilder {
	fun range(
			memory: DeviceMemory,
			offset: ULong,
			size: ULong,
			block: MappedMemoryRangeBuilder.() -> Unit = {}
	)
}

expect class DebugMarkerObjectNameInfoEXTBuilder {
	var objectType: DebugReportObjectTypeEXT?

	var `object`: ULong

	var objectName: String?
}

expect class DebugMarkerObjectTagInfoEXTBuilder {
	var objectType: DebugReportObjectTypeEXT?

	var `object`: ULong

	var tagName: ULong
}

expect class DisplayPowerInfoEXTBuilder {
	var powerState: DisplayPowerStateEXT?
}

expect class DeviceEventInfoEXTBuilder {
	var deviceEvent: DeviceEventTypeEXT?
}

expect class DisplayEventInfoEXTBuilder {
	var displayEvent: DisplayEventTypeEXT?
}

expect class BindBufferMemoryInfoBuilder {
	var memoryOffset: ULong
}

expect class BindBufferMemory2Builder {
	fun info(
			buffer: Buffer,
			memory: DeviceMemory,
			block: BindBufferMemoryInfoBuilder.() -> Unit = {}
	)
}

expect class BindImageMemoryInfoBuilder {
	var memoryOffset: ULong
}

expect class BindImageMemory2Builder {
	fun info(image: Image, memory: DeviceMemory, block: BindImageMemoryInfoBuilder.() -> Unit = {})
}

expect class XYColorEXTBuilder {
	var x: Float

	var y: Float
}

expect class HdrMetadataEXTBuilder {
	var maxLuminance: Float

	var minLuminance: Float

	var maxContentLightLevel: Float

	var maxFrameAverageLightLevel: Float

	fun displayPrimaryRed(block: XYColorEXTBuilder.() -> Unit = {})

	fun displayPrimaryGreen(block: XYColorEXTBuilder.() -> Unit = {})

	fun displayPrimaryBlue(block: XYColorEXTBuilder.() -> Unit = {})

	fun whitePoint(block: XYColorEXTBuilder.() -> Unit = {})
}

expect class SetHdrMetadataEXTBuilder {
	fun metadata(block: HdrMetadataEXTBuilder.() -> Unit)
}

expect class DeviceQueueInfo2Builder {
	var flags: VkFlag<DeviceQueueCreate>?

	var queueFamilyIndex: UInt

	var queueIndex: UInt
}

expect class DebugUtilsObjectTagInfoEXTBuilder {
	var objectType: ObjectType?

	var objectHandle: ULong

	var tagName: ULong
}

expect class BindAccelerationStructureMemoryInfoNVBuilder {
	var memoryOffset: ULong
}

expect class BindAccelerationStructureMemoryNVBuilder {
	fun info(
			accelerationStructure: AccelerationStructureNV,
			memory: DeviceMemory,
			deviceIndices: UIntArray,
			block: BindAccelerationStructureMemoryInfoNVBuilder.() -> Unit = {}
	)
}


expect class UpdateDescriptorSetsBuilder {
	fun writes(dstSet: DescriptorSet, texelBufferView: Collection<BufferView>? = null, block: WriteDescriptorSetBuilder.() -> Unit)

	fun copies(srcSet: DescriptorSet, dstSet: DescriptorSet, block: CopyDescriptorSetBuilder.() -> Unit = {})
}

expect class CopyDescriptorSetBuilder {
	var srcBinding: UInt

	var srcArrayElement: UInt

	var dstBinding: UInt

	var dstArrayElement: UInt

	var descriptorCount: UInt
}

