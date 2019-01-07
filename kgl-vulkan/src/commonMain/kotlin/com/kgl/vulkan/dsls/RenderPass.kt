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
import com.kgl.vulkan.utils.VkFlag

expect class AttachmentDescriptionBuilder {
	var flags: VkFlag<AttachmentDescription>?

	var format: Format?

	var samples: SampleCount?

	var loadOp: AttachmentLoadOp?

	var storeOp: AttachmentStoreOp?

	var stencilLoadOp: AttachmentLoadOp?

	var stencilStoreOp: AttachmentStoreOp?

	var initialLayout: ImageLayout?

	var finalLayout: ImageLayout?
}

expect class AttachmentDescriptionsBuilder {
	fun description(block: AttachmentDescriptionBuilder.() -> Unit = {})
}

expect class AttachmentReferenceBuilder {
	var attachment: UInt

	var layout: ImageLayout?
}

expect class AttachmentReferencesBuilder {
	fun reference(block: AttachmentReferenceBuilder.() -> Unit = {})
}

expect class SubpassDescriptionBuilder {
	var flags: VkFlag<SubpassDescription>?

	var pipelineBindPoint: PipelineBindPoint?

	fun inputAttachments(block: AttachmentReferencesBuilder.() -> Unit)

	fun colorAttachments(block: AttachmentReferencesBuilder.() -> Unit)

	fun resolveAttachments(block: AttachmentReferencesBuilder.() -> Unit)

	fun depthStencilAttachment(block: AttachmentReferenceBuilder.() -> Unit = {})
}

expect class SubpassDescriptionsBuilder {
	fun description(preserveAttachments: UIntArray? = null, block: SubpassDescriptionBuilder.() -> Unit)
}

expect class SubpassDependencyBuilder {
	var srcSubpass: UInt

	var dstSubpass: UInt

	var srcStageMask: VkFlag<PipelineStage>?

	var dstStageMask: VkFlag<PipelineStage>?

	var srcAccessMask: VkFlag<Access>?

	var dstAccessMask: VkFlag<Access>?

	var dependencyFlags: VkFlag<Dependency>?
}

expect class SubpassDependencysBuilder {
	fun dependency(block: SubpassDependencyBuilder.() -> Unit = {})
}

expect class RenderPassCreateInfoBuilder {
	fun attachments(block: AttachmentDescriptionsBuilder.() -> Unit)

	fun subpasses(block: SubpassDescriptionsBuilder.() -> Unit)

	fun dependencies(block: SubpassDependencysBuilder.() -> Unit)
}

expect class AttachmentDescription2KHRBuilder {
	var flags: VkFlag<AttachmentDescription>?

	var format: Format?

	var samples: SampleCount?

	var loadOp: AttachmentLoadOp?

	var storeOp: AttachmentStoreOp?

	var stencilLoadOp: AttachmentLoadOp?

	var stencilStoreOp: AttachmentStoreOp?

	var initialLayout: ImageLayout?

	var finalLayout: ImageLayout?
}

expect class AttachmentDescription2KHRsBuilder {
	fun description2(block: AttachmentDescription2KHRBuilder.() -> Unit = {})
}

expect class AttachmentReference2KHRBuilder {
	var attachment: UInt

	var layout: ImageLayout?

	var aspectMask: VkFlag<ImageAspect>?
}

expect class AttachmentReference2KHRsBuilder {
	fun reference2(block: AttachmentReference2KHRBuilder.() -> Unit = {})
}

expect class SubpassDescription2KHRBuilder {
	var flags: VkFlag<SubpassDescription>?

	var pipelineBindPoint: PipelineBindPoint?

	var viewMask: UInt

	fun inputAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit)

	fun colorAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit)

	fun resolveAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit)

	fun depthStencilAttachment(block: AttachmentReference2KHRBuilder.() -> Unit = {})
}

expect class SubpassDescription2KHRsBuilder {
	fun description2(preserveAttachments: UIntArray, block: SubpassDescription2KHRBuilder.() -> Unit)
}

expect class SubpassDependency2KHRBuilder {
	var srcSubpass: UInt

	var dstSubpass: UInt

	var srcStageMask: VkFlag<PipelineStage>?

	var dstStageMask: VkFlag<PipelineStage>?

	var srcAccessMask: VkFlag<Access>?

	var dstAccessMask: VkFlag<Access>?

	var dependencyFlags: VkFlag<Dependency>?

	var viewOffset: Int
}

expect class SubpassDependency2KHRsBuilder {
	fun dependency2(block: SubpassDependency2KHRBuilder.() -> Unit = {})
}

expect class RenderPassCreateInfo2KHRBuilder {
	fun attachments(block: AttachmentDescription2KHRsBuilder.() -> Unit)

	fun subpasses(block: SubpassDescription2KHRsBuilder.() -> Unit)

	fun dependencies(block: SubpassDependency2KHRsBuilder.() -> Unit)
}

