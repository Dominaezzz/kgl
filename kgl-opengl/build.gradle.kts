import codegen.opengl.*
import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	id("de.undercouch.download")
	`maven-publish`
}

val downloadRegistry by tasks.registering(Download::class) {
	val glXmlCommit = "89acc93eaa6acd97159fb069e66acb92f12d7b87"

	src("https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/$glXmlCommit/xml/gl.xml")
	dest(buildDir.resolve("cache/gl.xml"))
	overwrite(false)
}

val downloadedHeadersDir = buildDir.resolve("cache/headers")

val downloadHeaders by tasks.registering(Download::class) {
	val glXmlCommit = "89acc93eaa6acd97159fb069e66acb92f12d7b87"

	src("https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/$glXmlCommit/api/GL/glcorearb.h")
	dest(downloadedHeadersDir.resolve("GL/glcorearb.h"))
	overwrite(false)
}

val generateOpenGL by tasks.registering(GenerateOpenGL::class) {
	registryFile.set(downloadRegistry.map { RegularFile { it.dest } })
	outputDir.set(buildDir.resolve("generated-src"))
}

val lwjglVersion: String by rootProject.extra
val lwjglNatives: String by rootProject.extra

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	js()

	linuxX64()
	macosX64()
	mingwX64()

	targets.withType<KotlinNativeTarget> {
		compilations.named("main") {
			cinterops.create("copengl") {
				tasks.named(interopProcessingTaskName) {
					dependsOn(downloadHeaders)
				}
				includeDirs("src/nativeInterop/opengl", downloadedHeadersDir)
			}
			compileKotlinTask.dependsOn(generateOpenGL)
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
				api("org.lwjgl:lwjgl-opengl:$lwjglVersion")
				api("org.lwjgl:lwjgl-opengles:$lwjglVersion")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
				implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")
				implementation("org.lwjgl:lwjgl-opengles:$lwjglVersion:$lwjglNatives")
			}
		}

		named("jsMain") {}
		named("jsTest") {}

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
