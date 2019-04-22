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
import com.kgl.vulkan.handles.Pipeline
import com.kgl.vulkan.handles.PipelineLayout
import com.kgl.vulkan.handles.RenderPass
import com.kgl.vulkan.handles.ShaderModule
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import kotlinx.cinterop.set
import kotlinx.cinterop.toKString
import kotlinx.io.core.IoBuffer

actual class SpecializationMapEntryBuilder(internal val target: VkSpecializationMapEntry) {
	internal fun init(
			constantID: UInt,
			offset: UInt,
			size: ULong
	) {
		target.constantID = constantID.toVkType()
		target.offset = offset.toVkType()
		target.size = size.toVkType()
	}
}

actual class SpecializationMapEntriesBuilder {
	val targets: MutableList<(VkSpecializationMapEntry) -> Unit> = mutableListOf()

	actual fun entry(
			constantID: UInt,
			offset: UInt,
			size: ULong
	) {
		targets += {
			val builder = SpecializationMapEntryBuilder(it)
			builder.init(constantID, offset, size)
		}
	}
}

actual class SpecializationInfoBuilder(internal val target: VkSpecializationInfo) {
	actual fun mapEntries(block: SpecializationMapEntriesBuilder.() -> Unit) {
		val targets = SpecializationMapEntriesBuilder().apply(block).targets
		target.pMapEntries = targets.mapToStackArray()
		target.mapEntryCount = targets.size.toUInt()
	}

	internal fun init(pData: IoBuffer?) {
		pData?.readDirect {
			target.pData = it
			target.dataSize = pData.readRemaining.toULong()
			pData.readRemaining
		}
	}
}

actual class PipelineShaderStageCreateInfoBuilder(internal val target: VkPipelineShaderStageCreateInfo) {
	actual var name: String?
		get() = target.pName?.toKString()
		set(value) {
			target.pName = value.toVkType()
		}

	actual fun specializationInfo(pData: IoBuffer?, block: SpecializationInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkSpecializationInfo>()
		target.pSpecializationInfo = subTarget.ptr
		val builder = SpecializationInfoBuilder(subTarget)
		builder.init(pData)
		builder.apply(block)
	}

	internal fun init(stage: ShaderStage, module: ShaderModule) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.stage = stage.toVkType()
		target.module = module.toVkType()
	}
}

actual class PipelineShaderStageCreateInfosBuilder {
	val targets: MutableList<(VkPipelineShaderStageCreateInfo) -> Unit> = mutableListOf()

	actual fun stage(
			stage: ShaderStage,
			module: ShaderModule,
			block: PipelineShaderStageCreateInfoBuilder.() -> Unit
	) {
		targets += {
			val builder = PipelineShaderStageCreateInfoBuilder(it)
			builder.init(stage, module)
			builder.apply(block)
		}
	}
}

actual class VertexInputBindingDescriptionBuilder(internal val target: VkVertexInputBindingDescription) {
	actual var binding: UInt
		get() = target.binding
		set(value) {
			target.binding = value.toVkType()
		}

	actual var stride: UInt
		get() = target.stride
		set(value) {
			target.stride = value.toVkType()
		}

	actual var inputRate: VertexInputRate?
		get() = VertexInputRate.from(target.inputRate)
		set(value) {
			target.inputRate = value.toVkType()
		}

	internal fun init() {
	}
}

