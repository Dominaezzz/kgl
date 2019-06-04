import config.Config
import config.Versions
import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
}

val glfwVersion = "3.2.1"
val downloadsDir = buildDir.resolve("downloads")
val glfwDir = downloadsDir.resolve("glfw.bin")

val downloadBinaries by tasks.registering(Download::class) {
	src("https://github.com/glfw/glfw/releases/download/$glfwVersion/glfw-$glfwVersion.bin.WIN64.zip")
	dest(downloadsDir.resolve("glfw.bin.zip"))

	overwrite(false)
}

val unzipBinaries by tasks.registering(Copy::class) {
	from(downloadBinaries.map { zipTree(it.dest) }) {
		eachFile {
			relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
		}
		include("glfw-*/include/**", "glfw-*/lib-mingw-w64/**")

		includeEmptyDirs = false
	}
	into(glfwDir)
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
			}
		}
	}

	if (Config.OS.isWindows || !Config.isIdeaActive) {
		mingwX64 {
			compilations["main"].cinterops["cglfw"].apply {
				tasks.named(interopProcessingTaskName) {
					dependsOn(unzipBinaries)
				}
				includeDirs(unzipBinaries.map { it.destinationDir.resolve("include") })

				// This doesn't seem to work. https://github.com/JetBrains/kotlin-native/issues/2314
				// extraOpts("-include-binary", glfwDir.resolve("lib-mingw-w64/libglfw3.a").absolutePath)
			}
		}
	}
}

apply {
	from(rootProject.file("gradle/publish.gradle"))
}
