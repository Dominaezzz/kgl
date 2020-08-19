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

val konanUserDir = file(System.getenv("KONAN_DATA_DIR") ?: "${System.getProperty("user.home")}/.konan")
val toolChainFolderName = when {
	Config.OS.isLinux -> "clang-llvm-8.0.0-linux-x86-64"
	Config.OS.isMacOsX -> "clang-llvm-apple-8.0.0-darwin-macos"
	Config.OS.isWindows -> "msys2-mingw-w64-x86_64-clang-llvm-lld-compiler_rt-8.0.1"
	else -> TODO()
}
val llvmBinFolder = konanUserDir.resolve("dependencies/${toolChainFolderName}/bin")

val cppFile = file("src/nativeInterop/cinterop/stb.cpp")
val objFile = buildDir.resolve("lib/stb.o")
val staticLibFile = buildDir.resolve("lib/libstb.a")

val compileStb by tasks.registering(Exec::class) {
	dependsOn(unzipArchive)
	commandLine(
			llvmBinFolder.resolve("clang++").absolutePath,
			"-c", "-w",
			"-o", objFile.absolutePath,
			"-I", stbDir.absolutePath,
			cppFile.absolutePath
	)
	environment(
			"PATH" to "$llvmBinFolder;${System.getenv("PATH")}",
			"CPATH" to "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk/usr/include"
	)
	inputs.file(cppFile)
	outputs.file(objFile)
}
val archiveStb by tasks.registering(Exec::class) {
	dependsOn(compileStb)

	commandLine(
			llvmBinFolder.resolve("llvm-ar").absolutePath,
			"rc", staticLibFile.absolutePath,
			objFile.absolutePath
	)
	environment("PATH", "$llvmBinFolder;${System.getenv("PATH")}")
	inputs.file(objFile)
	outputs.file(staticLibFile)
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
					}
				}
				defaultSourceSet {
					kotlin.srcDir("src/nativeMain/kotlin")
					resources.srcDir("src/nativeMain/resources")
				}

				compileKotlinTask.apply {
					dependsOn(archiveStb)
					kotlinOptions {
						freeCompilerArgs = listOf("-include-binary", staticLibFile.absolutePath)
					}
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
