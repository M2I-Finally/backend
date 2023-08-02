package fr.fin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import fr.fin.model.entity.Product;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;
	
	@Test
	@Sql("findAllByOrderByProductId_ShouldReturn_OrderedProducts.sql")
	void findAllByOrderByProductId_ShouldReturn_OrderedProducts() {
		List<Product> products = productRepository.findAllByOrderByProductId();
		assertThat(products.get(0).getProductId()).isEqualTo(1);
		assertThat(products.get(1).getProductId()).isEqualTo(2);
		assertThat(products.get(2).getProductId()).isEqualTo(3);
	}
	
	@Test
	@Sql("findAllByCategoryId_ShouldReturn_ProductsOfGivenCategory.sql")
	void findAllByCategoryId_ShouldReturn_ProductsOfGivenCategory() {
		List<Product> products = productRepository.findAllByCategoryId(1);
		assertThat(products.size()).isEqualTo(2);
	}
}
