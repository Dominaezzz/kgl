import codegen.opengl.GenerateOpenGL
import config.Config
import config.Versions
import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
}

val downloadRegistry by tasks.registering(Download::class) {
	val glXmlCommit = "89acc93eaa6acd97159fb069e66acb92f12d7b87"

	src("https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/$glXmlCommit/xml/gl.xml")
	dest(buildDir.resolve("cache/gl.xml"))
	overwrite(false)
}

val downloadedHeadersDir = buildDir.resolve("cache/headers")

val downloadHeaders by tasks.registering(Download::class) {
	val glXmlCommit = "89acc93eaa6acd97159fb069e66acb92f12d7b87"

	src("https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/$glXmlCommit/api/GL/glcorearb.h")
	dest(downloadedHeadersDir.resolve("GL/glcorearb.h"))
	overwrite(false)
}

val generateOpenGL by tasks.registering(GenerateOpenGL::class) {
	registryFile.set(downloadRegistry.map { RegularFile { it.dest } })
	outputDir.set(buildDir.resolve("generated-src"))
}

kotlin {
	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
				api(project(":kgl-core"))
			}
		}
		commonTest {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}
	}

	js {
		compilations {
			"main" {
				dependencies {
					implementation(kotlin("stdlib-js"))
				}
			}
			"test" {
				dependencies {
					implementation(kotlin("test-js"))
				}
			}
		}
	}

	jvm {
		compilations {
			"main" {
				dependencies {
					implementation(kotlin("stdlib-jdk8"))
					api("org.lwjgl:lwjgl-opengl:${Versions.LWJGL}")
					api("org.lwjgl:lwjgl-opengles:${Versions.LWJGL}")
				}
			}
			"test" {
				dependencies {
					implementation(kotlin("test"))
					implementation(kotlin("test-junit"))
					implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
					implementation("org.lwjgl:lwjgl-opengl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
					implementation("org.lwjgl:lwjgl-opengles:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				}
			}
		}
	}

	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64 {
		compilations["main"].apply {
			defaultSourceSet {
				kotlin.srcDir(generateOpenGL.map { it.mingwDir })
			}
		}
	}
	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64 {
		compilations["main"].apply {
			defaultSourceSet {
				kotlin.srcDir(generateOpenGL.map { it.linuxDir })
			}
		}
	}
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64 {
		compilations["main"].apply {
			defaultSourceSet {
				kotlin.srcDir(generateOpenGL.map { it.macosDir })
			}
		}
	}

	targets.withType<KotlinNativeTarget> {
		compilations["main"].apply {
			cinterops.create("copengl") {
				tasks.named(interopProcessingTaskName) {
					dependsOn(downloadHeaders)
				}
				includeDirs("src/nativeInterop/opengl", downloadedHeadersDir)
			}
			defaultSourceSet {
				kotlin.srcDir("src/${name.takeWhile { it.isLowerCase() }}Main/kotlin")
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			compileKotlinTask.dependsOn(generateOpenGL)
		}
	}
}

apply {
   from(rootProject.file("gradle/publish.gradle"))
}
