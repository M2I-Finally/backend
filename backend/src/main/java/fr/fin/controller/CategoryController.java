package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.fin.model.dto.category.CategoryDto;
import fr.fin.model.dto.category.CreateCategoryDto;
import fr.fin.model.dto.category.UpdateCategoryNameDto;
import fr.fin.model.entity.Category;
import fr.fin.service.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * @return JSON containing all categories
	 */
	@GetMapping("/all")
	public List<CategoryDto> getAllCategories() {
		List<Category> categoriesAsEntity = categoryService.getAllCategories();
		List<CategoryDto> categoriesAsDto = new ArrayList<>();
		
		for(Category c : categoriesAsEntity) {
			categoriesAsDto.add(convertToDto(c));
		}
		
		return categoriesAsDto;
	}
	
	/**
	 * Get a category given an ID
	 * 
	 * @param categoryId	The id of the category to get
	 * @return	JSON containing the category information
	 */
	@GetMapping("/{categoryId}")
	public CategoryDto getCategoryById(@PathVariable("categoryId") Integer categoryId) {
		Category category = categoryService.getCategoryById(categoryId);
		if(category != null) {
			return convertToDto(category);
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with given ID was not found");	
	}
	
	/**
	 * Create a category
	 * 
	 * @param createCategoryDto	The DTO containing fields for the required data
	 * @return	JSON containing the created category information
	 */
	@PostMapping
	public CategoryDto createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
		Category categoryFromDto = modelMapper.map(createCategoryDto, Category.class);
		Category categoryToSave = categoryService.createNewCategory(categoryFromDto);	
		return convertToDto(categoryToSave);
	}
	
	/**
	 * Update a category status
	 * 
	 * @param categoryId	The id of the category to update
	 * @return	JSON containing the updated category information
	 */
	@PatchMapping("/status/{id}")
	public CategoryDto changeCategoryName(@PathVariable("id") Integer categoryId) {
		
		if(categoryService.getCategoryById(categoryId) != null) {
			Category category = categoryService.patchCategoryStatus(categoryId);
			return convertToDto(category);
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with given ID was not found");	
	}
	
	/**
	 * Update a category name
	 * 
	 * @param categoryId	The id of the category to update
	 * @param updateCategoryNameDto	The DTO containg the name field required for update
	 * @return	JSON containing the updated category information
	 */
	@PatchMapping("/name/{id}")
	public CategoryDto changeCategoryActiveState(@PathVariable("id") Integer categoryId, @Valid @RequestBody UpdateCategoryNameDto updateCategoryNameDto) {
		
		if(categoryService.getCategoryById(categoryId) != null) {
			Category category = categoryService.patchCategoryName(categoryId, updateCategoryNameDto.getName());
			return convertToDto(category);
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with given ID was not found");	
	}
	
	/**
	 * Delete a category by its id
	 * 
	 * @param categoryId	The id of the category to delete
	 * @return	String that confirms if the deletion was successful or not
	 */
	@DeleteMapping("/{id}")
	public String deleteCategory(@PathVariable("id") Integer categoryId) {
	
		if(categoryService.getCategoryById(categoryId) != null) {
			if(categoryService.deleteCategoryById(categoryId)) {
				return String.format("Deleted category with id %d from database", categoryId);
			}
			return String.format("Could not delete category with id %d", categoryId);
		}
		
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with given ID was not found");	
	}
	
	/*
	 * Converter for Category to CategoryDto
	 */
	private CategoryDto convertToDto(Category categoryAsEntity) {
		return modelMapper.map(categoryAsEntity, CategoryDto.class);
	}
	
}
