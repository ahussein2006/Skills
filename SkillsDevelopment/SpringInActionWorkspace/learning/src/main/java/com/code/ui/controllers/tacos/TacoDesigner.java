package com.code.ui.controllers.tacos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.code.business.taco.AMSessionTacoBusiness;
import com.code.dal.entities.Ingredient;
import com.code.dal.entities.Ingredient.Type;
import com.code.dal.entities.Order;
import com.code.dal.entities.Taco;
import com.code.dal.repositories.IngredientRepository;

@Controller
@RequestMapping("/TacoDesigner")
@SessionAttributes("order")
public class TacoDesigner {

	@Autowired
	private IngredientRepository ingredientRepo;

	@Autowired
//	private TacoBusiness tacoBusiness;
	private AMSessionTacoBusiness tacoBusiness;

	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}

	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	@GetMapping
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));

		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}

		return "tacos/TacoDesigner";
	}

	@PostMapping
	public String processDesign(Taco design, @ModelAttribute Order order, Model model) {
		try {
		tacoBusiness.manageTaco(design, "AHMED");
		order.addDesign(design);
		return "redirect:/OrderRequester";
		} catch (Exception e) {
			System.out.println("processDesign ......");
			System.out.println(e.getMessage());
			return "tacos/TacoDesigner";
		}
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients.stream().filter(x -> x.getType().equals(type.toString())).collect(Collectors.toList());
	}
}
