package com.example.demo.controller;


import com.example.demo.services.TestBClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {


	@Autowired Environment environment;

	@Autowired TestBClient testBclient;


	@GetMapping("/status/all")
	public String statusAll(){
		String responseB = testBclient.getStatus();
		return "This microservice is listening on port: " + environment.getProperty("local.server.port") +
				"<br>  Microservice B is " + responseB;
	}



	@GetMapping("/status")
	public String status(){
		return "Listening on port: " + environment.getProperty("local.server.port");
	}


}
