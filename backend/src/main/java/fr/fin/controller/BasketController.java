package fr.fin.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.fin.model.dto.BasketDto;
import fr.fin.model.entity.Basket;
import fr.fin.service.BasketService;

@RestController
public class BasketController {
	
	@Autowired
	private BasketService basketService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * Basket will be recorded in database
	 * validation: at least one line in basket, at least one method of payment.
	 * @param basketDto
	 * @return
	 */
	@PostMapping("/payment")
	public ResponseEntity<BasketDto> addProduct(@RequestBody BasketDto basketDto) {
		if( basketDto != null && basketDto.getBasketDetails().size()>0 && basketDto.getPayments().size()>0 ) {
			basketService.createBasket(convertToGestionEntity(basketDto));
			return new ResponseEntity<BasketDto>(basketDto, HttpStatus.CREATED);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur dans la requête");
	}
	
	private Basket convertToGestionEntity(BasketDto basketDto) {
		return modelMapper.map(basketDto, Basket.class);
	}
}
