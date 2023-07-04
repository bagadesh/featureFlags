plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    id("kotlin")
    id("maven-publish")
}



java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8


}

tasks.withType(JavaCompile::class.java) {
    targetCompatibility = "1.8"
    sourceCompatibility = "1.8"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}


dependencies {
    implementation(libs.kotlinx.coroutines.core)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
}

afterEvaluate {
    publishing {
        publications {
            register("mavenJava", MavenPublication::class.java) {
                from(components["java"])
                artifact(sourcesJar.get())
            }
        }
    }
}