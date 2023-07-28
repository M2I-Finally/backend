package fr.fin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;
import fr.fin.repository.BasketRepository;

@Service
public class BasketService {

	@Autowired
	private BasketRepository basketRepository;
	
	public List<Basket> getAllProducts() {
		return (List<Basket>) basketRepository.findAll();
	}
	
	public Basket createBasket(Basket basket) {
		Basket newBasket = basketRepository.save(basket);
		return newBasket;
	}
	
	public Basket getBasketById(Integer id) {
		if( basketRepository.findById(id).isPresent() ) {
			return basketRepository.findById(id).get();			
		}
		return null;
	}
	

}
