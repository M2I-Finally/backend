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
	
	public List<Product> getAllProducts() {
		return (List<Product>) productRepository.findAll();
	}
	
	public Product createProduct(Product product) {
		Product newProduct = productRepository.save(product);
		return newProduct;
	}
	
	public Product getProductById(Integer id) {
		if( productRepository.findById(id).isPresent() ) {
			return productRepository.findById(id).get();			
		}
		return null;
	}
	
	public void delete(Integer id) {		
		productRepository.deleteById(id);
	}
	
	public Product updateProductStatus(Integer id) {
		Product updatedProduct = this.getProductById(id);
		if( updatedProduct.getStatus() == true ) {
			updatedProduct.setStatus(false);
		} else {
			updatedProduct.setStatus(true);
		}
		productRepository.save(updatedProduct);
		return updatedProduct;
	}
}
