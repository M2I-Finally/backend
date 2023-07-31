package fr.fin.repository;

import org.springframework.data.repository.CrudRepository;

import fr.fin.model.entity.Basket;


public interface BasketRepository extends CrudRepository<Basket, Integer>{

}
