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
package com.kgl.vulkan.handles

import com.kgl.vulkan.enums.ShaderInfoTypeAMD
import com.kgl.vulkan.enums.ShaderStage
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleJVM
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import io.ktor.utils.io.bits.Memory
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.NVRayTracing.vkCompileDeferredNV
import org.lwjgl.vulkan.NVRayTracing.vkGetRayTracingShaderGroupHandlesNV
import org.lwjgl.vulkan.VK11.VK_SUCCESS
import org.lwjgl.vulkan.VK11.vkDestroyPipeline

actual class Pipeline(override val ptr: Long, actual val device: Device) : VkHandleJVM<Long>(), VkHandle {
	override fun close() {
		val pipeline = this
		val device = pipeline.device
		MemoryStack.stackPush()
		try {
			vkDestroyPipeline(device.toVkType(), pipeline.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getShaderInfoAMD(
			shaderStage: ShaderStage,
			infoType: ShaderInfoTypeAMD,
			info: Memory?
	) {
		TODO()
		val pipeline = this
		val device = pipeline.device
		MemoryStack.stackPush()
		try {
//            val result = vkGetShaderInfoAMD(device.toVkType(), pipeline.toVkType(),
//                    shaderStage.toVkType(), infoType.toVkType(), info.toVkType())
//            when (result) {
//                VK_SUCCESS -> Unit
//                VK_INCOMPLETE -> Unit
//                else -> handleVkResult(result)
//            }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun compileDeferredNV(shader: UInt) {
		val pipeline = this
		val device = pipeline.device
		MemoryStack.stackPush()
		try {
			val result = vkCompileDeferredNV(device.toVkType(), pipeline.toVkType(),
					shader.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getRayTracingShaderGroupHandlesNV(firstGroup: UInt, groupCount: UInt, data: Memory) {
		val pipeline = this
		val device = pipeline.device
		MemoryStack.stackPush()
		try {
			val result = vkGetRayTracingShaderGroupHandlesNV(device.toVkType(), pipeline.toVkType(),
					firstGroup.toVkType(), groupCount.toVkType(), data.buffer)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}
}

