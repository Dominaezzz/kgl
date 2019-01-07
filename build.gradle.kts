group = "com.kgl"
version = "0.0.1"

buildscript {
	extra["kotlinVersion"] = "1.3.11"
	extra["kotlinxIOVersion"] = "0.1.1"
	extra["lwjglVersion"] = "3.2.1"

	val os = org.gradle.internal.os.OperatingSystem.current()
	extra["os"] = os

	when {
		os.isWindows -> extra["lwjglNatives"] = "natives-windows"
		os.isLinux -> extra["lwjglNatives"] = "natives-linux"
		os.isMacOsX -> extra["lwjglNatives"] = "natives-macos"
	}

	val kotlinVersion: String by extra

	repositories {
		jcenter()
		maven("https://plugins.gradle.org/m2/")
		maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
	}
}

plugins {
	id("kotlin-multiplatform") version ("1.3.11")
}

allprojects {
	apply(plugin = "maven-publish")

	group = "com.kgl"
	version = "0.0.1"

	repositories {
		mavenCentral()
		jcenter()
		mavenLocal()
	}
}
