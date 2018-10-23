package com.imooc.snapup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author taohong on 09/10/2018
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@ServletComponentScan //配置druid必须加的注解，如果不加，访问页面打不开，filter和servlet、listener之类的需要单独进行注册才能使用，spring boot里面提供了该注解起到注册作用
public class  MainApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }
}
