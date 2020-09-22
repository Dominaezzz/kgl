import config.*
import org.jetbrains.kotlin.gradle.dsl.*
import java.io.*

plugins {
	kotlin("multiplatform") version ("1.4.10") apply false
	id("de.undercouch.download") version ("3.4.3") apply false
}

val stdout = ByteArrayOutputStream()
exec {
	commandLine("git", "describe", "--tags")
	standardOutput = stdout
}

group = "com.kgl"
version = stdout.toString().trim()

subprojects {
	group = rootProject.group
	version = rootProject.version

	repositories {
		jcenter()
	}

	afterEvaluate {
		configure<KotlinMultiplatformExtension> {
			sourceSets.all {
				languageSettings.apply {
					enableLanguageFeature("InlineClasses")
					useExperimentalAnnotation("kotlin.RequiresOptIn")
					useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
					useExperimentalAnnotation("io.ktor.utils.io.core.ExperimentalIoApi")
				}
			}
		}

		configure<PublishingExtension> {
			val vcs: String by project
			val bintrayOrg: String by project
			val bintrayRepository: String by project

			repositories {
				maven("https://api.bintray.com/maven/$bintrayOrg/$bintrayRepository/kgl/;publish=0;override=0") {
					name = "bintray"
					credentials {
						username = System.getenv("BINTRAY_USER")
						password = System.getenv("BINTRAY_API_KEY")
					}
				}
			}

			publications.withType<MavenPublication> {
				pom {
					name.set(project.name)
					description.set(project.description)
					url.set(vcs)
					licenses {
						license {
							name.set("The Apache Software License, Version 2.0")
							url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
							distribution.set("repo")
						}
					}
					developers {
						developer {
							id.set("Dominaezzz")
							name.set("Dominic Fischer")
						}
					}
					scm {
						connection.set("$vcs.git")
						developerConnection.set("$vcs.git")
						url.set(vcs)
					}
				}
			}
		}

		val taskPrefixes = when {
			Config.OS.isLinux -> listOf(
				"publishLinux",
				"publishJs",
				"publishJvm",
				"publishMetadata",
				"publishKotlinMultiplatform"
			)
			Config.OS.isMacOsX -> listOf(
				"publishMacos",
				"publishIos"
			)
			Config.OS.isWindows -> listOf(
				"publishMingw"
			)
			else -> TODO()
		}

		val publishTasks = tasks.withType<PublishToMavenRepository>().matching { task ->
			taskPrefixes.any { task.name.startsWith(it) }
		}

		tasks.register("smartPublish") {
			dependsOn(publishTasks)
		}
	}
}
