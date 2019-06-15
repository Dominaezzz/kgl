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
package codegen.vulkan

import com.squareup.kotlinpoet.ClassName


val VK_ENUM = ClassName("com.kgl.vulkan.utils", "VkEnum")
val VK_FLAG = ClassName("com.kgl.vulkan.utils", "VkFlag")
val VK_HANDLE = ClassName("com.kgl.vulkan.utils", "VkHandle")
val VK_HANDLE_JVM = ClassName("com.kgl.vulkan.utils", "VkHandleJVM")
val VK_HANDLE_NATIVE = ClassName("com.kgl.vulkan.utils", "VkHandleNative")
val VK_VERSION = ClassName("com.kgl.vulkan.utils", "VkVersion")

val BASE_OUT_STRUCTURE = ClassName("com.kgl.vulkan.structs", "BaseOutStructure")

const val LWJGLVulkanPackage = "org.lwjgl.vulkan"
val VK10 = ClassName(LWJGLVulkanPackage, "VK10")
val VK11 = ClassName(LWJGLVulkanPackage, "VK11")
