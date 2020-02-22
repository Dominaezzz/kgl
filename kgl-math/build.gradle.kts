import codegen.math.GenerateMath

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

val generateMath by tasks.registering(GenerateMath::class) {
	outputDir.set(buildDir.resolve("generated-src"))
}

kotlin {
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
				implementation(kotlin("test-annotation"))
			}
		}
	}
}
