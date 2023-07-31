package fr.fin.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import fr.fin.model.entity.Category;

@DataJpaTest
class CategoryRepositoryTests {

	@Autowired 
	private CategoryRepository categoryRepository;
	
	@Test
	@Sql("test.sql")
	void findAllOrderById_ShouldReturn_OrderedCategories() {
		List<Category> categories = categoryRepository.findAllByOrderById();
		System.out.println(categories.size());
	}

}
