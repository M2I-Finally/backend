package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.fin.model.dto.CategoryDto;
import fr.fin.model.entity.Category;
import fr.fin.service.CategoryService;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/all")
	public List<CategoryDto> getAllCategories() {
		List<Category> categoriesAsEntity = categoryService.getAllCategories();
		List<CategoryDto> categoriesAsDto = new ArrayList<>();
		
		for(Category c : categoriesAsEntity) {
			categoriesAsDto.add(convertToDto(c));
		}
		
		return categoriesAsDto;
	}
	
	@GetMapping("/{categoryId}")
	public CategoryDto getCategoryById(@PathVariable("categoryId") Integer categoryId) {
		Category category = categoryService.getCategoryById(categoryId);
		if(category != null) {
			return convertToDto(category);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with given ID was not found");	
	}
	
	private CategoryDto convertToDto(Category categoryAsEntity) {
		return modelMapper.map(categoryAsEntity, CategoryDto.class);
	}
}
