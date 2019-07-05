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
package com.kgl.vulkan

import com.kgl.vulkan.enums.*
import com.kgl.vulkan.handles.*
import com.kgl.vulkan.utils.VkVersion
import com.kgl.vulkan.utils.or
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class DslTests {
	private val instance: Instance = TODO("Only to be used for static analysis.")
	private val physicalDevice: PhysicalDevice = TODO("Only to be used for static analysis.")
	private val device: Device = TODO("Only to be used for static analysis.")
	private val surfaceKHR: SurfaceKHR = TODO("Only to be used for static analysis.")
	private val image: Image = TODO("Only to be used for static analysis.")
	private val pipelineLayout: PipelineLayout = TODO("Only to be used for static analysis.")
	private val renderPass: RenderPass = TODO("Only to be used for static analysis.")
	private val shaderModule: ShaderModule = TODO("Only to be used for static analysis.")
	private val commandPool: CommandPool = TODO("Only to be used for static analysis.")
	private val commandBuffer: CommandBuffer = TODO("Only to be used for static analysis.")
	private val framebuffer: Framebuffer = TODO("Only to be used for static analysis.")
	private val queue: Queue = TODO("Only to be used for static analysis.")

	// Should do!
	//vkCmdBeginRenderPass2KHR didn't work out. Command with multiple structs not supported yet.
	//vkCmdNextSubpass2KHR didn't work out. Command with multiple structs not supported yet.

	//vkCmdClearColorImage didn't work out. Index: 0, Size: 0
	//vkCmdClearDepthStencilImage didn't work out. Command with multiple structs not supported yet.
	//vkCmdClearAttachments didn't work out. Command with multiple structs not supported yet.

	// Eventually do!
	//vkGetRandROutputDisplayEXT didn't work out. An operation is not implemented: Display
	//vkCreateViSurfaceNN didn't work out. Empty list doesn't contain element at index 0.
	//vkCreateIOSSurfaceMVK didn't work out. Empty list doesn't contain element at index 0.
	//vkCreateMacOSSurfaceMVK didn't work out. Empty list doesn't contain element at index 0.

	@Test
	fun testCreateInstance() {
		val layers = listOf<String>()
		val extensions = listOf<String>()

		Instance.create(layers, extensions) {
			applicationInfo {
				applicationName = "Test"
				applicationVersion = VkVersion(1U, 1U, 0U)
				engineName = "No engine!"
				engineVersion = VkVersion(1U, 0U, 0U)
				apiVersion = VkVersion(1u, 0u, 0u)
			}
		}
	}

	@Test
	fun testCreateDevice() {
		val layers = listOf<String>()
		val extensions = listOf<String>()

		physicalDevice.createDevice(layers, extensions) {
			queues {
				queue(1U, 1f, 1f) {
					flags = DeviceQueueCreate.PROTECTED
				}

				queue(4U, 1f, 0.5f, 0.6f)
			}

			enabledFeatures {
				samplerAnisotropy = true
				geometryShader = true
				depthClamp = true
			}
		}
	}

	@Test
	fun testCreateOtherStuff() {
		device.createCommandPool(0U) {
			flags = CommandPoolCreate.RESET_COMMAND_BUFFER
		}

		device.createEvent()
		device.createFence { flags = FenceCreate.SIGNALED }
		device.createSemaphore()
		device.createPipelineCache()

		device.createSwapchainKHR(surfaceKHR, uintArrayOf(0U, 5U, 7U), null) {
			imageSharingMode = SharingMode.EXCLUSIVE

			minImageCount = 2U
			imageFormat = Format.R8G8B8A8_UNORM
			imageColorSpace = ColorSpaceKHR.SRGB_NONLINEAR
			imageExtent(1080U, 720U)
			imageArrayLayers = 1u
			imageUsage = ImageUsage.COLOR_ATTACHMENT
			preTransform = null
			compositeAlpha = CompositeAlphaKHR.OPAQUE
			presentMode = PresentModeKHR.MAILBOX
			clipped = true
		}

		device.createShaderModule(ubyteArrayOf())

		device.createPipelineLayout(null) {
			pushConstantRanges {
				range(ShaderStage.VERTEX or ShaderStage.FRAGMENT, 0U, 0U)
				range(ShaderStage.COMPUTE, 0U, 4U)
			}
		}

		device.createRenderPass {
			attachments {
				description {
					format = Format.R8G8B8A8_UNORM
					samples = SampleCount.`1`
					loadOp = AttachmentLoadOp.CLEAR
					storeOp = AttachmentStoreOp.STORE

					stencilLoadOp = AttachmentLoadOp.DONT_CARE
					stencilStoreOp = AttachmentStoreOp.DONT_CARE

					initialLayout = ImageLayout.UNDEFINED
					finalLayout = ImageLayout.PRESENT_SRC_KHR
				}
			}

			subpasses {
				description {
					pipelineBindPoint = PipelineBindPoint.GRAPHICS

					colorAttachments {
						// Inline VkAttachmentReference[*]
						reference {
							attachment = 0U
							layout = ImageLayout.COLOR_ATTACHMENT_OPTIMAL
						}
					}
				}
			}

			dependencies {
				dependency {
					srcSubpass = Vk.SUBPASS_EXTERNAL
					dstSubpass = 0U

					srcStageMask = PipelineStage.COLOR_ATTACHMENT_OUTPUT
					srcAccessMask = null

					dstStageMask = PipelineStage.COLOR_ATTACHMENT_OUTPUT
					dstAccessMask = Access.COLOR_ATTACHMENT_READ or Access.COLOR_ATTACHMENT_WRITE
				}
			}
		}

		device.createGraphicsPipelines {
			pipeline(pipelineLayout, renderPass, null) {
				flags = PipelineCreate.ALLOW_DERIVATIVES
				subpass = 0U
				basePipelineIndex = -1

				stages {
					// TODO: Inline `VkPipelineShaderStageCreateInfo[stage, name]`
					stage(ShaderStage.VERTEX, shaderModule, "main") {
						specializationInfo(null) {
							mapEntries {
								entry(0U, 0U, 0U)
							}
						}
					}

					stage(ShaderStage.FRAGMENT, shaderModule, "main")
				}
				vertexInputState {
					vertexBindingDescriptions {
						// TODO: Inline `VkVertexBindingDescription[*]`
						description {
							binding = 0U
							inputRate = VertexInputRate.VERTEX
							stride = 1U
						}
					}

					vertexAttributeDescriptions {
						description(0U, 0U, Format.R16G16B16A16_SFLOAT, 0U)
					}
				}
				inputAssemblyState {
					topology = PrimitiveTopology.TRIANGLE_LIST
					primitiveRestartEnable = false
				}
				viewportState {
					viewports {
						viewport {
							x = 0f
							y = 0f
							width = 1080f
							height = 720f
							minDepth = 0f
							maxDepth = 1f
						}
					}
					scissors {
						rect2D {
							offset(0, 0)
							extent(1080U, 720U)
						}
					}
				}
				rasterizationState {
					depthClampEnable = false
					rasterizerDiscardEnable = false
					polygonMode = PolygonMode.FILL
					lineWidth = 1.0f
					cullMode = CullMode.BACK
					frontFace = FrontFace.CLOCKWISE

					depthBiasEnable = false
					depthBiasConstantFactor = 0.0f
					depthBiasClamp = 0.0f
					depthBiasSlopeFactor = 0.0f
				}
				multisampleState {
					sampleShadingEnable = false
					rasterizationSamples = SampleCount.`1`
					minSampleShading = 1.0f

					alphaToCoverageEnable = false
					alphaToOneEnable = false
				}
				colorBlendState {
					logicOpEnable = false
					logicOp = LogicOp.NO_OP

					attachments {
						state {
							colorWriteMask = ColorComponent.R or ColorComponent.B or ColorComponent.G or ColorComponent.A
							blendEnable = true
							srcColorBlendFactor = BlendFactor.SRC_ALPHA
							dstColorBlendFactor = BlendFactor.ONE_MINUS_SRC_ALPHA
							colorBlendOp = BlendOp.ADD

							srcAlphaBlendFactor = BlendFactor.ONE
							dstAlphaBlendFactor = BlendFactor.ZERO
							alphaBlendOp = BlendOp.ADD
						}
					}
					blendConstants(0f, 0f, 0f, 0f)
				}
			}
		}

		image.createView(ImageViewType.`2D`, Format.R8G8B8A8_UNORM) {
			components {
				r = ComponentSwizzle.IDENTITY
				g = ComponentSwizzle.IDENTITY
				b = ComponentSwizzle.IDENTITY
				a = ComponentSwizzle.IDENTITY
			}

			subresourceRange {
				aspectMask = ImageAspect.COLOR
				baseMipLevel = 0U
				levelCount = 1U
				baseArrayLayer = 0U
				layerCount = 1U
			}
		}

		device.createFramebuffer(renderPass, null) {
			width = 1080U
			height = 720U
			layers = 1U
		}
	}

	@Test
	fun testCommandBufferStuff() {
		commandPool.allocate(CommandBufferLevel.PRIMARY, 7U)

		commandBuffer.begin {
			flags = CommandBufferUsage.ONE_TIME_SUBMIT

			inheritanceInfo(renderPass, null) {
				subpass = 0U
			}
		}

		commandBuffer.beginRenderPass(renderPass, framebuffer, SubpassContents.INLINE) {
			renderArea {
				offset(0, 0)
				extent(1080U, 720U)
			}

			clearValues {
				value {
					color(1.0f, 0.5f, 1.0f, 1.0f)
				}
				value {
					depthStencil(1.0f, 0U)
				}
			}
		}
	}

	@Test
	fun testQueueStuff() {

		queue.submit(null) {
			// TODO: Bespoke.
			submit(listOf(), listOf(), listOf())
			submit()
		}
	}
}
