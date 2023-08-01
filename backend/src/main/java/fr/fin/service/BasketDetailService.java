package fr.fin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.repository.BasketDetailRepository;

@Service
public class BasketDetailService {
	
	@Autowired
	private BasketDetailRepository basketDetailRepository;
	
	public List<BasketDetail> insertBasketDetails(List<BasketDetail> listBasketDetail, Integer basketId){
		
		
		for (BasketDetail basketDetail : listBasketDetail) {
			basketDetail.setBasketDetailId(null);
			basketDetail.getBasket().setBasketId(basketId);			
			
		}
		
		List<BasketDetail> listBasketDetailUpdate = (List<BasketDetail>) basketDetailRepository.saveAll(listBasketDetail);
		
		return listBasketDetailUpdate ;
	}
	
	public List<BasketDetail> getBasketById(Basket basket){
		return basketDetailRepository.findByBasketEquals(basket);
	}
}
