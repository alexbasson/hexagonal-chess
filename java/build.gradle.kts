plugins {
    java
    id("io.spring.dependency-management") version "1.1.6" apply false
    id("org.springframework.boot") version "3.3.5" apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    group = "com.hexagonalchess"
    version = "0.0.1-SNAPSHOT"

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.5")
        }
    }

    dependencies {
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
