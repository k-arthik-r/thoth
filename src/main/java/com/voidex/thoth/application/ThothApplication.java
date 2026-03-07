package com.voidex.thoth.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@ComponentScan(basePackages = "com.voidex.thoth")
@SpringBootApplication
public class ThothApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ThothApplication.class);

	public static void main(String[] args) {
		LOG.info("Starting Thoth Application...");
		SpringApplication.run(ThothApplication.class, args);
	}

}
