package fr.fin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.repository.BasketDetailRepository;
import fr.fin.repository.BasketRepository;
import fr.fin.repository.PaymentRepository;

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
		List<BasketDetail> listDetailInsert = basketDetailService.insertBasketDetails(basket.getBasketDetails(), basketId);
		List<Payment> listPaymentInsert = paymentService.insertPayments(basket, basketId);
		//System.out.println(basketToSave);
		return basketId;
	}
	
	
}
