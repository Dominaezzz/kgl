import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.ByteArrayOutputStream

buildscript {
	val kotlinVersion by extra("1.3.21")

	repositories {
		jcenter()
		maven(url = "https://plugins.gradle.org/m2/")
		maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4-jetbrains-3")
		classpath("de.undercouch:gradle-download-task:3.4.3")
	}
}

plugins {
	kotlin("multiplatform") version ("1.3.21") apply false
}

subprojects {
	apply {
		plugin("maven-publish")
	}

	group = "com.kgl"

	val stdout = ByteArrayOutputStream()
	exec {
		commandLine("git", "describe", "--tags")
		standardOutput = stdout
	}

	version = stdout.toString().trim()

	repositories {
		mavenCentral()
		jcenter()
		mavenLocal()
	}

	extra["kotlinxIOVersion"] = "0.1.4"
	extra["lwjglVersion"] = "3.2.1"

	val os = OperatingSystem.current()
	extra["lwjglNatives"] = when {
		os.isWindows -> "natives-windows"
		os.isLinux -> "natives-linux"
		os.isMacOsX -> "natives-macos"
		else -> throw Exception("Host platform not supported")
	}

	afterEvaluate {
		configure<KotlinMultiplatformExtension> {
			sourceSets.all {
				languageSettings.apply {
					enableLanguageFeature("InlineClasses")
					useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
				}
			}
		}
	}
}
