import org.jetbrains.kotlin.konan.target.KonanTarget

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

evaluationDependsOn(":kgl-glfw")
val unzipWin64Binaries by project(":kgl-glfw").tasks.getting(Copy::class)
val unzipMacOSBinaries by project(":kgl-glfw").tasks.getting(Copy::class)

kotlin {
	val configs = listOf(
			mingwX64() to unzipWin64Binaries.destinationDir.resolve("lib-mingw-w64/libglfw3.a"),
			linuxX64() to file("/usr/local/lib/libglfw3.a"),
			macosX64() to unzipMacOSBinaries.destinationDir.resolve("lib-macos/libglfw3.a")
	)

	for ((target, staticLib) in configs) {
		target.compilations["main"].apply {
			if (target.konanTarget == KonanTarget.MINGW_X64){
				compileKotlinTask.dependsOn(unzipWin64Binaries)
			} else if (target.konanTarget == KonanTarget.MACOS_X64){
				compileKotlinTask.dependsOn(unzipMacOSBinaries)
			}
			kotlinOptions {
				freeCompilerArgs = listOf("-include-binary", staticLib.absolutePath)
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
