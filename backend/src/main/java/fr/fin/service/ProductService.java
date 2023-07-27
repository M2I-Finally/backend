package fr.fin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Product;
import fr.fin.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> getAvailableProducts() {
		return (List<Product>) productRepository.findByStatusTrue();
	}
	
	public List<Product> getAllProducts() {
		return (List<Product>) productRepository.findAll();
	}
	
	public Product createProduct(Product product) {
		Product newProduct = productRepository.save(product);
		return newProduct;
	}
}
