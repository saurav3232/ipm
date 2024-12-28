package com.interactive_pom.ipm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.interactive_pom.ipm.*")
public class IpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpmApplication.class, args);
	}

}
