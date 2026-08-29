package com.datalyze.alquileres.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SistemaAlquileresApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaAlquileresApplication.class, args);
    }

}
