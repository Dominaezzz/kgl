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

