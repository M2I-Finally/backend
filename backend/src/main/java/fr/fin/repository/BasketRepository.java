package fr.fin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.Staff;

@Repository

public interface BasketRepository extends CrudRepository<Basket, Integer>{

	List<Basket> findByStaffIdAndCreatedAtIsAfter(Integer staffId, Date date);
		
}
