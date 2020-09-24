import codegen.opengl.*
import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.konan.target.*

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

val useSingleTarget: Boolean by rootProject.extra
val lwjglVersion: String by rootProject.extra
val lwjglNatives: String by rootProject.extra

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	js()

	if (!useSingleTarget || HostManager.hostIsLinux) linuxX64("linux")
	if (!useSingleTarget || HostManager.hostIsMac) macosX64("macos")
	if (!useSingleTarget || HostManager.hostIsMingw) mingwX64("mingw")

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
				api("org.lwjgl:lwjgl-opengl:$lwjglVersion")
				api("org.lwjgl:lwjgl-opengles:$lwjglVersion")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
				implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")
				implementation("org.lwjgl:lwjgl-opengles:$lwjglVersion:$lwjglNatives")
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
