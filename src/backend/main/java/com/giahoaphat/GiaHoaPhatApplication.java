package com.giahoaphat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GiaHoaPhatApplication {
    public static void main(String[] args) {
        SpringApplication.run(GiaHoaPhatApplication.class, args);
        System.out.println("\n==================================================");
        System.out.println("http://localhost:8080");
        System.out.println("==================================================\n");
    }
}
