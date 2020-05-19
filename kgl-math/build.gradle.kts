import config.Config
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

kotlin {
	jvm()
	js {
		nodejs() // FIXME: breaks CI. Why?
	}

	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64()
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64()
	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64()

	if (!Config.isIdeaActive) {
		presets.withType<KotlinNativeTargetPreset>(::targetFromPreset)
	}

	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
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
				implementation(kotlin("stdlib"))
			}
		}
		named("jvmTest") {
			dependencies {
				implementation(kotlin("test-junit"))
			}
		}
		named("jsMain") {
			dependencies {
				implementation(kotlin("stdlib-js"))
			}
		}
		named("jsTest") {
			dependencies {
				implementation(kotlin("test-js"))
			}
		}
	}
}
