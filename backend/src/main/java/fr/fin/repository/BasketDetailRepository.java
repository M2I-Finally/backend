package fr.fin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.BasketDetail;

@Repository
public interface BasketDetailRepository extends CrudRepository<BasketDetail, Integer>{

	//List<BasketDetail> saveAll(List<BasketDetail> baskets);
}
