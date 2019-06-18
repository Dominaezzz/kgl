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
package codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName


internal val VIRTUAL_STACK = ClassName("com.kgl.core", "VirtualStack")
internal val C_OPAQUE_POINTER = ClassName("kotlinx.cinterop", "COpaquePointer")

internal val THREAD_LOCAL = ClassName("kotlin.native.concurrent", "ThreadLocal")

internal val IO_BUFFER = ClassName("kotlinx.io.core", "IoBuffer")

internal val UBYTE = ClassName("kotlin", "UByte")
internal val USHORT = ClassName("kotlin", "UShort")
internal val UINT = ClassName("kotlin", "UInt")
internal val ULONG = ClassName("kotlin", "ULong")
internal val NOTHING = ClassName("kotlin", "Nothing")
internal val STRING = ClassName("kotlin", "String")
internal val ARRAY = ClassName("kotlin", "Array")
internal val COLLECTION = ClassName("kotlin.collections", "Collection")
internal val LIST = ClassName("kotlin.collections", "List")
internal val MUTABLE_LIST = ClassName("kotlin.collections", "MutableList")

internal val MEMORY_STACK = ClassName("org.lwjgl.system", "MemoryStack")

object KtxC {
	val REINTERPRET = MemberName("kotlinx.cinterop", "reinterpret")
	val POINTED = MemberName("kotlinx.cinterop", "pointed")
}
