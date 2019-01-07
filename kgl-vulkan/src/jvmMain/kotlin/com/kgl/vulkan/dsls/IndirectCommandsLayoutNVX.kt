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
package com.kgl.vulkan.dsls

import com.kgl.vulkan.enums.IndirectCommandsLayoutUsageNVX
import com.kgl.vulkan.enums.IndirectCommandsTokenTypeNVX
import com.kgl.vulkan.enums.PipelineBindPoint
import com.kgl.vulkan.utils.VkFlag
import com.kgl.vulkan.utils.mapToStackArray
import com.kgl.vulkan.utils.toVkType
import org.lwjgl.vulkan.NVXDeviceGeneratedCommands
import org.lwjgl.vulkan.VkIndirectCommandsLayoutCreateInfoNVX
import org.lwjgl.vulkan.VkIndirectCommandsLayoutTokenNVX

actual class IndirectCommandsLayoutTokenNVXBuilder(internal val target: VkIndirectCommandsLayoutTokenNVX) {
	actual var tokenType: IndirectCommandsTokenTypeNVX?
		get() = IndirectCommandsTokenTypeNVX.from(target.tokenType())
		set(value) {
			target.tokenType(value.toVkType())
		}

	actual var bindingUnit: UInt
		get() = target.bindingUnit().toUInt()
		set(value) {
			target.bindingUnit(value.toVkType())
		}

	actual var dynamicCount: UInt
		get() = target.dynamicCount().toUInt()
		set(value) {
			target.dynamicCount(value.toVkType())
		}

	actual var divisor: UInt
		get() = target.divisor().toUInt()
		set(value) {
			target.divisor(value.toVkType())
		}

	internal fun init() {
	}
}

actual class IndirectCommandsLayoutTokenNVXsBuilder {
	val targets: MutableList<(VkIndirectCommandsLayoutTokenNVX) -> Unit> = mutableListOf()

	actual fun token(block: IndirectCommandsLayoutTokenNVXBuilder.() -> Unit) {
		targets += {
			val builder = IndirectCommandsLayoutTokenNVXBuilder(it)
			builder.init()
			builder.apply(block)
		}
	}
}

actual class IndirectCommandsLayoutCreateInfoNVXBuilder(internal val target: VkIndirectCommandsLayoutCreateInfoNVX) {
	actual var pipelineBindPoint: PipelineBindPoint?
		get() = PipelineBindPoint.from(target.pipelineBindPoint())
		set(value) {
			target.pipelineBindPoint(value.toVkType())
		}

	actual var flags: VkFlag<IndirectCommandsLayoutUsageNVX>?
		get() = IndirectCommandsLayoutUsageNVX.fromMultiple(target.flags())
		set(value) {
			target.flags(value.toVkType())
		}

	actual fun tokens(block: IndirectCommandsLayoutTokenNVXsBuilder.() -> Unit) {
		val targets = IndirectCommandsLayoutTokenNVXsBuilder().apply(block).targets
		target.pTokens(targets.mapToStackArray(VkIndirectCommandsLayoutTokenNVX::callocStack))
	}

	internal fun init() {
		target.sType(NVXDeviceGeneratedCommands.VK_STRUCTURE_TYPE_INDIRECT_COMMANDS_LAYOUT_CREATE_INFO_NVX)
		target.pNext(0)
	}
}

