package com.code.integration.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.code.integration.entities.Taco;

@Service
public class TacosClient {
	RestTemplate rest = new RestTemplate();
	
	public Taco getTacoById(long id) {
		return rest.getForObject("http://localhost:8080/tacos/design/{id}",
				Taco.class, id);
	}
}
