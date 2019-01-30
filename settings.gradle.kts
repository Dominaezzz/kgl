pluginManagement {
	repositories {
		mavenCentral()
		maven(url = "https://plugins.gradle.org/m2/")
		maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
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

include("kgl-core")
include("kgl-glfw")
include("kgl-glfw-vulkan")
include("kgl-vulkan")
include("kgl-opengl")
