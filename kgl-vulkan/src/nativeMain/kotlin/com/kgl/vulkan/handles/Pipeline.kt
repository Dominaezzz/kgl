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

import com.kgl.core.ByteBuffer
import com.kgl.core.VirtualStack
import com.kgl.vulkan.enums.ShaderInfoTypeAMD
import com.kgl.vulkan.enums.ShaderStage
import com.kgl.vulkan.utils.VkHandle
import com.kgl.vulkan.utils.VkHandleNative
import com.kgl.vulkan.utils.handleVkResult
import com.kgl.vulkan.utils.toVkType
import cvulkan.VK_SUCCESS
import cvulkan.VkPipeline
import kotlinx.cinterop.invoke

actual class Pipeline(override val ptr: VkPipeline, actual val device: Device) : VkHandleNative<VkPipeline>(), VkHandle {
	internal val dispatchTable = device.dispatchTable

	override fun close() {
		val pipeline = this
		val device = pipeline.device
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyPipeline(device.toVkType(), pipeline.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getShaderInfoAMD(
			shaderStage: ShaderStage,
			infoType: ShaderInfoTypeAMD,
			info: ByteBuffer?
	) {
		TODO()
		val pipeline = this
		val device = pipeline.device
		VirtualStack.push()
		try {
//			val result = dispatchTable.vkGetShaderInfoAMD!!(device.toVkType(), pipeline.toVkType(),
//					shaderStage.toVkType(), infoType.toVkType(), info?.writeRemaining?.toULong() ?:
//			0U, info.toVkType())
//			when (result) {
//				VK_SUCCESS -> Unit
//				VK_INCOMPLETE -> Unit
//				else -> handleVkResult(result)
//			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun compileDeferredNV(shader: UInt) {
		val pipeline = this
		val device = pipeline.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkCompileDeferredNV!!(device.toVkType(), pipeline.toVkType(),
					shader.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getRayTracingShaderGroupHandlesNV(firstGroup: UInt, groupCount: UInt, data: ByteBuffer) {
		val pipeline = this
		val device = pipeline.device
		VirtualStack.push()
		try {
			val result = dispatchTable.vkGetRayTracingShaderGroupHandlesNV!!(device.toVkType(), pipeline.toVkType(),
					firstGroup.toVkType(), groupCount.toVkType(), data.size.toULong(), data.asCPointer())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}
}

