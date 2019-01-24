plugins {
	kotlin("multiplatform")
}

kotlin {
	val os = org.gradle.internal.os.OperatingSystem.current()

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-common"))
				api(project(":kgl-core"))
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
				api("org.lwjgl:lwjgl-glfw:${extra["lwjglVersion"]}")
			}
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
				implementation("org.lwjgl:lwjgl-glfw:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
			}
		}
	}

	val vulkanHeaderDir = rootProject.childProjects["kgl-vulkan"]!!.file("src/nativeInterop/vulkan/include")

	if (os.isWindows || System.getProperty("idea.active") != "true") {
		mingwX64("mingw") {
			compilations["main"].cinterops.apply {
				create("cglfw") {
					includeDirs(vulkanHeaderDir)
				}
			}
			compilations["main"].defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			compilations["test"].defaultSourceSet {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
	if (os.isLinux || System.getProperty("idea.active") != "true") {
		linuxX64("linux") {
			compilations["main"].cinterops.apply {
				create("cglfw") {
					includeDirs(vulkanHeaderDir)
				}
			}
			compilations["main"].defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			compilations["test"].defaultSourceSet {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
	if (os.isMacOsX || System.getProperty("idea.active") != "true") {
		macosX64("macos") {
			compilations["main"].cinterops.apply {
				create("cglfw") {
					includeDirs(vulkanHeaderDir)
				}
			}
			compilations["main"].defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				resources.srcDir("src/nativeMain/resources")
			}
			compilations["test"].defaultSourceSet {
				kotlin.srcDir("src/nativeTest/kotlin")
				resources.srcDir("src/nativeTest/resources")
			}
		}
	}
}

apply {
	from(rootProject.file("gradle/publish.gradle"))
}
