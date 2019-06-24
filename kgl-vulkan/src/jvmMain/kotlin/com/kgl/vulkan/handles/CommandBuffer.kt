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
import com.kgl.vulkan.utils.*
import kotlinx.io.core.IoBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.AMDBufferMarker.vkCmdWriteBufferMarkerAMD
import org.lwjgl.vulkan.AMDDrawIndirectCount.vkCmdDrawIndexedIndirectCountAMD
import org.lwjgl.vulkan.AMDDrawIndirectCount.vkCmdDrawIndirectCountAMD
import org.lwjgl.vulkan.EXTConditionalRendering.vkCmdBeginConditionalRenderingEXT
import org.lwjgl.vulkan.EXTConditionalRendering.vkCmdEndConditionalRenderingEXT
import org.lwjgl.vulkan.EXTDebugMarker.*
import org.lwjgl.vulkan.EXTDebugUtils.*
import org.lwjgl.vulkan.EXTDiscardRectangles.vkCmdSetDiscardRectangleEXT
import org.lwjgl.vulkan.EXTSampleLocations.vkCmdSetSampleLocationsEXT
import org.lwjgl.vulkan.EXTTransformFeedback.*
import org.lwjgl.vulkan.KHRCreateRenderpass2.vkCmdEndRenderPass2KHR
import org.lwjgl.vulkan.KHRDrawIndirectCount.vkCmdDrawIndexedIndirectCountKHR
import org.lwjgl.vulkan.KHRDrawIndirectCount.vkCmdDrawIndirectCountKHR
import org.lwjgl.vulkan.KHRPushDescriptor.vkCmdPushDescriptorSetKHR
import org.lwjgl.vulkan.KHRPushDescriptor.vkCmdPushDescriptorSetWithTemplateKHR
import org.lwjgl.vulkan.NVClipSpaceWScaling.vkCmdSetViewportWScalingNV
import org.lwjgl.vulkan.NVDeviceDiagnosticCheckpoints.vkCmdSetCheckpointNV
import org.lwjgl.vulkan.NVMeshShader.*
import org.lwjgl.vulkan.NVRayTracing.*
import org.lwjgl.vulkan.NVScissorExclusive.vkCmdSetExclusiveScissorNV
import org.lwjgl.vulkan.NVShadingRateImage.*
import org.lwjgl.vulkan.NVXDeviceGeneratedCommands.vkCmdProcessCommandsNVX
import org.lwjgl.vulkan.NVXDeviceGeneratedCommands.vkCmdReserveSpaceForCommandsNVX
import org.lwjgl.vulkan.VK11.*

