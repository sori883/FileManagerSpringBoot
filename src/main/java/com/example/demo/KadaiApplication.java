package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.example.demo.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class KadaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KadaiApplication.class, args);
	}

}
