package fr.fin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

import fr.fin.model.entity.Category;
import fr.fin.model.entity.Product;
import fr.fin.repository.CategoryRepository;

@SpringBootTest
class CategoryServiceTests {

	@MockBean
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Test
	void Given2Categories_WhenGetAllCategories_ThenReturnAListOfThem() {

		// Arrange
		List<Category> mockList = new ArrayList<>();
		mockList.add(new Category(1, "Pains", true, "Administrateur", new Date(), false));
		mockList.add(new Category(2, "Viennoiseries", true, "Administrateur", new Date(), false));

		when(categoryRepository.findAllByDeletedFalseOrderById()).thenReturn(mockList);

		// Execute
		List<Category> results = categoryService.getAllCategories();

		// Assert
		assertThat(results.get(0).getName()).isEqualTo("Pains");
		assertThat(results.get(1).getName()).isEqualTo("Viennoiseries");
	}

	@Test
	void givenACategoryIdAndDeletedIsFalse_WhenFindById_ShouldReturnExistingCategory() {

		// Arrange
		Category category = new Category(1, "Pains", true, "Administrateur", new Date(), false);
		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		// Execute
		Category result = categoryService.getCategoryById(1);

		// Assert
		assertThat(result.getName()).isEqualTo("Pains");
	}

	@Test
	void givenACategoryIdAndDeletedIsTrue_WhenFindById_ShouldReturnNull() {

		// Arrange
		Category category = new Category(1, "Pains", true, "Administrateur", new Date(), true);
		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		// Execute
		Category result = categoryService.getCategoryById(1);

		// Assert
		assertThat(result).isNull();
	}

	@Test
	void givenACategoryId_WhenFindById_ShouldReturnNull() {

		// Arrange
		when(categoryRepository.findById(2)).thenReturn(Optional.ofNullable(null));

		// Execute
		Category result = categoryService.getCategoryById(2);

		// Assert<
		assertThat(result).isNull();
	}

	@Test
	void givenACategory_WhenCreateNewCategory_ThenReturnNewCategory() {

		// Arrange
		Category category = new Category();
		category.setId(1);
		category.setName("chOcoLATS     ");
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		// Execute
		Category result = categoryService.createNewCategory(category);

		// Assert
		assertThat(result.getName()).isEqualTo("Chocolats");
		assertThat(result.getStatus()).isTrue();
	}

	@Test
	void givenARightName_WhenCheckIfCategoryExistsByName_ThenReturnTrue() {

		// Arrange
		Category category = new Category();
		category.setName("Patisseries");
		when(categoryRepository.findCategoryByNameIgnoreCaseAndDeletedFalse("Patisseries")).thenReturn(category);

		// Execute
		boolean result = categoryService.checkIfCategoryExistsByName("Patisseries");

		// Assert
		assertThat(result).isTrue();
	}

	@Test
	void givenAWrongName_WhenCheckIfCategoryExistsByName_ThenReturnFalse() {

		// Arrange
		when(categoryRepository.findCategoryByNameIgnoreCaseAndDeletedFalse(anyString())).thenReturn(null);

		// Execute
		boolean result = categoryService.checkIfCategoryExistsByName("inconnu");

		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void givenStatusFalse_WhenPatchCategoryStatus_ShouldBeTrue() {

		// Arrange
		Category category = new Category(1, "Pains", false, "Administrateur", new Date(), false);
		when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		// Execute
		Category result = categoryService.patchCategoryStatus(1);

		// Assert
		assertThat(result.getStatus()).isTrue();
	}

	@Test
	void givenStatusTrue_WhenPatchCategoryStatus_ShouldBeFalse() {

		// Arrange
		Category category = new Category(1, "Gateaux", true, "Administrateur", new Date(), false);
		when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		// Execute
		Category result = categoryService.patchCategoryStatus(1);

		// Assert
		assertThat(result.getStatus()).isFalse();
	}

	@Test
	void givenNewName_WhenPatchCategoryName_ShouldBeUpdated() {

		// Arrange
		Category category = new Category(1, "Gateaux", true, "Administrateur", new Date(), false);
		when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		// Execute
		Category result = categoryService.patchCategoryName(1, "Bonbons");

		// Assert
		assertThat(result.getName()).isEqualTo("Bonbons");
	}

	@Test
	void givenCategoryIdWithProducts_WhenDeleteCategoryById_ShouldCallSaveMethod() {

		// Arrange
		Category category = new Category(10, "Gateaux", true, "Administrateur", new Date(), false);
		category.setProducts(List.of(new Product()));
		when(categoryRepository.findById(10)).thenReturn(Optional.ofNullable(category));

		// Execute
		boolean result = categoryService.deleteCategoryById(10);

		// Assert
		assertThat(result).isTrue();
		verify(categoryRepository, times(1)).save(category);
	}

	@Test
	void givenCategoryIdWithNoProducts_WhenDeleteCategoryById_ShouldCallDeleteMethod() {

		// Arrange
		Category category = new Category(10, "Gateaux", true, "Administrateur", new Date(), false);
		category.setProducts(new ArrayList<>());
		category.setInactiveProducts(new ArrayList<>());

		when(categoryRepository.findById(10)).thenReturn(Optional.ofNullable(category));
		doNothing().when(categoryRepository).deleteById(10);

		// Execute
		boolean result = categoryService.deleteCategoryById(10);

		// Assert
		assertThat(result).isTrue();
		verify(categoryRepository, times(1)).deleteById(10);
	}

	@Test
	void givenNullCategory_WhenDeleteCategoryById_ShouldReturnFalse() {

		// Arrange
		when(categoryRepository.findById(10)).thenReturn(Optional.ofNullable(null));

		// Execute
		boolean result = categoryService.deleteCategoryById(10);

		// Assert
		assertThat(result).isFalse();
	}

}
