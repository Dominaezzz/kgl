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

import com.kgl.vulkan.enums.PipelineBindPoint
import com.kgl.vulkan.enums.SubpassDescription
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.KHRCreateRenderpass2
import org.lwjgl.vulkan.VkAttachmentReference2KHR
import org.lwjgl.vulkan.VkSubpassDescription2KHR

actual class SubpassDescription2KHRBuilder(internal val target: VkSubpassDescription2KHR) {
	actual var flags: VkFlag<SubpassDescription>?
		get() = SubpassDescription.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual var pipelineBindPoint: PipelineBindPoint?
		get() = PipelineBindPoint.from(target.pipelineBindPoint())
		set(value) {
			target.pipelineBindPoint(value.toVkType())
		}

	actual var viewMask: UInt
		get() = target.viewMask().toUInt()
		set(value) {
			target.viewMask(value.toVkType())
		}

	actual fun inputAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit) {
		val targets = AttachmentReference2KHRsBuilder().apply(block).targets
		target.pInputAttachments(targets.mapToStackArray(VkAttachmentReference2KHR::callocStack, ::AttachmentReference2KHRBuilder))
	}

	actual fun colorAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit) {
		val targets = AttachmentReference2KHRsBuilder().apply(block).targets
		target.pColorAttachments(targets.mapToStackArray(VkAttachmentReference2KHR::callocStack, ::AttachmentReference2KHRBuilder))
	}

	actual fun resolveAttachments(block: AttachmentReference2KHRsBuilder.() -> Unit) {
		val targets = AttachmentReference2KHRsBuilder().apply(block).targets
		target.pResolveAttachments(targets.mapToStackArray(VkAttachmentReference2KHR::callocStack, ::AttachmentReference2KHRBuilder))
	}

	actual fun depthStencilAttachment(block: AttachmentReference2KHRBuilder.() -> Unit) {
		val subTarget = VkAttachmentReference2KHR.callocStack()
		target.pDepthStencilAttachment(subTarget)
		val builder = AttachmentReference2KHRBuilder(subTarget)
		builder.init()
		builder.apply(block)
	}

	internal actual fun init(preserveAttachments: UIntArray) {
		target.sType(KHRCreateRenderpass2.VK_STRUCTURE_TYPE_SUBPASS_DESCRIPTION_2_KHR)
		target.pNext(0)
		target.pPreserveAttachments(preserveAttachments.toVkType())
	}
}