actual class CommandBuffer(override val ptr: VkCommandBuffer, actual val commandPool: CommandPool) : VkHandleJVM<VkCommandBuffer>(), VkHandle {
	override fun close() {
		vkFreeCommandBuffers(commandPool.device.ptr, commandPool.ptr, ptr)
	}

	actual fun begin(block: CommandBufferBeginInfoBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkCommandBufferBeginInfo.callocStack()
			val builder = CommandBufferBeginInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val result = vkBeginCommandBuffer(commandBuffer.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun end() {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val result = vkEndCommandBuffer(commandBuffer.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun reset(flags: VkFlag<CommandBufferReset>?) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val result = vkResetCommandBuffer(commandBuffer.toVkType(), flags.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindPipeline(pipelineBindPoint: PipelineBindPoint, pipeline: Pipeline) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBindPipeline(commandBuffer.toVkType(), pipelineBindPoint.toVkType(),
					pipeline.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setViewport(firstViewport: UInt, block: ViewportsBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = ViewportsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkViewport::callocStack, ::ViewportBuilder)
			vkCmdSetViewport(commandBuffer.toVkType(), firstViewport.toVkType(), targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setScissor(firstScissor: UInt, block: Rect2DsBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = Rect2DsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkRect2D::callocStack, ::Rect2DBuilder)
			vkCmdSetScissor(commandBuffer.toVkType(), firstScissor.toVkType(), targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setLineWidth(lineWidth: Float) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetLineWidth(commandBuffer.toVkType(), lineWidth.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setDepthBias(
			depthBiasConstantFactor: Float,
			depthBiasClamp: Float,
			depthBiasSlopeFactor: Float
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetDepthBias(commandBuffer.toVkType(), depthBiasConstantFactor.toVkType(),
					depthBiasClamp.toVkType(), depthBiasSlopeFactor.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setDepthBounds(minDepthBounds: Float, maxDepthBounds: Float) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetDepthBounds(commandBuffer.toVkType(), minDepthBounds.toVkType(),
					maxDepthBounds.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setStencilCompareMask(faceMask: VkFlag<StencilFace>, compareMask: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetStencilCompareMask(commandBuffer.toVkType(), faceMask.toVkType(),
					compareMask.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setStencilWriteMask(faceMask: VkFlag<StencilFace>, writeMask: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetStencilWriteMask(commandBuffer.toVkType(), faceMask.toVkType(),
					writeMask.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setStencilReference(faceMask: VkFlag<StencilFace>, reference: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetStencilReference(commandBuffer.toVkType(), faceMask.toVkType(),
					reference.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setBlendConstants(r: Float, g: Float, b: Float, a: Float) {
		vkCmdSetBlendConstants(ptr, floatArrayOf(r, g, b, a))
	}

	actual fun bindDescriptorSets(
			pipelineBindPoint: PipelineBindPoint,
			layout: PipelineLayout,
			firstSet: UInt,
			descriptorSets: Collection<DescriptorSet>,
			dynamicOffsets: UIntArray?
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBindDescriptorSets(commandBuffer.toVkType(), pipelineBindPoint.toVkType(),
					layout.toVkType(), firstSet.toVkType(), descriptorSets.toVkType(),
					dynamicOffsets?.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindIndexBuffer(
			buffer: Buffer,
			offset: ULong,
			indexType: IndexType
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBindIndexBuffer(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType(),
					indexType.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindVertexBuffers(firstBinding: UInt, buffers: Collection<Pair<Buffer, ULong>>) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBindVertexBuffers(commandBuffer.toVkType(), firstBinding.toVkType(),
					buffers.map { it.first }.toVkType(), buffers.map { it.second.toLong() }.toLongArray().toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun draw(
			vertexCount: UInt,
			instanceCount: UInt,
			firstVertex: UInt,
			firstInstance: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDraw(commandBuffer.toVkType(), vertexCount.toVkType(), instanceCount.toVkType(),
					firstVertex.toVkType(), firstInstance.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndexed(
			indexCount: UInt,
			instanceCount: UInt,
			firstIndex: UInt,
			vertexOffset: Int,
			firstInstance: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndexed(commandBuffer.toVkType(), indexCount.toVkType(),
					instanceCount.toVkType(), firstIndex.toVkType(), vertexOffset.toVkType(),
					firstInstance.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndirect(
			buffer: Buffer,
			offset: ULong,
			drawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndirect(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType(),
					drawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndexedIndirect(
			buffer: Buffer,
			offset: ULong,
			drawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndexedIndirect(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType(),
					drawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun dispatch(groupCountX: UInt, groupCountY: UInt, groupCountZ: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDispatch(commandBuffer.toVkType(), groupCountX.toVkType(), groupCountY.toVkType(),
					groupCountZ.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun dispatchIndirect(buffer: Buffer, offset: ULong) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDispatchIndirect(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun copyBuffer(srcBuffer: Buffer, dstBuffer: Buffer, block: BufferCopysBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = BufferCopysBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkBufferCopy::callocStack, ::BufferCopyBuilder)
			vkCmdCopyBuffer(commandBuffer.toVkType(), srcBuffer.toVkType(), dstBuffer.toVkType(),
					targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun copyImage(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			block: ImageCopysBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = ImageCopysBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkImageCopy::callocStack, ::ImageCopyBuilder)
			vkCmdCopyImage(commandBuffer.toVkType(), srcImage.toVkType(), srcImageLayout.toVkType(),
					dstImage.toVkType(), dstImageLayout.toVkType(), targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun blitImage(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			filter: Filter,
			block: ImageBlitsBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = ImageBlitsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkImageBlit::callocStack, ::ImageBlitBuilder)
			vkCmdBlitImage(commandBuffer.toVkType(), srcImage.toVkType(), srcImageLayout.toVkType(),
					dstImage.toVkType(), dstImageLayout.toVkType(), targetArray, filter.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun copyBufferToImage(srcBuffer: Buffer, dstImage: Image, dstImageLayout: ImageLayout, block: BufferImageCopysBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = BufferImageCopysBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkBufferImageCopy::callocStack, ::BufferImageCopyBuilder)
			vkCmdCopyBufferToImage(commandBuffer.toVkType(), srcBuffer.toVkType(),
					dstImage.toVkType(), dstImageLayout.toVkType(), targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun copyImageToBuffer(srcImage: Image, srcImageLayout: ImageLayout, dstBuffer: Buffer, block: BufferImageCopysBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = BufferImageCopysBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkBufferImageCopy::callocStack, ::BufferImageCopyBuilder)
			vkCmdCopyImageToBuffer(commandBuffer.toVkType(), srcImage.toVkType(),
					srcImageLayout.toVkType(), dstBuffer.toVkType(), targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun updateBuffer(dstBuffer: Buffer, dstOffset: ULong, data: IoBuffer) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			data.readDirect {
				vkCmdUpdateBuffer(commandBuffer.toVkType(), dstBuffer.toVkType(), dstOffset.toVkType(), it)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun fillBuffer(dstBuffer: Buffer, dstOffset: ULong, size: ULong, data: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdFillBuffer(commandBuffer.toVkType(), dstBuffer.toVkType(), dstOffset.toVkType(),
					size.toVkType(), data.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun resolveImage(srcImage: Image, srcImageLayout: ImageLayout, dstImage: Image, dstImageLayout: ImageLayout, block: ImageResolvesBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = ImageResolvesBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkImageResolve::callocStack, ::ImageResolveBuilder)
			vkCmdResolveImage(commandBuffer.toVkType(), srcImage.toVkType(),
					srcImageLayout.toVkType(), dstImage.toVkType(), dstImageLayout.toVkType(),
					targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun pipelineBarrier(srcStages: VkFlag<PipelineStage>, dstStages: VkFlag<PipelineStage>, dependencies: VkFlag<Dependency>?, block: BarrierBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val builder = BarrierBuilder().apply(block)
			vkCmdPipelineBarrier(commandBuffer.toVkType(), srcStages.toVkType(),
					dstStages.toVkType(), dependencies.toVkType(),
					builder.targets.mapToStackArray(VkMemoryBarrier::callocStack, ::MemoryBarrierBuilder),
					builder.targets1.mapToStackArray(VkBufferMemoryBarrier::callocStack, ::BufferMemoryBarrierBuilder),
					builder.targets2.mapToStackArray(VkImageMemoryBarrier::callocStack, ::ImageMemoryBarrierBuilder))
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun waitEvents(events: Collection<Event>, srcStages: VkFlag<PipelineStage>, dstStages: VkFlag<PipelineStage>, block: BarrierBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val builder = BarrierBuilder().apply(block)
			vkCmdWaitEvents(commandBuffer.toVkType(),
					events.toVkType(),
					srcStages.toVkType(),
					dstStages.toVkType(),
					builder.targets.mapToStackArray(VkMemoryBarrier::callocStack, ::MemoryBarrierBuilder),
					builder.targets1.mapToStackArray(VkBufferMemoryBarrier::callocStack, ::BufferMemoryBarrierBuilder),
					builder.targets2.mapToStackArray(VkImageMemoryBarrier::callocStack, ::ImageMemoryBarrierBuilder))
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setEvent(event: Event, stageMask: VkFlag<PipelineStage>) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetEvent(commandBuffer.toVkType(), event.toVkType(), stageMask.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun resetEvent(event: Event, stageMask: VkFlag<PipelineStage>) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdResetEvent(commandBuffer.toVkType(), event.toVkType(), stageMask.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun beginQuery(queryPool: QueryPool, query: UInt, flags: VkFlag<QueryControl>?) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBeginQuery(commandBuffer.toVkType(), queryPool.toVkType(), query.toVkType(),
					flags.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endQuery(queryPool: QueryPool, query: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdEndQuery(commandBuffer.toVkType(), queryPool.toVkType(), query.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun beginConditionalRenderingEXT(
			buffer: Buffer,
			offset: ULong,
			flags: VkFlag<ConditionalRenderingEXT>?,
			block: ConditionalRenderingBeginInfoEXTBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkConditionalRenderingBeginInfoEXT.callocStack()
			val builder = ConditionalRenderingBeginInfoEXTBuilder(target)
			builder.init(buffer, offset, flags)
			builder.apply(block)
			vkCmdBeginConditionalRenderingEXT(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endConditionalRenderingEXT() {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdEndConditionalRenderingEXT(commandBuffer.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun resetQueryPool(
			queryPool: QueryPool,
			firstQuery: UInt,
			queryCount: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdResetQueryPool(commandBuffer.toVkType(), queryPool.toVkType(),
					firstQuery.toVkType(), queryCount.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun writeTimestamp(
			pipelineStage: PipelineStage,
			queryPool: QueryPool,
			query: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdWriteTimestamp(commandBuffer.toVkType(), pipelineStage.toVkType(),
					queryPool.toVkType(), query.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun copyQueryPoolResults(
			queryPool: QueryPool,
			firstQuery: UInt,
			queryCount: UInt,
			dstBuffer: Buffer,
			dstOffset: ULong,
			stride: ULong,
			flags: VkFlag<QueryResult>?
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdCopyQueryPoolResults(commandBuffer.toVkType(), queryPool.toVkType(),
					firstQuery.toVkType(), queryCount.toVkType(), dstBuffer.toVkType(),
					dstOffset.toVkType(), stride.toVkType(), flags.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun pushConstants(
			layout: PipelineLayout,
			stageFlags: VkFlag<ShaderStage>,
			offset: UInt,
			values: IoBuffer
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			values.readDirect {
				vkCmdPushConstants(commandBuffer.toVkType(), layout.toVkType(), stageFlags.toVkType(),
						offset.toVkType(), it)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun beginRenderPass(
			renderPass: RenderPass,
			framebuffer: Framebuffer,
			contents: SubpassContents,
			block: RenderPassBeginInfoBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkRenderPassBeginInfo.callocStack()
			val builder = RenderPassBeginInfoBuilder(target)
			builder.init(renderPass, framebuffer)
			builder.apply(block)
			vkCmdBeginRenderPass(commandBuffer.toVkType(), target, contents.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun nextSubpass(contents: SubpassContents) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdNextSubpass(commandBuffer.toVkType(), contents.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endRenderPass() {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdEndRenderPass(commandBuffer.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun executeCommands(commandBuffers: Collection<CommandBuffer>) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdExecuteCommands(commandBuffer.toVkType(), commandBuffers.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun debugMarkerBeginEXT(block: DebugMarkerMarkerInfoEXTBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugMarkerMarkerInfoEXT.callocStack()
			val builder = DebugMarkerMarkerInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkCmdDebugMarkerBeginEXT(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun debugMarkerEndEXT() {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDebugMarkerEndEXT(commandBuffer.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun debugMarkerInsertEXT(block: DebugMarkerMarkerInfoEXTBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugMarkerMarkerInfoEXT.callocStack()
			val builder = DebugMarkerMarkerInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkCmdDebugMarkerInsertEXT(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndirectCountAMD(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndirectCountAMD(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndexedIndirectCountAMD(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndexedIndirectCountAMD(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun processCommandsNVX(
			objectTable: ObjectTableNVX,
			indirectCommandsLayout: IndirectCommandsLayoutNVX,
			targetCommandBuffer: CommandBuffer?,
			sequencesCountBuffer: Buffer?,
			sequencesIndexBuffer: Buffer?,
			block: CmdProcessCommandsInfoNVXBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkCmdProcessCommandsInfoNVX.callocStack()
			val builder = CmdProcessCommandsInfoNVXBuilder(target)
			builder.init(objectTable, indirectCommandsLayout, targetCommandBuffer,
					sequencesCountBuffer, sequencesIndexBuffer)
			builder.apply(block)
			vkCmdProcessCommandsNVX(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun reserveSpaceForCommandsNVX(
			objectTable: ObjectTableNVX,
			indirectCommandsLayout: IndirectCommandsLayoutNVX,
			block: CmdReserveSpaceForCommandsInfoNVXBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkCmdReserveSpaceForCommandsInfoNVX.callocStack()
			val builder = CmdReserveSpaceForCommandsInfoNVXBuilder(target)
			builder.init(objectTable, indirectCommandsLayout)
			builder.apply(block)
			vkCmdReserveSpaceForCommandsNVX(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun pushDescriptorSetKHR(
			pipelineBindPoint: PipelineBindPoint,
			layout: PipelineLayout,
			set: UInt,
			block: WriteDescriptorSetsBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = WriteDescriptorSetsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkWriteDescriptorSet::callocStack, ::WriteDescriptorSetBuilder)
			vkCmdPushDescriptorSetKHR(commandBuffer.toVkType(), pipelineBindPoint.toVkType(),
					layout.toVkType(), set.toVkType(), targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setDeviceMask(deviceMask: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetDeviceMask(commandBuffer.toVkType(), deviceMask.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun dispatchBase(
			baseGroupX: UInt,
			baseGroupY: UInt,
			baseGroupZ: UInt,
			groupCountX: UInt,
			groupCountY: UInt,
			groupCountZ: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDispatchBase(commandBuffer.toVkType(), baseGroupX.toVkType(),
					baseGroupY.toVkType(), baseGroupZ.toVkType(), groupCountX.toVkType(),
					groupCountY.toVkType(), groupCountZ.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun pushDescriptorSetWithTemplateKHR(
			descriptorUpdateTemplate: DescriptorUpdateTemplate,
			layout: PipelineLayout,
			set: UInt,
			data: IoBuffer
	) {
		TODO()
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			data.readDirect {
				vkCmdPushDescriptorSetWithTemplateKHR(commandBuffer.toVkType(),
						descriptorUpdateTemplate.toVkType(), layout.toVkType(), set.toVkType(),
						0)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setViewportWScalingNV(firstViewport: UInt, block: ViewportWScalingNVsBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = ViewportWScalingNVsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkViewportWScalingNV::callocStack, ::ViewportWScalingNVBuilder)
			vkCmdSetViewportWScalingNV(commandBuffer.toVkType(), firstViewport.toVkType(),
					targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setDiscardRectangleEXT(firstDiscardRectangle: UInt, block: Rect2DsBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = Rect2DsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkRect2D::callocStack, ::Rect2DBuilder)
			vkCmdSetDiscardRectangleEXT(commandBuffer.toVkType(), firstDiscardRectangle.toVkType(),
					targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setSampleLocationsEXT(block: SampleLocationsInfoEXTBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkSampleLocationsInfoEXT.callocStack()
			val builder = SampleLocationsInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkCmdSetSampleLocationsEXT(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun beginDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsLabelEXT.callocStack()
			val builder = DebugUtilsLabelEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkCmdBeginDebugUtilsLabelEXT(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endDebugUtilsLabelEXT() {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdEndDebugUtilsLabelEXT(commandBuffer.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun insertDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsLabelEXT.callocStack()
			val builder = DebugUtilsLabelEXTBuilder(target)
			builder.init()
			builder.apply(block)
			vkCmdInsertDebugUtilsLabelEXT(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun writeBufferMarkerAMD(pipelineStage: PipelineStage, dstBuffer: Buffer, dstOffset: ULong, marker: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdWriteBufferMarkerAMD(commandBuffer.toVkType(), pipelineStage.toVkType(),
					dstBuffer.toVkType(), dstOffset.toVkType(), marker.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endRenderPass2KHR(block: SubpassEndInfoKHRBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkSubpassEndInfoKHR.callocStack()
			val builder = SubpassEndInfoKHRBuilder(target)
			builder.init()
			builder.apply(block)
			vkCmdEndRenderPass2KHR(commandBuffer.toVkType(), target)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndirectCountKHR(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndirectCountKHR(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndexedIndirectCountKHR(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndexedIndirectCountKHR(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setCheckpointNV(pCheckpointMarker: IoBuffer) {
		TODO()
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdSetCheckpointNV(commandBuffer.toVkType(), 0)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindTransformFeedbackBuffersEXT(
			firstBinding: UInt,
			buffers: Collection<Buffer>,
			offsets: ULongArray,
			sizes: ULongArray
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBindTransformFeedbackBuffersEXT(commandBuffer.toVkType(), firstBinding.toVkType(),
					buffers.toVkType(), offsets.toVkType(), sizes.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun beginTransformFeedbackEXT(
			firstCounterBuffer: UInt,
			counterBuffers: Collection<Buffer>?,
			counterBufferOffsets: ULongArray
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBeginTransformFeedbackEXT(commandBuffer.toVkType(), firstCounterBuffer.toVkType(),
					counterBuffers?.toVkType(), counterBufferOffsets.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endTransformFeedbackEXT(
			firstCounterBuffer: UInt,
			counterBuffers: Collection<Buffer>?,
			counterBufferOffsets: ULongArray
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdEndTransformFeedbackEXT(commandBuffer.toVkType(), firstCounterBuffer.toVkType(),
					counterBuffers?.toVkType(), counterBufferOffsets.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun beginQueryIndexedEXT(
			queryPool: QueryPool,
			query: UInt,
			flags: VkFlag<QueryControl>?,
			index: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBeginQueryIndexedEXT(commandBuffer.toVkType(), queryPool.toVkType(),
					query.toVkType(), flags.toVkType(), index.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun endQueryIndexedEXT(
			queryPool: QueryPool,
			query: UInt,
			index: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdEndQueryIndexedEXT(commandBuffer.toVkType(), queryPool.toVkType(),
					query.toVkType(), index.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawIndirectByteCountEXT(
			instanceCount: UInt,
			firstInstance: UInt,
			counterBuffer: Buffer,
			counterBufferOffset: ULong,
			counterOffset: UInt,
			vertexStride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawIndirectByteCountEXT(commandBuffer.toVkType(), instanceCount.toVkType(),
					firstInstance.toVkType(), counterBuffer.toVkType(),
					counterBufferOffset.toVkType(), counterOffset.toVkType(),
					vertexStride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setExclusiveScissorNV(firstExclusiveScissor: UInt, block: Rect2DsBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = Rect2DsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkRect2D::callocStack, ::Rect2DBuilder)
			vkCmdSetExclusiveScissorNV(commandBuffer.toVkType(), firstExclusiveScissor.toVkType(),
					targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindShadingRateImageNV(imageView: ImageView, imageLayout: ImageLayout) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdBindShadingRateImageNV(commandBuffer.toVkType(), imageView.toVkType(),
					imageLayout.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setViewportShadingRatePaletteNV(firstViewport: UInt, block: ShadingRatePaletteNVsBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = ShadingRatePaletteNVsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkShadingRatePaletteNV::callocStack, ::ShadingRatePaletteNVBuilder)
			vkCmdSetViewportShadingRatePaletteNV(commandBuffer.toVkType(), firstViewport.toVkType(),
					targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setCoarseSampleOrderNV(sampleOrderType: CoarseSampleOrderTypeNV, block: CoarseSampleOrderCustomNVsBuilder.() -> Unit) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val targets = CoarseSampleOrderCustomNVsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkCoarseSampleOrderCustomNV::callocStack, ::CoarseSampleOrderCustomNVBuilder)
			vkCmdSetCoarseSampleOrderNV(commandBuffer.toVkType(), sampleOrderType.toVkType(),
					targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawMeshTasksNV(taskCount: UInt, firstTask: UInt) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawMeshTasksNV(commandBuffer.toVkType(), taskCount.toVkType(),
					firstTask.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawMeshTasksIndirectNV(
			buffer: Buffer,
			offset: ULong,
			drawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawMeshTasksIndirectNV(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), drawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun drawMeshTasksIndirectCountNV(
			buffer: Buffer,
			offset: ULong,
			countBuffer: Buffer,
			countBufferOffset: ULong,
			maxDrawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdDrawMeshTasksIndirectCountNV(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun copyAccelerationStructureNV(
			dst: AccelerationStructureNV,
			src: AccelerationStructureNV,
			mode: CopyAccelerationStructureModeNV
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdCopyAccelerationStructureNV(commandBuffer.toVkType(), dst.toVkType(),
					src.toVkType(), mode.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun writeAccelerationStructuresPropertiesNV(
			accelerationStructures: Collection<AccelerationStructureNV>,
			queryType: QueryType,
			queryPool: QueryPool,
			firstQuery: UInt
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdWriteAccelerationStructuresPropertiesNV(commandBuffer.toVkType(),
					accelerationStructures.toVkType(), queryType.toVkType(), queryPool.toVkType(),
					firstQuery.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun buildAccelerationStructureNV(
			instanceData: Buffer?,
			instanceOffset: ULong,
			update: Boolean,
			dst: AccelerationStructureNV,
			src: AccelerationStructureNV?,
			scratch: Buffer,
			scratchOffset: ULong,
			block: AccelerationStructureInfoNVBuilder.() -> Unit
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			val target = VkAccelerationStructureInfoNV.callocStack()
			val builder = AccelerationStructureInfoNVBuilder(target)
			builder.init()
			builder.apply(block)
			vkCmdBuildAccelerationStructureNV(commandBuffer.toVkType(), target,
					instanceData.toVkType(), instanceOffset.toVkType(), update.toVkType(),
					dst.toVkType(), src.toVkType(), scratch.toVkType(), scratchOffset.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun traceRaysNV(
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
	) {
		val commandBuffer = this
		MemoryStack.stackPush()
		try {
			vkCmdTraceRaysNV(commandBuffer.toVkType(), raygenShaderBindingTableBuffer.toVkType(),
					raygenShaderBindingOffset.toVkType(), missShaderBindingTableBuffer.toVkType(),
					missShaderBindingOffset.toVkType(), missShaderBindingStride.toVkType(),
					hitShaderBindingTableBuffer.toVkType(), hitShaderBindingOffset.toVkType(),
					hitShaderBindingStride.toVkType(), callableShaderBindingTableBuffer.toVkType(),
					callableShaderBindingOffset.toVkType(), callableShaderBindingStride.toVkType(),
					width.toVkType(), height.toVkType(), depth.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}
}

