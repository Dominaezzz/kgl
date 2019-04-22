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

import com.kgl.core.VirtualStack
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.io.core.IoBuffer

actual class DeviceQueueCreateInfoBuilder(internal val target: VkDeviceQueueCreateInfo) {
	actual var flags: VkFlag<DeviceQueueCreate>?
		get() = DeviceQueueCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	internal fun init(queueFamilyIndex: UInt, queuePriorities: FloatArray) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO
		target.pNext = null
		target.queueFamilyIndex = queueFamilyIndex.toVkType()
		target.pQueuePriorities = queuePriorities.toVkType()
		target.queueCount = queuePriorities.size.toUInt()
	}
}

actual class DeviceQueueCreateInfosBuilder {
	val targets: MutableList<(VkDeviceQueueCreateInfo) -> Unit> = mutableListOf()

	actual fun queue(
			queueFamilyIndex: UInt,
			vararg queuePriorities: Float,
			block: DeviceQueueCreateInfoBuilder.() -> Unit
	) {
		targets += {
			val builder = DeviceQueueCreateInfoBuilder(it)
			builder.init(queueFamilyIndex, queuePriorities)
			builder.apply(block)
		}
	}
}

actual class PhysicalDeviceFeaturesBuilder(internal val target: VkPhysicalDeviceFeatures) {
	actual var robustBufferAccess: Boolean
		get() = target.robustBufferAccess.toBoolean()
		set(value) {
			target.robustBufferAccess = value.toVkType()
		}

	actual var fullDrawIndexUint32: Boolean
		get() = target.fullDrawIndexUint32.toBoolean()
		set(value) {
			target.fullDrawIndexUint32 = value.toVkType()
		}

	actual var imageCubeArray: Boolean
		get() = target.imageCubeArray.toBoolean()
		set(value) {
			target.imageCubeArray = value.toVkType()
		}

	actual var independentBlend: Boolean
		get() = target.independentBlend.toBoolean()
		set(value) {
			target.independentBlend = value.toVkType()
		}

	actual var geometryShader: Boolean
		get() = target.geometryShader.toBoolean()
		set(value) {
			target.geometryShader = value.toVkType()
		}

	actual var tessellationShader: Boolean
		get() = target.tessellationShader.toBoolean()
		set(value) {
			target.tessellationShader = value.toVkType()
		}

	actual var sampleRateShading: Boolean
		get() = target.sampleRateShading.toBoolean()
		set(value) {
			target.sampleRateShading = value.toVkType()
		}

	actual var dualSrcBlend: Boolean
		get() = target.dualSrcBlend.toBoolean()
		set(value) {
			target.dualSrcBlend = value.toVkType()
		}

	actual var logicOp: Boolean
		get() = target.logicOp.toBoolean()
		set(value) {
			target.logicOp = value.toVkType()
		}

	actual var multiDrawIndirect: Boolean
		get() = target.multiDrawIndirect.toBoolean()
		set(value) {
			target.multiDrawIndirect = value.toVkType()
		}

	actual var drawIndirectFirstInstance: Boolean
		get() = target.drawIndirectFirstInstance.toBoolean()
		set(value) {
			target.drawIndirectFirstInstance = value.toVkType()
		}

	actual var depthClamp: Boolean
		get() = target.depthClamp.toBoolean()
		set(value) {
			target.depthClamp = value.toVkType()
		}

	actual var depthBiasClamp: Boolean
		get() = target.depthBiasClamp.toBoolean()
		set(value) {
			target.depthBiasClamp = value.toVkType()
		}

	actual var fillModeNonSolid: Boolean
		get() = target.fillModeNonSolid.toBoolean()
		set(value) {
			target.fillModeNonSolid = value.toVkType()
		}

	actual var depthBounds: Boolean
		get() = target.depthBounds.toBoolean()
		set(value) {
			target.depthBounds = value.toVkType()
		}

	actual var wideLines: Boolean
		get() = target.wideLines.toBoolean()
		set(value) {
			target.wideLines = value.toVkType()
		}

	actual var largePoints: Boolean
		get() = target.largePoints.toBoolean()
		set(value) {
			target.largePoints = value.toVkType()
		}

	actual var alphaToOne: Boolean
		get() = target.alphaToOne.toBoolean()
		set(value) {
			target.alphaToOne = value.toVkType()
		}

	actual var multiViewport: Boolean
		get() = target.multiViewport.toBoolean()
		set(value) {
			target.multiViewport = value.toVkType()
		}

	actual var samplerAnisotropy: Boolean
		get() = target.samplerAnisotropy.toBoolean()
		set(value) {
			target.samplerAnisotropy = value.toVkType()
		}

	actual var textureCompressionETC2: Boolean
		get() = target.textureCompressionETC2.toBoolean()
		set(value) {
			target.textureCompressionETC2 = value.toVkType()
		}

	actual var textureCompressionASTC_LDR: Boolean
		get() = target.textureCompressionASTC_LDR.toBoolean()
		set(value) {
			target.textureCompressionASTC_LDR = value.toVkType()
		}

	actual var textureCompressionBC: Boolean
		get() = target.textureCompressionBC.toBoolean()
		set(value) {
			target.textureCompressionBC = value.toVkType()
		}

	actual var occlusionQueryPrecise: Boolean
		get() = target.occlusionQueryPrecise.toBoolean()
		set(value) {
			target.occlusionQueryPrecise = value.toVkType()
		}

	actual var pipelineStatisticsQuery: Boolean
		get() = target.pipelineStatisticsQuery.toBoolean()
		set(value) {
			target.pipelineStatisticsQuery = value.toVkType()
		}

	actual var vertexPipelineStoresAndAtomics: Boolean
		get() = target.vertexPipelineStoresAndAtomics.toBoolean()
		set(value) {
			target.vertexPipelineStoresAndAtomics = value.toVkType()
		}

	actual var fragmentStoresAndAtomics: Boolean
		get() = target.fragmentStoresAndAtomics.toBoolean()
		set(value) {
			target.fragmentStoresAndAtomics = value.toVkType()
		}

	actual var shaderTessellationAndGeometryPointSize: Boolean
		get() = target.shaderTessellationAndGeometryPointSize.toBoolean()
		set(value) {
			target.shaderTessellationAndGeometryPointSize = value.toVkType()
		}

	actual var shaderImageGatherExtended: Boolean
		get() = target.shaderImageGatherExtended.toBoolean()
		set(value) {
			target.shaderImageGatherExtended = value.toVkType()
		}

	actual var shaderStorageImageExtendedFormats: Boolean
		get() = target.shaderStorageImageExtendedFormats.toBoolean()
		set(value) {
			target.shaderStorageImageExtendedFormats = value.toVkType()
		}

	actual var shaderStorageImageMultisample: Boolean
		get() = target.shaderStorageImageMultisample.toBoolean()
		set(value) {
			target.shaderStorageImageMultisample = value.toVkType()
		}

	actual var shaderStorageImageReadWithoutFormat: Boolean
		get() = target.shaderStorageImageReadWithoutFormat.toBoolean()
		set(value) {
			target.shaderStorageImageReadWithoutFormat = value.toVkType()
		}

	actual var shaderStorageImageWriteWithoutFormat: Boolean
		get() = target.shaderStorageImageWriteWithoutFormat.toBoolean()
		set(value) {
			target.shaderStorageImageWriteWithoutFormat = value.toVkType()
		}

	actual var shaderUniformBufferArrayDynamicIndexing: Boolean
		get() = target.shaderUniformBufferArrayDynamicIndexing.toBoolean()
		set(value) {
			target.shaderUniformBufferArrayDynamicIndexing = value.toVkType()
		}

	actual var shaderSampledImageArrayDynamicIndexing: Boolean
		get() = target.shaderSampledImageArrayDynamicIndexing.toBoolean()
		set(value) {
			target.shaderSampledImageArrayDynamicIndexing = value.toVkType()
		}

	actual var shaderStorageBufferArrayDynamicIndexing: Boolean
		get() = target.shaderStorageBufferArrayDynamicIndexing.toBoolean()
		set(value) {
			target.shaderStorageBufferArrayDynamicIndexing = value.toVkType()
		}

	actual var shaderStorageImageArrayDynamicIndexing: Boolean
		get() = target.shaderStorageImageArrayDynamicIndexing.toBoolean()
		set(value) {
			target.shaderStorageImageArrayDynamicIndexing = value.toVkType()
		}

	actual var shaderClipDistance: Boolean
		get() = target.shaderClipDistance.toBoolean()
		set(value) {
			target.shaderClipDistance = value.toVkType()
		}

	actual var shaderCullDistance: Boolean
		get() = target.shaderCullDistance.toBoolean()
		set(value) {
			target.shaderCullDistance = value.toVkType()
		}

	actual var shaderFloat64: Boolean
		get() = target.shaderFloat64.toBoolean()
		set(value) {
			target.shaderFloat64 = value.toVkType()
		}

	actual var shaderInt64: Boolean
		get() = target.shaderInt64.toBoolean()
		set(value) {
			target.shaderInt64 = value.toVkType()
		}

	actual var shaderInt16: Boolean
		get() = target.shaderInt16.toBoolean()
		set(value) {
			target.shaderInt16 = value.toVkType()
		}

	actual var shaderResourceResidency: Boolean
		get() = target.shaderResourceResidency.toBoolean()
		set(value) {
			target.shaderResourceResidency = value.toVkType()
		}

	actual var shaderResourceMinLod: Boolean
		get() = target.shaderResourceMinLod.toBoolean()
		set(value) {
			target.shaderResourceMinLod = value.toVkType()
		}

	actual var sparseBinding: Boolean
		get() = target.sparseBinding.toBoolean()
		set(value) {
			target.sparseBinding = value.toVkType()
		}

	actual var sparseResidencyBuffer: Boolean
		get() = target.sparseResidencyBuffer.toBoolean()
		set(value) {
			target.sparseResidencyBuffer = value.toVkType()
		}

	actual var sparseResidencyImage2D: Boolean
		get() = target.sparseResidencyImage2D.toBoolean()
		set(value) {
			target.sparseResidencyImage2D = value.toVkType()
		}

	actual var sparseResidencyImage3D: Boolean
		get() = target.sparseResidencyImage3D.toBoolean()
		set(value) {
			target.sparseResidencyImage3D = value.toVkType()
		}

	actual var sparseResidency2Samples: Boolean
		get() = target.sparseResidency2Samples.toBoolean()
		set(value) {
			target.sparseResidency2Samples = value.toVkType()
		}

	actual var sparseResidency4Samples: Boolean
		get() = target.sparseResidency4Samples.toBoolean()
		set(value) {
			target.sparseResidency4Samples = value.toVkType()
		}

	actual var sparseResidency8Samples: Boolean
		get() = target.sparseResidency8Samples.toBoolean()
		set(value) {
			target.sparseResidency8Samples = value.toVkType()
		}

	actual var sparseResidency16Samples: Boolean
		get() = target.sparseResidency16Samples.toBoolean()
		set(value) {
			target.sparseResidency16Samples = value.toVkType()
		}

	actual var sparseResidencyAliased: Boolean
		get() = target.sparseResidencyAliased.toBoolean()
		set(value) {
			target.sparseResidencyAliased = value.toVkType()
		}

	actual var variableMultisampleRate: Boolean
		get() = target.variableMultisampleRate.toBoolean()
		set(value) {
			target.variableMultisampleRate = value.toVkType()
		}

	actual var inheritedQueries: Boolean
		get() = target.inheritedQueries.toBoolean()
		set(value) {
			target.inheritedQueries = value.toVkType()
		}

	internal fun init() {
	}
}

