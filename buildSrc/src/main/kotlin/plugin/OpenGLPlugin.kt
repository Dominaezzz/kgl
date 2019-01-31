package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task

class OpenGLPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.run {
        val generateOpenGLTask = task<GenerateOpenGLTask>("generateOpenGL")

        // TODO: figure out the correct tasks to wire up

        // this would be for normal kotlin-jvm
//        val compileKotlin = tasks.getByName("compileKotlin") {
//            dependsOn(generateOpenGLTask)
//        }

        // TODO: add sourceSet generateOpenGLTask.outputDir for the correct multiplatform target
    }
}


