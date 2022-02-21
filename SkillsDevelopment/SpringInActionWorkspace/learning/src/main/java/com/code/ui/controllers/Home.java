package com.code.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {

	@GetMapping("/")
	public String home() {
		return "main/Home";
	}
}
