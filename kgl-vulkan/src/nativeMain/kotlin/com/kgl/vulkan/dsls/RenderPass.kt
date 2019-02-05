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
import com.kgl.core.utils.VirtualStack
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import cvulkan.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr

actual class AttachmentDescriptionBuilder(internal val target: VkAttachmentDescription) {
	actual var flags: VkFlag<AttachmentDescription>?
		get() = AttachmentDescription.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var format: Format?
		get() = Format.from(target.format)
		set(value) {
			target.format = value.toVkType()
		}

	actual var samples: SampleCount?
		get() = SampleCount.from(target.samples)
		set(value) {
			target.samples = value.toVkType()
		}

	actual var loadOp: AttachmentLoadOp?
		get() = AttachmentLoadOp.from(target.loadOp)
		set(value) {
			target.loadOp = value.toVkType()
		}

	actual var storeOp: AttachmentStoreOp?
		get() = AttachmentStoreOp.from(target.storeOp)
		set(value) {
			target.storeOp = value.toVkType()
		}

	actual var stencilLoadOp: AttachmentLoadOp?
		get() = AttachmentLoadOp.from(target.stencilLoadOp)
		set(value) {
			target.stencilLoadOp = value.toVkType()
		}

	actual var stencilStoreOp: AttachmentStoreOp?
		get() = AttachmentStoreOp.from(target.stencilStoreOp)
		set(value) {
			target.stencilStoreOp = value.toVkType()
		}

	actual var initialLayout: ImageLayout?
		get() = ImageLayout.from(target.initialLayout)
		set(value) {
			target.initialLayout = value.toVkType()
		}

	actual var finalLayout: ImageLayout?
		get() = ImageLayout.from(target.finalLayout)
		set(value) {
			target.finalLayout = value.toVkType()
		}

	internal fun init() {
	}
}

