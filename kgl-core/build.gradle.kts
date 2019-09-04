import config.Config
import config.Versions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
}

kotlin {
	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
				api("org.jetbrains.kotlinx:kotlinx-io:${Versions.KOTLINX_IO}")
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
				dependencies {
					implementation(kotlin("stdlib-jdk8"))
					api("org.lwjgl:lwjgl:${Versions.LWJGL}")
					api("org.jetbrains.kotlinx:kotlinx-io-jvm:${Versions.KOTLINX_IO}")
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
	js {
		compilations {
			"main" {
				dependencies {
					implementation(kotlin("stdlib-js"))
				}
			}
			"test" {
				dependencies {
					implementation(kotlin("test-js"))
				}
			}
		}
	}
	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64()
	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64()
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64()
	if (!Config.isIdeaActive) {
		// iosArm32()
		// iosArm64()
		// iosX64()
		// androidNativeArm32()
		// androidNativeArm64()
	}

	targets.withType<KotlinNativeTarget> {
		compilations {
			"main" {
				defaultSourceSet {
					kotlin.srcDir("src/nativeMain/kotlin")
					resources.srcDir("src/nativeMain/resources")
				}
				dependencies {
					api("org.jetbrains.kotlinx:kotlinx-io-native:${Versions.KOTLINX_IO}")
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
