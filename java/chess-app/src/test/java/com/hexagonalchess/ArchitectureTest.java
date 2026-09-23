package com.hexagonalchess;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter().importPackages("com.hexagonalchess");

    @Test
    void gameplay_domain_does_not_depend_on_spring() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.gameplay")
            .and().haveSimpleNameNotContaining("Controller")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .because("the gameplay domain must not depend on Spring");
        rule.check(classes);
    }

    @Test
    void organizing_domain_does_not_depend_on_spring() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.organizing")
            .and().haveSimpleNameNotContaining("Controller")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .because("the organizing domain must not depend on Spring");
        rule.check(classes);
    }

    @Test
    void gameplay_domain_does_not_depend_on_organizing_domain() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.gameplay")
            .should().dependOnClassesThat().resideInAPackage("com.hexagonalchess.organizing..")
            .because("bounded contexts must not depend on each other directly");
        rule.check(classes);
    }

    @Test
    void organizing_domain_does_not_depend_on_gameplay_domain() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.organizing")
            .should().dependOnClassesThat().resideInAPackage("com.hexagonalchess.gameplay..")
            .because("bounded contexts must not depend on each other directly");
        rule.check(classes);
    }

    @Test
    void cross_context_adapter_does_not_depend_on_app() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.crosscontext..")
            .should().dependOnClassesThat().resideInAPackage("com.hexagonalchess")
            .because("adapters must not depend on the deployable");
        rule.check(classes);
    }

    @Test
    void user_management_domain_does_not_depend_on_spring() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.usermanagement")
            .and().haveSimpleNameNotContaining("Controller")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .because("the user management domain must not depend on Spring");
        rule.check(classes);
    }

    @Test
    void user_management_does_not_depend_on_gameplay_or_organizing() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.usermanagement")
            .should().dependOnClassesThat()
            .resideInAnyPackage("com.hexagonalchess.gameplay..", "com.hexagonalchess.organizing..")
            .because("bounded contexts must not depend on each other directly");
        rule.check(classes);
    }

    @Test
    void organizing_does_not_depend_on_user_management() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.organizing")
            .should().dependOnClassesThat().resideInAPackage("com.hexagonalchess.usermanagement..")
            .because("bounded contexts must not depend on each other directly");
        rule.check(classes);
    }

    @Test
    void user_management_organizing_adapter_does_not_depend_on_app() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("com.hexagonalchess.usermanagementorganizing..")
            .should().dependOnClassesThat().resideInAPackage("com.hexagonalchess")
            .because("adapters must not depend on the deployable");
        rule.check(classes);
    }
}
