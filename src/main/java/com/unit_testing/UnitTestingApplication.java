package com.unit_testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class UnitTestingApplication {

    public static void main(String[] args) {

        System.out.println(SpringVersion.getVersion());
        SpringApplication.run(UnitTestingApplication.class, args);
    }

}