actual class DeviceCreateInfoBuilder(internal val target: VkDeviceCreateInfo) {
	actual fun queues(block: DeviceQueueCreateInfosBuilder.() -> Unit) {
		val targets = DeviceQueueCreateInfosBuilder().apply(block).targets
		target.pQueueCreateInfos = targets.mapToStackArray()
		target.queueCreateInfoCount = targets.size.toUInt()
	}

	actual fun enabledFeatures(block: PhysicalDeviceFeaturesBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPhysicalDeviceFeatures>()
		target.pEnabledFeatures = subTarget.ptr
		val builder = PhysicalDeviceFeaturesBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init(enabledLayerNames: Collection<String>?, enabledExtensionNames: Collection<String>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.ppEnabledLayerNames = enabledLayerNames?.toVkType()
		target.enabledLayerCount = enabledLayerNames?.size?.toUInt() ?: 0U
		target.ppEnabledExtensionNames = enabledExtensionNames?.toVkType()
		target.enabledExtensionCount = enabledExtensionNames?.size?.toUInt() ?: 0U
	}
}

actual class MemoryAllocateInfoBuilder(internal val target: VkMemoryAllocateInfo) {
	actual var allocationSize: ULong
		get() = target.allocationSize
		set(value) {
			target.allocationSize = value.toVkType()
		}

	actual var memoryTypeIndex: UInt
		get() = target.memoryTypeIndex
		set(value) {
			target.memoryTypeIndex = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO
		target.pNext = null
	}
}

actual class MappedMemoryRangeBuilder(internal val target: VkMappedMemoryRange) {
	internal fun init(memory: DeviceMemory, offset: ULong, size: ULong) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_MAPPED_MEMORY_RANGE
		target.pNext = null
		target.memory = memory.toVkType()
		target.offset = offset.toVkType()
		target.size = size.toVkType()
	}
}

