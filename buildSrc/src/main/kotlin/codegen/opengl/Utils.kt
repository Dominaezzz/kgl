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
package codegen.opengl

import codegen.*
import codegen.C_OPAQUE_POINTER
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy


internal fun String.escapeKt(): String = when (this) {
	"val" -> "`val`"
	else -> this
}

internal val Registry.Command.Param.isWritable: Boolean get() = type.asteriskCount > 0 && !type.isConst

internal fun CTypeDecl.toKtInteropParamType(): TypeName {
	return if (name == "void" && asteriskCount == 1) {
		if (isConst) {
			ClassName("kotlinx.cinterop", "CValuesRef")
					.parameterizedBy(STAR)
		} else {
			C_OPAQUE_POINTER
		}.copy(nullable = true)
	} else if (asteriskCount > 0) {
		ClassName("kotlinx.cinterop", if (isConst) "CValuesRef" else "CPointer")
				.parameterizedBy(glTypeToKtInteropType(name, asteriskCount - 1))
				.copy(nullable = true)
	} else {
		if (name == "GLsync") {
			ClassName("copengl", name).copy(nullable = true)
		} else {
			ClassName("copengl", name)
		}
	}
}

internal fun CTypeDecl.toKtInteropType(): TypeName {
	return if (name == "void" && asteriskCount == 1) {
		C_OPAQUE_POINTER.copy(nullable = true)
	} else if (asteriskCount > 0) {
		ClassName("kotlinx.cinterop", "CPointer")
				.parameterizedBy(glTypeToKtInteropType(name, asteriskCount - 1))
				.copy(nullable = true)
	} else {
		if (name == "GLsync") {
			ClassName("copengl", name).copy(nullable = true)
		} else {
			ClassName("copengl", name)
		}
	}
}

internal fun glTypeToKtInteropType(name: String, asteriskCount: Int): TypeName {
	return if (name == "void" && asteriskCount == 1) {
		ClassName("kotlinx.cinterop", "COpaquePointerVar")
	} else if (asteriskCount > 0) {
		ClassName("kotlinx.cinterop", "CPointerVar")
				.parameterizedBy(glTypeToKtInteropType(name, asteriskCount - 1))
	} else {
		ClassName("copengl", name + "Var")
	}
}


internal val primitiveTypes = mapOf(
		"GLchar" to BYTE,

		"GLboolean" to BOOLEAN,
		"GLbyte" to BYTE,
		"GLubyte" to U_BYTE,
		"GLshort" to SHORT,
		"GLushort" to U_SHORT,
		"GLint" to INT,
		"GLuint" to U_INT,
		"GLfixed" to INT,
		"GLint64" to LONG,
		"GLuint64" to U_LONG,

		// "GLsizei" to UINT,
		"GLsizei" to INT,

		//"GLenum" to INT,
		"GLenum" to U_INT,

		/* These should be able to store sizeof(void*) */
		"GLintptr" to LONG ,
		"GLsizeiptr" to LONG,
		// "GLsizeiptr" to ULONG,
		"GLsync" to LONG /* Probably should be here since, `typedef struct __GLsync *GLsync;` */,

		"GLbitfield" to U_INT,
		// "GLbitfield" to INT,

		//"GLhalf" to null,

		"GLfloat" to FLOAT,
		"GLclampf" to FLOAT /* From 0.0f to 1.0f */,
		"GLdouble" to DOUBLE,
		"GLclampd" to DOUBLE /* From 0.0 to 1.0 */
)
internal val primitiveTypeAliases = mapOf(
		// "GLeglImageOES" to "void*",
		"GLint64EXT" to "GLint64",
		"GLuint64EXT" to "GLuint64"
)
