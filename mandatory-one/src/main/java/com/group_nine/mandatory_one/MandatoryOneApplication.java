package com.group_nine.mandatory_one;

import java.util.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