actual class FlushMappedMemoryRangesBuilder {
	val targets: MutableList<(VkMappedMemoryRange) -> Unit> = mutableListOf()

	actual fun range(
			memory: DeviceMemory,
			offset: ULong,
			size: ULong,
			block: MappedMemoryRangeBuilder.() -> Unit
	) {
		targets += {
			val builder = MappedMemoryRangeBuilder(it)
			builder.init(memory, offset, size)
			builder.apply(block)
		}
	}
}

actual class InvalidateMappedMemoryRangesBuilder {
	val targets: MutableList<(VkMappedMemoryRange) -> Unit> = mutableListOf()

	actual fun range(
			memory: DeviceMemory,
			offset: ULong,
			size: ULong,
			block: MappedMemoryRangeBuilder.() -> Unit
	) {
		targets += {
			val builder = MappedMemoryRangeBuilder(it)
			builder.init(memory, offset, size)
			builder.apply(block)
		}
	}
}

actual class DebugMarkerObjectNameInfoEXTBuilder(internal val target: VkDebugMarkerObjectNameInfoEXT) {
	actual var objectType: DebugReportObjectTypeEXT?
		get() = DebugReportObjectTypeEXT.from(target.objectType)
		set(value) {
			target.objectType = value.toVkType()
		}

	actual var `object`: ULong
		get() = target.`object`
		set(value) {
			target.`object` = value.toVkType()
		}

	actual var objectName: String?
		get() = target.pObjectName?.toKString()
		set(value) {
			target.pObjectName = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEBUG_MARKER_OBJECT_NAME_INFO_EXT
		target.pNext = null
	}
}

