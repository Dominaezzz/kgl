import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.ByteArrayOutputStream

buildscript {
	val kotlinVersion by extra("1.3.20")

	repositories {
		jcenter()
		maven(url = "https://plugins.gradle.org/m2/")
		maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4-jetbrains-3")
	}
}

plugins {
	kotlin("multiplatform") version ("1.3.20") apply false
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

	val os = org.gradle.internal.os.OperatingSystem.current()
	when {
		os.isWindows -> extra["lwjglNatives"] = "natives-windows"
		os.isLinux -> extra["lwjglNatives"] = "natives-linux"
		os.isMacOsX -> extra["lwjglNatives"] = "natives-macos"
	}

	afterEvaluate {
		(property("kotlin") as KotlinMultiplatformExtension).sourceSets.all {
			languageSettings.apply {
				enableLanguageFeature("InlineClasses")
				useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
			}
		}
	}
}
