package com.checkcode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages="com.checkcode.dao")
@EnableScheduling
@EnableAsync
public class CheckCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckCodeApplication.class, args);
    }

}
