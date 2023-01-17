package com.example.sprinkler.apiserver;

import org.modelmapper.ModelMapper;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class    ApiServerApplication {
	@Bean
	public ModelMapper modelMapper() {

		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}

}
