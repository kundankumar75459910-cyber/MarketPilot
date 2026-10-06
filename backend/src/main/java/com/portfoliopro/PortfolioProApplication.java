package com.portfoliopro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. @SpringBootApplication = @Configuration + @EnableAutoConfiguration
 * + @ComponentScan. It scans this package and sub-packages (controller, service,
 * repository...) and creates the beans that Spring will inject where needed.
 */
@SpringBootApplication
public class PortfolioProApplication {
    public static void main(String[] args) {

        SpringApplication.run(PortfolioProApplication.class, args);
    }
}
