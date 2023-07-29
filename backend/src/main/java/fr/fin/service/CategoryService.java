package fr.fin.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Category;
import fr.fin.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * Fetch all categories and order them by ascending identifier
	 *
	 * @return List<Category> containing the categories
	 */
	public List<Category> getAllCategories() {
		return categoryRepository.findAllByOrderById();
	}

	/**
	 * Get a category by id and return it if it exists
	 *
	 * @param id	The id of the category
	 * @return		The category if it exists
	 */
	public Category getCategoryById(Integer id) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isPresent()) {
			return category.get();
		}
		return null;
	}

	/**
	 * Creates a new category and populate it in database
	 *
	 * @param category	A Category entity that represents the object we add
	 * @return			The newly created category
	 */
	public Category createNewCategory(Category category) {
		category.setStatus(true);
		category.setCreatedBy("Administrator");
		category.setCreatedAt(new Date());
		return categoryRepository.save(category);
	}

	public boolean checkIfCategoryExistsByName(String name) {
		Category category = categoryRepository.findCategoryByNameIgnoreCase(name);
		return !(category == null);
	}

	/**
	 * Patch category status. It becomes true if it's false (inverse works)
	 *
	 * @param id	The id of the category to patch
	 * @return		The patched category
	 */
	public Category patchCategoryStatus(Integer id) {
		Category category = getCategoryById(id);
		if (category != null) {

			// Ternary operation
			category.setStatus(category.getStatus() == true ? false : true);

			category.setUpdatedAt(new Date());
			category.setUpdatedBy("Administrator");
			return categoryRepository.save(category);
		}
		return null;
	}

	/**
	 * Patch category name given an identifier.
	 *
	 * @param id	The id of the category to patch
	 * @param newName	The new name to set
	 * @return		The patched category
	 */
	public Category patchCategoryName(Integer id, String newName) {
		Category category = getCategoryById(id);

		if (category != null) {
			category.setName(newName);
			category.setUpdatedAt(new Date());
			category.setUpdatedBy("Administrator");
			return categoryRepository.save(category);
		}

		return null;
	}

	/**
	 * Delete a category given it's identifier
	 *
	 * @param id	The id of the category to delete
	 * @return		True if the category has been successfully deleted
	 */
	public boolean deleteCategoryById(Integer id) {
		Category category = this.getCategoryById(id);
		if (category != null) {
			categoryRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
