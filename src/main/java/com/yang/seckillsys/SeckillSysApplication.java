package com.yang.seckillsys;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.yang.seckillsys.mapper")
public class SeckillSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillSysApplication.class, args);
    }

}
