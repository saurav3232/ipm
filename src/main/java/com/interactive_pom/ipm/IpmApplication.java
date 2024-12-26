package com.interactive_pom.ipm;

import com.interactive_pom.ipm.Service.DependencyExtractorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.interactive_pom.ipm.*")
@RequiredArgsConstructor
public class IpmApplication implements CommandLineRunner {

	private final DependencyExtractorServiceImpl dependencyExtractorService;

	public static void main(String[] args) {
		SpringApplication.run(IpmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dependencyExtractorService.extractAllDependencies();
	}
}
