import config.Config
import config.Versions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

kotlin {
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
	}

	jvm {
		compilations {
			"main" {
				dependencies {
					implementation(kotlin("stdlib-jdk8"))
				}
			}
			"test" {
				dependencies {
					implementation(kotlin("test"))
					implementation(kotlin("test-junit"))
					implementation("org.lwjgl:lwjgl:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
					implementation("org.lwjgl:lwjgl-glfw:${Versions.LWJGL}:${Versions.LWJGL_NATIVES}")
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
				defaultSourceSet {
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
