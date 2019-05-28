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
	implementation(group = "com.squareup", name = "kotlinpoet", version = "1.0.1")
}
