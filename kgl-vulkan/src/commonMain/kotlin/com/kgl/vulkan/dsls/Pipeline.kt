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
import com.kgl.vulkan.handles.Pipeline
import com.kgl.vulkan.handles.PipelineLayout
import com.kgl.vulkan.handles.RenderPass
import com.kgl.vulkan.handles.ShaderModule
import com.kgl.vulkan.utils.VkFlag
import kotlinx.io.core.IoBuffer

expect class SpecializationMapEntryBuilder

expect class SpecializationMapEntriesBuilder {
	fun entry(
			constantID: UInt,
			offset: UInt,
			size: ULong
	)
}

expect class SpecializationInfoBuilder {
	fun mapEntries(block: SpecializationMapEntriesBuilder.() -> Unit)
}

expect class PipelineShaderStageCreateInfoBuilder {
	var name: String?

	fun specializationInfo(pData: IoBuffer?, block: SpecializationInfoBuilder.() -> Unit)
}

expect class PipelineShaderStageCreateInfosBuilder {
	fun stage(
			stage: ShaderStage,
			module: ShaderModule,
			block: PipelineShaderStageCreateInfoBuilder.() -> Unit = {}
	)
}

expect class VertexInputBindingDescriptionBuilder {
	var binding: UInt

	var stride: UInt

	var inputRate: VertexInputRate?
}

expect class VertexInputBindingDescriptionsBuilder {
	fun description(block: VertexInputBindingDescriptionBuilder.() -> Unit = {})
}

expect class VertexInputAttributeDescriptionBuilder

expect class VertexInputAttributeDescriptionsBuilder {
	fun description(
			location: UInt,
			binding: UInt,
			format: Format,
			offset: UInt
	)
}

expect class PipelineVertexInputStateCreateInfoBuilder {
	fun vertexBindingDescriptions(block: VertexInputBindingDescriptionsBuilder.() -> Unit)

	fun vertexAttributeDescriptions(block: VertexInputAttributeDescriptionsBuilder.() -> Unit)
}

expect class PipelineInputAssemblyStateCreateInfoBuilder {
	var topology: PrimitiveTopology?

	var primitiveRestartEnable: Boolean
}

expect class PipelineTessellationStateCreateInfoBuilder {
	var patchControlPoints: UInt
}

expect class ViewportsBuilder {
	fun viewport(block: ViewportBuilder.() -> Unit = {})
}

expect class Offset2DBuilder

expect class Rect2DsBuilder {
	fun rect2D(block: Rect2DBuilder.() -> Unit)
}

expect class PipelineViewportStateCreateInfoBuilder {
	fun viewports(block: ViewportsBuilder.() -> Unit)

	fun scissors(block: Rect2DsBuilder.() -> Unit)
}

expect class PipelineRasterizationStateCreateInfoBuilder {
	var depthClampEnable: Boolean

	var rasterizerDiscardEnable: Boolean

	var polygonMode: PolygonMode?

	var cullMode: VkFlag<CullMode>?

	var frontFace: FrontFace?

	var depthBiasEnable: Boolean

	var depthBiasConstantFactor: Float

	var depthBiasClamp: Float

	var depthBiasSlopeFactor: Float

	var lineWidth: Float
}

expect class PipelineMultisampleStateCreateInfoBuilder {
	var rasterizationSamples: SampleCount?

	var sampleShadingEnable: Boolean

	var minSampleShading: Float

	var alphaToCoverageEnable: Boolean

	var alphaToOneEnable: Boolean
}

expect class StencilOpStateBuilder {
	var failOp: StencilOp?

	var passOp: StencilOp?

	var depthFailOp: StencilOp?

	var compareOp: CompareOp?

	var compareMask: UInt

	var writeMask: UInt

	var reference: UInt
}

expect class PipelineDepthStencilStateCreateInfoBuilder {
	var depthTestEnable: Boolean

	var depthWriteEnable: Boolean

	var depthCompareOp: CompareOp?

	var depthBoundsTestEnable: Boolean

	var stencilTestEnable: Boolean

	var minDepthBounds: Float

	var maxDepthBounds: Float

	fun front(block: StencilOpStateBuilder.() -> Unit = {})

	fun back(block: StencilOpStateBuilder.() -> Unit = {})
}

