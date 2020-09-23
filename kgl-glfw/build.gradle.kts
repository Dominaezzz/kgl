import config.*
import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.konan.target.*

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
	`maven-publish`
}

val glfwVersion = "3.3"
val downloadsDir = buildDir.resolve("downloads")
val glfwWin64Dir = downloadsDir.resolve("glfw.bin.WIN64")
val glfwMacosDir = downloadsDir.resolve("glfw.bin.MACOS")

val downloadWin64Binaries by tasks.registering(Download::class) {
	src("https://github.com/glfw/glfw/releases/download/$glfwVersion/glfw-$glfwVersion.bin.WIN64.zip")
	dest(downloadsDir.resolve("glfw.bin.WIN64.zip"))

	overwrite(false)
}

val downloadMacOSBinaries by tasks.registering(Download::class) {
	src("https://github.com/glfw/glfw/releases/download/$glfwVersion/glfw-$glfwVersion.bin.MACOS.zip")
	dest(downloadsDir.resolve("glfw.bin.MACOS.zip"))

	overwrite(false)
}

val unzipWin64Binaries by tasks.registering(Copy::class) {
	from(downloadWin64Binaries.map { zipTree(it.dest) }) {
		eachFile {
			relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
		}
		include("glfw-*/include/**", "glfw-*/lib-mingw-w64/**")

		includeEmptyDirs = false
	}
	into(glfwWin64Dir)
}

val unzipMacOSBinaries by tasks.registering(Copy::class) {
	from(downloadMacOSBinaries.map { zipTree(it.dest) }) {
		eachFile {
			relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
		}
		include("glfw-*/include/**", "glfw-*/lib-macos/**")

		includeEmptyDirs = false
	}
	into(glfwMacosDir)
}

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	evaluationDependsOn(":kgl-vulkan")
	val vulkanUnzipDocs = project(":kgl-vulkan").tasks.named<Copy>("unzipDocs")
	val vulkanHeaderDir = vulkanUnzipDocs.map { it.destinationDir.resolve("include") }

	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64("linux")
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64("macos")
	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64("mingw")

	targets.withType<KotlinNativeTarget> {
		compilations.named("main") {
			cinterops.create("cglfw") {
				tasks.named(interopProcessingTaskName) {
					dependsOn(vulkanUnzipDocs)
					if (konanTarget == KonanTarget.MACOS_X64 || !Config.isIdeaActive) {
						dependsOn(unzipMacOSBinaries)
					}
					if (konanTarget == KonanTarget.MINGW_X64 || !Config.isIdeaActive) {
						dependsOn(unzipWin64Binaries)
					}
				}
				includeDirs(vulkanHeaderDir)
				if (konanTarget == KonanTarget.MACOS_X64 || !Config.isIdeaActive) {
					includeDirs(unzipMacOSBinaries.map { it.destinationDir.resolve("include") })
				}
				if (konanTarget == KonanTarget.MINGW_X64 || !Config.isIdeaActive) {
					includeDirs(unzipWin64Binaries.map { it.destinationDir.resolve("include") })
				}
			}
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
				api("org.lwjgl:lwjgl-glfw:${Versions.LWJGL}")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				implementation("org.lwjgl:lwjgl-glfw:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
			}
		}

		targets.withType<KotlinNativeTarget> {
			named("${name}Main") {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}

			named("${name}Test") {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
				dependencies {
					implementation(project(":kgl-glfw-static"))
				}
			}
		}
	}
}
