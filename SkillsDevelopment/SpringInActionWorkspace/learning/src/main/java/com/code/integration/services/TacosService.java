package com.code.integration.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.business.taco.AMSessionTacoBusiness;
import com.code.dal.entities.Taco;
import com.code.exceptions.BusinessException;

@RestController
@RequestMapping(path = "/design", produces =  MediaType.APPLICATION_JSON_VALUE)

public class TacosService {

	@Autowired
	AMSessionTacoBusiness tacoBusiness;

	@GetMapping("/recent")
	public List<Taco> getRecentTacos() {
		try {
			return tacoBusiness.getRecentTacos();
		} catch (BusinessException e) {
			return null;
		}
	}

	@GetMapping("{id}")
	public Taco tacoById(@PathVariable("id") Long id) {
		try {
			return tacoBusiness.getTacoById(id);
		} catch (BusinessException e) {
			return null;
		}
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Taco postTaco(@RequestBody Taco taco) {
		try {
			return tacoBusiness.addOrUpdateTaco(taco, "USER_ID");
		} catch (BusinessException e) {
			return null;
		}
	}

}
