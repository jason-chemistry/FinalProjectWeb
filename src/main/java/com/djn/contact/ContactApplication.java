package com.djn.contact;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.djn.contact.mapper")
@ServletComponentScan(basePackages = "com.djn.contact.listener")
public class ContactApplication {


    public static void main(String[] args) {
        SpringApplication.run(ContactApplication.class, args);
    }

}
