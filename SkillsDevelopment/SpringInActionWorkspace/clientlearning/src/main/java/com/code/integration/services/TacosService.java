package com.code.integration.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.integration.clients.TacosClient;
import com.code.integration.entities.Taco;

@RestController
@RequestMapping(path = "/design", produces =  MediaType.APPLICATION_JSON_VALUE)
public class TacosService {

	@Autowired
	TacosClient tacosClient;
	
	@GetMapping("{id}")
	public Taco tacoById(@PathVariable("id") long id) {
		return tacosClient.getTacoById(id);
	}
}