actual class DebugMarkerObjectTagInfoEXTBuilder(internal val target: VkDebugMarkerObjectTagInfoEXT) {
	actual var objectType: DebugReportObjectTypeEXT?
		get() = DebugReportObjectTypeEXT.from(target.objectType)
		set(value) {
			target.objectType = value.toVkType()
		}

	actual var `object`: ULong
		get() = target.`object`
		set(value) {
			target.`object` = value.toVkType()
		}

	actual var tagName: ULong
		get() = target.tagName
		set(value) {
			target.tagName = value.toVkType()
		}

	internal fun init(pTag: IoBuffer) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEBUG_MARKER_OBJECT_TAG_INFO_EXT
		target.pNext = null
		pTag.readDirect {
			target.pTag = it
			target.tagSize = pTag.readRemaining.toULong()
			pTag.readRemaining
		}
	}
}

actual class DisplayPowerInfoEXTBuilder(internal val target: VkDisplayPowerInfoEXT) {
	actual var powerState: DisplayPowerStateEXT?
		get() = DisplayPowerStateEXT.from(target.powerState)
		set(value) {
			target.powerState = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DISPLAY_POWER_INFO_EXT
		target.pNext = null
	}
}

actual class DeviceEventInfoEXTBuilder(internal val target: VkDeviceEventInfoEXT) {
	actual var deviceEvent: DeviceEventTypeEXT?
		get() = DeviceEventTypeEXT.from(target.deviceEvent)
		set(value) {
			target.deviceEvent = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEVICE_EVENT_INFO_EXT
		target.pNext = null
	}
}

actual class DisplayEventInfoEXTBuilder(internal val target: VkDisplayEventInfoEXT) {
	actual var displayEvent: DisplayEventTypeEXT?
		get() = DisplayEventTypeEXT.from(target.displayEvent)
		set(value) {
			target.displayEvent = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DISPLAY_EVENT_INFO_EXT
		target.pNext = null
	}
}

