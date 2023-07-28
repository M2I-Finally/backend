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
	
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}
	
	public Category getCategoryById(Integer id) {
		Optional<Category> category = categoryRepository.findById(id);
		if(category.isPresent()) {
			return category.get();
		}
		return null;
	}
	
	public Category createNewCategory(Category category) {
		category.setStatus(true);
		category.setCreatedBy("Administrator");
		category.setCreatedAt(new Date());
		return categoryRepository.save(category);
	}
	
	public Category patchCategoryStatus(Integer id) {
		Category category = this.getCategoryById(id);
		if(category != null) {
			
			// Opération ternaire, si la catégorie est active on la désactive et inversement
			category.setStatus(category.getStatus() == true ? false : true);
			
			// Mis à jour des champs concernant l'update
			category.setUpdatedAt(new Date());
			category.setUpdatedBy("Administrator");
			return categoryRepository.save(category);	
		}
		return null;
	}
	
	public Category patchCategoryName(Integer id, String newName) {
		Category category = this.getCategoryById(id);
		if(category != null) {
			
			category.setName(newName);
			
			// Mis à jour des champs concernant l'update
			category.setUpdatedAt(new Date());
			category.setUpdatedBy("Administrator");
			return categoryRepository.save(category);	
		}
		return null;
	}
	
	public Boolean deleteCategoryById(Integer id) {
		Category category = this.getCategoryById(id);
		if(category != null) {
			categoryRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
}
