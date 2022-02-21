package com.code.integration.entities;

import java.util.Date;

import lombok.Data;

@Data
public class Taco {
	
	private Long id;
		
	private String name;

	private Date insertedAt;

	private Long version;
}
