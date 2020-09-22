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
import com.kgl.vulkan.utils.*

@StructMarker
expect class SubpassDescriptionBuilder {
	var flags: VkFlag<SubpassDescription>?

	var pipelineBindPoint: PipelineBindPoint?

	fun inputAttachments(block: AttachmentReferencesBuilder.() -> Unit)

	fun colorAttachments(block: AttachmentReferencesBuilder.() -> Unit)

	fun resolveAttachments(block: AttachmentReferencesBuilder.() -> Unit)

	fun depthStencilAttachment(block: AttachmentReferenceBuilder.() -> Unit = {})

	fun next(block: Next<SubpassDescriptionBuilder>.() -> Unit)

	internal fun init(preserveAttachments: UIntArray?)
}
