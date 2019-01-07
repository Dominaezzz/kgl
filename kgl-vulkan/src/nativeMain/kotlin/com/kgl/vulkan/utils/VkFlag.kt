@file:Suppress("NOTHING_TO_INLINE")

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

import com.kgl.core.and
import com.kgl.core.contains
import com.kgl.core.or

actual typealias VkFlag<T> = com.kgl.core.UFlag<T>

actual inline operator fun <T> VkFlag<T>?.contains(other: VkFlag<T>?): Boolean where T : Enum<T>, T : VkFlag<T> {
	return contains(other)
}

actual inline infix fun <T> VkFlag<T>?.and(other: VkFlag<T>?): VkFlag<T>? where T : Enum<T>, T : VkFlag<T> {
	return this and other
}

actual inline infix fun <T> VkFlag<T>.or(other: VkFlag<T>): VkFlag<T> where T : Enum<T>, T : VkFlag<T> {
	return this or other
}
