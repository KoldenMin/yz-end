package com.example.yz1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.yz1.mapper")
public class Yz1Application {

    public static void main(String[] args) {
        SpringApplication.run(Yz1Application.class, args);
    }

}
