package com.example.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="microserviceA", fallback = TestAFallbackClient.class)
public interface TestAClient {


	@GetMapping("/test/getStatus")
	public String getStatus();


}

@Component
class TestAFallbackClient implements TestAClient {

	@Autowired Environment environment;

	@Override public String getStatus() {
		return  environment.getProperty("message.status.unavailable");
	}
}