actual class BindBufferMemoryInfoBuilder(internal val target: VkBindBufferMemoryInfo) {
	actual var memoryOffset: ULong
		get() = target.memoryOffset
		set(value) {
			target.memoryOffset = value.toVkType()
		}

	internal fun init(buffer: Buffer, memory: DeviceMemory) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_BIND_BUFFER_MEMORY_INFO
		target.pNext = null
		target.buffer = buffer.toVkType()
		target.memory = memory.toVkType()
	}
}

actual class BindBufferMemory2Builder {
	val targets: MutableList<(VkBindBufferMemoryInfo) -> Unit> = mutableListOf()

	actual fun info(buffer: Buffer, memory: DeviceMemory, block: BindBufferMemoryInfoBuilder.() -> Unit) {
		targets += {
			val builder = BindBufferMemoryInfoBuilder(it)
			builder.init(buffer, memory)
			builder.apply(block)
		}
	}
}

actual class BindImageMemoryInfoBuilder(internal val target: VkBindImageMemoryInfo) {
	actual var memoryOffset: ULong
		get() = target.memoryOffset
		set(value) {
			target.memoryOffset = value.toVkType()
		}

	internal fun init(image: Image, memory: DeviceMemory) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_BIND_IMAGE_MEMORY_INFO
		target.pNext = null
		target.image = image.toVkType()
		target.memory = memory.toVkType()
	}
}

actual class BindImageMemory2Builder {
	val targets: MutableList<(VkBindImageMemoryInfo) -> Unit> = mutableListOf()

	actual fun info(image: Image, memory: DeviceMemory, block: BindImageMemoryInfoBuilder.() -> Unit) {
		targets += {
			val builder = BindImageMemoryInfoBuilder(it)
			builder.init(image, memory)
			builder.apply(block)
		}
	}
}

actual class XYColorEXTBuilder(internal val target: VkXYColorEXT) {
	actual var x: Float
		get() = target.x
		set(value) {
			target.x = value.toVkType()
		}

	actual var y: Float
		get() = target.y
		set(value) {
			target.y = value.toVkType()
		}

	internal fun init() {
	}
}

