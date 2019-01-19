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
package com.kgl.vulkan.handles

import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.unions.ClearValue
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.VkHandle
import kotlinx.io.core.IoBuffer

expect class CommandBuffer : VkHandle {
	val commandPool: CommandPool

	fun begin(block: CommandBufferBeginInfoBuilder.() -> Unit = {})

	fun end()

	fun reset(flags: VkFlag<CommandBufferReset>?)

	fun bindPipeline(pipelineBindPoint: PipelineBindPoint, pipeline: Pipeline)

	fun setViewport(firstViewport: UInt, block: CmdSetViewportBuilder.() -> Unit)

	fun setScissor(firstScissor: UInt, block: CmdSetScissorBuilder.() -> Unit)

	fun setLineWidth(lineWidth: Float)

	fun setDepthBias(depthBiasConstantFactor: Float, depthBiasClamp: Float, depthBiasSlopeFactor: Float)

	fun setDepthBounds(minDepthBounds: Float, maxDepthBounds: Float)

	fun setStencilCompareMask(faceMask: VkFlag<StencilFace>, compareMask: UInt)

	fun setStencilWriteMask(faceMask: VkFlag<StencilFace>, writeMask: UInt)

	fun setStencilReference(faceMask: VkFlag<StencilFace>, reference: UInt)

	fun setBlendConstants(r: Float, g: Float, b: Float, a: Float)

	fun bindDescriptorSets(
			pipelineBindPoint: PipelineBindPoint,
			layout: PipelineLayout,
			firstSet: UInt,
			descriptorSets: Collection<DescriptorSet>,
			dynamicOffsets: UIntArray
	)

	fun bindIndexBuffer(buffer: Buffer, offset: ULong, indexType: IndexType)

	fun bindVertexBuffers(firstBinding: UInt, buffers: Collection<Pair<Buffer, ULong>>)

	fun draw(vertexCount: UInt, instanceCount: UInt, firstVertex: UInt, firstInstance: UInt)

	fun drawIndexed(
			indexCount: UInt,
			instanceCount: UInt,
			firstIndex: UInt,
			vertexOffset: Int,
			firstInstance: UInt
	)

	fun drawIndirect(buffer: Buffer, offset: ULong, drawCount: UInt, stride: UInt)

	fun drawIndexedIndirect(buffer: Buffer, offset: ULong, drawCount: UInt, stride: UInt)

	fun dispatch(groupCountX: UInt, groupCountY: UInt, groupCountZ: UInt)

	fun dispatchIndirect(buffer: Buffer, offset: ULong)

	fun copyBuffer(srcBuffer: Buffer, dstBuffer: Buffer, block: CmdCopyBufferBuilder.() -> Unit)

	fun copyImage(srcImage: Image, srcImageLayout: ImageLayout, dstImage: Image, dstImageLayout: ImageLayout, block: CmdCopyImageBuilder.() -> Unit)

	fun blitImage(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			filter: Filter,
			block: CmdBlitImageBuilder.() -> Unit
	)

	fun copyBufferToImage(srcBuffer: Buffer, dstImage: Image, dstImageLayout: ImageLayout, block: CmdCopyBufferToImageBuilder.() -> Unit)

	fun copyImageToBuffer(srcImage: Image, srcImageLayout: ImageLayout, dstBuffer: Buffer, block: CmdCopyImageToBufferBuilder.() -> Unit)

	fun updateBuffer(dstBuffer: Buffer, dstOffset: ULong, pData: IoBuffer)

	fun fillBuffer(dstBuffer: Buffer, dstOffset: ULong, size: ULong, data: UInt)

	fun resolveImage(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			block: CmdResolveImageBuilder.() -> Unit
	)

	fun pipelineBarrier(srcStages: VkFlag<PipelineStage>, dstStages: VkFlag<PipelineStage>, dependencies: VkFlag<Dependency>? = null, block: MemoryBarriersBuilder.() -> Unit)

	fun waitEvents(events: Collection<Event>, srcStages: VkFlag<PipelineStage>, dstStages: VkFlag<PipelineStage>, block: MemoryBarriersBuilder.() -> Unit)

	fun setEvent(event: Event, stageMask: VkFlag<PipelineStage>)

	fun resetEvent(event: Event, stageMask: VkFlag<PipelineStage>)

	fun beginQuery(queryPool: QueryPool, query: UInt, flags: VkFlag<QueryControl>?)

	fun endQuery(queryPool: QueryPool, query: UInt)

	fun beginConditionalRenderingEXT(
			buffer: Buffer,
			offset: ULong,
			flags: VkFlag<ConditionalRenderingEXT>?,
			block: ConditionalRenderingBeginInfoEXTBuilder.() -> Unit = {}
	)

	fun endConditionalRenderingEXT()

	fun resetQueryPool(queryPool: QueryPool, firstQuery: UInt, queryCount: UInt)

	fun writeTimestamp(pipelineStage: PipelineStage, queryPool: QueryPool, query: UInt)

	fun copyQueryPoolResults(
			queryPool: QueryPool,
			firstQuery: UInt,
			queryCount: UInt,
			dstBuffer: Buffer,
			dstOffset: ULong,
			stride: ULong,
			flags: VkFlag<QueryResult>?
	)

	fun pushConstants(
			layout: PipelineLayout,
			stageFlags: VkFlag<ShaderStage>,
			offset: UInt,
			pValues: IoBuffer
	)

	fun beginRenderPass(
			renderPass: RenderPass,
			framebuffer: Framebuffer,
			clearValues: Collection<ClearValue>?,
			contents: SubpassContents,
			block: RenderPassBeginInfoBuilder.() -> Unit
	)

	fun nextSubpass(contents: SubpassContents)

	fun endRenderPass()

	fun executeCommands(commandBuffers: Collection<CommandBuffer>)

	fun debugMarkerBeginEXT(block: DebugMarkerMarkerInfoEXTBuilder.() -> Unit = {})

	fun debugMarkerEndEXT()

	fun debugMarkerInsertEXT(block: DebugMarkerMarkerInfoEXTBuilder.() -> Unit = {})

	fun drawIndirectCountAMD(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	)

	fun drawIndexedIndirectCountAMD(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	)

	fun processCommandsNVX(
			objectTable: ObjectTableNVX,
			indirectCommandsLayout: IndirectCommandsLayoutNVX,
			targetCommandBuffer: CommandBuffer?,
			sequencesCountBuffer: Buffer?,
			sequencesIndexBuffer: Buffer?,
			block: CmdProcessCommandsInfoNVXBuilder.() -> Unit
	)

	fun reserveSpaceForCommandsNVX(
			objectTable: ObjectTableNVX,
			indirectCommandsLayout: IndirectCommandsLayoutNVX,
			block: CmdReserveSpaceForCommandsInfoNVXBuilder.() -> Unit = {}
	)

	fun pushDescriptorSetKHR(
			pipelineBindPoint: PipelineBindPoint,
			layout: PipelineLayout,
			set: UInt,
			block: CmdPushDescriptorSetKHRBuilder.() -> Unit
	)

	fun setDeviceMask(deviceMask: UInt)

	fun dispatchBase(
			baseGroupX: UInt,
			baseGroupY: UInt,
			baseGroupZ: UInt,
			groupCountX: UInt,
			groupCountY: UInt,
			groupCountZ: UInt
	)

	fun pushDescriptorSetWithTemplateKHR(
			descriptorUpdateTemplate: DescriptorUpdateTemplate,
			layout: PipelineLayout,
			set: UInt,
			pData: IoBuffer
	)

	fun setViewportWScalingNV(firstViewport: UInt, block: CmdSetViewportWScalingNVBuilder.() -> Unit)

	fun setDiscardRectangleEXT(firstDiscardRectangle: UInt, block: CmdSetDiscardRectangleEXTBuilder.() -> Unit)

	fun setSampleLocationsEXT(block: SampleLocationsInfoEXTBuilder.() -> Unit)

	fun beginDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit = {})

	fun endDebugUtilsLabelEXT()

	fun insertDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit = {})

	fun writeBufferMarkerAMD(pipelineStage: PipelineStage, dstBuffer: Buffer, dstOffset: ULong, marker: UInt)

	fun endRenderPass2KHR(block: SubpassEndInfoKHRBuilder.() -> Unit = {})

	fun drawIndirectCountKHR(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	)

	fun drawIndexedIndirectCountKHR(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	)

	fun setCheckpointNV(pCheckpointMarker: IoBuffer)

	fun bindTransformFeedbackBuffersEXT(
			firstBinding: UInt,
			buffers: Collection<Buffer>,
			offsets: ULongArray,
			sizes: ULongArray
	)

	fun beginTransformFeedbackEXT(
			firstCounterBuffer: UInt,
			counterBuffers: Collection<Buffer>?,
			counterBufferOffsets: ULongArray
	)

	fun endTransformFeedbackEXT(
			firstCounterBuffer: UInt,
			counterBuffers: Collection<Buffer>?,
			counterBufferOffsets: ULongArray
	)

	fun beginQueryIndexedEXT(
			queryPool: QueryPool,
			query: UInt,
			flags: VkFlag<QueryControl>?,
			index: UInt
	)

	fun endQueryIndexedEXT(
			queryPool: QueryPool,
			query: UInt,
			index: UInt
	)

	fun drawIndirectByteCountEXT(
			instanceCount: UInt,
			firstInstance: UInt,
			counterBuffer: Buffer,
			counterBufferOffset: ULong,
			counterOffset: UInt,
			vertexStride: UInt
	)

	fun setExclusiveScissorNV(firstExclusiveScissor: UInt, block: CmdSetExclusiveScissorNVBuilder.() -> Unit)

	fun bindShadingRateImageNV(imageView: ImageView, imageLayout: ImageLayout)

	fun setViewportShadingRatePaletteNV(firstViewport: UInt, block: CmdSetViewportShadingRatePaletteNVBuilder.() -> Unit)

	fun setCoarseSampleOrderNV(sampleOrderType: CoarseSampleOrderTypeNV, block: CmdSetCoarseSampleOrderNVBuilder.() -> Unit)

	fun drawMeshTasksNV(taskCount: UInt, firstTask: UInt)

	fun drawMeshTasksIndirectNV(
			buffer: Buffer,
			offset: ULong,
			drawCount: UInt,
			stride: UInt
	)

	fun drawMeshTasksIndirectCountNV(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	)

	fun copyAccelerationStructureNV(
			dst: AccelerationStructureNV,
			src: AccelerationStructureNV,
			mode: CopyAccelerationStructureModeNV
	)

	fun writeAccelerationStructuresPropertiesNV(
			accelerationStructures: Collection<AccelerationStructureNV>,
			queryType: QueryType,
			queryPool: QueryPool,
			firstQuery: UInt
	)

	fun buildAccelerationStructureNV(
			instanceData: Buffer?,
			instanceOffset: ULong,
			update: Boolean,
			dst: AccelerationStructureNV,
			src: AccelerationStructureNV?,
			scratch: Buffer,
			scratchOffset: ULong,
			block: AccelerationStructureInfoNVBuilder.() -> Unit
	)

	fun traceRaysNV(
			raygenShaderBindingTableBuffer: Buffer,
			raygenShaderBindingOffset: ULong,
			missShaderBindingTableBuffer: Buffer?,
			missShaderBindingOffset: ULong,
			missShaderBindingStride: ULong,
			hitShaderBindingTableBuffer: Buffer?,
			hitShaderBindingOffset: ULong,
			hitShaderBindingStride: ULong,
			callableShaderBindingTableBuffer: Buffer?,
			callableShaderBindingOffset: ULong,
			callableShaderBindingStride: ULong,
			width: UInt,
			height: UInt,
			depth: UInt
	)
}

