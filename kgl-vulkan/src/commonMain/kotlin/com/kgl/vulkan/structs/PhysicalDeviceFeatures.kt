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

data class PhysicalDeviceFeatures(
		val robustBufferAccess: Boolean,
		val fullDrawIndexUint32: Boolean,
		val imageCubeArray: Boolean,
		val independentBlend: Boolean,
		val geometryShader: Boolean,
		val tessellationShader: Boolean,
		val sampleRateShading: Boolean,
		val dualSrcBlend: Boolean,
		val logicOp: Boolean,
		val multiDrawIndirect: Boolean,
		val drawIndirectFirstInstance: Boolean,
		val depthClamp: Boolean,
		val depthBiasClamp: Boolean,
		val fillModeNonSolid: Boolean,
		val depthBounds: Boolean,
		val wideLines: Boolean,
		val largePoints: Boolean,
		val alphaToOne: Boolean,
		val multiViewport: Boolean,
		val samplerAnisotropy: Boolean,
		val textureCompressionETC2: Boolean,
		val textureCompressionASTC_LDR: Boolean,
		val textureCompressionBC: Boolean,
		val occlusionQueryPrecise: Boolean,
		val pipelineStatisticsQuery: Boolean,
		val vertexPipelineStoresAndAtomics: Boolean,
		val fragmentStoresAndAtomics: Boolean,
		val shaderTessellationAndGeometryPointSize: Boolean,
		val shaderImageGatherExtended: Boolean,
		val shaderStorageImageExtendedFormats: Boolean,
		val shaderStorageImageMultisample: Boolean,
		val shaderStorageImageReadWithoutFormat: Boolean,
		val shaderStorageImageWriteWithoutFormat: Boolean,
		val shaderUniformBufferArrayDynamicIndexing: Boolean,
		val shaderSampledImageArrayDynamicIndexing: Boolean,
		val shaderStorageBufferArrayDynamicIndexing: Boolean,
		val shaderStorageImageArrayDynamicIndexing: Boolean,
		val shaderClipDistance: Boolean,
		val shaderCullDistance: Boolean,
		val shaderFloat64: Boolean,
		val shaderInt64: Boolean,
		val shaderInt16: Boolean,
		val shaderResourceResidency: Boolean,
		val shaderResourceMinLod: Boolean,
		val sparseBinding: Boolean,
		val sparseResidencyBuffer: Boolean,
		val sparseResidencyImage2D: Boolean,
		val sparseResidencyImage3D: Boolean,
		val sparseResidency2Samples: Boolean,
		val sparseResidency4Samples: Boolean,
		val sparseResidency8Samples: Boolean,
		val sparseResidency16Samples: Boolean,
		val sparseResidencyAliased: Boolean,
		val variableMultisampleRate: Boolean,
		val inheritedQueries: Boolean
) {
	companion object
}

