package fr.fin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Basket;

import fr.fin.repository.BasketRepository;

@Service
public class BasketService {

	@Autowired
	private BasketRepository basketRepository;

	/*
	 * public List<Basket> getAllProducts() { return (List<Basket>)
	 * basketRepository.findAll(); }
	 * 
	 * public Basket createBasket(Basket basket) { Basket newBasket =
	 * basketRepository.save(basket); return newBasket; }
	 * 
	 * public Basket getProductById(Integer id) { if(
	 * basketRepository.findById(id).isPresent() ) { return
	 * basketRepository.findById(id).get(); } return null; }
	 * 
	 * public void delete(Integer id) { basketRepository.deleteById(id); }
	 */

	public List<Basket> getBasketsByStaffId(Integer staff_id) {

		List<Basket> list2 = basketRepository.findByStaffIdAndCreatedAtIsAfter(staff_id, getDateOfCurrentDay());

		return list2;

	}

	private Date getDateOfCurrentDay() {
		LocalDate now = LocalDate.now();
		LocalDateTime starOfDay = now.atStartOfDay();
		Date dateOfDay = Date
				.from(starOfDay.toLocalDate().atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
		return dateOfDay;
	}

}
