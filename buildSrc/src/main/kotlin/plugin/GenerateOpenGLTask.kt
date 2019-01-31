package plugin

import opengl.OpenGLGenerator
import org.gradle.api.tasks.GradleBuild
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateOpenGLTask : GradleBuild() {
    @Input
    var platform: String = "undefined"

    @OutputDirectory
    var outputDir: File = project.buildDir.resolve("generated-src")

    @TaskAction
    fun exec() {
        outputDir.mkdirs()
        OpenGLGenerator.generate(outputDir.resolve(platform))
    }
}

