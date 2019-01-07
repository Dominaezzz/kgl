pluginManagement {
	repositories {
		mavenCentral()
		maven("https://plugins.gradle.org/m2/")
		maven("http://dl.bintray.com/kotlin/kotlin-eap")
	}

	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "kotlin-multiplatform") {
				useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
			}
		}
	}
}

enableFeaturePreview("GRADLE_METADATA")

rootProject.name = "kgl"

include(
		":kgl-core",
		":kgl-glfw",
		":kgl-glfw-vulkan",
		":kgl-vulkan"
)
