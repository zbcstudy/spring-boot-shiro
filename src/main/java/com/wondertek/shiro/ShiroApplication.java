package com.wondertek.shiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ShiroApplication {

        public static void main(String[] args) {
            SpringApplication.run(ShiroApplication.class);
        }
}
