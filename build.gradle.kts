import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.16.2")
    }
}

plugins {
    java
    `maven-publish`

    kotlin("jvm") version "1.5.21"
}

plugins.apply("kotlinx-atomicfu")

group = "fun.dimensional"
version = "1.0.1"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val sourcesJar = task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allJava)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}

tasks.build {
    dependsOn(tasks.jar)
    dependsOn(sourcesJar)
}

tasks.publish {
    dependsOn(tasks.build)
    onlyIf {
        (!System.getenv("JFROG_USERNAME").isNullOrBlank()
            && !System.getenv("JFROG_PASSWORD").isNullOrBlank()) ||
        (!System.getProperty("jfrog-username").isNullOrBlank()
            && !System.getProperty("jfrog-password").isNullOrBlank())
    }
}

publishing {
    repositories {
        maven("https://dimensional.jfrog.io/artifactory/maven") {
            name = "jfrog"
            credentials {
                username = System.getenv("JFROG_USERNAME") ?: System.getProperty("jfrog-username")
                password = System.getenv("JFROG_PASSWORD") ?: System.getProperty("jfrog-password")
            }
        }
    }

    publications {
        create<MavenPublication>("jfrog") {
            from(components["java"])

            group = project.group as String
            version = project.version as String
            artifactId = "cuid"

            pom {
                name.set("cuid")
                description.set("Kotlin implementation of CUIDs")
                url.set("https://github.com/dimensional-fun/cuid")

                organization {
                    name.set("Dimensional Fun")
                    url.set("https://dimensional.fun")
                }

                developers {
                    developer {
                        name.set("melike2d")
                    }
                }

                licenses {
                    license {
                        name.set("Apache 2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }
            }

            artifact(sourcesJar)
        }
    }
}
