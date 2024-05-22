package com.example.ratings.orderexporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrderExporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderExporterApplication.class, args);
	}

}
