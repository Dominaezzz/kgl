import codegen.vulkan.*
import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.konan.target.*

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

	if (HostManager.hostIsMingw) {
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

val lwjglVersion: String by rootProject.extra
val lwjglNatives: String by rootProject.extra

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

	linuxX64()
	macosX64()
	mingwX64()

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
				implementation(kotlin("stdlib"))
				api(project(":kgl-core"))
			}
		}

		commonTest {
			dependencies {
				implementation(kotlin("test"))
			}
		}

		named("jvmMain") {
			kotlin.srcDir(generateVulkan.map { it.jvmDir })
			dependencies {
				api("org.lwjgl:lwjgl-vulkan:$lwjglVersion")
			}
		}

		named("jvmTest") {
			dependencies {
				if (HostManager.hostIsMac) {
					implementation("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
				}
			}
		}

		val nativeMain by creating {
			dependsOn(commonMain.get())
			kotlin.srcDir(generateVulkan.map { it.nativeDir })
		}
		val nativeTest by creating {
			dependsOn(commonTest.get())
		}

		for (target in targets.withType<KotlinNativeTarget>()) {
			val main = getByName("${target.name}Main")
			main.dependsOn(nativeMain)
			main.kotlin.srcDir("src/${target.konanTarget.family.name.toLowerCase()}Main/kotlin")

			val test = getByName("${target.name}Test")
			test.dependsOn(nativeTest)
		}
	}
}
