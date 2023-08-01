package fr.fin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;


@Repository
public interface BasketDetailRepository extends CrudRepository<BasketDetail, Integer>{

	List<BasketDetail> findByBasketEquals(Basket basket);
}
