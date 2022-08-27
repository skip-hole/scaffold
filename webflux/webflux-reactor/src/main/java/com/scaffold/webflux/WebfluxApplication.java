package com.scaffold.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class WebfluxApplication {

	public static void main(String[] args) {
		Hooks.onOperatorDebug();
		SpringApplication.run(WebfluxApplication.class, args);
	}

}
