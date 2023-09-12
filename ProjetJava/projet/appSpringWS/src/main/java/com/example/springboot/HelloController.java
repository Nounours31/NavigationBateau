package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

	@RequestMapping("/hello")
	@GetMapping
	public String index() {
		return "Greetings from Spring Boot!";
	}
	

    @PostMapping
    public String helloName(@RequestBody String name) {
        return "Hello " + name;
    }

}