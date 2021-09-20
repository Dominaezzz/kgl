import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

val lwjglVersion: String by rootProject.extra
val lwjglNatives: String by rootProject.extra

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "1.8"
		}
	}

	linuxX64()
	macosX64()
	mingwX64()

	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib"))
				api(project(":kgl-glfw"))
				api(project(":kgl-vulkan"))
			}
		}

		commonTest {
			dependencies {
				implementation(kotlin("test"))
			}
		}

		named("jvmMain") {}
		named("jvmTest") {
			dependencies {
				implementation("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
				implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
			}
		}

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
