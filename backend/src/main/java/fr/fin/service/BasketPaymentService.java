package fr.fin.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.repository.BasketRepository;


@Service
public class BasketPaymentService {
	
	private Integer basketId;
	
	@Autowired
	private BasketDetailService basketDetailService;
	
	@Autowired
	private PaymentService paymentService;	
	
	@Autowired
	private BasketRepository basketRepository;
	
	public Integer createBasket(Basket basket) {
		
		Basket basketToSave = basketRepository.save(basket);
		basketId = basketToSave.getBasketId();
		/*
		List<BasketDetail> listDetailInsert = basketDetailService.insertBasketDetails(basket.getBasketDetails(), basketId);
		List<Payment> listPaymentInsert = paymentService.insertPayments(basket, basketId);
		*/
		basketDetailService.insertBasketDetails(basket.getBasketDetails(), basketId);
		paymentService.insertPayments(basket, basketId);
		
		return basketId;
		
	}

	public Basket getBasket(Integer id) {
		System.err.println(id);
		Basket basket = new Basket();
		basket.setBasketId(id);
		List<Payment> listPayment = paymentService.getBasketById(basket);
		for (Payment payment : listPayment) {
			System.out.println(payment.getType());
		}
		List<BasketDetail> basketDetail = basketDetailService.getBasketById(basket);
		//Optional<Basket> basketLoaded = basketRepository.findById(id);
		/*if(basketLoaded.isPresent()) {
			System.out.println("dans le if");
			return basketLoaded.get();
		}else {*/
			return null;
		//}
		
	}
	
	
}
