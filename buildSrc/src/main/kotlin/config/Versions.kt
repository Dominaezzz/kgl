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
package config

object Versions {
	const val KOTLINX_IO = "0.1.16"
	const val LWJGL = "3.2.2"
	val LWJGL_NATIVES = when {
		Config.OS.isWindows -> "natives-windows"
		Config.OS.isLinux -> "natives-linux"
		Config.OS.isMacOsX -> "natives-macos"
		else -> throw Exception("Host platform not supported")
	}
}
