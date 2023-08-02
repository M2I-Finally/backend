package fr.fin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import fr.fin.model.entity.Product;

@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;
	
	@Test
	@Sql("findAllByOrderByProductId_ShouldReturn_OrderedProducts.sql")
	void findAllByOrderByProductId_ShouldReturn_OrderedProducts() {
		List<Product> products = productRepository.findAllByOrderByProductId();
		assertThat(products.get(0).getProductId()).isEqualTo(4);
		assertThat(products.get(1).getProductId()).isEqualTo(5);
		assertThat(products.get(2).getProductId()).isEqualTo(6);
	}
	
	@Test
	@Sql("findAllByCategoryId_ShouldReturn_ProductsOfGivenCategory.sql")
	void findAllByCategoryId_ShouldReturn_ProductsOfGivenCategory() {
		List<Product> products = productRepository.findAllByCategoryId(1);
		assertThat(products.size()).isEqualTo(2);
	}
}
