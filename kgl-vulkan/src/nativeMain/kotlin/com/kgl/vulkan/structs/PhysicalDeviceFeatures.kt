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

import com.kgl.vulkan.utils.toBoolean
import cvulkan.VkPhysicalDeviceFeatures

fun PhysicalDeviceFeatures.Companion.from(ptr: VkPhysicalDeviceFeatures): PhysicalDeviceFeatures =
		PhysicalDeviceFeatures(
				ptr.robustBufferAccess.toBoolean(),
				ptr.fullDrawIndexUint32.toBoolean(),
				ptr.imageCubeArray.toBoolean(),
				ptr.independentBlend.toBoolean(),
				ptr.geometryShader.toBoolean(),
				ptr.tessellationShader.toBoolean(),
				ptr.sampleRateShading.toBoolean(),
				ptr.dualSrcBlend.toBoolean(),
				ptr.logicOp.toBoolean(),
				ptr.multiDrawIndirect.toBoolean(),
				ptr.drawIndirectFirstInstance.toBoolean(),
				ptr.depthClamp.toBoolean(),
				ptr.depthBiasClamp.toBoolean(),
				ptr.fillModeNonSolid.toBoolean(),
				ptr.depthBounds.toBoolean(),
				ptr.wideLines.toBoolean(),
				ptr.largePoints.toBoolean(),
				ptr.alphaToOne.toBoolean(),
				ptr.multiViewport.toBoolean(),
				ptr.samplerAnisotropy.toBoolean(),
				ptr.textureCompressionETC2.toBoolean(),
				ptr.textureCompressionASTC_LDR.toBoolean(),
				ptr.textureCompressionBC.toBoolean(),
				ptr.occlusionQueryPrecise.toBoolean(),
				ptr.pipelineStatisticsQuery.toBoolean(),
				ptr.vertexPipelineStoresAndAtomics.toBoolean(),
				ptr.fragmentStoresAndAtomics.toBoolean(),
				ptr.shaderTessellationAndGeometryPointSize.toBoolean(),
				ptr.shaderImageGatherExtended.toBoolean(),
				ptr.shaderStorageImageExtendedFormats.toBoolean(),
				ptr.shaderStorageImageMultisample.toBoolean(),
				ptr.shaderStorageImageReadWithoutFormat.toBoolean(),
				ptr.shaderStorageImageWriteWithoutFormat.toBoolean(),
				ptr.shaderUniformBufferArrayDynamicIndexing.toBoolean(),
				ptr.shaderSampledImageArrayDynamicIndexing.toBoolean(),
				ptr.shaderStorageBufferArrayDynamicIndexing.toBoolean(),
				ptr.shaderStorageImageArrayDynamicIndexing.toBoolean(),
				ptr.shaderClipDistance.toBoolean(),
				ptr.shaderCullDistance.toBoolean(),
				ptr.shaderFloat64.toBoolean(),
				ptr.shaderInt64.toBoolean(),
				ptr.shaderInt16.toBoolean(),
				ptr.shaderResourceResidency.toBoolean(),
				ptr.shaderResourceMinLod.toBoolean(),
				ptr.sparseBinding.toBoolean(),
				ptr.sparseResidencyBuffer.toBoolean(),
				ptr.sparseResidencyImage2D.toBoolean(),
				ptr.sparseResidencyImage3D.toBoolean(),
				ptr.sparseResidency2Samples.toBoolean(),
				ptr.sparseResidency4Samples.toBoolean(),
				ptr.sparseResidency8Samples.toBoolean(),
				ptr.sparseResidency16Samples.toBoolean(),
				ptr.sparseResidencyAliased.toBoolean(),
				ptr.variableMultisampleRate.toBoolean(),
				ptr.inheritedQueries.toBoolean()
		)
