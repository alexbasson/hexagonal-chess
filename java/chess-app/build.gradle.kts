apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":gameplay-policy"))
    implementation(project(":gameplay-api-adapter"))
    implementation(project(":gameplay-db-adapter"))
    implementation(project(":organizing-policy"))
    implementation(project(":organizing-api-adapter"))
    implementation(project(":organizing-db-adapter"))
    implementation(project(":cross-context-adapter"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
}
