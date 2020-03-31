import codegen.math.GenerateMath
import config.Config
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

val generateMath by tasks.registering(GenerateMath::class) {
	outputDir.set(buildDir.resolve("generated-src"))
}

kotlin {
	jvm().compilations {
		"main" {
			dependencies {
				implementation(kotlin("stdlib"))
			}
		}
		"test" {
			dependencies {
				implementation(kotlin("test-junit"))
			}
		}
	}

	js().compilations {
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

	if (Config.OS.isWindows || !Config.isIdeaActive) mingwX64()
	if (Config.OS.isLinux || !Config.isIdeaActive) linuxX64()
	if (Config.OS.isMacOsX || !Config.isIdeaActive) macosX64()
	if (!Config.isIdeaActive) {
		presets.withType<KotlinNativeTargetPreset>(::targetFromPreset)
	}

	sourceSets {
		commonMain {
			kotlin.srcDir(generateMath.map { it.commonDir })
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
	}
}
