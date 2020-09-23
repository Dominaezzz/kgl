import codegen.opengl.*
import config.*
import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
	`maven-publish`
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
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	js()

	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64("linux")
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64("macos")
	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64("mingw")

	targets.withType<KotlinNativeTarget> {
		compilations.named("main") {
			cinterops.create("copengl") {
				tasks.named(interopProcessingTaskName) {
					dependsOn(downloadHeaders)
				}
				includeDirs("src/nativeInterop/opengl", downloadedHeadersDir)
			}
			compileKotlinTask.dependsOn(generateOpenGL)
		}
	}

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

		named("jvmMain") {
			dependencies {
				api("org.lwjgl:lwjgl-opengl:${Versions.LWJGL}")
				api("org.lwjgl:lwjgl-opengles:${Versions.LWJGL}")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				implementation("org.lwjgl:lwjgl-opengl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				implementation("org.lwjgl:lwjgl-opengles:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
			}
		}

		named("jsMain") {}

		named("jsTest") {
			dependencies {
				implementation(kotlin("test-js"))
			}
		}

		targets.withType<KotlinNativeTarget> {
			named("${name}Main") {
				kotlin.srcDir(generateOpenGL.map { it.nativeDir })
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}

			named("${name}Test") {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
}
