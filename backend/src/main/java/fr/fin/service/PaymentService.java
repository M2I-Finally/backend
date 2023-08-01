package fr.fin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.Payment;
import fr.fin.repository.PaymentRepository;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	public List<Payment> insertPayments(Basket basket, Integer basketId){
		
		for (Payment payment : basket.getPayments()) {
			Basket basket1 = new Basket(basketId, null, null, null, null, null);
			payment.setBasket(basket1);
			payment.setCreatedAt(new Date());
		}
	
		List<Payment> listPaymentUpdate = (List<Payment>) paymentRepository.saveAll(basket.getPayments());
		
		return listPaymentUpdate ;
	}
	
}
