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

import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import io.ktor.utils.io.bits.Memory
import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.EXTDebugMarker.vkDebugMarkerSetObjectNameEXT
import org.lwjgl.vulkan.EXTDebugMarker.vkDebugMarkerSetObjectTagEXT
import org.lwjgl.vulkan.EXTDebugUtils.vkSetDebugUtilsObjectNameEXT
import org.lwjgl.vulkan.EXTDebugUtils.vkSetDebugUtilsObjectTagEXT
import org.lwjgl.vulkan.EXTDisplayControl.*
import org.lwjgl.vulkan.EXTExternalMemoryHost.vkGetMemoryHostPointerPropertiesEXT
import org.lwjgl.vulkan.EXTHdrMetadata.vkSetHdrMetadataEXT
import org.lwjgl.vulkan.EXTValidationCache.vkCreateValidationCacheEXT
import org.lwjgl.vulkan.KHRCreateRenderpass2.vkCreateRenderPass2KHR
import org.lwjgl.vulkan.KHRDisplaySwapchain.vkCreateSharedSwapchainsKHR
import org.lwjgl.vulkan.KHRExternalMemoryFd.vkGetMemoryFdPropertiesKHR
import org.lwjgl.vulkan.KHRSwapchain.*
import org.lwjgl.vulkan.NVRayTracing.vkBindAccelerationStructureMemoryNV
import org.lwjgl.vulkan.NVRayTracing.vkCreateAccelerationStructureNV
import org.lwjgl.vulkan.NVXDeviceGeneratedCommands.vkCreateIndirectCommandsLayoutNVX
import org.lwjgl.vulkan.NVXDeviceGeneratedCommands.vkCreateObjectTableNVX
import org.lwjgl.vulkan.VK10.vkUpdateDescriptorSets
import org.lwjgl.vulkan.VK11.*

