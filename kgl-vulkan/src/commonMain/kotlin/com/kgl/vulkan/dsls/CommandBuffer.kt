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

expect class CommandBufferInheritanceInfoBuilder {
	var subpass: UInt

	var occlusionQueryEnable: Boolean

	var queryFlags: VkFlag<QueryControl>?

	var pipelineStatistics: VkFlag<QueryPipelineStatistic>?
}

expect class CommandBufferBeginInfoBuilder {
	var flags: VkFlag<CommandBufferUsage>?

	fun inheritanceInfo(
			renderPass: RenderPass?,
			framebuffer: Framebuffer?,
			block: CommandBufferInheritanceInfoBuilder.() -> Unit = {}
	)
}

expect class CmdSetViewportBuilder {
	fun viewport(block: ViewportBuilder.() -> Unit = {})
}

expect class CmdSetScissorBuilder {
	fun rect2D(block: Rect2DBuilder.() -> Unit)
}

expect class BufferCopyBuilder {
	var srcOffset: ULong

	var dstOffset: ULong

	var size: ULong
}

expect class CmdCopyBufferBuilder {
	fun copy(block: BufferCopyBuilder.() -> Unit = {})
}

expect class ImageSubresourceLayersBuilder {
	var aspectMask: VkFlag<ImageAspect>?

	var mipLevel: UInt

	var baseArrayLayer: UInt

	var layerCount: UInt
}

expect class ImageCopyBuilder {
	fun srcSubresource(block: ImageSubresourceLayersBuilder.() -> Unit = {})

	fun srcOffset(
			x: Int,
			y: Int,
			z: Int
	)

	fun dstSubresource(block: ImageSubresourceLayersBuilder.() -> Unit = {})

	fun dstOffset(
			x: Int,
			y: Int,
			z: Int
	)

	fun extent(
			width: UInt,
			height: UInt,
			depth: UInt
	)
}

expect class CmdCopyImageBuilder {
	fun copy(block: ImageCopyBuilder.() -> Unit)
}

expect class ImageBlitBuilder {
	fun srcSubresource(block: ImageSubresourceLayersBuilder.() -> Unit = {})

	fun srcOffset0(x: Int, y: Int, z: Int)
	fun srcOffset1(x: Int, y: Int, z: Int)

	fun dstSubresource(block: ImageSubresourceLayersBuilder.() -> Unit = {})

	fun dstOffset0(x: Int, y: Int, z: Int)
	fun dstOffset1(x: Int, y: Int, z: Int)
}

expect class CmdBlitImageBuilder {
	fun blit(block: ImageBlitBuilder.() -> Unit)
}

expect class BufferImageCopyBuilder {
	var bufferOffset: ULong

	var bufferRowLength: UInt

	var bufferImageHeight: UInt

	fun imageSubresource(block: ImageSubresourceLayersBuilder.() -> Unit = {})

	fun imageOffset(
			x: Int,
			y: Int,
			z: Int
	)

	fun imageExtent(
			width: UInt,
			height: UInt,
			depth: UInt
	)
}

expect class CmdCopyBufferToImageBuilder {
	fun copy(block: BufferImageCopyBuilder.() -> Unit)
}

expect class CmdCopyImageToBufferBuilder {
	fun copy(block: BufferImageCopyBuilder.() -> Unit)
}

expect class ImageResolveBuilder {
	fun srcSubresource(block: ImageSubresourceLayersBuilder.() -> Unit = {})

	fun srcOffset(
			x: Int,
			y: Int,
			z: Int
	)

	fun dstSubresource(block: ImageSubresourceLayersBuilder.() -> Unit = {})

	fun dstOffset(
			x: Int,
			y: Int,
			z: Int
	)

	fun extent(
			width: UInt,
			height: UInt,
			depth: UInt
	)
}

expect class CmdResolveImageBuilder {
	fun resolve(block: ImageResolveBuilder.() -> Unit)
}

expect class ConditionalRenderingBeginInfoEXTBuilder

expect class RenderPassBeginInfoBuilder {
	fun renderArea(block: Rect2DBuilder.() -> Unit)
}

expect class DebugMarkerMarkerInfoEXTBuilder {
	var markerName: String?

	fun color(
			arg0: Float,
			arg1: Float,
			arg2: Float,
			arg3: Float
	)
}

expect class IndirectCommandsTokenNVXBuilder

expect class IndirectCommandsTokenNVXsBuilder {
	fun token(
			tokenType: IndirectCommandsTokenTypeNVX,
			buffer: Buffer,
			offset: ULong
	)
}

