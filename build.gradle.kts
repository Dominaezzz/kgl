import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.HostManager
import java.io.ByteArrayOutputStream

buildscript {
	val kotlinVersion by extra("1.3.30")

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
	kotlin("multiplatform") version ("1.3.30") apply false
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

	extra["kotlinxIOVersion"] = "0.1.8"
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

			// Hack until https://youtrack.jetbrains.com/issue/KT-30498
			targets.filterIsInstance<KotlinNativeTarget>()
					.filter { it.konanTarget != HostManager.host }
					.forEach { target ->
						target.compilations.forEach { comp ->
							comp.cinterops.forEach {
								project.tasks[it.interopProcessingTaskName].enabled = false
							}
							comp.compileKotlinTask.enabled = false
						}
						target.binaries.forEach { it.linkTask.enabled = false }

						target.mavenPublication(Action {
							val publicationToDisable = this

							tasks.filterIsInstance<AbstractPublishToMaven>().forEach { publish ->
								publish.onlyIf {
									publish.publication != publicationToDisable
								}
							}
							tasks.filterIsInstance<GenerateModuleMetadata>().forEach { generate ->
								generate.onlyIf {
									generate.publication.get() != publicationToDisable
								}
							}
						})
					}
		}
	}
}