expect class PipelineColorBlendAttachmentStateBuilder {
	var blendEnable: Boolean

	var srcColorBlendFactor: BlendFactor?

	var dstColorBlendFactor: BlendFactor?

	var colorBlendOp: BlendOp?

	var srcAlphaBlendFactor: BlendFactor?

	var dstAlphaBlendFactor: BlendFactor?

	var alphaBlendOp: BlendOp?

	var colorWriteMask: VkFlag<ColorComponent>?
}

expect class PipelineColorBlendAttachmentStatesBuilder {
	fun state(block: PipelineColorBlendAttachmentStateBuilder.() -> Unit = {})
}

expect class PipelineColorBlendStateCreateInfoBuilder {
	var logicOpEnable: Boolean

	var logicOp: LogicOp?

	fun attachments(block: PipelineColorBlendAttachmentStatesBuilder.() -> Unit)

	fun blendConstants(
			arg0: Float,
			arg1: Float,
			arg2: Float,
			arg3: Float
	)
}

expect class PipelineDynamicStateCreateInfoBuilder

expect class GraphicsPipelineCreateInfoBuilder {
	var flags: VkFlag<PipelineCreate>?

	var subpass: UInt

	var basePipelineIndex: Int

	fun stages(block: PipelineShaderStageCreateInfosBuilder.() -> Unit)

	fun vertexInputState(block: PipelineVertexInputStateCreateInfoBuilder.() -> Unit)

	fun inputAssemblyState(block: PipelineInputAssemblyStateCreateInfoBuilder.() -> Unit = {})

	fun tessellationState(block: PipelineTessellationStateCreateInfoBuilder.() -> Unit = {})

	fun viewportState(block: PipelineViewportStateCreateInfoBuilder.() -> Unit = {})

	fun rasterizationState(block: PipelineRasterizationStateCreateInfoBuilder.() -> Unit = {})

	fun multisampleState(sampleMask: UIntArray? = null, block: PipelineMultisampleStateCreateInfoBuilder.() -> Unit = {})

	fun depthStencilState(block: PipelineDepthStencilStateCreateInfoBuilder.() -> Unit)

	fun colorBlendState(block: PipelineColorBlendStateCreateInfoBuilder.() -> Unit)

	fun dynamicState(dynamicStates: Collection<DynamicState>?, block: PipelineDynamicStateCreateInfoBuilder.() -> Unit = {})
}

expect class CreateGraphicsPipelinesBuilder {
	fun pipeline(
			layout: PipelineLayout,
			renderPass: RenderPass,
			basePipelineHandle: Pipeline? = null,
			block: GraphicsPipelineCreateInfoBuilder.() -> Unit
	)
}

expect class ComputePipelineCreateInfoBuilder {
	var flags: VkFlag<PipelineCreate>?

	var basePipelineIndex: Int

	fun stage(
			stage: ShaderStage,
			module: ShaderModule,
			block: PipelineShaderStageCreateInfoBuilder.() -> Unit = {}
	)
}

expect class CreateComputePipelinesBuilder {
	fun pipeline(
			layout: PipelineLayout,
			basePipelineHandle: Pipeline? = null,
			block: ComputePipelineCreateInfoBuilder.() -> Unit
	)
}

expect class RayTracingShaderGroupCreateInfoNVBuilder {
	var type: RayTracingShaderGroupTypeNV?

	var generalShader: UInt

	var closestHitShader: UInt

	var anyHitShader: UInt

	var intersectionShader: UInt
}

expect class RayTracingShaderGroupCreateInfoNVsBuilder {
	fun group(block: RayTracingShaderGroupCreateInfoNVBuilder.() -> Unit = {})
}

expect class RayTracingPipelineCreateInfoNVBuilder {
	var flags: VkFlag<PipelineCreate>?

	var maxRecursionDepth: UInt

	var basePipelineIndex: Int

	fun stages(block: PipelineShaderStageCreateInfosBuilder.() -> Unit)

	fun groups(block: RayTracingShaderGroupCreateInfoNVsBuilder.() -> Unit)
}

expect class CreateRayTracingPipelinesNVBuilder {
	fun pipeline(
			layout: PipelineLayout,
			basePipelineHandle: Pipeline? = null,
			block: RayTracingPipelineCreateInfoNVBuilder.() -> Unit
	)
}

