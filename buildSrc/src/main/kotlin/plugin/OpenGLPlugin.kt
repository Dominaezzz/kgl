package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task

class OpenGLPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        // Kotlin native
        val generateOpenGLTask = task<GenerateOpenGLNativeTask>("generateOpenGL")

        tasks.findByName("compileKotlinMingw")?.apply {
            dependsOn(generateOpenGLTask)
        }

        tasks.findByName("compileKotlinLinux")?.apply {
            dependsOn(generateOpenGLTask)
        }

        tasks.findByName("compileKotlinMacos")?.apply {
            dependsOn(generateOpenGLTask)
        }

        // TODO: JVM

        // TODO: Javascript
        
        // TODO: figure out the correct tasks to wire up
    }
}


