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

import com.kgl.core.VirtualStack
import com.kgl.vulkan.dsls.*
import com.kgl.vulkan.enums.*
import com.kgl.vulkan.structs.*
import com.kgl.vulkan.utils.*
import cvulkan.*
import kotlinx.cinterop.*
import io.ktor.utils.io.bits.Memory

actual class Device(override val ptr: VkDevice, actual val physicalDevice: PhysicalDevice) : VkHandleNative<VkDevice>(), VkHandle {
	internal val dispatchTable = DeviceDispatchTable {
		VirtualStack.push()
		try {
			physicalDevice.dispatchTable.vkGetDeviceProcAddr(ptr, it.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual val groupPresentCapabilitiesKHR: DeviceGroupPresentCapabilitiesKHR
		get() {
			val device = this
			VirtualStack.push()
			try {
				val outputVar = VirtualStack.alloc<VkDeviceGroupPresentCapabilitiesKHR>()
				val outputPtr = outputVar.ptr
				val result = dispatchTable.vkGetDeviceGroupPresentCapabilitiesKHR!!(device.toVkType(), outputPtr)
				if (result != VK_SUCCESS) handleVkResult(result)
				return DeviceGroupPresentCapabilitiesKHR.from(outputVar)
			} finally {
				VirtualStack.pop()
			}
		}

	override fun close() {
		val device = this
		VirtualStack.push()
		try {
			dispatchTable.vkDestroyDevice(device.toVkType(), null)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getProcAddr(name: String) {
		val device = this
		VirtualStack.push()
		try {
			physicalDevice.dispatchTable.vkGetDeviceProcAddr(device.toVkType(), name.toVkType())
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getQueue(queueFamilyIndex: UInt, queueIndex: UInt): Queue {
		val device = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkQueueVar>()
			val outputPtr = outputVar.ptr
			dispatchTable.vkGetDeviceQueue(device.toVkType(), queueFamilyIndex.toVkType(), queueIndex.toVkType(), outputPtr)
			return Queue(outputVar.value!!, this, queueFamilyIndex)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun waitIdle() {
		val device = this
		VirtualStack.push()
		try {
			val result = dispatchTable.vkDeviceWaitIdle(device.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun allocateMemory(block: MemoryAllocateInfoBuilder.() -> Unit): DeviceMemory {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkMemoryAllocateInfo>().ptr
			val builder = MemoryAllocateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDeviceMemoryVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkAllocateMemory(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DeviceMemory(outputVar.value!!, this, builder.allocationSize, builder.memoryTypeIndex)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun flushMappedMemoryRanges(block: MappedMemoryRangesBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val targets = MappedMemoryRangesBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::MappedMemoryRangeBuilder)
			val result = dispatchTable.vkFlushMappedMemoryRanges(device.toVkType(), targets.size.toUInt(),
					targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun invalidateMappedMemoryRanges(block: MappedMemoryRangesBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val targets = MappedMemoryRangesBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::MappedMemoryRangeBuilder)
			val result = dispatchTable.vkInvalidateMappedMemoryRanges(device.toVkType(), targets.size.toUInt(),
					targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createFence(block: FenceCreateInfoBuilder.() -> Unit): Fence {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkFenceCreateInfo>().ptr
			val builder = FenceCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkFenceVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateFence(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Fence(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun resetFences(fences: Collection<Fence>) {
		val device = this
		VirtualStack.push()
		try {
			val result = dispatchTable.vkResetFences(device.toVkType(), fences.size.toUInt(), fences.toVkType())
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun waitForFences(fences: Collection<Fence>, waitAll: Boolean, timeout: ULong): Boolean {
		val device = this
		VirtualStack.push()
		try {
			val result = dispatchTable.vkWaitForFences(device.toVkType(), fences.size.toUInt(),
					fences.toVkType(), waitAll.toVkType(), timeout.toVkType())
			return when (result) {
				VK_SUCCESS -> true
				VK_TIMEOUT -> false
				else -> handleVkResult(result)
			}
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createSemaphore(block: SemaphoreCreateInfoBuilder.() -> Unit): Semaphore {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkSemaphoreCreateInfo>().ptr
			val builder = SemaphoreCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkSemaphoreVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateSemaphore(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Semaphore(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createEvent(block: EventCreateInfoBuilder.() -> Unit): Event {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkEventCreateInfo>().ptr
			val builder = EventCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkEventVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateEvent(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Event(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createQueryPool(block: QueryPoolCreateInfoBuilder.() -> Unit): QueryPool {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkQueryPoolCreateInfo>().ptr
			val builder = QueryPoolCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkQueryPoolVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateQueryPool(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return QueryPool(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createBuffer(queueFamilyIndices: UIntArray?, block: BufferCreateInfoBuilder.() -> Unit): Buffer {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkBufferCreateInfo>().ptr
			val builder = BufferCreateInfoBuilder(target.pointed)
			builder.init(queueFamilyIndices)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkBufferVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateBuffer(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Buffer(outputVar.value!!, this, builder.size)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createImage(queueFamilyIndices: UIntArray?, block: ImageCreateInfoBuilder.() -> Unit): Image {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkImageCreateInfo>().ptr
			val builder = ImageCreateInfoBuilder(target.pointed)
			builder.init(queueFamilyIndices)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkImageVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateImage(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Image(outputVar.value!!, this,
					builder.imageType!!, builder.format!!, builder.mipLevels,
					Extent3D.from(target.pointed.extent), builder.arrayLayers)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createShaderModule(code: UByteArray, block: ShaderModuleCreateInfoBuilder.() -> Unit): ShaderModule {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkShaderModuleCreateInfo>().ptr
			val builder = ShaderModuleCreateInfoBuilder(target.pointed)
			builder.init(code)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkShaderModuleVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateShaderModule(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ShaderModule(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createPipelineCache(initialData: Memory?, block: PipelineCacheCreateInfoBuilder.() -> Unit): PipelineCache {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPipelineCacheCreateInfo>().ptr
			val builder = PipelineCacheCreateInfoBuilder(target.pointed)
			builder.init(initialData)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkPipelineCacheVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreatePipelineCache(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return PipelineCache(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createPipelineLayout(setLayouts: Collection<DescriptorSetLayout>?, block: PipelineLayoutCreateInfoBuilder.() -> Unit): PipelineLayout {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkPipelineLayoutCreateInfo>().ptr
			val builder = PipelineLayoutCreateInfoBuilder(target.pointed)
			builder.init(setLayouts)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkPipelineLayoutVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreatePipelineLayout(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return PipelineLayout(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createGraphicsPipelines(pipelineCache: PipelineCache?, block: GraphicsPipelineCreateInfosBuilder.() -> Unit): List<Pipeline> {
		val device = this
		VirtualStack.push()
		try {
			val targets = GraphicsPipelineCreateInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::GraphicsPipelineCreateInfoBuilder)
			val outputCount = targets.size
			val outputPtr = VirtualStack.allocArray<VkPipelineVar>(outputCount)
			val result = dispatchTable.vkCreateGraphicsPipelines(device.toVkType(), pipelineCache.toVkType(),
					targets.size.toUInt(), targetArray, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { Pipeline(outputPtr[it]!!, this) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createComputePipelines(pipelineCache: PipelineCache?, block: ComputePipelineCreateInfosBuilder.() -> Unit): List<Pipeline> {
		val device = this
		VirtualStack.push()
		try {
			val targets = ComputePipelineCreateInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::ComputePipelineCreateInfoBuilder)
			val outputCount = targets.size
			val outputPtr = VirtualStack.allocArray<VkPipelineVar>(outputCount)
			val result = dispatchTable.vkCreateComputePipelines(device.toVkType(), pipelineCache.toVkType(),
					targets.size.toUInt(), targetArray, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { Pipeline(outputPtr[it]!!, this) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createRayTracingPipelinesNV(pipelineCache: PipelineCache?, block: RayTracingPipelineCreateInfoNVsBuilder.() -> Unit): List<Pipeline> {
		val device = this
		VirtualStack.push()
		try {
			val targets = RayTracingPipelineCreateInfoNVsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::RayTracingPipelineCreateInfoNVBuilder)
			val outputCount = targets.size
			val outputPtr = VirtualStack.allocArray<VkPipelineVar>(outputCount)
			val result = dispatchTable.vkCreateRayTracingPipelinesNV!!(device.toVkType(), pipelineCache.toVkType(),
					targets.size.toUInt(), targetArray, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { Pipeline(outputPtr[it]!!, this) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createSampler(block: SamplerCreateInfoBuilder.() -> Unit): Sampler {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkSamplerCreateInfo>().ptr
			val builder = SamplerCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkSamplerVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateSampler(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Sampler(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDescriptorSetLayout(block: DescriptorSetLayoutCreateInfoBuilder.() -> Unit): DescriptorSetLayout {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDescriptorSetLayoutCreateInfo>().ptr
			val builder = DescriptorSetLayoutCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDescriptorSetLayoutVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateDescriptorSetLayout(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DescriptorSetLayout(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createDescriptorPool(maxSets: UInt, block: DescriptorPoolCreateInfoBuilder.() -> Unit): DescriptorPool {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDescriptorPoolCreateInfo>().ptr
			val builder = DescriptorPoolCreateInfoBuilder(target.pointed)
			builder.init(maxSets)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDescriptorPoolVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateDescriptorPool(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DescriptorPool(outputVar.value!!, this, maxSets)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createRenderPass(block: RenderPassCreateInfoBuilder.() -> Unit): RenderPass {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkRenderPassCreateInfo>().ptr
			val builder = RenderPassCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkRenderPassVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateRenderPass(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return RenderPass(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createCommandPool(queueFamilyIndex: UInt, block: CommandPoolCreateInfoBuilder.() -> Unit): CommandPool {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkCommandPoolCreateInfo>().ptr
			val builder = CommandPoolCreateInfoBuilder(target.pointed)
			builder.init(queueFamilyIndex)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkCommandPoolVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateCommandPool(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return CommandPool(outputVar.value!!, this, queueFamilyIndex)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createSharedSwapchainsKHR(block: CreateSharedSwapchainsKHRBuilder.() -> Unit): List<SwapchainKHR> {
		val device = this
		VirtualStack.push()
		try {
			val builder = CreateSharedSwapchainsKHRBuilder().apply(block)
			val targets = builder.targets
			val targetArray = targets.mapToStackArray(::SwapchainCreateInfoKHRBuilder)
			val outputCount = targets.size
			val outputPtr = VirtualStack.allocArray<VkSwapchainKHRVar>(outputCount)
			val result = dispatchTable.vkCreateSharedSwapchainsKHR!!(device.toVkType(), targets.size.toUInt(),
					targetArray, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return List(outputCount) { SwapchainKHR(
					outputPtr[it]!!,
					builder.surfaces[it],
					device,
					Format.from(targetArray[it].imageFormat),
					Extent2D.from(targetArray[it].imageExtent),
					targetArray[it].imageArrayLayers
			) }
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createSwapchainKHR(
			surface: SurfaceKHR,
			queueFamilyIndices: UIntArray?,
			oldSwapchain: SwapchainKHR?,
			block: SwapchainCreateInfoKHRBuilder.() -> Unit
	): SwapchainKHR {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkSwapchainCreateInfoKHR>().ptr
			val builder = SwapchainCreateInfoKHRBuilder(target.pointed)
			builder.init(surface, queueFamilyIndices, oldSwapchain)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkSwapchainKHRVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateSwapchainKHR!!(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SwapchainKHR(outputVar.value!!, surface, this,
					builder.imageFormat!!, Extent2D.from(target.pointed.imageExtent), builder.imageArrayLayers)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createFramebuffer(renderPass: RenderPass, attachments: Collection<ImageView>?, block: FramebufferCreateInfoBuilder.() -> Unit): Framebuffer {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkFramebufferCreateInfo>().ptr
			val builder = FramebufferCreateInfoBuilder(target.pointed)
			builder.init(renderPass, attachments)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkFramebufferVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateFramebuffer(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Framebuffer(outputVar.value!!, this,
					renderPass, attachments?.toTypedArray(),
					builder.width, builder.height, builder.layers)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun debugMarkerSetObjectNameEXT(block: DebugMarkerObjectNameInfoEXTBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugMarkerObjectNameInfoEXT>().ptr
			val builder = DebugMarkerObjectNameInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val result = dispatchTable.vkDebugMarkerSetObjectNameEXT!!(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun debugMarkerSetObjectTagEXT(tag: Memory, block: DebugMarkerObjectTagInfoEXTBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugMarkerObjectTagInfoEXT>().ptr
			val builder = DebugMarkerObjectTagInfoEXTBuilder(target.pointed)
			builder.init(tag)
			builder.apply(block)
			val result = dispatchTable.vkDebugMarkerSetObjectTagEXT!!(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createIndirectCommandsLayoutNVX(block: IndirectCommandsLayoutCreateInfoNVXBuilder.() -> Unit): IndirectCommandsLayoutNVX {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkIndirectCommandsLayoutCreateInfoNVX>().ptr
			val builder = IndirectCommandsLayoutCreateInfoNVXBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkIndirectCommandsLayoutNVXVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateIndirectCommandsLayoutNVX!!(device.toVkType(), target, null,
					outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return IndirectCommandsLayoutNVX(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createObjectTableNVX(
			objectEntryTypes: Collection<ObjectEntryTypeNVX>,
			objectEntryCounts: UIntArray,
			objectEntryUsageFlags: Collection<VkFlag<ObjectEntryUsageNVX>>,
			block: ObjectTableCreateInfoNVXBuilder.() -> Unit
	): ObjectTableNVX {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkObjectTableCreateInfoNVX>().ptr
			val builder = ObjectTableCreateInfoNVXBuilder(target.pointed)
			builder.init(objectEntryTypes, objectEntryCounts, objectEntryUsageFlags)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkObjectTableNVXVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateObjectTableNVX!!(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ObjectTableNVX(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getMemoryFdPropertiesKHR(handleType: ExternalMemoryHandleType, fd: Int): MemoryFdPropertiesKHR {
		val device = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkMemoryFdPropertiesKHR>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetMemoryFdPropertiesKHR!!(device.toVkType(), handleType.toVkType(),
					fd.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return MemoryFdPropertiesKHR.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun displayPowerControlEXT(display: DisplayKHR, block: DisplayPowerInfoEXTBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDisplayPowerInfoEXT>().ptr
			val builder = DisplayPowerInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val result = dispatchTable.vkDisplayPowerControlEXT!!(device.toVkType(), display.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun registerEventEXT(block: DeviceEventInfoEXTBuilder.() -> Unit): Fence {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDeviceEventInfoEXT>().ptr
			val builder = DeviceEventInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkFenceVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkRegisterDeviceEventEXT!!(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Fence(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun registerDisplayEventEXT(display: DisplayKHR, block: DisplayEventInfoEXTBuilder.() -> Unit): Fence {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDisplayEventInfoEXT>().ptr
			val builder = DisplayEventInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkFenceVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkRegisterDisplayEventEXT!!(device.toVkType(), display.toVkType(), target,
					null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return Fence(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getGroupPeerMemoryFeatures(
			heapIndex: UInt,
			localDeviceIndex: UInt,
			remoteDeviceIndex: UInt
	): VkFlag<PeerMemoryFeature> {
		val device = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<UIntVar>()
			val outputPtr = outputVar.ptr
			dispatchTable.vkGetDeviceGroupPeerMemoryFeatures!!(device.toVkType(), heapIndex.toVkType(),
					localDeviceIndex.toVkType(), remoteDeviceIndex.toVkType(), outputPtr)
			return PeerMemoryFeature.fromMultiple(outputVar.value)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindBufferMemory2(block: BindBufferMemoryInfosBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val targets = BindBufferMemoryInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::BindBufferMemoryInfoBuilder)
			val result = dispatchTable.vkBindBufferMemory2!!(device.toVkType(), targets.size.toUInt(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindImageMemory2(block: BindImageMemoryInfosBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val targets = BindImageMemoryInfosBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::BindImageMemoryInfoBuilder)
			val result = dispatchTable.vkBindImageMemory2!!(device.toVkType(), targets.size.toUInt(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getGroupSurfacePresentModesKHR(surface: SurfaceKHR): VkFlag<DeviceGroupPresentModeKHR> {
		val device = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<UIntVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetDeviceGroupSurfacePresentModesKHR!!(device.toVkType(),
					surface.toVkType(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return DeviceGroupPresentModeKHR.fromMultiple(outputVar.value)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setHdrMetadataEXT(swapchains: Collection<SwapchainKHR>, block: HdrMetadataEXTsBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val targets = HdrMetadataEXTsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::HdrMetadataEXTBuilder)
			dispatchTable.vkSetHdrMetadataEXT!!(device.toVkType(), targets.size.toUInt(), swapchains.toVkType(),
					targetArray)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createSamplerYcbcrConversion(block: SamplerYcbcrConversionCreateInfoBuilder.() -> Unit): SamplerYcbcrConversion {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkSamplerYcbcrConversionCreateInfo>().ptr
			val builder = SamplerYcbcrConversionCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkSamplerYcbcrConversionVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateSamplerYcbcrConversion!!(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return SamplerYcbcrConversion(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getQueue2(block: DeviceQueueInfo2Builder.() -> Unit): Queue {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDeviceQueueInfo2>().ptr
			val builder = DeviceQueueInfo2Builder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkQueueVar>()
			val outputPtr = outputVar.ptr
			dispatchTable.vkGetDeviceQueue2!!(device.toVkType(), target, outputPtr)
			return Queue(outputVar.value!!, this, builder.queueFamilyIndex)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createValidationCacheEXT(initialData: Memory?, block: ValidationCacheCreateInfoEXTBuilder.() -> Unit): ValidationCacheEXT {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkValidationCacheCreateInfoEXT>().ptr
			val builder = ValidationCacheCreateInfoEXTBuilder(target.pointed)
			builder.init(initialData)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkValidationCacheEXTVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateValidationCacheEXT!!(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return ValidationCacheEXT(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getDescriptorSetLayoutSupport(block: DescriptorSetLayoutCreateInfoBuilder.() -> Unit): DescriptorSetLayoutSupport {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDescriptorSetLayoutCreateInfo>().ptr
			val builder = DescriptorSetLayoutCreateInfoBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkDescriptorSetLayoutSupport>()
			val outputPtr = outputVar.ptr
			dispatchTable.vkGetDescriptorSetLayoutSupport!!(device.toVkType(), target, outputPtr)
			return DescriptorSetLayoutSupport.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setDebugUtilsObjectNameEXT(block: DebugUtilsObjectNameInfoEXTBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsObjectNameInfoEXT>().ptr
			val builder = DebugUtilsObjectNameInfoEXTBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val result = dispatchTable.vkSetDebugUtilsObjectNameEXT!!(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun setDebugUtilsObjectTagEXT(tag: Memory, block: DebugUtilsObjectTagInfoEXTBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkDebugUtilsObjectTagInfoEXT>().ptr
			val builder = DebugUtilsObjectTagInfoEXTBuilder(target.pointed)
			builder.init(tag)
			builder.apply(block)
			val result = dispatchTable.vkSetDebugUtilsObjectTagEXT!!(device.toVkType(), target)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun getMemoryHostPointerPropertiesEXT(handleType: ExternalMemoryHandleType, pHostPointer: Long): MemoryHostPointerPropertiesEXT {
		val device = this
		VirtualStack.push()
		try {
			val outputVar = VirtualStack.alloc<VkMemoryHostPointerPropertiesEXT>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkGetMemoryHostPointerPropertiesEXT!!(device.toVkType(),
					handleType.toVkType(), pHostPointer.toCPointer(), outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return MemoryHostPointerPropertiesEXT.from(outputVar)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createRenderPass2KHR(correlatedViewMasks: UIntArray, block: RenderPassCreateInfo2KHRBuilder.() -> Unit): RenderPass {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkRenderPassCreateInfo2KHR>().ptr
			val builder = RenderPassCreateInfo2KHRBuilder(target.pointed)
			builder.init(correlatedViewMasks)
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkRenderPassVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateRenderPass2KHR!!(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return RenderPass(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun createAccelerationStructureNV(block: AccelerationStructureCreateInfoNVBuilder.() -> Unit): AccelerationStructureNV {
		val device = this
		VirtualStack.push()
		try {
			val target = VirtualStack.alloc<VkAccelerationStructureCreateInfoNV>().ptr
			val builder = AccelerationStructureCreateInfoNVBuilder(target.pointed)
			builder.init()
			builder.apply(block)
			val outputVar = VirtualStack.alloc<VkAccelerationStructureNVVar>()
			val outputPtr = outputVar.ptr
			val result = dispatchTable.vkCreateAccelerationStructureNV!!(device.toVkType(), target, null, outputPtr)
			if (result != VK_SUCCESS) handleVkResult(result)
			return AccelerationStructureNV(outputVar.value!!, this)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun bindAccelerationStructureMemoryNV(block: BindAccelerationStructureMemoryInfoNVsBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val targets = BindAccelerationStructureMemoryInfoNVsBuilder().apply(block).targets
			val targetArray = targets.mapToStackArray(::BindAccelerationStructureMemoryInfoNVBuilder)
			val result = dispatchTable.vkBindAccelerationStructureMemoryNV!!(device.toVkType(),
					targets.size.toUInt(), targetArray)
			if (result != VK_SUCCESS) handleVkResult(result)
		} finally {
			VirtualStack.pop()
		}
	}

	actual fun updateDescriptorSets(block: UpdateDescriptorSetsBuilder.() -> Unit) {
		val device = this
		VirtualStack.push()
		try {
			val builder = UpdateDescriptorSetsBuilder().apply(block)
			dispatchTable.vkUpdateDescriptorSets(device.toVkType(),
					builder.targets0.size.toUInt(),
					builder.targets0.mapToStackArray(::WriteDescriptorSetBuilder),
					builder.targets1.size.toUInt(),
					builder.targets1.mapToStackArray(::CopyDescriptorSetBuilder))
		} finally {
			VirtualStack.pop()
		}
	}
}