actual class VertexInputBindingDescriptionsBuilder {
	val targets: MutableList<(VkVertexInputBindingDescription) -> Unit> = mutableListOf()

	actual fun description(block: VertexInputBindingDescriptionBuilder.() -> Unit) {
		targets += {
			val builder = VertexInputBindingDescriptionBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class VertexInputAttributeDescriptionBuilder(internal val target: VkVertexInputAttributeDescription) {
	internal fun init(
			location: UInt,
			binding: UInt,
			format: Format,
			offset: UInt
	) {
		target.location = location.toVkType()
		target.binding = binding.toVkType()
		target.format = format.toVkType()
		target.offset = offset.toVkType()
	}
}

actual class VertexInputAttributeDescriptionsBuilder {
	val targets: MutableList<(VkVertexInputAttributeDescription) -> Unit> = mutableListOf()

	actual fun description(
			location: UInt,
			binding: UInt,
			format: Format,
			offset: UInt
	) {
		targets += {
			val builder = VertexInputAttributeDescriptionBuilder(it)
			builder.init(location, binding, format, offset)
		}
	}
}

actual class PipelineVertexInputStateCreateInfoBuilder(internal val target: VkPipelineVertexInputStateCreateInfo) {
	actual fun vertexBindingDescriptions(block: VertexInputBindingDescriptionsBuilder.() -> Unit) {
		val targets = VertexInputBindingDescriptionsBuilder().apply(block).targets
		target.pVertexBindingDescriptions = targets.mapToStackArray()
		target.vertexBindingDescriptionCount = targets.size.toUInt()
	}

	actual fun vertexAttributeDescriptions(block: VertexInputAttributeDescriptionsBuilder.() -> Unit) {
		val targets = VertexInputAttributeDescriptionsBuilder().apply(block).targets
		target.pVertexAttributeDescriptions = targets.mapToStackArray()
		target.vertexAttributeDescriptionCount = targets.size.toUInt()
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class PipelineInputAssemblyStateCreateInfoBuilder(internal val target: VkPipelineInputAssemblyStateCreateInfo) {
	actual var topology: PrimitiveTopology?
		get() = PrimitiveTopology.from(target.topology)
		set(value) {
			target.topology = value.toVkType()
		}

	actual var primitiveRestartEnable: Boolean
		get() = target.primitiveRestartEnable.toBoolean()
		set(value) {
			target.primitiveRestartEnable = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class PipelineTessellationStateCreateInfoBuilder(internal val target: VkPipelineTessellationStateCreateInfo) {
	actual var patchControlPoints: UInt
		get() = target.patchControlPoints
		set(value) {
			target.patchControlPoints = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_TESSELLATION_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class ViewportsBuilder {
	val targets: MutableList<(VkViewport) -> Unit> = mutableListOf()

	actual fun viewport(block: ViewportBuilder.() -> Unit) {
		targets += {
			val builder = ViewportBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class Offset2DBuilder(internal val target: VkOffset2D) {
	internal fun init(x: Int, y: Int) {
		target.x = x.toVkType()
		target.y = y.toVkType()
	}
}

actual class Rect2DsBuilder {
	val targets: MutableList<(VkRect2D) -> Unit> = mutableListOf()

	actual fun rect2D(block: Rect2DBuilder.() -> Unit) {
		targets += {
			val builder = Rect2DBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class PipelineViewportStateCreateInfoBuilder(internal val target: VkPipelineViewportStateCreateInfo) {
	actual fun viewports(block: ViewportsBuilder.() -> Unit) {
		val targets = ViewportsBuilder().apply(block).targets
		target.pViewports = targets.mapToStackArray()
		target.viewportCount = targets.size.toUInt()
	}

	actual fun scissors(block: Rect2DsBuilder.() -> Unit) {
		val targets = Rect2DsBuilder().apply(block).targets
		target.pScissors = targets.mapToStackArray()
		target.scissorCount = targets.size.toUInt()
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class PipelineRasterizationStateCreateInfoBuilder(internal val target: VkPipelineRasterizationStateCreateInfo) {
	actual var depthClampEnable: Boolean
		get() = target.depthClampEnable.toBoolean()
		set(value) {
			target.depthClampEnable = value.toVkType()
		}

	actual var rasterizerDiscardEnable: Boolean
		get() = target.rasterizerDiscardEnable.toBoolean()
		set(value) {
			target.rasterizerDiscardEnable = value.toVkType()
		}

	actual var polygonMode: PolygonMode?
		get() = PolygonMode.from(target.polygonMode)
		set(value) {
			target.polygonMode = value.toVkType()
		}

	actual var cullMode: VkFlag<CullMode>?
		get() = CullMode.fromMultiple(target.cullMode)
		set(value) {
			target.cullMode = value.toVkType()
		}

	actual var frontFace: FrontFace?
		get() = FrontFace.from(target.frontFace)
		set(value) {
			target.frontFace = value.toVkType()
		}

	actual var depthBiasEnable: Boolean
		get() = target.depthBiasEnable.toBoolean()
		set(value) {
			target.depthBiasEnable = value.toVkType()
		}

	actual var depthBiasConstantFactor: Float
		get() = target.depthBiasConstantFactor
		set(value) {
			target.depthBiasConstantFactor = value.toVkType()
		}

	actual var depthBiasClamp: Float
		get() = target.depthBiasClamp
		set(value) {
			target.depthBiasClamp = value.toVkType()
		}

	actual var depthBiasSlopeFactor: Float
		get() = target.depthBiasSlopeFactor
		set(value) {
			target.depthBiasSlopeFactor = value.toVkType()
		}

	actual var lineWidth: Float
		get() = target.lineWidth
		set(value) {
			target.lineWidth = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class PipelineMultisampleStateCreateInfoBuilder(internal val target: VkPipelineMultisampleStateCreateInfo) {
	actual var rasterizationSamples: SampleCount?
		get() = SampleCount.from(target.rasterizationSamples)
		set(value) {
			target.rasterizationSamples = value.toVkType()
		}

	actual var sampleShadingEnable: Boolean
		get() = target.sampleShadingEnable.toBoolean()
		set(value) {
			target.sampleShadingEnable = value.toVkType()
		}

	actual var minSampleShading: Float
		get() = target.minSampleShading
		set(value) {
			target.minSampleShading = value.toVkType()
		}

	actual var alphaToCoverageEnable: Boolean
		get() = target.alphaToCoverageEnable.toBoolean()
		set(value) {
			target.alphaToCoverageEnable = value.toVkType()
		}

	actual var alphaToOneEnable: Boolean
		get() = target.alphaToOneEnable.toBoolean()
		set(value) {
			target.alphaToOneEnable = value.toVkType()
		}

	internal fun init(sampleMask: UIntArray?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.pSampleMask = sampleMask?.toVkType()
	}
}

actual class StencilOpStateBuilder(internal val target: VkStencilOpState) {
	actual var failOp: StencilOp?
		get() = StencilOp.from(target.failOp)
		set(value) {
			target.failOp = value.toVkType()
		}

	actual var passOp: StencilOp?
		get() = StencilOp.from(target.passOp)
		set(value) {
			target.passOp = value.toVkType()
		}

	actual var depthFailOp: StencilOp?
		get() = StencilOp.from(target.depthFailOp)
		set(value) {
			target.depthFailOp = value.toVkType()
		}

	actual var compareOp: CompareOp?
		get() = CompareOp.from(target.compareOp)
		set(value) {
			target.compareOp = value.toVkType()
		}

	actual var compareMask: UInt
		get() = target.compareMask
		set(value) {
			target.compareMask = value.toVkType()
		}

	actual var writeMask: UInt
		get() = target.writeMask
		set(value) {
			target.writeMask = value.toVkType()
		}

	actual var reference: UInt
		get() = target.reference
		set(value) {
			target.reference = value.toVkType()
		}

	internal fun init() {
	}
}

actual class PipelineDepthStencilStateCreateInfoBuilder(internal val target: VkPipelineDepthStencilStateCreateInfo) {
	actual var depthTestEnable: Boolean
		get() = target.depthTestEnable.toBoolean()
		set(value) {
			target.depthTestEnable = value.toVkType()
		}

	actual var depthWriteEnable: Boolean
		get() = target.depthWriteEnable.toBoolean()
		set(value) {
			target.depthWriteEnable = value.toVkType()
		}

	actual var depthCompareOp: CompareOp?
		get() = CompareOp.from(target.depthCompareOp)
		set(value) {
			target.depthCompareOp = value.toVkType()
		}

	actual var depthBoundsTestEnable: Boolean
		get() = target.depthBoundsTestEnable.toBoolean()
		set(value) {
			target.depthBoundsTestEnable = value.toVkType()
		}

	actual var stencilTestEnable: Boolean
		get() = target.stencilTestEnable.toBoolean()
		set(value) {
			target.stencilTestEnable = value.toVkType()
		}

	actual var minDepthBounds: Float
		get() = target.minDepthBounds
		set(value) {
			target.minDepthBounds = value.toVkType()
		}

	actual var maxDepthBounds: Float
		get() = target.maxDepthBounds
		set(value) {
			target.maxDepthBounds = value.toVkType()
		}

	actual fun front(block: StencilOpStateBuilder.() -> Unit) {
		val subTarget = target.front
		val builder = StencilOpStateBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun back(block: StencilOpStateBuilder.() -> Unit) {
		val subTarget = target.back
		val builder = StencilOpStateBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class PipelineColorBlendAttachmentStateBuilder(internal val target: VkPipelineColorBlendAttachmentState) {
	actual var blendEnable: Boolean
		get() = target.blendEnable.toBoolean()
		set(value) {
			target.blendEnable = value.toVkType()
		}

	actual var srcColorBlendFactor: BlendFactor?
		get() = BlendFactor.from(target.srcColorBlendFactor)
		set(value) {
			target.srcColorBlendFactor = value.toVkType()
		}

	actual var dstColorBlendFactor: BlendFactor?
		get() = BlendFactor.from(target.dstColorBlendFactor)
		set(value) {
			target.dstColorBlendFactor = value.toVkType()
		}

	actual var colorBlendOp: BlendOp?
		get() = BlendOp.from(target.colorBlendOp)
		set(value) {
			target.colorBlendOp = value.toVkType()
		}

	actual var srcAlphaBlendFactor: BlendFactor?
		get() = BlendFactor.from(target.srcAlphaBlendFactor)
		set(value) {
			target.srcAlphaBlendFactor = value.toVkType()
		}

	actual var dstAlphaBlendFactor: BlendFactor?
		get() = BlendFactor.from(target.dstAlphaBlendFactor)
		set(value) {
			target.dstAlphaBlendFactor = value.toVkType()
		}

	actual var alphaBlendOp: BlendOp?
		get() = BlendOp.from(target.alphaBlendOp)
		set(value) {
			target.alphaBlendOp = value.toVkType()
		}

	actual var colorWriteMask: VkFlag<ColorComponent>?
		get() = ColorComponent.fromMultiple(target.colorWriteMask)
		set(value) {
			target.colorWriteMask = value.toVkType()
		}

	internal fun init() {
	}
}

actual class PipelineColorBlendAttachmentStatesBuilder {
	val targets: MutableList<(VkPipelineColorBlendAttachmentState) -> Unit> = mutableListOf()

	actual fun state(block: PipelineColorBlendAttachmentStateBuilder.() -> Unit) {
		targets += {
			val builder = PipelineColorBlendAttachmentStateBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class PipelineColorBlendStateCreateInfoBuilder(internal val target: VkPipelineColorBlendStateCreateInfo) {
	actual var logicOpEnable: Boolean
		get() = target.logicOpEnable.toBoolean()
		set(value) {
			target.logicOpEnable = value.toVkType()
		}

	actual var logicOp: LogicOp?
		get() = LogicOp.from(target.logicOp)
		set(value) {
			target.logicOp = value.toVkType()
		}

	actual fun attachments(block: PipelineColorBlendAttachmentStatesBuilder.() -> Unit) {
		val targets = PipelineColorBlendAttachmentStatesBuilder().apply(block).targets
		target.pAttachments = targets.mapToStackArray()
		target.attachmentCount = targets.size.toUInt()
	}

	actual fun blendConstants(
			arg0: Float,
			arg1: Float,
			arg2: Float,
			arg3: Float
	) {
		target.blendConstants[0] = arg0.toVkType()
		target.blendConstants[1] = arg1.toVkType()
		target.blendConstants[2] = arg2.toVkType()
		target.blendConstants[3] = arg3.toVkType()
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class PipelineDynamicStateCreateInfoBuilder(internal val target: VkPipelineDynamicStateCreateInfo) {
	internal fun init(dynamicStates: Collection<DynamicState>?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO
		target.pNext = null
		target.flags = 0U
		target.pDynamicStates = dynamicStates?.toVkType()
		target.dynamicStateCount = dynamicStates?.size?.toUInt() ?: 0U
	}
}

actual class GraphicsPipelineCreateInfoBuilder(internal val target: VkGraphicsPipelineCreateInfo) {
	actual var flags: VkFlag<PipelineCreate>?
		get() = PipelineCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var subpass: UInt
		get() = target.subpass
		set(value) {
			target.subpass = value.toVkType()
		}

	actual var basePipelineIndex: Int
		get() = target.basePipelineIndex
		set(value) {
			target.basePipelineIndex = value.toVkType()
		}

	actual fun stages(block: PipelineShaderStageCreateInfosBuilder.() -> Unit) {
		val targets = PipelineShaderStageCreateInfosBuilder().apply(block).targets
		target.pStages = targets.mapToStackArray()
		target.stageCount = targets.size.toUInt()
	}

	actual fun vertexInputState(block: PipelineVertexInputStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineVertexInputStateCreateInfo>()
		target.pVertexInputState = subTarget.ptr
		val builder = PipelineVertexInputStateCreateInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun inputAssemblyState(block: PipelineInputAssemblyStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineInputAssemblyStateCreateInfo>()
		target.pInputAssemblyState = subTarget.ptr
		val builder = PipelineInputAssemblyStateCreateInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun tessellationState(block: PipelineTessellationStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineTessellationStateCreateInfo>()
		target.pTessellationState = subTarget.ptr
		val builder = PipelineTessellationStateCreateInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun viewportState(block: PipelineViewportStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineViewportStateCreateInfo>()
		target.pViewportState = subTarget.ptr
		val builder = PipelineViewportStateCreateInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun rasterizationState(block: PipelineRasterizationStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineRasterizationStateCreateInfo>()
		target.pRasterizationState = subTarget.ptr
		val builder = PipelineRasterizationStateCreateInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun multisampleState(sampleMask: UIntArray?, block: PipelineMultisampleStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineMultisampleStateCreateInfo>()
		target.pMultisampleState = subTarget.ptr
		val builder = PipelineMultisampleStateCreateInfoBuilder(subTarget)
		builder.init(sampleMask)
		builder.apply(block)
	}

	actual fun depthStencilState(block: PipelineDepthStencilStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineDepthStencilStateCreateInfo>()
		target.pDepthStencilState = subTarget.ptr
		val builder = PipelineDepthStencilStateCreateInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun colorBlendState(block: PipelineColorBlendStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineColorBlendStateCreateInfo>()
		target.pColorBlendState = subTarget.ptr
		val builder = PipelineColorBlendStateCreateInfoBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun dynamicState(dynamicStates: Collection<DynamicState>?, block: PipelineDynamicStateCreateInfoBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkPipelineDynamicStateCreateInfo>()
		target.pDynamicState = subTarget.ptr
		val builder = PipelineDynamicStateCreateInfoBuilder(subTarget)
		builder.init(dynamicStates)
		builder.apply(block)
	}

	internal fun init(
			layout: PipelineLayout,
			renderPass: RenderPass,
			basePipelineHandle: Pipeline?
	) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO
		target.pNext = null
		target.layout = layout.toVkType()
		target.renderPass = renderPass.toVkType()
		target.basePipelineHandle = basePipelineHandle?.toVkType()
	}
}

actual class CreateGraphicsPipelinesBuilder {
	val targets: MutableList<(VkGraphicsPipelineCreateInfo) -> Unit> = mutableListOf()

	actual fun pipeline(
			layout: PipelineLayout,
			renderPass: RenderPass,
			basePipelineHandle: Pipeline?,
			block: GraphicsPipelineCreateInfoBuilder.() -> Unit
	) {
		targets += {
			val builder = GraphicsPipelineCreateInfoBuilder(it)
			builder.init(layout, renderPass, basePipelineHandle)
			builder.apply(block)
		}
	}
}

actual class ComputePipelineCreateInfoBuilder(internal val target: VkComputePipelineCreateInfo) {
	actual var flags: VkFlag<PipelineCreate>?
		get() = PipelineCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var basePipelineIndex: Int
		get() = target.basePipelineIndex
		set(value) {
			target.basePipelineIndex = value.toVkType()
		}

	actual fun stage(
			stage: ShaderStage,
			module: ShaderModule,
			block: PipelineShaderStageCreateInfoBuilder.() -> Unit
	) {
		val subTarget = target.stage
		val builder = PipelineShaderStageCreateInfoBuilder(subTarget)
		builder.init(stage, module)
		builder.apply(block)
	}

	internal fun init(layout: PipelineLayout, basePipelineHandle: Pipeline?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_COMPUTE_PIPELINE_CREATE_INFO
		target.pNext = null
		target.layout = layout.toVkType()
		target.basePipelineHandle = basePipelineHandle?.toVkType()
	}
}

actual class CreateComputePipelinesBuilder {
	val targets: MutableList<(VkComputePipelineCreateInfo) -> Unit> = mutableListOf()

	actual fun pipeline(
			layout: PipelineLayout,
			basePipelineHandle: Pipeline?,
			block: ComputePipelineCreateInfoBuilder.() -> Unit
	) {
		targets += {
			val builder = ComputePipelineCreateInfoBuilder(it)
			builder.init(layout, basePipelineHandle)
			builder.apply(block)
		}
	}
}

actual class RayTracingShaderGroupCreateInfoNVBuilder(internal val target: VkRayTracingShaderGroupCreateInfoNV) {
	actual var type: RayTracingShaderGroupTypeNV?
		get() = RayTracingShaderGroupTypeNV.from(target.type)
		set(value) {
			target.type = value.toVkType()
		}

	actual var generalShader: UInt
		get() = target.generalShader
		set(value) {
			target.generalShader = value.toVkType()
		}

	actual var closestHitShader: UInt
		get() = target.closestHitShader
		set(value) {
			target.closestHitShader = value.toVkType()
		}

	actual var anyHitShader: UInt
		get() = target.anyHitShader
		set(value) {
			target.anyHitShader = value.toVkType()
		}

	actual var intersectionShader: UInt
		get() = target.intersectionShader
		set(value) {
			target.intersectionShader = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_RAY_TRACING_SHADER_GROUP_CREATE_INFO_NV
		target.pNext = null
	}
}

actual class RayTracingShaderGroupCreateInfoNVsBuilder {
	val targets: MutableList<(VkRayTracingShaderGroupCreateInfoNV) -> Unit> = mutableListOf()

	actual fun group(block: RayTracingShaderGroupCreateInfoNVBuilder.() -> Unit) {
		targets += {
			val builder = RayTracingShaderGroupCreateInfoNVBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class RayTracingPipelineCreateInfoNVBuilder(internal val target: VkRayTracingPipelineCreateInfoNV) {
	actual var flags: VkFlag<PipelineCreate>?
		get() = PipelineCreate.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var maxRecursionDepth: UInt
		get() = target.maxRecursionDepth
		set(value) {
			target.maxRecursionDepth = value.toVkType()
		}

	actual var basePipelineIndex: Int
		get() = target.basePipelineIndex
		set(value) {
			target.basePipelineIndex = value.toVkType()
		}

	actual fun stages(block: PipelineShaderStageCreateInfosBuilder.() -> Unit) {
		val targets = PipelineShaderStageCreateInfosBuilder().apply(block).targets
		target.pStages = targets.mapToStackArray()
		target.stageCount = targets.size.toUInt()
	}

	actual fun groups(block: RayTracingShaderGroupCreateInfoNVsBuilder.() -> Unit) {
		val targets = RayTracingShaderGroupCreateInfoNVsBuilder().apply(block).targets
		target.pGroups = targets.mapToStackArray()
		target.groupCount = targets.size.toUInt()
	}

	internal fun init(layout: PipelineLayout, basePipelineHandle: Pipeline?) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_RAY_TRACING_PIPELINE_CREATE_INFO_NV
		target.pNext = null
		target.layout = layout.toVkType()
		target.basePipelineHandle = basePipelineHandle?.toVkType()
	}
}

actual class CreateRayTracingPipelinesNVBuilder {
	val targets: MutableList<(VkRayTracingPipelineCreateInfoNV) -> Unit> = mutableListOf()

	actual fun pipeline(
			layout: PipelineLayout,
			basePipelineHandle: Pipeline?,
			block: RayTracingPipelineCreateInfoNVBuilder.() -> Unit
	) {
		targets += {
			val builder = RayTracingPipelineCreateInfoNVBuilder(it)
			builder.init(layout, basePipelineHandle)
			builder.apply(block)
		}
	}
}

