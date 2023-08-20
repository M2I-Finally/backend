package fr.fin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;
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
	
	/**insert a new basket in database 
	 * when repository give new basketId :
	 * -> populate the association tables (basketId - productId)
	 * -> insert new paiement	
	 * 
	 * @param basket
	 * @return Provides the ID of the newly inserted basket in the database.
	 */
	public Integer createBasket(Basket basket) {
		
		Basket basketToSave = basketRepository.save(basket);
		basketId = basketToSave.getBasketId();
		if (basketId > 0 ) {
			basketDetailService.insertBasketDetails(basket.getBasketDetails(), basketId);
			paymentService.insertPayments(basket, basketId);
		}else {
			return null;
		}
		
		
		return basketId;
	}
	
	
}
