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
package com.kgl.vulkan.enums

import com.kgl.vulkan.utils.VkEnum
import cvulkan.VK_ATTACHMENT_STORE_OP_DONT_CARE
import cvulkan.VK_ATTACHMENT_STORE_OP_STORE
import cvulkan.VkAttachmentStoreOp

actual enum class AttachmentStoreOp(override val value: VkAttachmentStoreOp) : VkEnum<AttachmentStoreOp> {
	STORE(VK_ATTACHMENT_STORE_OP_STORE),

	DONT_CARE(VK_ATTACHMENT_STORE_OP_DONT_CARE);

	companion object {
		fun from(value: VkAttachmentStoreOp): AttachmentStoreOp =
				enumValues<AttachmentStoreOp>()[value.toInt()]
	}
}

