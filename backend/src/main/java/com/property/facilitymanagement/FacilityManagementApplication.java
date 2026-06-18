package com.property.facilitymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FacilityManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacilityManagementApplication.class, args);
    }
}