package com.ecs.ecs_product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EcsProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcsProductApplication.class, args);
	}

}
