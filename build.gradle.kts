import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.HostManager
import java.io.ByteArrayOutputStream

plugins {
	kotlin("multiplatform") version ("1.3.50") apply false
	id("com.jfrog.bintray") version ("1.8.4-jetbrains-3") apply false
	id("de.undercouch.download") version ("3.4.3") apply false
}

subprojects {
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
						target.compilations.all {
							cinterops.all {
								project.tasks[interopProcessingTaskName].enabled = false
							}
							compileKotlinTask.enabled = false
						}
						target.binaries.all {
							linkTask.enabled = false
						}

						target.mavenPublication(Action {
							val publicationToDisable = this

							tasks.withType<AbstractPublishToMaven> {
								onlyIf {
									publication != publicationToDisable
								}
							}
							tasks.withType<GenerateModuleMetadata> {
								onlyIf {
									publication.get() != publicationToDisable
								}
							}
						})
					}
		}
	}
}
