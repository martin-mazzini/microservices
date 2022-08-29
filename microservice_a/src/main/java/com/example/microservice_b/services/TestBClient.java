package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="microserviceB", fallback = TestBFallbackClient.class)
public interface TestBClient {


	@GetMapping("/test/getStatus")
	public String getStatus();

}


@Component
class TestBFallbackClient implements TestBClient{

	@Autowired Environment environment;

	@Override public String getStatus() {
		return  environment.getProperty("message.status.unavailable");
	}
}

