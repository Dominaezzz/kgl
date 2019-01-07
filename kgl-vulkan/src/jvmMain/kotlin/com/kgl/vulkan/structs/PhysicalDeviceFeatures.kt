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

import org.lwjgl.vulkan.VkPhysicalDeviceFeatures

fun PhysicalDeviceFeatures.Companion.from(ptr: VkPhysicalDeviceFeatures): PhysicalDeviceFeatures =
		PhysicalDeviceFeatures(
				ptr.robustBufferAccess(),
				ptr.fullDrawIndexUint32(),
				ptr.imageCubeArray(),
				ptr.independentBlend(),
				ptr.geometryShader(),
				ptr.tessellationShader(),
				ptr.sampleRateShading(),
				ptr.dualSrcBlend(),
				ptr.logicOp(),
				ptr.multiDrawIndirect(),
				ptr.drawIndirectFirstInstance(),
				ptr.depthClamp(),
				ptr.depthBiasClamp(),
				ptr.fillModeNonSolid(),
				ptr.depthBounds(),
				ptr.wideLines(),
				ptr.largePoints(),
				ptr.alphaToOne(),
				ptr.multiViewport(),
				ptr.samplerAnisotropy(),
				ptr.textureCompressionETC2(),
				ptr.textureCompressionASTC_LDR(),
				ptr.textureCompressionBC(),
				ptr.occlusionQueryPrecise(),
				ptr.pipelineStatisticsQuery(),
				ptr.vertexPipelineStoresAndAtomics(),
				ptr.fragmentStoresAndAtomics(),
				ptr.shaderTessellationAndGeometryPointSize(),
				ptr.shaderImageGatherExtended(),
				ptr.shaderStorageImageExtendedFormats(),
				ptr.shaderStorageImageMultisample(),
				ptr.shaderStorageImageReadWithoutFormat(),
				ptr.shaderStorageImageWriteWithoutFormat(),
				ptr.shaderUniformBufferArrayDynamicIndexing(),
				ptr.shaderSampledImageArrayDynamicIndexing(),
				ptr.shaderStorageBufferArrayDynamicIndexing(),
				ptr.shaderStorageImageArrayDynamicIndexing(),
				ptr.shaderClipDistance(),
				ptr.shaderCullDistance(),
				ptr.shaderFloat64(),
				ptr.shaderInt64(),
				ptr.shaderInt16(),
				ptr.shaderResourceResidency(),
				ptr.shaderResourceMinLod(),
				ptr.sparseBinding(),
				ptr.sparseResidencyBuffer(),
				ptr.sparseResidencyImage2D(),
				ptr.sparseResidencyImage3D(),
				ptr.sparseResidency2Samples(),
				ptr.sparseResidency4Samples(),
				ptr.sparseResidency8Samples(),
				ptr.sparseResidency16Samples(),
				ptr.sparseResidencyAliased(),
				ptr.variableMultisampleRate(),
				ptr.inheritedQueries()
		)
