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
package com.kgl.opengl

import com.kgl.core.*
import kotlinx.cinterop.*

fun glGetProgramInfoLog(program: UInt): String {
	val infoLogLength = glGetProgrami(program, GL_INFO_LOG_LENGTH)
	val infoLog = ByteArray(infoLogLength + 1) // +1 for null terminator
	infoLog.usePinned {
		glGetProgramInfoLog(program, infoLogLength + 1, null, it.addressOf(0))
	}
	return infoLog.decodeToString()
}

fun glGetShaderInfoLog(shader: UInt): String {
	val infoLogLength = glGetShaderi(shader, GL_INFO_LOG_LENGTH)
	val infoLog = ByteArray(infoLogLength + 1)
	infoLog.usePinned {
		glGetShaderInfoLog(shader, infoLogLength + 1, null, it.addressOf(0))
	}
	return infoLog.decodeToString()
}

fun glShaderSource(shader: UInt, string: String) {
	VirtualStack.push()
	try {
		val input = VirtualStack.alloc<CPointerVar<ByteVar>> {
			value = string.cstr.getPointer(VirtualStack.currentFrame!!)
		}
		val inputLength = VirtualStack.alloc<IntVar> {
			value = string.length
		}

		glShaderSource(shader, 1, input.ptr, inputLength.ptr)
	} finally {
		VirtualStack.pop()
	}
}

fun glShaderSource(shader: UInt, strings: List<String>) {
	VirtualStack.push()
	try {
		val input = VirtualStack.allocArray<CPointerVar<ByteVar>>(strings.size) {
			value = strings[it].cstr.getPointer(VirtualStack.currentFrame!!)
		}
		val inputLength = VirtualStack.allocArray<IntVar>(strings.size) {
			value = strings[it].length
		}

		glShaderSource(shader, strings.size, input, inputLength)
	} finally {
		VirtualStack.pop()
	}
}
