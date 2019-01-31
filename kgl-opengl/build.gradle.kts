import plugin.GenerateOpenGLNativeTask

plugins {
	kotlin("multiplatform")
	id("opengl-generator")
}

// run this whenever you want to generate the opengl code
//OpenGLGenerator.generate(buildDir.resolve("generated-src"), buildDir.resolve("opengl-cache"))

kotlin {
	val os = org.gradle.internal.os.OperatingSystem.current()
	val isIdeaActive = System.getProperty("idea.active") != "true"

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

	jvm {
		compilations["main"].defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-jdk8"))
				api("org.lwjgl:lwjgl-opengl:${extra["lwjglVersion"]}")
				api("org.lwjgl:lwjgl-opengles:${extra["lwjglVersion"]}")
			}
		}
		compilations["test"].defaultSourceSet {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-junit"))
				implementation("org.lwjgl:lwjgl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
				implementation("org.lwjgl:lwjgl-opengl:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
				implementation("org.lwjgl:lwjgl-opengles:${extra["lwjglVersion"]}:${extra["lwjglNatives"]}")
			}
		}
	}

	val openglHeaderDir = project.file("src/nativeInterop/opengl")

	// For ARM, should be changed to iosArm32 or iosArm64
	// For Linux, should be changed to e.g. linuxX64
	// For MacOS, should be changed to e.g. macosX64
	// For Windows, should be changed to e.g. mingwX64

	if (os.isWindows || isIdeaActive) {
		mingwX64("mingw") {
			compilations {
				val main by getting {
					cinterops.create("copengl") {
						includeDirs(openglHeaderDir)
					}
					defaultSourceSet {
						kotlin.srcDir("src/nativeMain/kotlin")
						kotlin.srcDir("src/mingwMain/kotlin")
						tasks.withType(GenerateOpenGLNativeTask::class) {
							kotlin.srcDir(outputDir)
						}
						resources.srcDir("src/nativeMain/resources")
					}
				}
				val test by getting {
					defaultSourceSet {
						kotlin.srcDir("src/nativeTest/kotlin")
						resources.srcDir("src/nativeTest/resources")
					}
				}
			}
		}
	}
	if(os.isLinux) {
		linuxX64("linux") {

			compilations {
				val main by getting {
					cinterops.create("copengl") {
						includeDirs(openglHeaderDir)
					}
					defaultSourceSet {
						kotlin.srcDir("src/nativeMain/kotlin")
						kotlin.srcDir("src/linuxMain/kotlin")
						tasks.withType(GenerateOpenGLNativeTask::class) {
							kotlin.srcDir(outputDir)
						}
						resources.srcDir("src/nativeMain/resources")
					}
				}
				val test by getting {
					defaultSourceSet {
						kotlin.srcDir("src/nativeTest/kotlin")
						resources.srcDir("src/nativeTest/resources")
					}
				}
			}
		}
	}

}

//apply {
//    from(rootProject.file("gradle/publish.gradle"))
//}
