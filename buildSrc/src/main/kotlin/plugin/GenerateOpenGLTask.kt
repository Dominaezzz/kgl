package plugin

import opengl.OpenGLGenerator
import org.gradle.api.tasks.GradleBuild
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

open class GenerateOpenGLTask : GradleBuild() {
    @OutputDirectory
    val outputDir = project.buildDir.resolve("generated-src")

    @TaskAction
    fun exec() {
        OpenGLGenerator.generate(outputDir)
    }
}

