package fr.fin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
	
	public List<Product> findAllByOrderByProductId();

}
