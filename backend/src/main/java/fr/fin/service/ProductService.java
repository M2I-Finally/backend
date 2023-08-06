package fr.fin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.fin.model.entity.Product;
import fr.fin.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAllByDeletedFalseOrderByProductId();
	}

	public Product getProductById(Integer id) {
		Optional<Product> product = productRepository.findById(id);
		if(product.isPresent() && !product.get().isDeleted()) {
			return product.get();
		}
		return null;
	}

	public List<Product> getProductsByCategory(Integer id) {
		return productRepository.findAllByDeletedFalseAndCategoryId(id);
	}

	public Product createProduct(Product product) {
		product.setName(trimAndCapitalize(product.getName()));
		product.setDeleted(false);
		return productRepository.save(product);
	}

	public boolean delete(Integer id) {
		Product product = this.getProductById(id);
		if(product != null && !product.isDeleted()) {

			// If product has foreign keys constraint we set the product to "deleted" instead of deleting it really
			if( !product.getBasketDetails().isEmpty()) {
				product.setDeleted(true);
				productRepository.save(product);
				return true;
			} else {
				productRepository.delete(product);
				return true;
			}
		}
		return false;
	}

	public Product updateProductStatus(Integer id) {
		Product updatedProduct = this.getProductById(id);
		if(updatedProduct != null && !updatedProduct.isDeleted()) {
			updatedProduct.setStatus(updatedProduct.getStatus() ? false : true);
			productRepository.save(updatedProduct);
			return updatedProduct;
		}
		return null;
	}

	/**
	 * Trim useless spaces character and capitalize the first letter of the string
	 * @param processedString	The string to process
	 * @return trimmed and capitalized string
	 */
	private String trimAndCapitalize(String processedString) {
		processedString = processedString.trim();
		processedString = StringUtils.capitalize(processedString.toLowerCase());
		return processedString;
	}

}
