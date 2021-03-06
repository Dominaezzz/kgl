import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.konan.target.*

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

evaluationDependsOn(":kgl-glfw")
val unzipWin64Binaries by project(":kgl-glfw").tasks.getting(Copy::class)
val unzipMacOSBinaries by project(":kgl-glfw").tasks.getting(Copy::class)

val useSingleTarget: Boolean by rootProject.extra

kotlin {
	val staticLibs = mapOf(
		KonanTarget.LINUX_X64 to file("/usr/local/lib/libglfw3.a"),
		KonanTarget.MACOS_X64 to unzipMacOSBinaries.destinationDir.resolve("lib-macos/libglfw3.a"),
		KonanTarget.MINGW_X64 to unzipWin64Binaries.destinationDir.resolve("lib-mingw-w64/libglfw3.a")
	)

	if (!useSingleTarget || HostManager.hostIsLinux) linuxX64("linux")
	if (!useSingleTarget || HostManager.hostIsMac) macosX64("macos")
	if (!useSingleTarget || HostManager.hostIsMingw) mingwX64("mingw")

	targets.withType<KotlinNativeTarget> {
		compilations.named("main") {
			if (konanTarget == KonanTarget.MACOS_X64) compileKotlinTask.dependsOn(unzipMacOSBinaries)
			if (konanTarget == KonanTarget.MINGW_X64) compileKotlinTask.dependsOn(unzipWin64Binaries)

			kotlinOptions {
				freeCompilerArgs = listOf("-include-binary", staticLibs.getValue(konanTarget).absolutePath)
			}
		}
	}

	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
			}
		}

		commonTest {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}
	}
}
