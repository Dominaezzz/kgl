import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.HostManager
import java.io.ByteArrayOutputStream

plugins {
	kotlin("multiplatform") version ("1.3.50") apply false
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

		apply<MavenPlugin>()
		apply<MavenPublishPlugin>()

		configure<PublishingExtension> {
			val vcs: String by project
			val bintrayOrg: String by project
			val bintrayRepository: String by project

			repositories {
				maven("https://api.bintray.com/maven/$bintrayOrg/$bintrayRepository/kgl/;publish=1;override=1") {
					name = "bintray"
					credentials {
						username = System.getenv("BINTRAY_USER")
						password = System.getenv("BINTRAY_API_KEY")
					}
				}
			}

			// Create empty jar for javadoc classifier to satisfy maven requirements
			val stubJavadoc by tasks.registering(Jar::class) {
				archiveClassifier.set("javadoc")
			}

			publications.withType<MavenPublication> {
				pom {
					withXml {
						with(asNode()) {
							appendNode("name", project.name)
							appendNode("description", project.description)
							appendNode("url", project.properties["vcs"])
						}
					}
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

				// Add empty javadocs (no need for MPP root publication which publishes only pom file)
				if (name != "kotlinMultiplatform") {
					artifact(stubJavadoc.get())
				}

				// Disable metadata everywhere, but in native modules
				if (name == "metadata" || name == "jvm" || name == "js") {
					this as DefaultMavenPublication
					setModuleDescriptorGenerator(null)
				}
			}

			// TODO :kludge this is required for K/N publishing
			tasks.named("publish") {
				dependsOn("publishToMavenLocal")
			}
		}
	}
}
