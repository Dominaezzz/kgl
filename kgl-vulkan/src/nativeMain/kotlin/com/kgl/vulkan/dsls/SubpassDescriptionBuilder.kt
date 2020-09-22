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

import com.kgl.core.*
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*

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
		target.pInputAttachments = targets.mapToStackArray(::AttachmentReferenceBuilder)
		target.inputAttachmentCount = targets.size.toUInt()
	}

	actual fun colorAttachments(block: AttachmentReferencesBuilder.() -> Unit) {
		val targets = AttachmentReferencesBuilder().apply(block).targets
		target.pColorAttachments = targets.mapToStackArray(::AttachmentReferenceBuilder)
		target.colorAttachmentCount = targets.size.toUInt()
	}

	actual fun resolveAttachments(block: AttachmentReferencesBuilder.() -> Unit) {
		val targets = AttachmentReferencesBuilder().apply(block).targets
		target.pResolveAttachments = targets.mapToStackArray(::AttachmentReferenceBuilder)
		target.colorAttachmentCount = targets.size.toUInt()
	}

	actual fun depthStencilAttachment(block: AttachmentReferenceBuilder.() -> Unit) {
		val subTarget = VirtualStack.alloc<VkAttachmentReference>()
		target.pDepthStencilAttachment = subTarget.ptr
		val builder = AttachmentReferenceBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	actual fun next(block: Next<SubpassDescriptionBuilder>.() -> Unit) {
		Next(this).apply(block)
	}

	internal actual fun init(preserveAttachments: UIntArray?) {
		target.pPreserveAttachments = preserveAttachments?.toVkType()
		target.preserveAttachmentCount = preserveAttachments?.size?.toUInt() ?: 0U
	}
}
