package fr.fin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fr.fin.model.dto.BasketInsertDto;
import fr.fin.model.dto.BasketRegistredDto;
import fr.fin.service.BasketService;

@Controller
public class BasketController {
	
	@Autowired
	private BasketService basketService;
	
	@PostMapping("/payment")
	public BasketRegistredDto insertBasket(@RequestBody BasketInsertDto basketInsertDto) {
		
		
		return null;
	}
	
}
