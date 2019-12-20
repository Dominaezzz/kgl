import codegen.vulkan.GenerateVulkan
import codegen.vulkan.Registry
import config.Config
import config.Versions
import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import javax.xml.parsers.DocumentBuilderFactory

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

val makeHtmlDocs by tasks.registering(Task::class) {
	dependsOn(unzipDocs)

	val docsDir = unzipDocs.get().destinationDir
	val vkXml = docsDir.resolve("xml/vk.xml")

	inputs.file(vkXml)
	outputs.file(docsDir.resolve("out/apispec.html"))

	doLast {
		val registry = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(vkXml)
				.let {
					it.documentElement.normalize()
					Registry(it)
				}

		val extensionsConcat = registry.extensions.filter { it.supported == "vulkan" }
				.joinToString(" ") { it.name }

		exec {
			workingDir = docsDir

			if (Config.OS.isWindows) {
				// Requires unix environment to build.
				executable = "C:\\Program Files\\Git\\bin\\sh.exe"
				args("-c", "make PYTHON=python EXTENSIONS='$extensionsConcat' manhtml")
			} else {
				commandLine("make", "EXTENSIONS=$extensionsConcat", "manhtml")
			}
		}
	}
}

val generateVulkan by tasks.registering(GenerateVulkan::class) {
	dependsOn(makeHtmlDocs)

	docsDir.set(unzipDocs.get().destinationDir)
	outputDir.set(buildDir.resolve("generated-src"))
}

kotlin {
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
	}

	jvm {
		compilations {
			"main" {
				compileKotlinTask.dependsOn(generateVulkan)

				defaultSourceSet {
					kotlin.srcDir(generateVulkan.map { it.jvmDir })
				}

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
				compileKotlinTask.dependsOn(generateVulkan)

				cinterops {
					create("cvulkan") {
						tasks.named(interopProcessingTaskName) {
							dependsOn(unzipDocs)
						}
						includeDirs(unzipDocs.map { it.destinationDir.resolve("include") })
					}
				}

				defaultSourceSet {
					kotlin.srcDir(generateVulkan.map { it.nativeDir })
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
