package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;


@RestController
public class HelloController {

	@Value("${helloworld.message:HelloWorldNotSet}")
	private String helloWorld;

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot! : "+helloWorld;
	}

}
