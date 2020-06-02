import config.Config
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.HostManager
import java.io.ByteArrayOutputStream

plugins {
	kotlin("multiplatform") version ("1.3.72") apply false
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
		mavenCentral()
		jcenter()
		mavenLocal()
	}

	afterEvaluate {
		configure<KotlinMultiplatformExtension> {
			sourceSets.all {
				languageSettings.apply {
					enableLanguageFeature("InlineClasses")
					enableLanguageFeature("NewInference")
					useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
					useExperimentalAnnotation("io.ktor.utils.io.core.ExperimentalIoApi")
				}
			}

			// Hack until https://youtrack.jetbrains.com/issue/KT-30498
			targets.withType<KotlinNativeTarget> {
				// Disable cross-platform build
				if (konanTarget != HostManager.host) {
					compilations.all {
						cinterops.all {
							tasks.named(interopProcessingTaskName).configure {
								enabled = false
							}
						}
						compileKotlinTask.enabled = false
					}
					binaries.all {
						linkTask.enabled = false
					}
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

		val publishTasks = tasks.withType<PublishToMavenRepository>()
				.matching {
					when {
						Config.OS.isWindows -> it.name.startsWith("publishMingw")
						Config.OS.isMacOsX -> it.name.startsWith("publishMacos") || it.name.startsWith("publishIos")
						Config.OS.isLinux -> it.name.startsWith("publishLinux") ||
								it.name.startsWith("publishJs") ||
								it.name.startsWith("publishJvm") ||
								it.name.startsWith("publishMetadata") ||
								it.name.startsWith("publishKotlinMultiplatform")
						else -> TODO()
					}
				}
		tasks.register("smartPublish") {
			dependsOn(publishTasks)
		}
	}
}
