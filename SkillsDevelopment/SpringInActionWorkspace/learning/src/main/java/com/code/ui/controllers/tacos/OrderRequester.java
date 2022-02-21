package com.code.ui.controllers.tacos;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.code.dal.entities.Order;
import com.code.dal.repositories.OrderRepository;

@Controller
@RequestMapping("/OrderRequester")
@SessionAttributes("order")
public class OrderRequester {

	@Autowired
	private OrderRepository orderRepo;

	@GetMapping
	public String orderForm(Model model) {
		return "tacos/OrderRequester";
	}

	@PostMapping("/execute")
	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
		if (errors.hasErrors()) {
			return "tacos/OrderRequester";
		}
		
		orderRepo.save(order);
	    sessionStatus.setComplete();
	    
		return "redirect:/";
	}
}
