import config.*
import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

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
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64("linux")
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64("macos")
	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64("mingw")

	targets.withType<KotlinNativeTarget> {
		compilations.named("main") {
			cinterops.create("cstb") {
				tasks.named(interopProcessingTaskName) {
					dependsOn(unzipArchive)
				}
				includeDirs(stbDir)
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
				api("org.lwjgl:lwjgl-stb:${Versions.LWJGL}")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				implementation("org.lwjgl:lwjgl-stb:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
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
			}
		}
	}
}
