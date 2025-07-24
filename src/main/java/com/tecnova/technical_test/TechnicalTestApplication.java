package com.tecnova.technical_test;

import com.tecnova.technical_test.infrastructure.adapter.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TechnicalTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechnicalTestApplication.class, args);
	}

}
