package com.doubleowner.revibe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReVibeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReVibeApplication.class, args);
    }

}