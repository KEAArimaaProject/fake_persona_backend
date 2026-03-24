package com.group_nine.mandatory_one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class MandatoryOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(MandatoryOneApplication.class, args);
	}

	@Bean
	public Random random() {
		return new Random();
	}
}
