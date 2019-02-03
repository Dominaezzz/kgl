import plugin.GenerateOpenGLNativeTask

plugins {
	kotlin("multiplatform")
	id("opengl-generator")
}

// run this whenever you want to generate the opengl code
//OpenGLGenerator.generate(buildDir.resolve("generated-src"), buildDir.resolve("opengl-cache"))

kotlin {
	val os = org.gradle.internal.os.OperatingSystem.current()
	val isIdeaActive = System.getProperty("idea.active") == "true"

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
	if (os.isWindows || !isIdeaActive) mingwX64("mingw") {
		val main by compilations.getting {
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
	}
	if (os.isLinux || !isIdeaActive) linuxX64("linux") {
		val main by compilations.getting {
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
	}
	if (os.isMacOsX || !isIdeaActive) macosX64("macos") {
		val main by compilations.getting {
			cinterops.create("copengl") {
				includeDirs(openglHeaderDir)
			}
			defaultSourceSet {
				kotlin.srcDir("src/nativeMain/kotlin")
				kotlin.srcDir("src/macosMain/kotlin")
				tasks.withType(GenerateOpenGLNativeTask::class) {
					kotlin.srcDir(outputDir)
				}
				resources.srcDir("src/nativeMain/resources")
			}
		}
	}
}

//apply {
//    from(rootProject.file("gradle/publish.gradle"))
//}
