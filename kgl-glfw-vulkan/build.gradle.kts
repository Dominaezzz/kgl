import config.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	`maven-publish`
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

	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
				api(project(":kgl-glfw"))
				api(project(":kgl-vulkan"))
			}
		}

		commonTest {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		named("jvmMain") {}

		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
				implementation("org.lwjgl:lwjgl-glfw:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
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
