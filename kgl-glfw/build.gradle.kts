import config.Config
import config.Versions
import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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

	jvm {
		compilations {
			"main" {
				dependencies {
					implementation(kotlin("stdlib-jdk8"))
					api("org.lwjgl:lwjgl-glfw:${Versions.LWJGL}")
				}
			}
			"test" {
				dependencies {
					implementation(kotlin("test"))
					implementation(kotlin("test-junit"))
					implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
					implementation("org.lwjgl:lwjgl-glfw:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				}
			}
		}
	}

	evaluationDependsOn(":kgl-vulkan")
	val vulkanUnzipDocs = project(":kgl-vulkan").tasks.named<Copy>("unzipDocs")
	val vulkanHeaderDir = vulkanUnzipDocs.map { it.destinationDir.resolve("include") }

	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64()
	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64()
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64()

	targets.withType<KotlinNativeTarget> {
		compilations {
			"main" {
				cinterops {
					create("cglfw") {
						tasks.named(interopProcessingTaskName) {
							dependsOn(vulkanUnzipDocs)
						}
						includeDirs(vulkanHeaderDir)
					}
				}
				defaultSourceSet {
					kotlin.srcDir("src/nativeMain/kotlin")
					resources.srcDir("src/nativeMain/resources")
				}
			}

			"test" {
				defaultSourceSet {
					kotlin.srcDir("src/nativeTest/kotlin")
					resources.srcDir("src/nativeTest/resources")
				}
				dependencies {
					implementation(project(":kgl-glfw-static"))
				}
			}
		}
	}

	if (Config.OS.isWindows || !Config.isIdeaActive) {
		mingwX64 {
			compilations["main"].cinterops["cglfw"].apply {
				tasks.named(interopProcessingTaskName) {
					dependsOn(unzipWin64Binaries)
				}
				includeDirs(unzipWin64Binaries.map { it.destinationDir.resolve("include") })
			}
		}
	}
	if (Config.OS.isMacOsX || !Config.isIdeaActive) {
		macosX64 {
			compilations["main"].cinterops["cglfw"].apply {
				tasks.named(interopProcessingTaskName) {
					dependsOn(unzipMacOSBinaries)
				}
				includeDirs(unzipMacOSBinaries.map { it.destinationDir.resolve("include") })
			}
		}
	}
}
