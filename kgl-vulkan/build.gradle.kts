import codegen.vulkan.*
import config.*
import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
	`maven-publish`
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

val makeHtmlDocs by tasks.registering(Exec::class) {
	dependsOn(unzipDocs)

	val docsDir = unzipDocs.get().destinationDir

	inputs.file(docsDir.resolve("xml/vk.xml"))
	outputs.file(docsDir.resolve("out/apispec.html"))

	workingDir = docsDir

	if (Config.OS.isWindows) {
		// Requires unix environment to build.
		executable = "C:\\Program Files\\Git\\bin\\sh.exe"
		args("-c", "PYTHON=python ./makeAllExts manhtml")
	} else {
		executable = docsDir.resolve("makeAllExts").absolutePath
		args("manhtml")
	}
}

val generateVulkan by tasks.registering(GenerateVulkan::class) {
	dependsOn(makeHtmlDocs)

	docsDir.set(unzipDocs.get().destinationDir)
	outputDir.set(buildDir.resolve("generated-src"))
}

kotlin {
	jvm {
		compilations {
			all {
				kotlinOptions.jvmTarget = "1.8"
			}
			named("main") {
				compileKotlinTask.dependsOn(generateVulkan)
			}
		}
	}

	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64("linux")
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64("macos")
	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64("mingw")

	targets.withType<KotlinNativeTarget> {
		compilations.named("main") {
			cinterops.create("cvulkan") {
				tasks.named(interopProcessingTaskName) {
					dependsOn(unzipDocs)
				}
				includeDirs(unzipDocs.map { it.destinationDir.resolve("include") })
			}
			compileKotlinTask.dependsOn(generateVulkan)
		}
	}

	sourceSets {
		commonMain {
			kotlin.srcDir(generateVulkan.map { it.commonDir })
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
			kotlin.srcDir(generateVulkan.map { it.jvmDir })
			dependencies {
				api("org.lwjgl:lwjgl-vulkan:${Versions.LWJGL}")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
				if (Versions.LWJGL_NATIVES == "natives-macos") {
					implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				}
			}
		}

		targets.withType<KotlinNativeTarget> {
			named("${name}Main") {
				kotlin.srcDir(generateVulkan.map { it.nativeDir })
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
