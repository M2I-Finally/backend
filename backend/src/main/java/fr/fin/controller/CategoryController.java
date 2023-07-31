package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import fr.fin.model.dto.category.CategoryWithProductCountDto;
import fr.fin.model.dto.category.CreateUpdateCategoryDto;
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
	 * @return JSON containing all categories with product count
	 */
	@GetMapping
	public List<CategoryWithProductCountDto> getAllCategories() {
		List<Category> categoriesAsEntity = categoryService.getAllCategories();
		List<CategoryWithProductCountDto> categoriesAsDto = new ArrayList<>();

		for (Category c : categoriesAsEntity) {
			CategoryWithProductCountDto categoryDto = modelMapper.map(c, CategoryWithProductCountDto.class);
			categoryDto.setProductCount(c.getProducts().size());
			categoriesAsDto.add(categoryDto);
		}

		return categoriesAsDto;
	}

	/**
	 * Get a category given an ID
	 *
	 * @param categoryId	The id of the category to get
	 * @return	JSON containing the category information
	 */
	@GetMapping("/{id}")
	public CategoryDto getCategoryById(@PathVariable("id") Integer categoryId) {
		Category category = categoryService.getCategoryById(categoryId);
		if (category != null) {
			return convertToDto(category);
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La catégorie avec l'ID indiqué n'existe pas");
	}

	/**
	 * Create a category
	 *
	 * @param createCategoryDto	The DTO containing fields for the required data
	 * @return	JSON containing the created category information
	 */
	@PostMapping
	public CategoryDto createCategory(@Valid @RequestBody CreateUpdateCategoryDto createCategoryDto, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur de validation");
		}
		
		if (!categoryService.checkIfCategoryExistsByName(createCategoryDto.getName())) {
			Category categoryFromDto = modelMapper.map(createCategoryDto, Category.class);
			Category categoryToSave = categoryService.createNewCategory(categoryFromDto);
			return convertToDto(categoryToSave);
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"La catégorie avec le nom \"" + createCategoryDto.getName() + "\" existe déjà");
	}

	/**
	 * Update a category status
	 *
	 * @param categoryId	The id of the category to update
	 * @return	JSON containing the updated category information
	 */
	@PatchMapping("/status/{id}")
	public CategoryDto changeCategoryName(@PathVariable("id") Integer categoryId) {

		if (categoryService.getCategoryById(categoryId) != null) {
			Category category = categoryService.patchCategoryStatus(categoryId);
			return convertToDto(category);
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La catégorie avec l'ID indiqué n'existe pas");
	}

	/**
	 * Update a category name
	 *
	 * @param categoryId	The id of the category to update
	 * @param updateCategoryNameDto	The DTO containg the name field required for update
	 * @return	JSON containing the updated category information
	 */
	@PatchMapping("/name/{id}")
	public CategoryDto changeCategoryActiveState(@PathVariable("id") Integer categoryId,
			@Valid @RequestBody CreateUpdateCategoryDto updateCategoryNameDto, BindingResult bindingResult) {

		if(bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur de validation");
		}
		
		if (categoryService.getCategoryById(categoryId) != null) {
			if (!categoryService.checkIfCategoryExistsByName(updateCategoryNameDto.getName())) {
				Category category = categoryService.patchCategoryName(categoryId, updateCategoryNameDto.getName());
				return convertToDto(category);
			}
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"La catégorie avec le nom \"" + updateCategoryNameDto.getName() + "\" existe déjà");
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La catégorie avec l'ID indiqué n'existe pas");
	}

	/**
	 * Delete a category by its id;
	 *
	 * @param categoryId	The id of the category to delete
	 * @return	String that confirms if the deletion was successful or not
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable("id") Integer categoryId) {
		Category category = categoryService.getCategoryById(categoryId);
		if (category != null) {
			if (category.getProducts().isEmpty() || category.getProducts() == null) {
				if (categoryService.deleteCategoryById(categoryId)) {
					return new ResponseEntity<String>("[]", HttpStatus.OK);
				}
				
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur inconnue s'est produite lors de la suppression");
			}

			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"La catégorie contient des produits, impossible de la supprimer");
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La catégorie avec l'ID indiqué n'existe pas");
	}

	/*
	 * Converter for Category to CategoryDto
	 */
	private CategoryDto convertToDto(Category categoryAsEntity) {
		return modelMapper.map(categoryAsEntity, CategoryDto.class);
	}

}
