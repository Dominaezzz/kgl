import codegen.opengl.OpenGLGenerator
import de.undercouch.gradle.tasks.download.Download
import org.gradle.internal.os.OperatingSystem

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
}

val downloadRegistry by tasks.creating(Download::class) {
	val glXmlCommit = "89acc93eaa6acd97159fb069e66acb92f12d7b87"

	src("https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/$glXmlCommit/xml/gl.xml")
	dest(buildDir.resolve("cache/gl.xml"))
	overwrite(false)
}

val generateOpenGL by tasks.creating(OpenGLGenerator::class) {
	registryFile = downloadRegistry.dest
	outputDir = buildDir.resolve("generated-src")

	dependsOn(downloadRegistry)
}

kotlin {
	val os = OperatingSystem.current()
	val isIdeaActive = System.getProperty("idea.active") == "true"

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-common"))
				api(project(":kgl-core"))
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}
	}

	js {
		compilations["main"].defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-js"))
			}
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test-js"))
			}
		}
	}

	jvm {
		compilations["main"].defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-jdk8"))
				api("org.lwjgl:lwjgl-opengl:${extra["lwjglVersion"]}")
				api("org.lwjgl:lwjgl-opengles:${extra["lwjglVersion"]}")
			}
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
				implementation("org.lwjgl:lwjgl-opengl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
				implementation("org.lwjgl:lwjgl-opengles:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
			}
		}
	}

	val openglHeaderDir = project.file("src/nativeInterop/opengl")

	if (os.isWindows || !isIdeaActive) mingwX64 {
		val main by compilations.getting {
			cinterops.create("copengl") {
				includeDirs(openglHeaderDir)
			}
			defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				kotlin.srcDir("src/mingwMain/kotlin")
				kotlin.srcDir(generateOpenGL.mingwDir)

				resources.srcDir("src/nativeMain/resources")
			}

			compileKotlinTask.dependsOn(generateOpenGL)
		}
	}
	if (os.isLinux || !isIdeaActive) linuxX64 {
		val main by compilations.getting {
			cinterops.create("copengl") {
				includeDirs(openglHeaderDir)
			}
			defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				kotlin.srcDir("src/linuxMain/kotlin")
				kotlin.srcDir(generateOpenGL.linuxDir)

				resources.srcDir("src/nativeMain/resources")
			}
			compileKotlinTask.dependsOn(generateOpenGL)
		}
	}
	if (os.isMacOsX || !isIdeaActive) macosX64 {
		val main by compilations.getting {
			cinterops.create("copengl") {
				includeDirs(openglHeaderDir)
			}
			defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				kotlin.srcDir("src/macosMain/kotlin")
				kotlin.srcDir(generateOpenGL.macosDir)

				resources.srcDir("src/nativeMain/resources")
			}
			compileKotlinTask.dependsOn(generateOpenGL)
		}
	}
}

apply {
   from(rootProject.file("gradle/publish.gradle"))
}
