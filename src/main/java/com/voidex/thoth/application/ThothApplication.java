package com.voidex.thoth.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@ComponentScan(basePackages = "com.voidex.thoth")
@SpringBootApplication
public class ThothApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThothApplication.class, args);
	}

}
