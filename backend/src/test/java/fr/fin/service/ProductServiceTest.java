package fr.fin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
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

		// Arrange
		List<Product> mocklist = new ArrayList<Product>();

		Product product1 = new Product(1, "Baguette", true, 0.9d, false);
		product1.setCategory(new Category(1, "Pains", true, "Administrateur", new Date(), false));

		Product product2 = new Product(2, "Croissant", true, 0.7d, false);
		product2.setCategory(new Category(2, "Viennoiserie", true, "Administrateur", new Date(), false));

		mocklist.addAll(List.of(product1, product2));
		when(productRepository.findAllByDeletedFalseOrderByProductId()).thenReturn(mocklist);

		// Execute
		List<Product> products = productService.getAllProducts();

		// Assert
		assertThat(products.get(0).getName()).isEqualTo("Baguette");
		assertThat(products.get(1).getName()).isEqualTo("Croissant");
	}

	@Test
	void givenProducts_WhenGetAllProducts_ShouldReturnAllOfThem() {

		// Arrange
		List<Product> mocklist = new ArrayList<Product>();

		Product product1 = new Product(1, "Baguette", true, 0.9d, false);
		product1.setCategory(new Category(1, "Pains", true, "Administrateur", new Date(), false));

		Product product2 = new Product(2, "Croissant", true, 0.7d, false);
		product2.setCategory(new Category(2, "Viennoiserie", true, "Administrateur", new Date(), false));

		mocklist.addAll(List.of(product1, product2));
		when(productRepository.findAllByDeletedFalseOrderByProductId()).thenReturn(mocklist);

		// Execute
		List<Product> products = productService.getAllProducts();

		// Assert
		assertThat(products.size()).isEqualTo(2);
	}

	@Test
	void givenProductIdAndDeletedIsFalse_WhenFindById_ShouldReturnExistingProduct() {

		// Arrange
		Product product = new Product(1, "Baguette", true, 0.9d, false);
		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		/// Execute
		Product result = productService.getProductById(1);

		// Assert
		assertThat(result.getName()).isEqualTo("Baguette");
	}

	@Test
	void givenProductIdAndDeletedIsTrue_WhenFindById_ShouldReturnNull() {

		// Arrange
		Product product = new Product(1, "Baguette", true, 0.9d, true);
		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		/// Execute
		Product result = productService.getProductById(1);

		// Assert
		assertThat(result).isNull();
	}

	@Test
	void givenNotExistingProductId_WhenFindById_ShouldReturnNull() {

		// Arrange
		when(productRepository.findById(2)).thenReturn(Optional.ofNullable(null));

		// Execute
		Product result = productService.getProductById(2);

		// Assert
		assertThat(result).isNull();
	}

	@Test
	void givenCategoryId_WhenFindProductByCategoryId_ShouldReturnProductsOfTheCategory() {

		// Arrange
		List<Product> mocklist = new ArrayList<Product>();
		Category category = new Category(2, "Viennoiserie", true, "Administrateur", new Date(), false);

		Product product1 = new Product(1, "Petit pain", true, 0.9d, false);
		product1.setCategory(category);

		Product product2 = new Product(2, "Croissant", true, 0.7d, false);
		product2.setCategory(category);

		mocklist.addAll(List.of(product1, product2));
		when(productRepository.findAllByDeletedFalseAndCategoryId(2)).thenReturn(mocklist);

		// Execute
		List<Product> products = productService.getProductsByCategory(2);

		// Assert
		assertThat(products.get(0).getCategory().getId()).isEqualTo(2);
		assertThat(products.get(1).getCategory().getId()).isEqualTo(2);
	}

	@Test
	void givenCategoryId_WhenFindProductByCategoryId_ShouldReturnAllProductsOfTheCategory() {

		// Arrange
		List<Product> mocklist = new ArrayList<Product>();
		Category category = new Category(2, "Viennoiserie", true, "Administrateur", new Date(), false);

		Product product1 = new Product(1, "Petit pain", true, 0.9d, false);
		product1.setCategory(category);

		Product product2 = new Product(2, "Croissant", true, 0.7d, false);
		product2.setCategory(category);

		mocklist.addAll(List.of(product1, product2));

		when(productRepository.findAllByDeletedFalseAndCategoryId(2)).thenReturn(mocklist);

		// Execute
		List<Product> products = productService.getProductsByCategory(2);

		// Assert
		assertThat(products.size()).isEqualTo(2);
	}

	@Test
	void givenProduct_WhenCreateNewProduct_ShouldReturnNewProduct() {

		// Arrange
		Product product = new Product();
		product.setProductId(1);
		product.setName("Fraisier");

		when(productRepository.save(product)).thenReturn(product);

		// Execute
		Product result = productService.createProduct(product);

		// Assert
		assertThat(result.getName()).isEqualTo("Fraisier");
	}

	@Test
	void givenProductWithoutBasketDetails_WhenDelete_ShouldCallDeleteMethod() {

		// Arrange
		Product product = new Product(1, "Petit pain", true, 0.9d, false);
		product.setBasketDetails(new ArrayList<>());
		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		// Execute
		boolean result = productService.delete(1);

		// Assert
		assertThat(result).isTrue();
		verify(productRepository, times(1)).delete(product);
	}

	@Test
	void givenProductWithBasketDetails_WhenDelete_ShouldCallSaveMethod() {

		// Arrange
		Product product = new Product(1, "Petit pain", true, 0.9d, false);
		Basket basket = new Basket();
		BasketDetail basketDetail = new BasketDetail(1, 0.2f, basket, product);
		product.setBasketDetails(List.of(basketDetail));
		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		// Execute
		boolean result = productService.delete(1);

		// Assert
		assertThat(result).isTrue();
		verify(productRepository, times(1)).save(product);
	}

	@Test
	void givenProductIsNull_WhenDelete_ShouldReturnFalse() {

		// Arrange
		when(productRepository.findById(1)).thenReturn(Optional.ofNullable(null));

		// Execute
		boolean result = productService.delete(1);

		// Assert
		assertThat(result).isFalse();
		verify(productRepository, never()).delete(any(Product.class));
		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	void givenStatusFalse_WhenPatchProductStatus_ShouldBeTrue() {

		// Arrange
		Product product = new Product(1, "Petit pain", false, 0.9d, false);
		when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
		when(productRepository.save(product)).thenReturn(product);

		// Execute
		Product result = productService.updateProductStatus(1);

		// Assert
		assertThat(result.getStatus()).isTrue();
	}

	@Test
	void givenStatusTrue_WhenPatchProductStatus_ShouldBeFalse() {

		// Arrange
		Product product = new Product(1, "Petit pain", true, 0.9d, false);
		when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
		when(productRepository.save(product)).thenReturn(product);

		// Execute
		Product result = productService.updateProductStatus(1);

		// Assert
		assertThat(result.getStatus()).isFalse();
	}
}
