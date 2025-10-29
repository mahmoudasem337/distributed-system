package com.asem.grpc_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.asem.grpc_service.ServiceImpl")
@ComponentScan(basePackages = "com.asem.grpc_service")
public class GrpcServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcServiceApplication.class, args);
	}

}
