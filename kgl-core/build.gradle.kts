import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
	kotlin("multiplatform")
}

kotlin {
	val os = OperatingSystem.current()
	val isIdeaActive = System.getProperty("idea.active") == "true"

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-common"))
				api("org.jetbrains.kotlinx:kotlinx-io:${extra["kotlinxIOVersion"]}")
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}
	}

	jvm {
		compilations {
			"main" {
				defaultSourceSet {
					dependencies {
						implementation(kotlin("stdlib-jdk8"))
						api("org.lwjgl:lwjgl:${extra["lwjglVersion"]}")
					}
				}
				dependencies {
					api("org.jetbrains.kotlinx:kotlinx-io-jvm:${extra["kotlinxIOVersion"]}")
				}
			}

			"test" {
				defaultSourceSet {
					dependencies {
						implementation(kotlin("test"))
						implementation(kotlin("test-junit"))
						implementation("org.lwjgl:lwjgl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
					}
				}
			}
		}
	}
	js {
		compilations["main"].defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-js"))
			}
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test-js"))
			}
		}
	}
	if (os.isWindows || !isIdeaActive) mingwX64()
	if (os.isLinux || !isIdeaActive) linuxX64()
	if (os.isMacOsX || !isIdeaActive) macosX64()
	if (!isIdeaActive) {
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
					api("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
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

apply {
	from(rootProject.file("gradle/publish.gradle"))
}