actual class HdrMetadataEXTBuilder(internal val target: VkHdrMetadataEXT) {
	actual var maxLuminance: Float
		get() = target.maxLuminance
		set(value) {
			target.maxLuminance = value.toVkType()
		}

	actual var minLuminance: Float
		get() = target.minLuminance
		set(value) {
			target.minLuminance = value.toVkType()
		}

	actual var maxContentLightLevel: Float
		get() = target.maxContentLightLevel
		set(value) {
			target.maxContentLightLevel = value.toVkType()
		}

	actual var maxFrameAverageLightLevel: Float
		get() = target.maxFrameAverageLightLevel
		set(value) {
			target.maxFrameAverageLightLevel = value.toVkType()
		}

	actual fun displayPrimaryRed(block: XYColorEXTBuilder.() -> Unit) {
		val subTarget = target.displayPrimaryRed
		val builder = XYColorEXTBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun displayPrimaryGreen(block: XYColorEXTBuilder.() -> Unit) {
		val subTarget = target.displayPrimaryGreen
		val builder = XYColorEXTBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun displayPrimaryBlue(block: XYColorEXTBuilder.() -> Unit) {
		val subTarget = target.displayPrimaryBlue
		val builder = XYColorEXTBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun whitePoint(block: XYColorEXTBuilder.() -> Unit) {
		val subTarget = target.whitePoint
		val builder = XYColorEXTBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_HDR_METADATA_EXT
		target.pNext = null
	}
}

actual class SetHdrMetadataEXTBuilder {
	val targets: MutableList<(VkHdrMetadataEXT) -> Unit> = mutableListOf()

	actual fun metadata(block: HdrMetadataEXTBuilder.() -> Unit) {
		targets += {
			val builder = HdrMetadataEXTBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class DeviceQueueInfo2Builder(internal val target: VkDeviceQueueInfo2) {
	actual var flags: VkFlag<DeviceQueueCreate>?
		get() = DeviceQueueCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var queueFamilyIndex: UInt
		get() = target.queueFamilyIndex
		set(value) {
			target.queueFamilyIndex = value.toVkType()
		}

	actual var queueIndex: UInt
		get() = target.queueIndex
		set(value) {
			target.queueIndex = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEVICE_QUEUE_INFO_2
		target.pNext = null
	}
}

actual class DebugUtilsObjectTagInfoEXTBuilder(internal val target: VkDebugUtilsObjectTagInfoEXT) {
	actual var objectType: ObjectType?
		get() = ObjectType.from(target.objectType)
		set(value) {
			target.objectType = value.toVkType()
		}

	actual var objectHandle: ULong
		get() = target.objectHandle
		set(value) {
			target.objectHandle = value.toVkType()
		}

	actual var tagName: ULong
		get() = target.tagName
		set(value) {
			target.tagName = value.toVkType()
		}

	internal fun init(pTag: IoBuffer) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEBUG_UTILS_OBJECT_TAG_INFO_EXT
		target.pNext = null
		pTag.readDirect {
			target.pTag = it
			target.tagSize = pTag.readRemaining.toULong()
			pTag.readRemaining
		}
	}
}

actual class BindAccelerationStructureMemoryInfoNVBuilder(internal val target: VkBindAccelerationStructureMemoryInfoNV) {
	actual var memoryOffset: ULong
		get() = target.memoryOffset
		set(value) {
			target.memoryOffset = value.toVkType()
		}

	internal fun init(
			accelerationStructure: AccelerationStructureNV,
			memory: DeviceMemory,
			deviceIndices: UIntArray
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_BIND_ACCELERATION_STRUCTURE_MEMORY_INFO_NV
		target.pNext = null
		target.accelerationStructure = accelerationStructure.toVkType()
		target.memory = memory.toVkType()
		target.pDeviceIndices = deviceIndices.toVkType()
		target.deviceIndexCount = deviceIndices.size.toUInt()
	}
}

actual class BindAccelerationStructureMemoryNVBuilder {
	val targets: MutableList<(VkBindAccelerationStructureMemoryInfoNV) -> Unit> = mutableListOf()

	actual fun info(
			accelerationStructure: AccelerationStructureNV,
			memory: DeviceMemory,
			deviceIndices: UIntArray,
			block: BindAccelerationStructureMemoryInfoNVBuilder.() -> Unit
	) {
		targets += {
			val builder = BindAccelerationStructureMemoryInfoNVBuilder(it)
			builder.init(accelerationStructure, memory, deviceIndices)
			builder.apply(block)
		}
	}
}


actual class UpdateDescriptorSetsBuilder {
	val targets0: MutableList<(VkWriteDescriptorSet) -> Unit> = mutableListOf()

	actual fun writes(dstSet: DescriptorSet, texelBufferView: Collection<BufferView>?, block: WriteDescriptorSetBuilder.() -> Unit) {
		targets0 += {
			val builder = WriteDescriptorSetBuilder(it)
			builder.init(dstSet, texelBufferView)
			builder.apply(block)
		}
	}

	val targets1: MutableList<(VkCopyDescriptorSet) -> Unit> = mutableListOf()

	actual fun copies(srcSet: DescriptorSet, dstSet: DescriptorSet, block: CopyDescriptorSetBuilder.() -> Unit) {
		targets1 += {
			val builder = CopyDescriptorSetBuilder(it)
			builder.init(srcSet, dstSet)
			builder.apply(block)
		}
	}
}

actual class CopyDescriptorSetBuilder(internal val target: VkCopyDescriptorSet) {
	actual var srcBinding: UInt
		get() = target.srcBinding
		set(value) {
			target.srcBinding = value.toVkType()
		}

	actual var srcArrayElement: UInt
		get() = target.srcArrayElement
		set(value) {
			target.srcArrayElement = value.toVkType()
		}

	actual var dstBinding: UInt
		get() = target.dstBinding
		set(value) {
			target.dstBinding = value.toVkType()
		}

	actual var dstArrayElement: UInt
		get() = target.dstArrayElement
		set(value) {
			target.dstArrayElement = value.toVkType()
		}

	actual var descriptorCount: UInt
		get() = target.descriptorCount
		set(value) {
			target.descriptorCount = value.toVkType()
		}

	internal fun init(srcSet: DescriptorSet, dstSet: DescriptorSet) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_COPY_DESCRIPTOR_SET
		target.pNext = null
		target.srcSet = srcSet.toVkType()
		target.dstSet = dstSet.toVkType()
	}
}

