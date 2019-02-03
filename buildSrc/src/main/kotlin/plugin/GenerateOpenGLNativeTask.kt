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
package plugin

import opengl.kn.OpenGLNativeGenerator
import org.gradle.api.tasks.GradleBuild
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem
import java.io.File

open class GenerateOpenGLNativeTask : GradleBuild() {
	private val os = OperatingSystem.current()
	private val target: String
		get() = when {
			os.isWindows -> "mingw"
			os.isLinux -> "linux"
			os.isMacOsX -> "macos"
			else -> throw IllegalStateException("unrecognized operating system")
		}

	@OutputDirectory
	var outputDir: File = project.buildDir.resolve("generated-src").resolve(target)

	@TaskAction
	fun exec() {
		outputDir.mkdirs()
		OpenGLNativeGenerator.generate(outputDir)
	}
}
