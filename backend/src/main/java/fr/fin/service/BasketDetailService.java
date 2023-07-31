package fr.fin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.fin.model.entity.BasketDetail;
import fr.fin.repository.BasketDetailRepository;

@Service
public class BasketDetailService {
	
	@Autowired
	private BasketDetailRepository basketDetailRepository;
	
	public List<BasketDetail> insertBasketDetails(List<BasketDetail> listBasketDetail, Integer basketId){
		System.err.println(basketId);
		for (BasketDetail basketDetail : listBasketDetail) {
			System.out.println("avant modif id du basket "+basketDetail.getBasketDetailId());
			
		}
		for (BasketDetail basketDetail : listBasketDetail) {
			basketDetail.setBasketDetailId(null);
			basketDetail.getBasket().setBasketId(basketId);;
			//System.out.println("Apres modif id du basket "+basketDetail);
			
		}
		for (BasketDetail basketDetail : listBasketDetail) {
			System.out.println("Apres modif id du basket "+basketDetail);
			
		}
		List<BasketDetail> listBasketDetailUpdate = (List<BasketDetail>) basketDetailRepository.saveAll(listBasketDetail);
		
		return listBasketDetailUpdate ;
	}
}
