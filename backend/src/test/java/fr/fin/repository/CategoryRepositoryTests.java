package fr.fin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import fr.fin.model.entity.Category;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	@DisplayName("FindAllByOrderById(), Should return categories ordered by ID")
	@Sql("Category_findAllByDeletedFalseOrderById.sql")
	void findAllOrderById_ShouldReturn_OrderedCategories() {
		List<Category> categories = categoryRepository.findAllByDeletedFalseOrderById();
		assertThat(categories.get(0).getId()).isEqualTo(1);
		assertThat(categories.get(1).getId()).isEqualTo(2);
	}

	@Test
	@DisplayName("FindCategoryByNameIgnoreCase(), should return category")
	@Sql("Category_findCategoryByNameIgnoreCaseAndDeletedFalse.sql")
	void findCategoryByNameIgnoreCase_ShouldReturn_Categories() {
		Category category = categoryRepository.findCategoryByNameIgnoreCaseAndDeletedFalse("bonBONS");
		assertThat(category.getName()).isEqualTo("Bonbons");
	}

}
