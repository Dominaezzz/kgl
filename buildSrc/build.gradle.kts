plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
	idea
}

repositories {
	jcenter()
	mavenCentral()
}

dependencies {
	implementation(group = "com.squareup", name = "kotlinpoet", version = "1.5.0")
	implementation(group = "com.beust", name = "klaxon", version = "5.0.5")
	implementation(group = "org.jsoup", name = "jsoup", version = "1.11.3")
}
