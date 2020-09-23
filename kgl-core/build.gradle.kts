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

	js()

	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64("linux")
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64("macos")
	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64("mingw")
//TODO
//	if (!Config.isIdeaActive) {
//		iosArm32()
//		iosArm64()
//		iosX64()
//		androidNativeArm32()
//		androidNativeArm64()
//	}

	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
				api("io.ktor:ktor-io:${Versions.KTOR_IO}")
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
				api("org.lwjgl:lwjgl:${Versions.LWJGL}")
			}
		}

		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
			}
		}

		named("jsMain") {}

		named("jsTest") {
			dependencies {
				implementation(kotlin("test-js"))
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
