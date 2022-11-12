package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HelloController {


	private static int helloRequestCount;
	private static int passwordRequestCount;
	private static int yodaQuoteRequestCount;

	@Value("${helloworld.message:HelloWorldNotSet}")
	private String hello;

	@Value("${mypassword}")
	private String password;

	@Autowired
    private DiscoveryClient discoveryClient;

	@GetMapping("/yoda")
	public String yoda(Model model) {
		String message = "Whoopsie daisies.... couldn't connect to the API server";
		try{
			model.addAttribute("message", message);

			log.info("--- Finding service discovery URI");
			discoveryClient.getServices().stream().forEach(System.err::println);
			log.info("--- Found services :  "+(!discoveryClient.getInstances("appapi").isEmpty()));

			String serviceURI = discoveryClient.getInstances("appapi").stream().findFirst().get().getUri().toString();
			
			log.info(">>>>>> Making request to 'appapi' : {} ",serviceURI);		

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.getForEntity(serviceURI , String.class);

			log.info("<<<<  Response '{}' ",response);		

			message = response.getBody();
			model.addAttribute("message", "["+(++yodaQuoteRequestCount)+"] "+message);

		} catch (Throwable t){
			log.error("Unable to get make request to the API",t);
		}
		return "yoda";
	}

	@GetMapping("/hello")
	public String hello(Model model) {
		model.addAttribute("message", "["+(++helloRequestCount)+"] "+hello);
		return "hello";
	}

	@GetMapping("/password")
	public String password(Model model) {
		model.addAttribute("message", "["+(++passwordRequestCount)+"] "+password);
		return "password";
	}

}
