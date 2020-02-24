import codegen.math.GenerateMath
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

val generateMath by tasks.registering(GenerateMath::class) {
	outputDir.set(buildDir.resolve("generated-src"))
}

kotlin {
	jvm {
		compilations {
			"main" {
				dependencies {
					implementation(kotlin("stdlib"))
				}
			}
			"test" {
				dependencies {
					implementation(kotlin("test"))
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

	presets.withType<KotlinNativeTargetPreset> {
		targetFromPreset(this)
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
