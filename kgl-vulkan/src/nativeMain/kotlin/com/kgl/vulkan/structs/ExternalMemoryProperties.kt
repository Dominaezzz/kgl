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
package com.kgl.vulkan.structs

import com.kgl.vulkan.enums.ExternalMemoryFeature
import com.kgl.vulkan.enums.ExternalMemoryHandleType
import cvulkan.VkExternalMemoryProperties

fun ExternalMemoryProperties.Companion.from(ptr: VkExternalMemoryProperties): ExternalMemoryProperties = ExternalMemoryProperties(
		ExternalMemoryFeature.fromMultiple(ptr.externalMemoryFeatures),
		ExternalMemoryHandleType.fromMultiple(ptr.exportFromImportedHandleTypes),
		ExternalMemoryHandleType.fromMultiple(ptr.compatibleHandleTypes)
)
