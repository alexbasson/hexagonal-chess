apply(plugin = "org.springframework.boot")

dependencies {
    implementation(project(":gameplay-policy"))
    implementation(project(":gameplay-api-adapter"))
    implementation(project(":gameplay-db-adapter"))
    implementation(project(":organizing-policy"))
    implementation(project(":organizing-api-adapter"))
    implementation(project(":organizing-db-adapter"))
    implementation(project(":cross-context-adapter"))
    implementation(project(":user-management-policy"))
    implementation(project(":user-management-api-adapter"))
    implementation(project(":user-management-db-adapter"))
    implementation(project(":user-management-organizing-adapter"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
}
