package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task

class OpenGLPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.run {
        val generatedSrc = project.buildDir.resolve("generated-src")

        val generateMingwOpenGLTask = task<GenerateOpenGLTask>("generateMingwOpenGL") {
            platform = TargetPlatform.mingw
        }

        val generateLinuxOpenGLTask = task<GenerateOpenGLTask>("generateLinuxOpenGL") {
            platform = TargetPlatform.linux
        }

        // TODO: figure out the correct tasks to wire up

        val compileKotlinMingw = tasks.findByName("compileKotlinMingw")?.apply {
            dependsOn(generateMingwOpenGLTask)
        }

        val compileKotlinLinux = tasks.findByName("compileKotlinLinux")?.apply {
            dependsOn(generateLinuxOpenGLTask)
        }

        afterEvaluate {
            // TODO: add sourceSet generateOpenGLTask.outputDir for the correct multiplatform target

//            configure<KotlinMultiplatformExtension> {
//
//            }


            // this would be for normal kotlin-jvm
//        val compileKotlin = tasks.getByName("compileKotlin") {
//            dependsOn(generateOpenGLTask)
//        }

        }
    }
}


