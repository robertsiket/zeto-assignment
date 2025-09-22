package org.zeto.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The ApplicationConfiguration class serves as the entry point for the Spring Boot application.
 * It is annotated with @SpringBootApplication, which is a convenience annotation that adds
 * <p>
 * This class contains the main method that launches the entire application by invoking the SpringApplication.run method.
 * <p>
 * The primary purpose of this class is to bootstrap and initialize the Spring application context.
 */
@SpringBootApplication
public class ApplicationConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }
}
