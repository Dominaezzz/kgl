import config.Versions
import config.Config
import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
	`maven-publish`
}

val downloadsDir = buildDir.resolve("downloads")
val stbDir = downloadsDir.resolve("stb-master")

val downloadArchive by tasks.registering(Download::class) {
	src("https://github.com/nothings/stb/archive/master.zip")
	dest(downloadsDir.resolve("master.zip"))

	overwrite(false)
}

val unzipArchive by tasks.registering(Copy::class) {
	dependsOn(downloadArchive)

	from(downloadArchive.map { zipTree(it.dest) })
	into(downloadsDir)
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
		compilations["main"].defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-jdk8"))
				api("org.lwjgl:lwjgl-stb:${Versions.LWJGL}")
			}
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				implementation("org.lwjgl:lwjgl-stb:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
			}
		}
	}

	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64()
	if (Config.OS.isLinux   || !Config.isIdeaActive) linuxX64()
	if (Config.OS.isMacOsX  || !Config.isIdeaActive) macosX64()

	targets.withType<KotlinNativeTarget> {
		compilations {
			"main" {
				cinterops {
					create("cstb") {
						tasks[interopProcessingTaskName].dependsOn(unzipArchive)

						includeDirs(stbDir)

						extraOpts("-Xsource-compiler-option", "-I${stbDir.absolutePath}")
						extraOpts("-Xcompile-source", file("src/nativeInterop/cinterop/stb.cpp").absolutePath)
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
}
