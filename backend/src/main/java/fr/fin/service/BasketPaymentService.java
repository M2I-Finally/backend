package fr.fin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;
import fr.fin.repository.BasketRepository;
import fr.fin.repository.PaymentRepository;

@Service
public class BasketPaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private BasketRepository basketRepository;
	
	public void createBasket(Basket basket) {
		Basket basketToSave = basketRepository.save(basket);
		System.out.println(basketToSave);
	}
	
	
}
