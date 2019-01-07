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

import cglfw.*

actual enum class OpenGLProfile(internal val value: Int) {
	Any(GLFW_OPENGL_ANY_PROFILE),
	Core(GLFW_OPENGL_CORE_PROFILE),
	Compat(GLFW_OPENGL_COMPAT_PROFILE)
}

actual enum class ClientApi(internal val value: Int) {
	OpenGL(GLFW_OPENGL_API),
	OpenGLES(GLFW_OPENGL_ES_API),
	None(GLFW_NO_API)
}

actual enum class CreationApi(internal val value: Int) {
	Native(GLFW_NATIVE_CONTEXT_API),
	EGL(GLFW_EGL_CONTEXT_API)
}

actual enum class Robustness(internal val value: Int) {
	None(GLFW_NO_ROBUSTNESS)
}

actual enum class ReleaseBehaviour(internal val value: Int) {
	Any(GLFW_ANY_RELEASE_BEHAVIOR),
	Flush(GLFW_RELEASE_BEHAVIOR_FLUSH),
	None(GLFW_RELEASE_BEHAVIOR_NONE)
}
