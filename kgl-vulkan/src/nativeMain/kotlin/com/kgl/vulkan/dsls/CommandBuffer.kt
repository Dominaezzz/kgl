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
import com.kgl.vulkan.unions.ClearValue
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toBoolean
import com.kgl.vulkan.utils.toVkType
import cvulkan.*
import kotlinx.cinterop.*

actual class CommandBufferInheritanceInfoBuilder(internal val target: VkCommandBufferInheritanceInfo) {
	actual var subpass: UInt
		get() = target.subpass
		set(value) {
			target.subpass = value.toVkType()
		}

	actual var occlusionQueryEnable: Boolean
		get() = target.occlusionQueryEnable.toBoolean()
		set(value) {
			target.occlusionQueryEnable = value.toVkType()
		}

	actual var queryFlags: VkFlag<QueryControl>?
		get() = QueryControl.fromMultiple(target.queryFlags)
		set(value) {
			target.queryFlags = value.toVkType()
		}

	actual var pipelineStatistics: VkFlag<QueryPipelineStatistic>?
		get() = QueryPipelineStatistic.fromMultiple(target.pipelineStatistics)
		set(value) {
			target.pipelineStatistics = value.toVkType()
		}

	internal fun init(renderPass: RenderPass?, framebuffer: Framebuffer?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_COMMAND_BUFFER_INHERITANCE_INFO
		target.pNext = null
		target.renderPass = renderPass?.toVkType()
		target.framebuffer = framebuffer?.toVkType()
	}
}

actual class CommandBufferBeginInfoBuilder(internal val target: VkCommandBufferBeginInfo) {
	actual var flags: VkFlag<CommandBufferUsage>?
		get() = CommandBufferUsage.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual fun inheritanceInfo(
			renderPass: RenderPass?,
			framebuffer: Framebuffer?,
			block: CommandBufferInheritanceInfoBuilder.() -> Unit
	) {
		val subTarget = VirtualStack.alloc<VkCommandBufferInheritanceInfo>()
		target.pInheritanceInfo = subTarget.ptr
		val builder = CommandBufferInheritanceInfoBuilder(subTarget)
		builder.init(renderPass, framebuffer)
		builder.apply(block)
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO
		target.pNext = null
	}
}