actual class Device(override val ptr: VkDevice, actual val physicalDevice: PhysicalDevice) : VkHandleJVM<VkDevice>(), VkHandle {
	actual val groupPresentCapabilitiesKHR: DeviceGroupPresentCapabilitiesKHR
		get() {
			val device = this
			MemoryStack.stackPush()
			try {
				val outputPtr = VkDeviceGroupPresentCapabilitiesKHR.mallocStack()
				val result = vkGetDeviceGroupPresentCapabilitiesKHR(device.toVkType(), outputPtr)
				if (result != VK_SUCCESS) handleVkResult(result)
				return DeviceGroupPresentCapabilitiesKHR.from(outputPtr)
			} finally {
				MemoryStack.stackPop()
			}
		}

	override fun close() {
		val device = this
		MemoryStack.stackPush()
		try {
			vkDestroyDevice(device.toVkType(), null)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getProcAddr(name: String): Long {
		val device = this
		MemoryStack.stackPush()
		try {
			return vkGetDeviceProcAddr(device.toVkType(), name.toVkType())
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getQueue(queueFamilyIndex: UInt, queueIndex: UInt): Queue {
		val device = this
		MemoryStack.stackPush()
		try {
			val outputPtr = MemoryStack.stackGet().mallocPointer(1)
			vkGetDeviceQueue(device.toVkType(), queueFamilyIndex.toVkType(), queueIndex.toVkType(),
					outputPtr)
			return Queue(VkQueue(outputPtr[0], ptr), this, queueFamilyIndex)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun waitIdle() {
		val device = this
		MemoryStack.stackPush()
		try {
			val result = vkDeviceWaitIdle(device.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun allocateMemory(block: MemoryAllocateInfoBuilder.() -> Unit): DeviceMemory {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkMemoryAllocateInfo.callocStack()
			val builder = MemoryAllocateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkAllocateMemory(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DeviceMemory(outputPtr[0], this, builder.allocationSize, builder.memoryTypeIndex)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun flushMappedMemoryRanges(block: MappedMemoryRangesBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = MappedMemoryRangesBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkMappedMemoryRange::callocStack, ::MappedMemoryRangeBuilder)
			val result = vkFlushMappedMemoryRanges(device.toVkType(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun invalidateMappedMemoryRanges(block: MappedMemoryRangesBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = MappedMemoryRangesBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkMappedMemoryRange::callocStack, ::MappedMemoryRangeBuilder)
			val result = vkInvalidateMappedMemoryRanges(device.toVkType(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createFence(block: FenceCreateInfoBuilder.() -> Unit): Fence {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkFenceCreateInfo.callocStack()
			val builder = FenceCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateFence(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Fence(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun resetFences(fences: Collection<Fence>) {
		val device = this
		MemoryStack.stackPush()
		try {
			val result = vkResetFences(device.toVkType(), fences.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun waitForFences(fences: Collection<Fence>, waitAll: Boolean, timeout: ULong): Boolean {
		val device = this
		MemoryStack.stackPush()
		try {
			val result = vkWaitForFences(device.toVkType(), fences.toVkType(), waitAll.toVkType(),
					timeout.toVkType())
			return when (result) {
				VK_SUCCESS -> true
				VK_TIMEOUT -> false
				else -> handleVkResult(result)
			}
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createSemaphore(block: SemaphoreCreateInfoBuilder.() -> Unit): Semaphore {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkSemaphoreCreateInfo.callocStack()
			val builder = SemaphoreCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateSemaphore(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Semaphore(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createEvent(block: EventCreateInfoBuilder.() -> Unit): Event {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkEventCreateInfo.callocStack()
			val builder = EventCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateEvent(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Event(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createQueryPool(block: QueryPoolCreateInfoBuilder.() -> Unit): QueryPool {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkQueryPoolCreateInfo.callocStack()
			val builder = QueryPoolCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateQueryPool(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return QueryPool(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createBuffer(queueFamilyIndices: UIntArray?, block: BufferCreateInfoBuilder.() -> Unit): Buffer {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkBufferCreateInfo.callocStack()
			val builder = BufferCreateInfoBuilder(target)
			builder.init(queueFamilyIndices)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateBuffer(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Buffer(outputPtr[0], this, builder.size)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createImage(queueFamilyIndices: UIntArray?, block: ImageCreateInfoBuilder.() -> Unit): Image {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkImageCreateInfo.callocStack()
			val builder = ImageCreateInfoBuilder(target)
			builder.init(queueFamilyIndices)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateImage(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Image(outputPtr[0], this,
					builder.imageType!!, builder.format!!, builder.mipLevels,
					Extent3D.from(target.extent()), builder.arrayLayers)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createShaderModule(code: UByteArray, block: ShaderModuleCreateInfoBuilder.() -> Unit): ShaderModule {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkShaderModuleCreateInfo.callocStack()
			val builder = ShaderModuleCreateInfoBuilder(target)
			builder.init(code)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateShaderModule(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ShaderModule(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createPipelineCache(initialData: Memory?, block: PipelineCacheCreateInfoBuilder.() -> Unit): PipelineCache {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkPipelineCacheCreateInfo.callocStack()
			val builder = PipelineCacheCreateInfoBuilder(target)
			builder.init(initialData)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreatePipelineCache(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return PipelineCache(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createPipelineLayout(setLayouts: Collection<DescriptorSetLayout>?, block: PipelineLayoutCreateInfoBuilder.() -> Unit): PipelineLayout {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkPipelineLayoutCreateInfo.callocStack()
			val builder = PipelineLayoutCreateInfoBuilder(target)
			builder.init(setLayouts)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreatePipelineLayout(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return PipelineLayout(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createGraphicsPipelines(pipelineCache: PipelineCache?, block: GraphicsPipelineCreateInfosBuilder.() -> Unit): List<Pipeline> {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = GraphicsPipelineCreateInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkGraphicsPipelineCreateInfo::callocStack, ::GraphicsPipelineCreateInfoBuilder)
			val outputCount = targets.size
			val outputPtr = MemoryStack.stackGet().mallocLong(outputCount)
			val result = VK11.vkCreateGraphicsPipelines(device.toVkType(), pipelineCache.toVkType(),
					targetArray, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { Pipeline(outputPtr[it], this) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createComputePipelines(pipelineCache: PipelineCache?, block: ComputePipelineCreateInfosBuilder.() -> Unit): List<Pipeline> {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = ComputePipelineCreateInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkComputePipelineCreateInfo::callocStack, ::ComputePipelineCreateInfoBuilder)
			val outputCount = targets.size
			val outputPtr = MemoryStack.stackGet().mallocLong(outputCount)
			val result = VK11.vkCreateComputePipelines(device.toVkType(), pipelineCache.toVkType(),
					targetArray, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { Pipeline(outputPtr[it], this) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createRayTracingPipelinesNV(pipelineCache: PipelineCache?, block: RayTracingPipelineCreateInfoNVsBuilder.() -> Unit): List<Pipeline> {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = RayTracingPipelineCreateInfoNVsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkRayTracingPipelineCreateInfoNV::callocStack, ::RayTracingPipelineCreateInfoNVBuilder)
			val outputCount = targets.size
			val outputPtr = MemoryStack.stackGet().mallocLong(outputCount)
			val result = NVRayTracing.vkCreateRayTracingPipelinesNV(device.toVkType(), pipelineCache.toVkType(),
					targetArray, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { Pipeline(outputPtr[it], this) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createSampler(block: SamplerCreateInfoBuilder.() -> Unit): Sampler {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkSamplerCreateInfo.callocStack()
			val builder = SamplerCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateSampler(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Sampler(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDescriptorSetLayout(block: DescriptorSetLayoutCreateInfoBuilder.() -> Unit): DescriptorSetLayout {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDescriptorSetLayoutCreateInfo.callocStack()
			val builder = DescriptorSetLayoutCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateDescriptorSetLayout(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DescriptorSetLayout(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createDescriptorPool(maxSets: UInt, block: DescriptorPoolCreateInfoBuilder.() -> Unit): DescriptorPool {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDescriptorPoolCreateInfo.callocStack()
			val builder = DescriptorPoolCreateInfoBuilder(target)
			builder.init(maxSets)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateDescriptorPool(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DescriptorPool(outputPtr[0], this, maxSets)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createRenderPass(block: RenderPassCreateInfoBuilder.() -> Unit): RenderPass {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkRenderPassCreateInfo.callocStack()
			val builder = RenderPassCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateRenderPass(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return RenderPass(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createCommandPool(queueFamilyIndex: UInt, block: CommandPoolCreateInfoBuilder.() -> Unit): CommandPool {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkCommandPoolCreateInfo.callocStack()
			val builder = CommandPoolCreateInfoBuilder(target)
			builder.init(queueFamilyIndex)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateCommandPool(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return CommandPool(outputPtr[0], this, queueFamilyIndex)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createSharedSwapchainsKHR(block: CreateSharedSwapchainsKHRBuilder.() -> Unit): List<SwapchainKHR> {
		val device = this
		MemoryStack.stackPush()
		try {
			val builder = CreateSharedSwapchainsKHRBuilder().apply(block)
			val targets = builder.targets
			val targetArray = targets.mapToStackArray(VkSwapchainCreateInfoKHR::callocStack, ::SwapchainCreateInfoKHRBuilder)
			val outputCount = targets.size
			val outputPtr = MemoryStack.stackGet().mallocLong(outputCount)
			val result = vkCreateSharedSwapchainsKHR(device.toVkType(), targetArray, null,
					outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { SwapchainKHR(
					outputPtr[it],
					builder.surfaces[it],
					device,
					Format.from(targetArray[it].imageFormat()),
					Extent2D.from(targetArray[it].imageExtent()),
					targetArray[it].imageArrayLayers().toUInt()
			) }
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createSwapchainKHR(
			surface: SurfaceKHR,
			queueFamilyIndices: UIntArray?,
			oldSwapchain: SwapchainKHR?,
			block: SwapchainCreateInfoKHRBuilder.() -> Unit
	): SwapchainKHR {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkSwapchainCreateInfoKHR.callocStack()
			val builder = SwapchainCreateInfoKHRBuilder(target)
			builder.init(surface, queueFamilyIndices, oldSwapchain)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateSwapchainKHR(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SwapchainKHR(outputPtr[0], surface, device,
					builder.imageFormat!!, Extent2D.from(target.imageExtent()), builder.imageArrayLayers)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createFramebuffer(renderPass: RenderPass, attachments: Collection<ImageView>?, block: FramebufferCreateInfoBuilder.() -> Unit): Framebuffer {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkFramebufferCreateInfo.callocStack()
			val builder = FramebufferCreateInfoBuilder(target)
			builder.init(renderPass, attachments)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = VK11.vkCreateFramebuffer(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Framebuffer(outputPtr[0], this,
					renderPass, attachments?.toTypedArray(),
					builder.width, builder.height, builder.layers)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun debugMarkerSetObjectNameEXT(block: DebugMarkerObjectNameInfoEXTBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugMarkerObjectNameInfoEXT.callocStack()
			val builder = DebugMarkerObjectNameInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			val result = vkDebugMarkerSetObjectNameEXT(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun debugMarkerSetObjectTagEXT(tag: Memory, block: DebugMarkerObjectTagInfoEXTBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugMarkerObjectTagInfoEXT.callocStack()
			val builder = DebugMarkerObjectTagInfoEXTBuilder(target)
			builder.init(tag)
			builder.apply(block)
			val result = vkDebugMarkerSetObjectTagEXT(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createIndirectCommandsLayoutNVX(block: IndirectCommandsLayoutCreateInfoNVXBuilder.() -> Unit): IndirectCommandsLayoutNVX {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkIndirectCommandsLayoutCreateInfoNVX.callocStack()
			val builder = IndirectCommandsLayoutCreateInfoNVXBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateIndirectCommandsLayoutNVX(device.toVkType(), target, null,
					outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return IndirectCommandsLayoutNVX(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createObjectTableNVX(
			objectEntryTypes: Collection<ObjectEntryTypeNVX>,
			objectEntryCounts: UIntArray,
			objectEntryUsageFlags: Collection<VkFlag<ObjectEntryUsageNVX>>,
			block: ObjectTableCreateInfoNVXBuilder.() -> Unit
	): ObjectTableNVX {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkObjectTableCreateInfoNVX.callocStack()
			val builder = ObjectTableCreateInfoNVXBuilder(target)
			builder.init(objectEntryTypes, objectEntryCounts, objectEntryUsageFlags)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateObjectTableNVX(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ObjectTableNVX(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getMemoryFdPropertiesKHR(handleType: ExternalMemoryHandleType, fd: Int): MemoryFdPropertiesKHR {
		val device = this
		MemoryStack.stackPush()
		try {
			val outputPtr = VkMemoryFdPropertiesKHR.mallocStack()
			val result = vkGetMemoryFdPropertiesKHR(device.toVkType(), handleType.toVkType(),
					fd.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return MemoryFdPropertiesKHR.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun displayPowerControlEXT(display: DisplayKHR, block: DisplayPowerInfoEXTBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDisplayPowerInfoEXT.callocStack()
			val builder = DisplayPowerInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			val result = vkDisplayPowerControlEXT(device.toVkType(), display.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun registerEventEXT(block: DeviceEventInfoEXTBuilder.() -> Unit): Fence {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDeviceEventInfoEXT.callocStack()
			val builder = DeviceEventInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkRegisterDeviceEventEXT(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Fence(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun registerDisplayEventEXT(display: DisplayKHR, block: DisplayEventInfoEXTBuilder.() -> Unit): Fence {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDisplayEventInfoEXT.callocStack()
			val builder = DisplayEventInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkRegisterDisplayEventEXT(device.toVkType(), display.toVkType(), target,
					null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Fence(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getGroupPeerMemoryFeatures(heapIndex: UInt, localDeviceIndex: UInt, remoteDeviceIndex: UInt): VkFlag<PeerMemoryFeature> {
		val device = this
		MemoryStack.stackPush()
		try {
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			vkGetDeviceGroupPeerMemoryFeatures(device.toVkType(), heapIndex.toVkType(),
					localDeviceIndex.toVkType(), remoteDeviceIndex.toVkType(), outputPtr)
			return PeerMemoryFeature.fromMultiple(outputPtr[0])
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindBufferMemory2(block: BindBufferMemoryInfosBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = BindBufferMemoryInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkBindBufferMemoryInfo::callocStack, ::BindBufferMemoryInfoBuilder)
			val result = vkBindBufferMemory2(device.toVkType(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindImageMemory2(block: BindImageMemoryInfosBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = BindImageMemoryInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkBindImageMemoryInfo::callocStack, ::BindImageMemoryInfoBuilder)
			val result = vkBindImageMemory2(device.toVkType(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getGroupSurfacePresentModesKHR(surface: SurfaceKHR): VkFlag<DeviceGroupPresentModeKHR> {
		val device = this
		MemoryStack.stackPush()
		try {
			val outputPtr = MemoryStack.stackGet().mallocInt(1)
			val result = vkGetDeviceGroupSurfacePresentModesKHR(device.toVkType(),
					surface.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DeviceGroupPresentModeKHR.fromMultiple(outputPtr[0])
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setHdrMetadataEXT(swapchains: Collection<SwapchainKHR>, block: HdrMetadataEXTsBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = HdrMetadataEXTsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(VkHdrMetadataEXT::callocStack, ::HdrMetadataEXTBuilder)
			vkSetHdrMetadataEXT(device.toVkType(), swapchains.toVkType(), targetArray)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createSamplerYcbcrConversion(block: SamplerYcbcrConversionCreateInfoBuilder.() -> Unit): SamplerYcbcrConversion {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkSamplerYcbcrConversionCreateInfo.callocStack()
			val builder = SamplerYcbcrConversionCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateSamplerYcbcrConversion(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SamplerYcbcrConversion(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getQueue2(block: DeviceQueueInfo2Builder.() -> Unit): Queue {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDeviceQueueInfo2.callocStack()
			val builder = DeviceQueueInfo2Builder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocPointer(1)
			vkGetDeviceQueue2(device.toVkType(), target, outputPtr)
			return Queue(VkQueue(outputPtr[0], ptr), this, builder.queueFamilyIndex)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createValidationCacheEXT(initialData: Memory?, block: ValidationCacheCreateInfoEXTBuilder.() -> Unit): ValidationCacheEXT {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkValidationCacheCreateInfoEXT.callocStack()
			val builder = ValidationCacheCreateInfoEXTBuilder(target)
			builder.init(initialData)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateValidationCacheEXT(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ValidationCacheEXT(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getDescriptorSetLayoutSupport(block: DescriptorSetLayoutCreateInfoBuilder.() -> Unit): DescriptorSetLayoutSupport {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDescriptorSetLayoutCreateInfo.callocStack()
			val builder = DescriptorSetLayoutCreateInfoBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = VkDescriptorSetLayoutSupport.mallocStack()
			vkGetDescriptorSetLayoutSupport(device.toVkType(), target, outputPtr)
			return DescriptorSetLayoutSupport.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setDebugUtilsObjectNameEXT(block: DebugUtilsObjectNameInfoEXTBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsObjectNameInfoEXT.callocStack()
			val builder = DebugUtilsObjectNameInfoEXTBuilder(target)
			builder.init()
			builder.apply(block)
			val result = vkSetDebugUtilsObjectNameEXT(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun setDebugUtilsObjectTagEXT(tag: Memory, block: DebugUtilsObjectTagInfoEXTBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkDebugUtilsObjectTagInfoEXT.callocStack()
			val builder = DebugUtilsObjectTagInfoEXTBuilder(target)
			builder.init(tag)
			builder.apply(block)
			val result = vkSetDebugUtilsObjectTagEXT(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun getMemoryHostPointerPropertiesEXT(handleType: ExternalMemoryHandleType, pHostPointer: Long): MemoryHostPointerPropertiesEXT {
		MemoryStack.stackPush()
		try {
			val outputPtr = VkMemoryHostPointerPropertiesEXT.mallocStack()
			val result = vkGetMemoryHostPointerPropertiesEXT(ptr,
					handleType.toVkType(), pHostPointer.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return MemoryHostPointerPropertiesEXT.from(outputPtr)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createRenderPass2KHR(correlatedViewMasks: UIntArray, block: RenderPassCreateInfo2KHRBuilder.() -> Unit): RenderPass {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkRenderPassCreateInfo2KHR.callocStack()
			val builder = RenderPassCreateInfo2KHRBuilder(target)
			builder.init(correlatedViewMasks)
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateRenderPass2KHR(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return RenderPass(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun createAccelerationStructureNV(block: AccelerationStructureCreateInfoNVBuilder.() -> Unit): AccelerationStructureNV {
		val device = this
		MemoryStack.stackPush()
		try {
			val target = VkAccelerationStructureCreateInfoNV.callocStack()
			val builder = AccelerationStructureCreateInfoNVBuilder(target)
			builder.init()
			builder.apply(block)
			val outputPtr = MemoryStack.stackGet().mallocLong(1)
			val result = vkCreateAccelerationStructureNV(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return AccelerationStructureNV(outputPtr[0], this)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun bindAccelerationStructureMemoryNV(block: BindAccelerationStructureMemoryInfoNVsBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val targets = BindAccelerationStructureMemoryInfoNVsBuilder().apply(block).targets
			val targetArray =
					targets.mapToStackArray(VkBindAccelerationStructureMemoryInfoNV::callocStack, ::BindAccelerationStructureMemoryInfoNVBuilder)
			val result = vkBindAccelerationStructureMemoryNV(device.toVkType(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			MemoryStack.stackPop()
		}
	}

	actual fun updateDescriptorSets(block: UpdateDescriptorSetsBuilder.() -> Unit) {
		val device = this
		MemoryStack.stackPush()
		try {
			val builder = UpdateDescriptorSetsBuilder().apply(block)
			vkUpdateDescriptorSets(device.toVkType(),
					builder.targets0.mapToStackArray(VkWriteDescriptorSet::callocStack, ::WriteDescriptorSetBuilder),
					builder.targets1.mapToStackArray(VkCopyDescriptorSet::callocStack, ::CopyDescriptorSetBuilder))
		} finally {
			MemoryStack.stackPop()
		}
	}
}

