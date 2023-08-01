package fr.fin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer>{
	
	List<Payment> findByBasketEquals(Basket basket);
	
}
