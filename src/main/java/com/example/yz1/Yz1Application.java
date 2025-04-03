package com.example.yz1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Yz1Application {

    public static void main(String[] args) {
        SpringApplication.run(Yz1Application.class, args);
    }

}
