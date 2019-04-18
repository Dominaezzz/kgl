pluginManagement {
	repositories {
		jcenter()
		mavenCentral()
		maven(url = "https://plugins.gradle.org/m2/")
		maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
		maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
	}

	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "kotlin-multiplatform") {
				useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
			}
			if (requested.id.id == "com.jfrog.bintray") {
				useModule("com.jfrog.bintray.gradle:gradle-bintray-plugin:${requested.version}")
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
