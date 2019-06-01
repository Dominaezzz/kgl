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

import com.kgl.core.VirtualStack
import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*
import kotlinx.io.core.IoBuffer

actual class CommandBuffer(override val ptr: VkCommandBuffer, actual val commandPool: CommandPool) : VkHandleNative<VkCommandBuffer>(), VkHandle {
	internal val dispatchTable = commandPool.dispatchTable

	override fun close() {
		VirtualStack.push()
		try {
			val commandBuffer = VirtualStack.alloc<VkCommandBufferVar> { value = this@CommandBuffer.ptr }
			dispatchTable.vkFreeCommandBuffers(commandPool.device.ptr, commandPool.ptr, 1U, commandBuffer.ptr)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun begin(block: CommandBufferBeginInfoBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkCommandBufferBeginInfo>().ptr
			val builder = CommandBufferBeginInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val result = dispatchTable.vkBeginCommandBuffer(commandBuffer.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun end() {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val result = dispatchTable.vkEndCommandBuffer(commandBuffer.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun reset(flags: VkFlag<CommandBufferReset>?) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val result = dispatchTable.vkResetCommandBuffer(commandBuffer.toVkType(), flags.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindPipeline(pipelineBindPoint: PipelineBindPoint, pipeline: Pipeline) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBindPipeline(commandBuffer.toVkType(), pipelineBindPoint.toVkType(),
					pipeline.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setViewport(firstViewport: UInt, block: CmdSetViewportBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdSetViewportBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdSetViewport(commandBuffer.toVkType(), firstViewport.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setScissor(firstScissor: UInt, block: CmdSetScissorBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdSetScissorBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdSetScissor(commandBuffer.toVkType(), firstScissor.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setLineWidth(lineWidth: Float) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetLineWidth(commandBuffer.toVkType(), lineWidth.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setDepthBias(
			depthBiasConstantFactor: Float,
			depthBiasClamp: Float,
			depthBiasSlopeFactor: Float
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetDepthBias(commandBuffer.toVkType(), depthBiasConstantFactor.toVkType(),
					depthBiasClamp.toVkType(), depthBiasSlopeFactor.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setDepthBounds(minDepthBounds: Float, maxDepthBounds: Float) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetDepthBounds(commandBuffer.toVkType(), minDepthBounds.toVkType(),
					maxDepthBounds.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setStencilCompareMask(faceMask: VkFlag<StencilFace>, compareMask: UInt) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetStencilCompareMask(commandBuffer.toVkType(), faceMask.toVkType(),
					compareMask.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setStencilWriteMask(faceMask: VkFlag<StencilFace>, writeMask: UInt) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetStencilWriteMask(commandBuffer.toVkType(), faceMask.toVkType(),
					writeMask.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setStencilReference(faceMask: VkFlag<StencilFace>, reference: UInt) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetStencilReference(commandBuffer.toVkType(), faceMask.toVkType(),
					reference.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setBlendConstants(r: Float, g: Float, b: Float, a: Float) {
		VirtualStack.push()
		try {
			val constants = VirtualStack.allocArray<FloatVar>(4)
			constants[0] = r
			constants[1] = g
			constants[2] = b
			constants[3] = a
			dispatchTable.vkCmdSetBlendConstants(ptr, constants)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindDescriptorSets(
			pipelineBindPoint: PipelineBindPoint,
			layout: PipelineLayout,
			firstSet: UInt,
			descriptorSets: Collection<DescriptorSet>,
			dynamicOffsets: UIntArray?
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBindDescriptorSets(commandBuffer.toVkType(), pipelineBindPoint.toVkType(),
					layout.toVkType(), firstSet.toVkType(), descriptorSets.size.toUInt(),
					descriptorSets.toVkType(), dynamicOffsets?.size?.toUInt() ?: 0u,
					dynamicOffsets?.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindIndexBuffer(
			buffer: Buffer,
			offset: ULong,
			indexType: IndexType
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBindIndexBuffer(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType(),
					indexType.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindVertexBuffers(firstBinding: UInt, buffers: Collection<Pair<Buffer, ULong>>) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBindVertexBuffers(commandBuffer.toVkType(), firstBinding.toVkType(),
					buffers.size.toUInt(), buffers.map { it.first }.toVkType(),
					buffers.map { it.second.toLong() }.toLongArray().asULongArray().toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun draw(
			vertexCount: UInt,
			instanceCount: UInt,
			firstVertex: UInt,
			firstInstance: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDraw(commandBuffer.toVkType(), vertexCount.toVkType(), instanceCount.toVkType(),
					firstVertex.toVkType(), firstInstance.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndexed(commandBuffer.toVkType(), indexCount.toVkType(),
					instanceCount.toVkType(), firstIndex.toVkType(), vertexOffset.toVkType(),
					firstInstance.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun drawIndirect(
			buffer: Buffer,
			offset: ULong,
			drawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndirect(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType(),
					drawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun drawIndexedIndirect(
			buffer: Buffer,
			offset: ULong,
			drawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndexedIndirect(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType(),
					drawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun dispatch(
			groupCountX: UInt,
			groupCountY: UInt,
			groupCountZ: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDispatch(commandBuffer.toVkType(), groupCountX.toVkType(), groupCountY.toVkType(),
					groupCountZ.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun dispatchIndirect(buffer: Buffer, offset: ULong) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDispatchIndirect(commandBuffer.toVkType(), buffer.toVkType(), offset.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun copyBuffer(
			srcBuffer: Buffer,
			dstBuffer: Buffer,
			block: CmdCopyBufferBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdCopyBufferBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdCopyBuffer(commandBuffer.toVkType(), srcBuffer.toVkType(), dstBuffer.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun copyImage(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			block: CmdCopyImageBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdCopyImageBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdCopyImage(commandBuffer.toVkType(), srcImage.toVkType(), srcImageLayout.toVkType(),
					dstImage.toVkType(), dstImageLayout.toVkType(), targets.size.toUInt(),
					targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun blitImage(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			filter: Filter,
			block: CmdBlitImageBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdBlitImageBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdBlitImage(commandBuffer.toVkType(), srcImage.toVkType(), srcImageLayout.toVkType(),
					dstImage.toVkType(), dstImageLayout.toVkType(), targets.size.toUInt(),
					targetArray, filter.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun copyBufferToImage(
			srcBuffer: Buffer,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			block: CmdCopyBufferToImageBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdCopyBufferToImageBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdCopyBufferToImage(commandBuffer.toVkType(), srcBuffer.toVkType(),
					dstImage.toVkType(), dstImageLayout.toVkType(), targets.size.toUInt(),
					targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun copyImageToBuffer(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstBuffer: Buffer,
			block: CmdCopyImageToBufferBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdCopyImageToBufferBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdCopyImageToBuffer(commandBuffer.toVkType(), srcImage.toVkType(),
					srcImageLayout.toVkType(), dstBuffer.toVkType(), targets.size.toUInt(),
					targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun updateBuffer(
			dstBuffer: Buffer,
			dstOffset: ULong,
			data: IoBuffer
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdUpdateBuffer(commandBuffer.toVkType(), dstBuffer.toVkType(), dstOffset.toVkType(),
					data.readRemaining.toULong(), data.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun fillBuffer(
			dstBuffer: Buffer,
			dstOffset: ULong,
			size: ULong,
			data: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdFillBuffer(commandBuffer.toVkType(), dstBuffer.toVkType(), dstOffset.toVkType(),
					size.toVkType(), data.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun resolveImage(
			srcImage: Image,
			srcImageLayout: ImageLayout,
			dstImage: Image,
			dstImageLayout: ImageLayout,
			block: CmdResolveImageBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdResolveImageBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdResolveImage(commandBuffer.toVkType(), srcImage.toVkType(),
					srcImageLayout.toVkType(), dstImage.toVkType(), dstImageLayout.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun pipelineBarrier(srcStages: VkFlag<PipelineStage>, dstStages: VkFlag<PipelineStage>, dependencies: VkFlag<Dependency>?, block: MemoryBarriersBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val builder = MemoryBarriersBuilder().apply(block)
			dispatchTable.vkCmdPipelineBarrier(commandBuffer.toVkType(), srcStages.toVkType(),
					dstStages.toVkType(), dependencies.toVkType(),
					builder.targets.size.toUInt(),
					builder.targets.mapToStackArray(),
					builder.targets1.size.toUInt(),
					builder.targets1.mapToStackArray(),
					builder.targets2.size.toUInt(),
					builder.targets2.mapToStackArray())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun waitEvents(events: Collection<Event>, srcStages: VkFlag<PipelineStage>, dstStages: VkFlag<PipelineStage>, block: MemoryBarriersBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val builder = MemoryBarriersBuilder().apply(block)
			dispatchTable.vkCmdWaitEvents(commandBuffer.toVkType(),
					events.size.toUInt(),
					events.toVkType(),
					srcStages.toVkType(),
					dstStages.toVkType(),
					builder.targets.size.toUInt(),
					builder.targets.mapToStackArray(),
					builder.targets1.size.toUInt(),
					builder.targets1.mapToStackArray(),
					builder.targets2.size.toUInt(),
					builder.targets2.mapToStackArray())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setEvent(event: Event, stageMask: VkFlag<PipelineStage>) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetEvent(commandBuffer.toVkType(), event.toVkType(), stageMask.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun resetEvent(event: Event, stageMask: VkFlag<PipelineStage>) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdResetEvent(commandBuffer.toVkType(), event.toVkType(), stageMask.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun beginQuery(
			queryPool: QueryPool,
			query: UInt,
			flags: VkFlag<QueryControl>?
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBeginQuery(commandBuffer.toVkType(), queryPool.toVkType(), query.toVkType(),
					flags.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endQuery(queryPool: QueryPool, query: UInt) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdEndQuery(commandBuffer.toVkType(), queryPool.toVkType(), query.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun beginConditionalRenderingEXT(
			buffer: Buffer,
			offset: ULong,
			flags: VkFlag<ConditionalRenderingEXT>?,
			block: ConditionalRenderingBeginInfoEXTBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkConditionalRenderingBeginInfoEXT>().ptr
			val builder = ConditionalRenderingBeginInfoEXTBuilder(target.pointed)
			builder.init(buffer, offset, flags)
			builder.apply(block)
			dispatchTable.vkCmdBeginConditionalRenderingEXT!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endConditionalRenderingEXT() {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdEndConditionalRenderingEXT!!(commandBuffer.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun resetQueryPool(
			queryPool: QueryPool,
			firstQuery: UInt,
			queryCount: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdResetQueryPool(commandBuffer.toVkType(), queryPool.toVkType(),
					firstQuery.toVkType(), queryCount.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun writeTimestamp(
			pipelineStage: PipelineStage,
			queryPool: QueryPool,
			query: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdWriteTimestamp(commandBuffer.toVkType(), pipelineStage.toVkType(),
					queryPool.toVkType(), query.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdCopyQueryPoolResults(commandBuffer.toVkType(), queryPool.toVkType(),
					firstQuery.toVkType(), queryCount.toVkType(), dstBuffer.toVkType(),
					dstOffset.toVkType(), stride.toVkType(), flags.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun pushConstants(
			layout: PipelineLayout,
			stageFlags: VkFlag<ShaderStage>,
			offset: UInt,
			values: IoBuffer
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			values.readDirect {
				dispatchTable.vkCmdPushConstants(commandBuffer.toVkType(), layout.toVkType(), stageFlags.toVkType(),
						offset.toVkType(), values.readRemaining.toUInt(), it)
				values.readRemaining
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun beginRenderPass(
			renderPass: RenderPass,
			framebuffer: Framebuffer,
			contents: SubpassContents,
			block: RenderPassBeginInfoBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkRenderPassBeginInfo>().ptr
			val builder = RenderPassBeginInfoBuilder(target.pointed)
			builder.init(renderPass, framebuffer)
			builder.apply(block)
			dispatchTable.vkCmdBeginRenderPass(commandBuffer.toVkType(), target, contents.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun nextSubpass(contents: SubpassContents) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdNextSubpass(commandBuffer.toVkType(), contents.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endRenderPass() {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdEndRenderPass(commandBuffer.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun executeCommands(commandBuffers: Collection<CommandBuffer>) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdExecuteCommands(commandBuffer.toVkType(), commandBuffers.size.toUInt(), commandBuffers.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun debugMarkerBeginEXT(block: DebugMarkerMarkerInfoEXTBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugMarkerMarkerInfoEXT>().ptr
			val builder = DebugMarkerMarkerInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkCmdDebugMarkerBeginEXT!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun debugMarkerEndEXT() {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDebugMarkerEndEXT!!(commandBuffer.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun debugMarkerInsertEXT(block: DebugMarkerMarkerInfoEXTBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugMarkerMarkerInfoEXT>().ptr
			val builder = DebugMarkerMarkerInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkCmdDebugMarkerInsertEXT!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndirectCountAMD!!(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndexedIndirectCountAMD!!(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkCmdProcessCommandsInfoNVX>().ptr
			val builder = CmdProcessCommandsInfoNVXBuilder(target.pointed)
			builder.init(objectTable, indirectCommandsLayout, targetCommandBuffer,
					sequencesCountBuffer, sequencesIndexBuffer)
			builder.apply(block)
			dispatchTable.vkCmdProcessCommandsNVX!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun reserveSpaceForCommandsNVX(
			objectTable: ObjectTableNVX,
			indirectCommandsLayout: IndirectCommandsLayoutNVX,
			block: CmdReserveSpaceForCommandsInfoNVXBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkCmdReserveSpaceForCommandsInfoNVX>().ptr
			val builder = CmdReserveSpaceForCommandsInfoNVXBuilder(target.pointed)
			builder.init(objectTable, indirectCommandsLayout)
			builder.apply(block)
			dispatchTable.vkCmdReserveSpaceForCommandsNVX!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun pushDescriptorSetKHR(
			pipelineBindPoint: PipelineBindPoint,
			layout: PipelineLayout,
			set: UInt,
			block: CmdPushDescriptorSetKHRBuilder.() -> Unit
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdPushDescriptorSetKHRBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdPushDescriptorSetKHR!!(commandBuffer.toVkType(), pipelineBindPoint.toVkType(),
					layout.toVkType(), set.toVkType(), targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setDeviceMask(deviceMask: UInt) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetDeviceMask!!(commandBuffer.toVkType(), deviceMask.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDispatchBase!!(commandBuffer.toVkType(), baseGroupX.toVkType(),
					baseGroupY.toVkType(), baseGroupZ.toVkType(), groupCountX.toVkType(),
					groupCountY.toVkType(), groupCountZ.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun pushDescriptorSetWithTemplateKHR(
			descriptorUpdateTemplate: DescriptorUpdateTemplate,
			layout: PipelineLayout,
			set: UInt,
			data: IoBuffer
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdPushDescriptorSetWithTemplateKHR!!(commandBuffer.toVkType(),
					descriptorUpdateTemplate.toVkType(), layout.toVkType(), set.toVkType(),
					data.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setViewportWScalingNV(firstViewport: UInt, block: CmdSetViewportWScalingNVBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdSetViewportWScalingNVBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdSetViewportWScalingNV!!(commandBuffer.toVkType(), firstViewport.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setDiscardRectangleEXT(firstDiscardRectangle: UInt, block: CmdSetDiscardRectangleEXTBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdSetDiscardRectangleEXTBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdSetDiscardRectangleEXT!!(commandBuffer.toVkType(), firstDiscardRectangle.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setSampleLocationsEXT(block: SampleLocationsInfoEXTBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkSampleLocationsInfoEXT>().ptr
			val builder = SampleLocationsInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkCmdSetSampleLocationsEXT!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun beginDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsLabelEXT>().ptr
			val builder = DebugUtilsLabelEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkCmdBeginDebugUtilsLabelEXT!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endDebugUtilsLabelEXT() {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdEndDebugUtilsLabelEXT!!(commandBuffer.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun insertDebugUtilsLabelEXT(block: DebugUtilsLabelEXTBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsLabelEXT>().ptr
			val builder = DebugUtilsLabelEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkCmdInsertDebugUtilsLabelEXT!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun writeBufferMarkerAMD(
			pipelineStage: PipelineStage,
			dstBuffer: Buffer,
			dstOffset: ULong,
			marker: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdWriteBufferMarkerAMD!!(commandBuffer.toVkType(), pipelineStage.toVkType(),
					dstBuffer.toVkType(), dstOffset.toVkType(), marker.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endRenderPass2KHR(block: SubpassEndInfoKHRBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkSubpassEndInfoKHR>().ptr
			val builder = SubpassEndInfoKHRBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkCmdEndRenderPass2KHR!!(commandBuffer.toVkType(), target)
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndirectCountKHR!!(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndexedIndirectCountKHR!!(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setCheckpointNV(pCheckpointMarker: IoBuffer) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdSetCheckpointNV!!(commandBuffer.toVkType(), pCheckpointMarker.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindTransformFeedbackBuffersEXT(
			firstBinding: UInt,
			buffers: Collection<Buffer>,
			offsets: ULongArray,
			sizes: ULongArray
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBindTransformFeedbackBuffersEXT!!(commandBuffer.toVkType(), firstBinding.toVkType(),
					buffers.size.toUInt(), buffers.toVkType(), offsets.toVkType(),
					sizes.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun beginTransformFeedbackEXT(
			firstCounterBuffer: UInt,
			counterBuffers: Collection<Buffer>?,
			counterBufferOffsets: ULongArray
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBeginTransformFeedbackEXT!!(commandBuffer.toVkType(), firstCounterBuffer.toVkType(),
					counterBuffers?.size?.toUInt() ?: 0U, counterBuffers?.toVkType(),
					counterBufferOffsets.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endTransformFeedbackEXT(
			firstCounterBuffer: UInt,
			counterBuffers: Collection<Buffer>?,
			counterBufferOffsets: ULongArray
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdEndTransformFeedbackEXT!!(commandBuffer.toVkType(), firstCounterBuffer.toVkType(),
					counterBuffers?.size?.toUInt() ?: 0U, counterBuffers?.toVkType(),
					counterBufferOffsets.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun beginQueryIndexedEXT(
			queryPool: QueryPool,
			query: UInt,
			flags: VkFlag<QueryControl>?,
			index: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBeginQueryIndexedEXT!!(commandBuffer.toVkType(), queryPool.toVkType(),
					query.toVkType(), flags.toVkType(), index.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun endQueryIndexedEXT(
			queryPool: QueryPool,
			query: UInt,
			index: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdEndQueryIndexedEXT!!(commandBuffer.toVkType(), queryPool.toVkType(),
					query.toVkType(), index.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawIndirectByteCountEXT!!(commandBuffer.toVkType(), instanceCount.toVkType(),
					firstInstance.toVkType(), counterBuffer.toVkType(),
					counterBufferOffset.toVkType(), counterOffset.toVkType(),
					vertexStride.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setExclusiveScissorNV(firstExclusiveScissor: UInt, block: CmdSetExclusiveScissorNVBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdSetExclusiveScissorNVBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdSetExclusiveScissorNV!!(commandBuffer.toVkType(), firstExclusiveScissor.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindShadingRateImageNV(imageView: ImageView, imageLayout: ImageLayout) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdBindShadingRateImageNV!!(commandBuffer.toVkType(), imageView.toVkType(),
					imageLayout.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setViewportShadingRatePaletteNV(firstViewport: UInt, block: CmdSetViewportShadingRatePaletteNVBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdSetViewportShadingRatePaletteNVBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdSetViewportShadingRatePaletteNV!!(commandBuffer.toVkType(), firstViewport.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setCoarseSampleOrderNV(sampleOrderType: CoarseSampleOrderTypeNV, block: CmdSetCoarseSampleOrderNVBuilder.() -> Unit) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			val targets = CmdSetCoarseSampleOrderNVBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray()
			dispatchTable.vkCmdSetCoarseSampleOrderNV!!(commandBuffer.toVkType(), sampleOrderType.toVkType(),
					targets.size.toUInt(), targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun drawMeshTasksNV(taskCount: UInt, firstTask: UInt) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawMeshTasksNV!!(commandBuffer.toVkType(), taskCount.toVkType(),
					firstTask.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun drawMeshTasksIndirectNV(
			buffer: Buffer,
			offset: ULong,
			drawCount: UInt,
			stride: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawMeshTasksIndirectNV!!(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), drawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdDrawMeshTasksIndirectCountNV!!(commandBuffer.toVkType(), buffer.toVkType(),
					offset.toVkType(), countBuffer.toVkType(), countBufferOffset.toVkType(),
					maxDrawCount.toVkType(), stride.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun copyAccelerationStructureNV(dst: AccelerationStructureNV, src: AccelerationStructureNV, mode: CopyAccelerationStructureModeNV) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdCopyAccelerationStructureNV!!(commandBuffer.toVkType(), dst.toVkType(),
					src.toVkType(), mode.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun writeAccelerationStructuresPropertiesNV(
			accelerationStructures: Collection<AccelerationStructureNV>,
			queryType: QueryType,
			queryPool: QueryPool,
			firstQuery: UInt
	) {
		val commandBuffer = this
		VirtualStack.push()
		try {
			dispatchTable.vkCmdWriteAccelerationStructuresPropertiesNV!!(commandBuffer.toVkType(),
					accelerationStructures.size.toUInt(), accelerationStructures.toVkType(),
					queryType.toVkType(), queryPool.toVkType(), firstQuery.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkAccelerationStructureInfoNV>().ptr
			val builder = AccelerationStructureInfoNVBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			dispatchTable.vkCmdBuildAccelerationStructureNV!!(commandBuffer.toVkType(), target,
					instanceData.toVkType(), instanceOffset.toVkType(), update.toVkType(),
					dst.toVkType(), src.toVkType(), scratch.toVkType(), scratchOffset.toVkType())
		} finally {
			VirtualStack.pop()
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
		VirtualStack.push()
		try {
			dispatchTable.vkCmdTraceRaysNV!!(commandBuffer.toVkType(), raygenShaderBindingTableBuffer.toVkType(),
					raygenShaderBindingOffset.toVkType(), missShaderBindingTableBuffer.toVkType(),
					missShaderBindingOffset.toVkType(), missShaderBindingStride.toVkType(),
					hitShaderBindingTableBuffer.toVkType(), hitShaderBindingOffset.toVkType(),
					hitShaderBindingStride.toVkType(), callableShaderBindingTableBuffer.toVkType(),
					callableShaderBindingOffset.toVkType(), callableShaderBindingStride.toVkType(),
					width.toVkType(), height.toVkType(), depth.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}
}

