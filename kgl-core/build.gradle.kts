import org.gradle.internal.os.OperatingSystem

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
		compilations["main"].defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-jdk8"))
				api("org.lwjgl:lwjgl:${extra["lwjglVersion"]}")
			}
		}
		compilations["main"].dependencies {
			api("org.jetbrains.kotlinx:kotlinx-io-jvm:${extra["kotlinxIOVersion"]}")
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
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
	if (os.isWindows || !isIdeaActive) {
		mingwX64("mingw") {
			compilations["main"].defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			compilations["main"].dependencies {
				api("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
			}
			compilations["test"].defaultSourceSet {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
	if (os.isLinux || !isIdeaActive) {
		linuxX64("linux") {
			compilations["main"].defaultSourceSet {
				kotlin.srcDirs("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			compilations["main"].dependencies {
				api("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
			}
			compilations["test"].defaultSourceSet {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
	if (os.isMacOsX || !isIdeaActive) {
		macosX64("macos") {
			compilations["main"].defaultSourceSet {
				kotlin.srcDirs("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			compilations["main"].dependencies {
				api("org.jetbrains.kotlinx:kotlinx-io-native:${extra["kotlinxIOVersion"]}")
			}
			compilations["test"].defaultSourceSet {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
		// iosArm32("iosArm32")
		// iosArm64("iosArm64")
		// iosX64("iosX64")
	}
	if ((os.isLinux || os.isMacOsX) && !isIdeaActive) {
		// androidNativeArm32("androidNativeArm32")
		// androidNativeArm64("androidNativeArm64")
	}
}

apply {
	from(rootProject.file("gradle/publish.gradle"))
}
