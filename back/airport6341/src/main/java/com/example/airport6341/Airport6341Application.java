package com.example.airport6341;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan(basePackages = {
	"com.example.controller", 
	"com.example.restcontroller", 
	"com.example.config", 
	"com.example.service", 
	"com.example.handler", 
	"com.example.jwt",
})

@MapperScan(basePackages = "com.example.mapper")
@EntityScan(basePackages = {"com.example.entity"})
@EnableJpaRepositories(basePackages = "com.example.repository")

@SpringBootApplication
public class Airport6341Application extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Airport6341Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(Airport6341Application.class);
	}
}