import config.Config
import config.Versions
import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
}

val vulkanVersion = "1.1.95"
val downloadsDir = buildDir.resolve("downloads")

val downloadDocs by tasks.registering(Download::class) {
	src("https://github.com/KhronosGroup/Vulkan-Docs/archive/v$vulkanVersion.zip")
	dest(downloadsDir.resolve("Vulkan-Docs.zip"))
	overwrite(false)
}

val unzipDocs by tasks.registering(Copy::class) {
	from(downloadDocs.map { zipTree(it.dest) }) {
		eachFile {
			relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
		}
		includeEmptyDirs = false
	}
	into(downloadDocs.map { it.dest.resolveSibling("Vulkan-Docs") })
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
					api("org.lwjgl:lwjgl-vulkan:${Versions.LWJGL}")
				}
			}
			"test" {
				dependencies {
					implementation(kotlin("test"))
					implementation(kotlin("test-junit"))
					implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				}
			}
		}
	}

	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64()
	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64()
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64()

	targets.withType<KotlinNativeTarget> {
		compilations {
			"main" {
				cinterops {
					create("cvulkan") {
						tasks.named(interopProcessingTaskName) {
							dependsOn(unzipDocs)
						}
						includeDirs(unzipDocs.map { it.destinationDir.resolve("include") })
					}
				}

				defaultSourceSet {
					kotlin.srcDir("src/${name.takeWhile { it.isLowerCase() }}Main/kotlin")

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
