package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HelloController {

	@Value("${helloworld.message:HelloWorldNotSet}")
	private String helloWorld;

	@Value("${mypassword}")
	private String password;

	@Autowired
    private DiscoveryClient discoveryClient;

	@GetMapping("/")
	public String index() {

		log.info("--- Finding service discovery URI");
		discoveryClient.getServices().stream().forEach(System.err::println);
		log.info("--- Found services :  "+(!discoveryClient.getInstances("appapi").isEmpty()));

		String serviceURI = discoveryClient.getInstances("appapi").stream().findFirst().get().getUri().toString();
		
		log.info(">>>>>> Making request to 'appapi' : {} ",serviceURI);		

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(serviceURI , String.class);

		log.info("<<<<  Response '{}' ",response);		

		return response.getBody();
	}

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello message from config service > "+helloWorld;
	}

	@GetMapping("/password")
	public String password() {
		return password;
	}

}
