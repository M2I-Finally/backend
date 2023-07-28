package fr.fin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Basket;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Integer>{

}
