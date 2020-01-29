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
package com.kgl.glfw

import org.lwjgl.glfw.GLFW.*

actual enum class OpenGLProfile(val value: Int) {
	Any(GLFW_OPENGL_ANY_PROFILE),
	Core(GLFW_OPENGL_CORE_PROFILE),
	Compat(GLFW_OPENGL_COMPAT_PROFILE);

	companion object {
		fun from(value: Int): OpenGLProfile = when(value) {
			GLFW_OPENGL_ANY_PROFILE -> Any
			GLFW_OPENGL_CORE_PROFILE -> Core
			GLFW_OPENGL_COMPAT_PROFILE -> Compat
			else -> throw NoSuchElementException("Unknown OpenGLProfile value of $value")
		}
	}
}

actual enum class ClientApi(val value: Int) {
	OpenGL(GLFW_OPENGL_API),
	OpenGLES(GLFW_OPENGL_ES_API),
	None(GLFW_NO_API);

	companion object {
		fun from(value: Int): ClientApi = when(value) {
			GLFW_OPENGL_API -> OpenGL
			GLFW_OPENGL_ES_API -> OpenGLES
			GLFW_NO_API -> None
			else -> throw NoSuchElementException("Unknown ClientApi value of $value")
		}
	}
}

actual enum class CreationApi(val value: Int) {
	Native(GLFW_NATIVE_CONTEXT_API),
	EGL(GLFW_EGL_CONTEXT_API);

	companion object {
		fun from(value: Int): CreationApi = when(value) {
			GLFW_NATIVE_CONTEXT_API -> Native
			GLFW_EGL_CONTEXT_API -> EGL
			else -> throw NoSuchElementException("Unknown CreationApi value of $value")
		}
	}
}

actual enum class Robustness(val value: Int) {
	LoseContextOnReset(GLFW_LOSE_CONTEXT_ON_RESET),
	NoResetNotification(GLFW_LOSE_CONTEXT_ON_RESET),
	None(GLFW_NO_ROBUSTNESS);

	companion object {
		fun from(value: Int): Robustness = when(value) {
			GLFW_LOSE_CONTEXT_ON_RESET -> LoseContextOnReset
			GLFW_NO_RESET_NOTIFICATION -> NoResetNotification
			GLFW_NO_ROBUSTNESS -> None
			else -> throw NoSuchElementException("Unknown Robustness value of $value")
		}
	}
}

actual enum class ReleaseBehaviour(val value: Int) {
	Any(GLFW_ANY_RELEASE_BEHAVIOR),
	Flush(GLFW_RELEASE_BEHAVIOR_FLUSH),
	None(GLFW_RELEASE_BEHAVIOR_NONE);

	companion object {
		fun from(value: Int): ReleaseBehaviour = when(value) {
			GLFW_ANY_RELEASE_BEHAVIOR -> Any
			GLFW_RELEASE_BEHAVIOR_FLUSH -> Flush
			GLFW_RELEASE_BEHAVIOR_NONE -> None
			else -> throw NoSuchElementException("Unknown ReleaseBehaviour value of $value")
		}
	}
}