actual class CmdSetViewportBuilder {
	val targets: MutableList<(VkViewport) -> Unit> = mutableListOf()

	actual fun viewport(block: ViewportBuilder.() -> Unit) {
		targets += {
			val builder = ViewportBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class CmdSetScissorBuilder {
	val targets: MutableList<(VkRect2D) -> Unit> = mutableListOf()

	actual fun rect2D(block: Rect2DBuilder.() -> Unit) {
		targets += {
			val builder = Rect2DBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class BufferCopyBuilder(internal val target: VkBufferCopy) {
	actual var srcOffset: ULong
		get() = target.srcOffset
		set(value) {
			target.srcOffset = value.toVkType()
		}

	actual var dstOffset: ULong
		get() = target.dstOffset
		set(value) {
			target.dstOffset = value.toVkType()
		}

	actual var size: ULong
		get() = target.size
		set(value) {
			target.size = value.toVkType()
		}

	internal fun init() {
	}
}

actual class CmdCopyBufferBuilder {
	val targets: MutableList<(VkBufferCopy) -> Unit> = mutableListOf()

	actual fun copy(block: BufferCopyBuilder.() -> Unit) {
		targets += {
			val builder = BufferCopyBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class ImageSubresourceLayersBuilder(internal val target: VkImageSubresourceLayers) {
	actual var aspectMask: VkFlag<ImageAspect>?
		get() = ImageAspect.fromMultiple(target.aspectMask)
		set(value) {
			target.aspectMask = value.toVkType()
		}

	actual var mipLevel: UInt
		get() = target.mipLevel
		set(value) {
			target.mipLevel = value.toVkType()
		}

	actual var baseArrayLayer: UInt
		get() = target.baseArrayLayer
		set(value) {
			target.baseArrayLayer = value.toVkType()
		}

	actual var layerCount: UInt
		get() = target.layerCount
		set(value) {
			target.layerCount = value.toVkType()
		}

	internal fun init() {
	}
}

actual class ImageCopyBuilder(internal val target: VkImageCopy) {
	actual fun srcSubresource(block: ImageSubresourceLayersBuilder.() -> Unit) {
		val subTarget = target.srcSubresource
		val builder = ImageSubresourceLayersBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun srcOffset(
			x: Int,
			y: Int,
			z: Int
	) {
		val subTarget = target.srcOffset
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun dstSubresource(block: ImageSubresourceLayersBuilder.() -> Unit) {
		val subTarget = target.dstSubresource
		val builder = ImageSubresourceLayersBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun dstOffset(
			x: Int,
			y: Int,
			z: Int
	) {
		val subTarget = target.dstOffset
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun extent(
			width: UInt,
			height: UInt,
			depth: UInt
	) {
		val subTarget = target.extent
		val builder = Extent3DBuilder(subTarget)
		builder.init(width, height, depth)
	}

	internal fun init() {
	}
}

actual class CmdCopyImageBuilder {
	val targets: MutableList<(VkImageCopy) -> Unit> = mutableListOf()

	actual fun copy(block: ImageCopyBuilder.() -> Unit) {
		targets += {
			val builder = ImageCopyBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class ImageBlitBuilder(internal val target: VkImageBlit) {
	actual fun srcSubresource(block: ImageSubresourceLayersBuilder.() -> Unit) {
		val subTarget = target.srcSubresource
		val builder = ImageSubresourceLayersBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun srcOffset0(x: Int, y: Int, z: Int) {
		val subTarget = target.srcOffsets[0]
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun srcOffset1(x: Int, y: Int, z: Int) {
		val subTarget = target.srcOffsets[1]
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun dstSubresource(block: ImageSubresourceLayersBuilder.() -> Unit) {
		val subTarget = target.dstSubresource
		val builder = ImageSubresourceLayersBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun dstOffset0(x: Int, y: Int, z: Int) {
		val subTarget = target.dstOffsets[0]
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun dstOffset1(x: Int, y: Int, z: Int) {
		val subTarget = target.dstOffsets[1]
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	internal fun init() {
	}
}

actual class CmdBlitImageBuilder {
	val targets: MutableList<(VkImageBlit) -> Unit> = mutableListOf()

	actual fun blit(block: ImageBlitBuilder.() -> Unit) {
		targets += {
			val builder = ImageBlitBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class BufferImageCopyBuilder(internal val target: VkBufferImageCopy) {
	actual var bufferOffset: ULong
		get() = target.bufferOffset
		set(value) {
			target.bufferOffset = value.toVkType()
		}

	actual var bufferRowLength: UInt
		get() = target.bufferRowLength
		set(value) {
			target.bufferRowLength = value.toVkType()
		}

	actual var bufferImageHeight: UInt
		get() = target.bufferImageHeight
		set(value) {
			target.bufferImageHeight = value.toVkType()
		}

	actual fun imageSubresource(block: ImageSubresourceLayersBuilder.() -> Unit) {
		val subTarget = target.imageSubresource
		val builder = ImageSubresourceLayersBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun imageOffset(
			x: Int,
			y: Int,
			z: Int
	) {
		val subTarget = target.imageOffset
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun imageExtent(
			width: UInt,
			height: UInt,
			depth: UInt
	) {
		val subTarget = target.imageExtent
		val builder = Extent3DBuilder(subTarget)
		builder.init(width, height, depth)
	}

	internal fun init() {
	}
}

actual class CmdCopyBufferToImageBuilder {
	val targets: MutableList<(VkBufferImageCopy) -> Unit> = mutableListOf()

	actual fun copy(block: BufferImageCopyBuilder.() -> Unit) {
		targets += {
			val builder = BufferImageCopyBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class CmdCopyImageToBufferBuilder {
	val targets: MutableList<(VkBufferImageCopy) -> Unit> = mutableListOf()

	actual fun copy(block: BufferImageCopyBuilder.() -> Unit) {
		targets += {
			val builder = BufferImageCopyBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class ImageResolveBuilder(internal val target: VkImageResolve) {
	actual fun srcSubresource(block: ImageSubresourceLayersBuilder.() -> Unit) {
		val subTarget = target.srcSubresource
		val builder = ImageSubresourceLayersBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun srcOffset(
			x: Int,
			y: Int,
			z: Int
	) {
		val subTarget = target.srcOffset
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun dstSubresource(block: ImageSubresourceLayersBuilder.() -> Unit) {
		val subTarget = target.dstSubresource
		val builder = ImageSubresourceLayersBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun dstOffset(
			x: Int,
			y: Int,
			z: Int
	) {
		val subTarget = target.dstOffset
		val builder = Offset3DBuilder(subTarget)
		builder.init(x, y, z)
	}

	actual fun extent(
			width: UInt,
			height: UInt,
			depth: UInt
	) {
		val subTarget = target.extent
		val builder = Extent3DBuilder(subTarget)
		builder.init(width, height, depth)
	}

	internal fun init() {
	}
}

actual class CmdResolveImageBuilder {
	val targets: MutableList<(VkImageResolve) -> Unit> = mutableListOf()

	actual fun resolve(block: ImageResolveBuilder.() -> Unit) {
		targets += {
			val builder = ImageResolveBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class ConditionalRenderingBeginInfoEXTBuilder(internal val target: VkConditionalRenderingBeginInfoEXT) {
	internal fun init(buffer: Buffer, offset: ULong, flags: VkFlag<ConditionalRenderingEXT>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_CONDITIONAL_RENDERING_BEGIN_INFO_EXT
		target.pNext = null
		target.buffer = buffer.toVkType()
		target.offset = offset.toVkType()
		target.flags = flags?.toVkType() ?: 0U
	}
}

actual class RenderPassBeginInfoBuilder(internal val target: VkRenderPassBeginInfo) {
	actual fun renderArea(block: Rect2DBuilder.() -> Unit) {
		val subTarget = target.renderArea
		val builder = Rect2DBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init(renderPass: RenderPass, framebuffer: Framebuffer, clearValues: Collection<ClearValue>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO
		target.pNext = null
		target.renderPass = renderPass.toVkType()
		target.framebuffer = framebuffer.toVkType()
		target.pClearValues = clearValues?.toVkType()
		target.clearValueCount = clearValues?.size?.toUInt() ?: 0U
	}
}

actual class DebugMarkerMarkerInfoEXTBuilder(internal val target: VkDebugMarkerMarkerInfoEXT) {
	actual var markerName: String?
		get() = target.pMarkerName?.toKString()
		set(value) {
			target.pMarkerName = value.toVkType()
		}

	actual fun color(arg0: Float, arg1: Float, arg2: Float, arg3: Float) {
		target.color[0] = arg0.toVkType()
		target.color[1] = arg1.toVkType()
		target.color[2] = arg2.toVkType()
		target.color[3] = arg3.toVkType()
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_DEBUG_MARKER_MARKER_INFO_EXT
		target.pNext = null
	}
}

actual class IndirectCommandsTokenNVXBuilder(internal val target: VkIndirectCommandsTokenNVX) {
	internal fun init(tokenType: IndirectCommandsTokenTypeNVX, buffer: Buffer, offset: ULong) {
		target.tokenType = tokenType.toVkType()
		target.buffer = buffer.toVkType()
		target.offset = offset.toVkType()
	}
}

actual class IndirectCommandsTokenNVXsBuilder {
	val targets: MutableList<(VkIndirectCommandsTokenNVX) -> Unit> = mutableListOf()

	actual fun token(tokenType: IndirectCommandsTokenTypeNVX, buffer: Buffer, offset: ULong) {
		targets += {
			val builder = IndirectCommandsTokenNVXBuilder(it)
			builder.init(tokenType, buffer, offset)
		}
	}
}

actual class CmdProcessCommandsInfoNVXBuilder(internal val target: VkCmdProcessCommandsInfoNVX) {
	actual var maxSequencesCount: UInt
		get() = target.maxSequencesCount
		set(value) {
			target.maxSequencesCount = value.toVkType()
		}

	actual var sequencesCountOffset: ULong
		get() = target.sequencesCountOffset
		set(value) {
			target.sequencesCountOffset = value.toVkType()
		}

	actual var sequencesIndexOffset: ULong
		get() = target.sequencesIndexOffset
		set(value) {
			target.sequencesIndexOffset = value.toVkType()
		}

	actual fun indirectCommandsTokens(block: IndirectCommandsTokenNVXsBuilder.() -> Unit) {
		val targets = IndirectCommandsTokenNVXsBuilder().apply(block).targets
		target.pIndirectCommandsTokens = targets.mapToStackArray()
		target.indirectCommandsTokenCount = targets.size.toUInt()
	}

	internal fun init(
			objectTable: ObjectTableNVX,
			indirectCommandsLayout: IndirectCommandsLayoutNVX,
			targetCommandBuffer: CommandBuffer?,
			sequencesCountBuffer: Buffer?,
			sequencesIndexBuffer: Buffer?
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_CMD_PROCESS_COMMANDS_INFO_NVX
		target.pNext = null
		target.objectTable = objectTable.toVkType()
		target.indirectCommandsLayout = indirectCommandsLayout.toVkType()
		target.targetCommandBuffer = targetCommandBuffer?.toVkType()
		target.sequencesCountBuffer = sequencesCountBuffer?.toVkType()
		target.sequencesIndexBuffer = sequencesIndexBuffer?.toVkType()
	}
}

actual class CmdReserveSpaceForCommandsInfoNVXBuilder(internal val target: VkCmdReserveSpaceForCommandsInfoNVX) {
	actual var maxSequencesCount: UInt
		get() = target.maxSequencesCount
		set(value) {
			target.maxSequencesCount = value.toVkType()
		}

	internal fun init(objectTable: ObjectTableNVX, indirectCommandsLayout: IndirectCommandsLayoutNVX) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_CMD_RESERVE_SPACE_FOR_COMMANDS_INFO_NVX
		target.pNext = null
		target.objectTable = objectTable.toVkType()
		target.indirectCommandsLayout = indirectCommandsLayout.toVkType()
	}
}

actual class DescriptorImageInfoBuilder(internal val target: VkDescriptorImageInfo) {
	internal fun init(sampler: Sampler, imageView: ImageView, imageLayout: ImageLayout) {
		target.sampler = sampler.toVkType()
		target.imageView = imageView.toVkType()
		target.imageLayout = imageLayout.toVkType()
	}
}

actual class DescriptorImageInfosBuilder {
	val targets: MutableList<(VkDescriptorImageInfo) -> Unit> = mutableListOf()

	actual fun info(sampler: Sampler, imageView: ImageView, imageLayout: ImageLayout) {
		targets += {
			val builder = DescriptorImageInfoBuilder(it)
			builder.init(sampler, imageView, imageLayout)
		}
	}
}

actual class DescriptorBufferInfoBuilder(internal val target: VkDescriptorBufferInfo) {
	internal fun init(buffer: Buffer, offset: ULong, range: ULong) {
		target.buffer = buffer.toVkType()
		target.offset = offset.toVkType()
		target.range = range.toVkType()
	}
}

actual class DescriptorBufferInfosBuilder {
	val targets: MutableList<(VkDescriptorBufferInfo) -> Unit> = mutableListOf()

	actual fun info(buffer: Buffer, offset: ULong, range: ULong) {
		targets += {
			val builder = DescriptorBufferInfoBuilder(it)
			builder.init(buffer, offset, range)
		}
	}
}

actual class WriteDescriptorSetBuilder(internal val target: VkWriteDescriptorSet) {
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

	actual var descriptorType: DescriptorType?
		get() = DescriptorType.from(target.descriptorType)
		set(value) {
			target.descriptorType = value.toVkType()
		}

	actual fun imageInfo(block: DescriptorImageInfosBuilder.() -> Unit) {
		val targets = DescriptorImageInfosBuilder().apply(block).targets
		target.pImageInfo = targets.mapToStackArray()
		target.descriptorCount = targets.size.toUInt()
	}

	actual fun bufferInfo(block: DescriptorBufferInfosBuilder.() -> Unit) {
		val targets = DescriptorBufferInfosBuilder().apply(block).targets
		target.pBufferInfo = targets.mapToStackArray()
		target.descriptorCount = targets.size.toUInt()
	}

	internal fun init(dstSet: DescriptorSet, texelBufferView: Collection<BufferView>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET
		target.pNext = null
		target.dstSet = dstSet.toVkType()
		target.pTexelBufferView = texelBufferView?.toVkType()
		target.descriptorCount = texelBufferView?.size?.toUInt() ?: 0U
	}
}

actual class CmdPushDescriptorSetKHRBuilder {
	val targets: MutableList<(VkWriteDescriptorSet) -> Unit> = mutableListOf()

	actual fun set(dstSet: DescriptorSet, texelBufferView: Collection<BufferView>?, block: WriteDescriptorSetBuilder.() -> Unit) {
		targets += {
			val builder = WriteDescriptorSetBuilder(it)
			builder.init(dstSet, texelBufferView)
			builder.apply(block)
		}
	}
}

actual class ViewportWScalingNVBuilder(internal val target: VkViewportWScalingNV) {
	actual var xcoeff: Float
		get() = target.xcoeff
		set(value) {
			target.xcoeff = value.toVkType()
		}

	actual var ycoeff: Float
		get() = target.ycoeff
		set(value) {
			target.ycoeff = value.toVkType()
		}

	internal fun init() {
	}
}

actual class CmdSetViewportWScalingNVBuilder {
	val targets: MutableList<(VkViewportWScalingNV) -> Unit> = mutableListOf()

	actual fun scaling(block: ViewportWScalingNVBuilder.() -> Unit) {
		targets += {
			val builder = ViewportWScalingNVBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class CmdSetDiscardRectangleEXTBuilder {
	val targets: MutableList<(VkRect2D) -> Unit> = mutableListOf()

	actual fun rect2D(block: Rect2DBuilder.() -> Unit) {
		targets += {
			val builder = Rect2DBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class SampleLocationEXTBuilder(internal val target: VkSampleLocationEXT) {
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

actual class SampleLocationEXTsBuilder {
	val targets: MutableList<(VkSampleLocationEXT) -> Unit> = mutableListOf()

	actual fun location(block: SampleLocationEXTBuilder.() -> Unit) {
		targets += {
			val builder = SampleLocationEXTBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class SampleLocationsInfoEXTBuilder(internal val target: VkSampleLocationsInfoEXT) {
	actual var sampleLocationsPerPixel: SampleCount?
		get() = SampleCount.from(target.sampleLocationsPerPixel)
		set(value) {
			target.sampleLocationsPerPixel = value.toVkType()
		}

	actual fun sampleLocationGridSize(width: UInt, height: UInt) {
		val subTarget = target.sampleLocationGridSize
		val builder = Extent2DBuilder(subTarget)
		builder.init(width, height)
	}

	actual fun sampleLocations(block: SampleLocationEXTsBuilder.() -> Unit) {
		val targets = SampleLocationEXTsBuilder().apply(block).targets
		target.pSampleLocations = targets.mapToStackArray()
		target.sampleLocationsCount = targets.size.toUInt()
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SAMPLE_LOCATIONS_INFO_EXT
		target.pNext = null
	}
}

actual class SubpassEndInfoKHRBuilder(internal val target: VkSubpassEndInfoKHR) {
	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SUBPASS_END_INFO_KHR
		target.pNext = null
	}
}

actual class CmdSetExclusiveScissorNVBuilder {
	val targets: MutableList<(VkRect2D) -> Unit> = mutableListOf()

	actual fun rect2D(block: Rect2DBuilder.() -> Unit) {
		targets += {
			val builder = Rect2DBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class ShadingRatePaletteNVBuilder(internal val target: VkShadingRatePaletteNV) {
	internal fun init(shadingRatePaletteEntries: Collection<ShadingRatePaletteEntryNV>) {
		target.pShadingRatePaletteEntries = shadingRatePaletteEntries.toVkType()
		target.shadingRatePaletteEntryCount = shadingRatePaletteEntries.size.toUInt()
	}
}

actual class CmdSetViewportShadingRatePaletteNVBuilder {
	val targets: MutableList<(VkShadingRatePaletteNV) -> Unit> = mutableListOf()

	actual fun palette(shadingRatePaletteEntries: Collection<ShadingRatePaletteEntryNV>) {
		targets += {
			val builder = ShadingRatePaletteNVBuilder(it)
			builder.init(shadingRatePaletteEntries)
		}
	}
}

actual class CoarseSampleLocationNVBuilder(internal val target: VkCoarseSampleLocationNV) {
	actual var pixelX: UInt
		get() = target.pixelX
		set(value) {
			target.pixelX = value.toVkType()
		}

	actual var pixelY: UInt
		get() = target.pixelY
		set(value) {
			target.pixelY = value.toVkType()
		}

	actual var sample: UInt
		get() = target.sample
		set(value) {
			target.sample = value.toVkType()
		}

	internal fun init() {
	}
}

actual class CoarseSampleLocationNVsBuilder {
	val targets: MutableList<(VkCoarseSampleLocationNV) -> Unit> = mutableListOf()

	actual fun location(block: CoarseSampleLocationNVBuilder.() -> Unit) {
		targets += {
			val builder = CoarseSampleLocationNVBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class CoarseSampleOrderCustomNVBuilder(internal val target: VkCoarseSampleOrderCustomNV) {
	actual var shadingRate: ShadingRatePaletteEntryNV?
		get() = ShadingRatePaletteEntryNV.from(target.shadingRate)
		set(value) {
			target.shadingRate = value.toVkType()
		}

	actual var sampleCount: UInt
		get() = target.sampleCount
		set(value) {
			target.sampleCount = value.toVkType()
		}

	actual fun sampleLocations(block: CoarseSampleLocationNVsBuilder.() -> Unit) {
		val targets = CoarseSampleLocationNVsBuilder().apply(block).targets
		target.pSampleLocations = targets.mapToStackArray()
		target.sampleLocationCount = targets.size.toUInt()
	}

	internal fun init() {
	}
}

actual class CmdSetCoarseSampleOrderNVBuilder {
	val targets: MutableList<(VkCoarseSampleOrderCustomNV) -> Unit> = mutableListOf()

	actual fun custom(block: CoarseSampleOrderCustomNVBuilder.() -> Unit) {
		targets += {
			val builder = CoarseSampleOrderCustomNVBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}


actual class MemoryBarrierBuilder(internal val target: VkMemoryBarrier) {
	actual var srcAccessMask: VkFlag<Access>?
		get() = Access.fromMultiple(target.srcAccessMask)
		set(value) {
			target.srcAccessMask = value.toVkType()
		}

	actual var dstAccessMask: VkFlag<Access>?
		get() = Access.fromMultiple(target.dstAccessMask)
		set(value) {
			target.dstAccessMask = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_MEMORY_BARRIER
		target.pNext = null
	}
}

actual class BufferMemoryBarrierBuilder(internal val target: VkBufferMemoryBarrier) {
	actual var srcAccessMask: VkFlag<Access>?
		get() = Access.fromMultiple(target.srcAccessMask)
		set(value) {
			target.srcAccessMask = value.toVkType()
		}

	actual var dstAccessMask: VkFlag<Access>?
		get() = Access.fromMultiple(target.dstAccessMask)
		set(value) {
			target.dstAccessMask = value.toVkType()
		}

	actual var srcQueueFamilyIndex: UInt
		get() = target.srcQueueFamilyIndex
		set(value) {
			target.srcQueueFamilyIndex = value.toVkType()
		}

	actual var dstQueueFamilyIndex: UInt
		get() = target.dstQueueFamilyIndex
		set(value) {
			target.dstQueueFamilyIndex = value.toVkType()
		}

	internal fun init(
			buffer: Buffer,
			offset: ULong,
			size: ULong
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_BUFFER_MEMORY_BARRIER
		target.pNext = null
		target.buffer = buffer.toVkType()
		target.offset = offset.toVkType()
		target.size = size.toVkType()
	}
}

actual class ImageMemoryBarrierBuilder(internal val target: VkImageMemoryBarrier) {
	actual var srcAccessMask: VkFlag<Access>?
		get() = Access.fromMultiple(target.srcAccessMask)
		set(value) {
			target.srcAccessMask = value.toVkType()
		}

	actual var dstAccessMask: VkFlag<Access>?
		get() = Access.fromMultiple(target.dstAccessMask)
		set(value) {
			target.dstAccessMask = value.toVkType()
		}

	actual var oldLayout: ImageLayout?
		get() = ImageLayout.from(target.oldLayout)
		set(value) {
			target.oldLayout = value.toVkType()
		}

	actual var newLayout: ImageLayout?
		get() = ImageLayout.from(target.newLayout)
		set(value) {
			target.newLayout = value.toVkType()
		}

	actual var srcQueueFamilyIndex: UInt
		get() = target.srcQueueFamilyIndex
		set(value) {
			target.srcQueueFamilyIndex = value.toVkType()
		}

	actual var dstQueueFamilyIndex: UInt
		get() = target.dstQueueFamilyIndex
		set(value) {
			target.dstQueueFamilyIndex = value.toVkType()
		}

	actual fun subresourceRange(block: ImageSubresourceRangeBuilder.() -> Unit) {
		val subTarget = target.subresourceRange
		val builder = ImageSubresourceRangeBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init(image: Image) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER
		target.pNext = null
		target.image = image.toVkType()
	}
}

actual class MemoryBarriersBuilder {
	val targets: MutableList<(VkMemoryBarrier) -> Unit> = mutableListOf()
	val targets1: MutableList<(VkBufferMemoryBarrier) -> Unit> = mutableListOf()
	val targets2: MutableList<(VkImageMemoryBarrier) -> Unit> = mutableListOf()

	actual fun barrier(block: MemoryBarrierBuilder.() -> Unit) {
		targets += {
			val builder = MemoryBarrierBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}

	actual fun barrier(buffer: Buffer, offset: ULong, size: ULong, block: BufferMemoryBarrierBuilder.() -> Unit) {
		targets1 += {
			val builder = BufferMemoryBarrierBuilder(it)
			builder.init(buffer, offset, size)
			builder.apply(block)
		}
	}

	actual fun barrier(image: Image, block: ImageMemoryBarrierBuilder.() -> Unit) {
		targets2 += {
			val builder = ImageMemoryBarrierBuilder(it)
			builder.init(image)
			builder.apply(block)
		}
	}
}