actual class AttachmentDescriptionsBuilder {
	val targets: MutableList<(VkAttachmentDescription) -> Unit> = mutableListOf()

	actual fun description(block: AttachmentDescriptionBuilder.() -> Unit) {
		targets += {
			val builder = AttachmentDescriptionBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class AttachmentReferenceBuilder(internal val target: VkAttachmentReference) {
	actual var attachment: UInt
		get() = target.attachment
		set(value) {
			target.attachment = value.toVkType()
		}

	actual var layout: ImageLayout?
		get() = ImageLayout.from(target.layout)
		set(value) {
			target.layout = value.toVkType()
		}

	internal fun init() {
	}
}

actual class AttachmentReferencesBuilder {
	val targets: MutableList<(VkAttachmentReference) -> Unit> = mutableListOf()

	actual fun reference(block: AttachmentReferenceBuilder.() -> Unit) {
		targets += {
			val builder = AttachmentReferenceBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class SubpassDescriptionBuilder(internal val target: VkSubpassDescription) {
	actual var flags: VkFlag<SubpassDescription>?
		get() = SubpassDescription.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var pipelineBindPoint: PipelineBindPoint?
		get() = PipelineBindPoint.from(target.pipelineBindPoint)
		set(value) {
			target.pipelineBindPoint = value.toVkType()
		}

	actual fun inputAttachments(block: AttachmentReferencesBuilder.() -> Unit) {
		val targets = AttachmentReferencesBuilder().apply(block).targets
		target.pInputAttachments = targets.mapToStackArray()
		target.inputAttachmentCount = targets.size.toUInt()
	}

	actual fun colorAttachments(block: AttachmentReferencesBuilder.() -> Unit) {
		val targets = AttachmentReferencesBuilder().apply(block).targets
		target.pColorAttachments = targets.mapToStackArray()
		target.colorAttachmentCount = targets.size.toUInt()
	}

	actual fun resolveAttachments(block: AttachmentReferencesBuilder.() -> Unit) {
		val targets = AttachmentReferencesBuilder().apply(block).targets
		target.pResolveAttachments = targets.mapToStackArray()
		target.colorAttachmentCount = targets.size.toUInt()
	}

	actual fun depthStencilAttachment(block: AttachmentReferenceBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkAttachmentReference>()
		target.pDepthStencilAttachment = subTarget.ptr
		val builder = AttachmentReferenceBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init(preserveAttachments: UIntArray?) {
		target.pPreserveAttachments = preserveAttachments?.toVkType()
		target.preserveAttachmentCount = preserveAttachments?.size?.toUInt() ?: 0U
	}
}

actual class SubpassDescriptionsBuilder {
	val targets: MutableList<(VkSubpassDescription) -> Unit> = mutableListOf()

	actual fun description(preserveAttachments: UIntArray?, block: SubpassDescriptionBuilder.() -> Unit) {
		targets += {
			val builder = SubpassDescriptionBuilder(it)
			builder.init(preserveAttachments)
			builder.apply(block)
		}
	}
}

actual class SubpassDependencyBuilder(internal val target: VkSubpassDependency) {
	actual var srcSubpass: UInt
		get() = target.srcSubpass
		set(value) {
			target.srcSubpass = value.toVkType()
		}

	actual var dstSubpass: UInt
		get() = target.dstSubpass
		set(value) {
			target.dstSubpass = value.toVkType()
		}

	actual var srcStageMask: VkFlag<PipelineStage>?
		get() = PipelineStage.fromMultiple(target.srcStageMask)
		set(value) {
			target.srcStageMask = value.toVkType()
		}

	actual var dstStageMask: VkFlag<PipelineStage>?
		get() = PipelineStage.fromMultiple(target.dstStageMask)
		set(value) {
			target.dstStageMask = value.toVkType()
		}

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

	actual var dependencyFlags: VkFlag<Dependency>?
		get() = Dependency.fromMultiple(target.dependencyFlags)
		set(value) {
			target.dependencyFlags = value.toVkType()
		}

	internal fun init() {
	}
}

actual class SubpassDependencysBuilder {
	val targets: MutableList<(VkSubpassDependency) -> Unit> = mutableListOf()

	actual fun dependency(block: SubpassDependencyBuilder.() -> Unit) {
		targets += {
			val builder = SubpassDependencyBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class RenderPassCreateInfoBuilder(internal val target: VkRenderPassCreateInfo) {
	actual fun attachments(block: AttachmentDescriptionsBuilder.() -> Unit) {
		val targets = AttachmentDescriptionsBuilder().apply(block).targets
		target.pAttachments = targets.mapToStackArray()
		target.attachmentCount = targets.size.toUInt()
	}

	actual fun subpasses(block: SubpassDescriptionsBuilder.() -> Unit) {
		val targets = SubpassDescriptionsBuilder().apply(block).targets
		target.pSubpasses = targets.mapToStackArray()
		target.subpassCount = targets.size.toUInt()
	}

	actual fun dependencies(block: SubpassDependencysBuilder.() -> Unit) {
		val targets = SubpassDependencysBuilder().apply(block).targets
		target.pDependencies = targets.mapToStackArray()
		target.dependencyCount = targets.size.toUInt()
	}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO
		target.pNext = null
		target.flags = 0U
	}
}

actual class AttachmentDescription2KHRBuilder(internal val target: VkAttachmentDescription2KHR) {
	actual var flags: VkFlag<AttachmentDescription>?
		get() = AttachmentDescription.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var format: Format?
		get() = Format.from(target.format)
		set(value) {
			target.format = value.toVkType()
		}

	actual var samples: SampleCount?
		get() = SampleCount.from(target.samples)
		set(value) {
			target.samples = value.toVkType()
		}

	actual var loadOp: AttachmentLoadOp?
		get() = AttachmentLoadOp.from(target.loadOp)
		set(value) {
			target.loadOp = value.toVkType()
		}

	actual var storeOp: AttachmentStoreOp?
		get() = AttachmentStoreOp.from(target.storeOp)
		set(value) {
			target.storeOp = value.toVkType()
		}

	actual var stencilLoadOp: AttachmentLoadOp?
		get() = AttachmentLoadOp.from(target.stencilLoadOp)
		set(value) {
			target.stencilLoadOp = value.toVkType()
		}

	actual var stencilStoreOp: AttachmentStoreOp?
		get() = AttachmentStoreOp.from(target.stencilStoreOp)
		set(value) {
			target.stencilStoreOp = value.toVkType()
		}

	actual var initialLayout: ImageLayout?
		get() = ImageLayout.from(target.initialLayout)
		set(value) {
			target.initialLayout = value.toVkType()
		}

	actual var finalLayout: ImageLayout?
		get() = ImageLayout.from(target.finalLayout)
		set(value) {
			target.finalLayout = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_ATTACHMENT_DESCRIPTION_2_KHR
		target.pNext = null
	}
}

actual class AttachmentDescription2KHRsBuilder {
	val targets: MutableList<(VkAttachmentDescription2KHR) -> Unit> = mutableListOf()

	actual fun description2(block: AttachmentDescription2KHRBuilder.() -> Unit) {
		targets += {
			val builder = AttachmentDescription2KHRBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class AttachmentReference2KHRBuilder(internal val target: VkAttachmentReference2KHR) {
	actual var attachment: UInt
		get() = target.attachment
		set(value) {
			target.attachment = value.toVkType()
		}

	actual var layout: ImageLayout?
		get() = ImageLayout.from(target.layout)
		set(value) {
			target.layout = value.toVkType()
		}

	actual var aspectMask: VkFlag<ImageAspect>?
		get() = ImageAspect.fromMultiple(target.aspectMask)
		set(value) {
			target.aspectMask = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_ATTACHMENT_REFERENCE_2_KHR
		target.pNext = null
	}
}

actual class AttachmentReference2KHRsBuilder {
	val targets: MutableList<(VkAttachmentReference2KHR) -> Unit> = mutableListOf()

	actual fun reference2(block: AttachmentReference2KHRBuilder.() -> Unit) {
		targets += {
			val builder = AttachmentReference2KHRBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class SubpassDescription2KHRBuilder(internal val target: VkSubpassDescription2KHR) {
	actual var flags: VkFlag<SubpassDescription>?
		get() = SubpassDescription.fromMultiple(target.flags)
		set(value) {
			target.flags = value.toVkType()
		}

	actual var pipelineBindPoint: PipelineBindPoint?
		get() = PipelineBindPoint.from(target.pipelineBindPoint)
		set(value) {
			target.pipelineBindPoint = value.toVkType()
		}

	actual var viewMask: UInt
		get() = target.viewMask
		set(value) {
			target.viewMask = value.toVkType()
		}

	actual fun inputAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit) {
		val targets = AttachmentReference2KHRsBuilder().apply(block).targets
		target.pInputAttachments = targets.mapToStackArray()
		target.inputAttachmentCount = targets.size.toUInt()
	}

	actual fun colorAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit) {
		val targets = AttachmentReference2KHRsBuilder().apply(block).targets
		target.pColorAttachments = targets.mapToStackArray()
		target.colorAttachmentCount = targets.size.toUInt()
	}

	actual fun resolveAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit) {
		val targets = AttachmentReference2KHRsBuilder().apply(block).targets
		target.pResolveAttachments = targets.mapToStackArray()
		target.colorAttachmentCount = targets.size.toUInt()
	}

	actual fun depthStencilAttachment(block: AttachmentReference2KHRBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkAttachmentReference2KHR>()
		target.pDepthStencilAttachment = subTarget.ptr
		val builder = AttachmentReference2KHRBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal fun init(preserveAttachments: UIntArray) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SUBPASS_DESCRIPTION_2_KHR
		target.pNext = null
		target.pPreserveAttachments = preserveAttachments.toVkType()
		target.preserveAttachmentCount = preserveAttachments.size.toUInt()
	}
}

actual class SubpassDescription2KHRsBuilder {
	val targets: MutableList<(VkSubpassDescription2KHR) -> Unit> = mutableListOf()

	actual fun description2(preserveAttachments: UIntArray, block: SubpassDescription2KHRBuilder.() -> Unit) {
		targets += {
			val builder = SubpassDescription2KHRBuilder(it)
			builder.init(preserveAttachments)
			builder.apply(block)
		}
	}
}

actual class SubpassDependency2KHRBuilder(internal val target: VkSubpassDependency2KHR) {
	actual var srcSubpass: UInt
		get() = target.srcSubpass
		set(value) {
			target.srcSubpass = value.toVkType()
		}

	actual var dstSubpass: UInt
		get() = target.dstSubpass
		set(value) {
			target.dstSubpass = value.toVkType()
		}

	actual var srcStageMask: VkFlag<PipelineStage>?
		get() = PipelineStage.fromMultiple(target.srcStageMask)
		set(value) {
			target.srcStageMask = value.toVkType()
		}

	actual var dstStageMask: VkFlag<PipelineStage>?
		get() = PipelineStage.fromMultiple(target.dstStageMask)
		set(value) {
			target.dstStageMask = value.toVkType()
		}

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

	actual var dependencyFlags: VkFlag<Dependency>?
		get() = Dependency.fromMultiple(target.dependencyFlags)
		set(value) {
			target.dependencyFlags = value.toVkType()
		}

	actual var viewOffset: Int
		get() = target.viewOffset
		set(value) {
			target.viewOffset = value.toVkType()
		}

	internal fun init() {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_SUBPASS_DEPENDENCY_2_KHR
		target.pNext = null
	}
}

actual class SubpassDependency2KHRsBuilder {
	val targets: MutableList<(VkSubpassDependency2KHR) -> Unit> = mutableListOf()

	actual fun dependency2(block: SubpassDependency2KHRBuilder.() -> Unit) {
		targets += {
			val builder = SubpassDependency2KHRBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class RenderPassCreateInfo2KHRBuilder(internal val target: VkRenderPassCreateInfo2KHR) {
	actual fun attachments(block: AttachmentDescription2KHRsBuilder.() -> Unit) {
		val targets = AttachmentDescription2KHRsBuilder().apply(block).targets
		target.pAttachments = targets.mapToStackArray()
		target.attachmentCount = targets.size.toUInt()
	}

	actual fun subpasses(block: SubpassDescription2KHRsBuilder.() -> Unit) {
		val targets = SubpassDescription2KHRsBuilder().apply(block).targets
		target.pSubpasses = targets.mapToStackArray()
		target.subpassCount = targets.size.toUInt()
	}

	actual fun dependencies(block: SubpassDependency2KHRsBuilder.() -> Unit) {
		val targets = SubpassDependency2KHRsBuilder().apply(block).targets
		target.pDependencies = targets.mapToStackArray()
		target.dependencyCount = targets.size.toUInt()
	}

	internal fun init(correlatedViewMasks: UIntArray) {
		target.sType = cvulkan.VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO_2_KHR
		target.pNext = null
		target.flags = 0U
		target.pCorrelatedViewMasks = correlatedViewMasks.toVkType()
		target.correlatedViewMaskCount = correlatedViewMasks.size.toUInt()
	}
}

