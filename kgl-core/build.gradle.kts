import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

val ktorIoVersion: String by rootProject.extra
val lwjglVersion: String by rootProject.extra
val lwjglNatives: String by rootProject.extra

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	js()

	linuxX64()
	macosX64()
	mingwX64()
	// iosArm32()
	// iosArm64()
	// iosX64()
	// androidNativeArm32()
	// androidNativeArm64()

	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib"))
				api("io.ktor:ktor-io:$ktorIoVersion")
			}
		}
		commonTest {
			dependencies {
				implementation(kotlin("test"))
			}
		}

		named("jvmMain") {
			dependencies {
				api("org.lwjgl:lwjgl:$lwjglVersion")
			}
		}
		named("jvmTest") {
			dependencies {
				implementation("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
			}
		}

		named("jsMain") {}
		named("jsTest") {}

		val nativeMain by creating {
			dependsOn(commonMain.get())
		}
		val nativeTest by creating {
			dependsOn(commonTest.get())
		}

		for (target in targets.withType<KotlinNativeTarget>()) {
			val main = getByName("${target.name}Main")
			main.dependsOn(nativeMain)

			val test = getByName("${target.name}Test")
			test.dependsOn(nativeTest)
		}
	}
}
