import de.undercouch.gradle.tasks.download.Download
import org.gradle.internal.os.OperatingSystem
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
	dependsOn(downloadBinaries)

	from(zipTree(downloadBinaries.get().dest)) {
		eachFile {
			relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
		}
		include("glfw-*/include/**", "glfw-*/lib-mingw-w64/**")

		includeEmptyDirs = false
	}
	into(glfwDir)
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

	jvm {
		compilations["main"].defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-jdk8"))
				api("org.lwjgl:lwjgl-glfw:${extra["lwjglVersion"]}")
			}
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
				implementation("org.lwjgl:lwjgl-glfw:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
			}
		}
	}

	val vulkanHeaderDir = rootProject.childProjects["kgl-vulkan"]!!.file("src/nativeInterop/vulkan/include")

	if (os.isWindows || !isIdeaActive) {
		mingwX64 {
			compilations["main"].cinterops.apply {
				create("cglfw") {
					tasks[interopProcessingTaskName].dependsOn(unzipBinaries)

					includeDirs(glfwDir.resolve("include"), vulkanHeaderDir)

					// This doesn't seem to work. https://github.com/JetBrains/kotlin-native/issues/2314
					// extraOpts("-include-binary", glfwDir.resolve("lib-mingw-w64/libglfw3.a").absolutePath)
				}
			}
		}
	}
	if (os.isLinux || !isIdeaActive) {
		linuxX64 {
			compilations["main"].cinterops.apply {
				create("cglfw") {
					includeDirs(vulkanHeaderDir)
				}
			}
		}
	}
	if (os.isMacOsX || !isIdeaActive) {
		macosX64 {
			compilations["main"].cinterops.apply {
				create("cglfw") {
					includeDirs(vulkanHeaderDir)
				}
			}
		}
	}

	targets.withType<KotlinNativeTarget> {
		compilations {
			"main" {
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
}

apply {
	from(rootProject.file("gradle/publish.gradle"))
}
