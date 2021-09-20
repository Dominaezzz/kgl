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

val lwjglVersion: String by rootProject.extra
val lwjglNatives: String by rootProject.extra

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	linuxX64()
	macosX64()
	mingwX64()

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
			dependencies {
				api("org.lwjgl:lwjgl-stb:$lwjglVersion")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
				implementation("org.lwjgl:lwjgl-stb:$lwjglVersion:$lwjglNatives")
			}
		}

		val nativeMain by creating {
			dependsOn(commonMain.get())
		}
		val nativeTest by creating {
			dependsOn(commonTest.get())
		}

		for (target in targets.withType<KotlinNativeTarget>()) {
			val main = getByName("${target.name}Main")
			main.dependsOn(nativeMain)

			val test = getByName("${target.name}Test")
			test.dependsOn(nativeTest)
		}
	}
}
