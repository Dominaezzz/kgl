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
package com.kgl.vulkan.utils

import kotlin.jvm.*

@JvmInline
value class VkVersion(private val version: UInt) {
	constructor(major: UInt, minor: UInt, patch: UInt) : this(major shl 22 or (minor shl 12) or patch)

	val major: UInt get() = version.shr(22)
	val minor: UInt get() = version.shr(12) and 0x3FFU
	val patch: UInt get() = version and 0xFFFU

	val value: UInt get() = version
}
