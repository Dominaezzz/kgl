plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
	idea
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.squareup:kotlinpoet:1.6.0")
	implementation("com.beust:klaxon:5.2")
	implementation("org.jsoup:jsoup:1.13.1")
}
