package fr.fin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.fin.model.entity.Category;
import fr.fin.model.entity.Product;
import fr.fin.repository.ProductRepository;

@SpringBootTest
public class ProductServiceTest {

	@MockBean
	private ProductRepository productRepository;
	
	@Autowired
	private ProductService productService;
	
	@Test
	void givenProducts_WhenGetAllProducts_ShouldReturnAListOfThem() {
		List<Product> mocklist = new ArrayList<Product>();
		mocklist.add(new Product(1, "Baguette", 0.9d, new Category(1, "Pains", true, "Administrateur", new Date()), true));
		mocklist.add(new Product(2, "Croissant", 0.7d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true));
		mocklist.add(new Product(3, "Café", 1.5d, new Category(3, "Boisson chaude", true, "Administrateur", new Date()), true));
		
		when(productRepository.findAllByOrderByProductId()).thenReturn(mocklist);
		
		List<Product> products = productService.getAllProducts();
		
		assertThat(products.get(0).getName()).isEqualTo("Baguette");
		assertThat(products.get(1).getName()).isEqualTo("Croissant");
		assertThat(products.get(2).getName()).isEqualTo("Café");
	}
	
	@Test
	void givenProducts_WhenGetAllProducts_ShouldReturnAllOfThem() {
		List<Product> mocklist = new ArrayList<Product>();
		mocklist.add(new Product(1, "Baguette", 0.9d, new Category(1, "Pains", true, "Administrateur", new Date()), true));
		mocklist.add(new Product(2, "Croissant", 0.7d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true));
		mocklist.add(new Product(3, "Café", 1.5d, new Category(3, "Boisson chaude", true, "Administrateur", new Date()), true));
		
		when(productRepository.findAllByOrderByProductId()).thenReturn(mocklist);
		
		List<Product> products = productService.getAllProducts();
		
		assertThat(products.size()).isEqualTo(3);
	}
	
	@Test
	void givenProductId_WhenFindById_ShouldReturnExistingProduct() {
		Product product = new Product(1, "Baguette", 0.9d, new Category(1, "Pains", true, "Administrateur", new Date()), true);
		when(productRepository.findById(1)).thenReturn(Optional.of(product));
		
		Product result = productService.getProductById(1);
		
		assertThat(result.getName()).isEqualTo("Baguette");
	}
	
	@Test
	void givenNotExistingProductId_WhenFindById_ShouldReturnNull() {
		when(productRepository.findById(2)).thenReturn(Optional.ofNullable(null));
		
		Product result = productService.getProductById(2);
		
		assertThat(result).isNull();
	}
	
	@Test
	void givenCategoryId_WhenFindProductByCategoryId_ShouldReturnProductsOfTheCategory() {
		List<Product> mocklist = new ArrayList<Product>();
		mocklist.add(new Product(1, "Petit pain", 0.9d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true));
		mocklist.add(new Product(2, "Croissant", 0.7d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true));
		
		when(productRepository.findAllByCategoryId(2)).thenReturn(mocklist);
		
		List<Product> products = productService.getProductsByCategory(2);
		
		assertThat(products.get(0).getCategory().getId()).isEqualTo(2);
		assertThat(products.get(1).getCategory().getId()).isEqualTo(2);
	}
	
	@Test
	void givenCategoryId_WhenFindProductByCategoryId_ShouldReturnAllProductsOfTheCategory() {
		List<Product> mocklist = new ArrayList<Product>();
		mocklist.add(new Product(1, "Petit pain", 0.9d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true));
		mocklist.add(new Product(2, "Croissant", 0.7d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true));
		
		when(productRepository.findAllByCategoryId(2)).thenReturn(mocklist);
		
		List<Product> products = productService.getProductsByCategory(2);
		
		assertThat(products.size()).isEqualTo(2);
	}
	
	@Test
	void givenProduct_WhenCreateNewProduct_ShouldReturnNewProduct() {
		Product product = new Product();
		product.setProductId(1);
		product.setName("Fraisier");
		
		when(productRepository.save(product)).thenReturn(product);
		
		Product result = productService.createProduct(product);
		
		assertThat(result.getName()).isEqualTo("Fraisier");
	}
	
	@Test
	void givenProductId_WhenDelete_ShouldReturnTrue() {
		Product product = new Product(1, "Petit pain", 0.9d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true);
		
		when(productRepository.findById(1)).thenReturn(Optional.of(product));
		doNothing().when(productRepository).deleteById(1);
		
		boolean result = productService.delete(1);
		
		assertThat(result).isTrue();
	}
	
	@Test
	void givenStatusFalse_WhenPatchProductStatus_ShouldBeTrue() {
		Product product = new Product(1, "Petit pain", 0.9d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), false);		
		when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
		when(productRepository.save(product)).thenReturn(product);

		Product result = productService.updateProductStatus(1);

		assertThat(result.getStatus()).isTrue();
	}
	
	@Test
	void givenStatusTrue_WhenPatchProductStatus_ShouldBeFalse() {
		Product product = new Product(1, "Petit pain", 0.9d, new Category(2, "Viennoiserie", true, "Administrateur", new Date()), true);		
		when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
		when(productRepository.save(product)).thenReturn(product);

		Product result = productService.updateProductStatus(1);

		assertThat(result.getStatus()).isFalse();
	}	
}