expect class CmdProcessCommandsInfoNVXBuilder {
	var maxSequencesCount: UInt

	var sequencesCountOffset: ULong

	var sequencesIndexOffset: ULong

	fun indirectCommandsTokens(block: IndirectCommandsTokenNVXsBuilder.() -> Unit)
}

expect class CmdReserveSpaceForCommandsInfoNVXBuilder {
	var maxSequencesCount: UInt
}

expect class DescriptorImageInfoBuilder

expect class DescriptorImageInfosBuilder {
	fun info(sampler: Sampler, imageView: ImageView, imageLayout: ImageLayout)
}

expect class DescriptorBufferInfoBuilder

expect class DescriptorBufferInfosBuilder {
	fun info(buffer: Buffer, offset: ULong, range: ULong)
}

expect class WriteDescriptorSetBuilder {
	var dstBinding: UInt

	var dstArrayElement: UInt

	var descriptorType: DescriptorType?

	fun imageInfo(block: DescriptorImageInfosBuilder.() -> Unit)

	fun bufferInfo(block: DescriptorBufferInfosBuilder.() -> Unit)
}

expect class CmdPushDescriptorSetKHRBuilder {
	fun set(dstSet: DescriptorSet, texelBufferView: Collection<BufferView>? = null, block: WriteDescriptorSetBuilder.() -> Unit)
}

expect class ViewportWScalingNVBuilder {
	var xcoeff: Float

	var ycoeff: Float
}

expect class CmdSetViewportWScalingNVBuilder {
	fun scaling(block: ViewportWScalingNVBuilder.() -> Unit = {})
}

expect class CmdSetDiscardRectangleEXTBuilder {
	fun rect2D(block: Rect2DBuilder.() -> Unit)
}

expect class SampleLocationEXTBuilder {
	var x: Float

	var y: Float
}

expect class SampleLocationEXTsBuilder {
	fun location(block: SampleLocationEXTBuilder.() -> Unit = {})
}

expect class SampleLocationsInfoEXTBuilder {
	var sampleLocationsPerPixel: SampleCount?

	fun sampleLocationGridSize(width: UInt, height: UInt)

	fun sampleLocations(block: SampleLocationEXTsBuilder.() -> Unit)
}

expect class SubpassEndInfoKHRBuilder

expect class CmdSetExclusiveScissorNVBuilder {
	fun rect2D(block: Rect2DBuilder.() -> Unit)
}

expect class ShadingRatePaletteNVBuilder

expect class CmdSetViewportShadingRatePaletteNVBuilder {
	fun palette(shadingRatePaletteEntries: Collection<ShadingRatePaletteEntryNV>)
}

expect class CoarseSampleLocationNVBuilder {
	var pixelX: UInt

	var pixelY: UInt

	var sample: UInt
}

expect class CoarseSampleLocationNVsBuilder {
	fun location(block: CoarseSampleLocationNVBuilder.() -> Unit = {})
}

expect class CoarseSampleOrderCustomNVBuilder {
	var shadingRate: ShadingRatePaletteEntryNV?

	var sampleCount: UInt

	fun sampleLocations(block: CoarseSampleLocationNVsBuilder.() -> Unit)
}

expect class CmdSetCoarseSampleOrderNVBuilder {
	fun custom(block: CoarseSampleOrderCustomNVBuilder.() -> Unit)
}


expect class MemoryBarrierBuilder {
	var srcAccessMask: VkFlag<Access>?

	var dstAccessMask: VkFlag<Access>?
}

expect class BufferMemoryBarrierBuilder {
	var srcAccessMask: VkFlag<Access>?

	var dstAccessMask: VkFlag<Access>?

	var srcQueueFamilyIndex: UInt

	var dstQueueFamilyIndex: UInt
}

expect class ImageMemoryBarrierBuilder {
	var srcAccessMask: VkFlag<Access>?

	var dstAccessMask: VkFlag<Access>?

	var oldLayout: ImageLayout?

	var newLayout: ImageLayout?

	var srcQueueFamilyIndex: UInt

	var dstQueueFamilyIndex: UInt

	fun subresourceRange(block: ImageSubresourceRangeBuilder.() -> Unit = {})
}

expect class MemoryBarriersBuilder {
	fun barrier(block: MemoryBarrierBuilder.() -> Unit = {})

	fun barrier(
			buffer: Buffer,
			offset: ULong,
			size: ULong,
			block: BufferMemoryBarrierBuilder.() -> Unit = {}
	)

	fun barrier(image: Image, block: ImageMemoryBarrierBuilder.() -> Unit)
}

