package fr.fin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.fin.model.dto.ProductGestionPageDto;
import fr.fin.model.dto.ProductShopPageDto;
import fr.fin.model.entity.Category;
import fr.fin.model.entity.Product;
import fr.fin.service.CategoryService;
import fr.fin.service.ProductService;

@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public List<ProductShopPageDto> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		List<ProductShopPageDto> productsDto = new ArrayList<ProductShopPageDto>();
		for( Product product: products ) {
			productsDto.add(convertToShopDto(product));
		}
		return productsDto;
	}
	
	private ProductShopPageDto convertToShopDto(Product product) {
		return modelMapper.map(product, ProductShopPageDto.class);
	}
	
	@GetMapping("/category/{id}")
	public List<ProductShopPageDto> getProductByCategory(@PathVariable Integer id) {
		List<Product> products = productService.getProductsByCategory(id);
		List<ProductShopPageDto> productsDto = new ArrayList<ProductShopPageDto>();
		for( Product product: products ) {
			productsDto.add(convertToShopDto(product));
		}
		return productsDto;
	}
	
	@PostMapping
	public ResponseEntity<ProductGestionPageDto> addProduct(@RequestBody ProductGestionPageDto productDto) {
		if( productDto != null && !productDto.getName().isBlank() && !productDto.getPrice().isNaN() ) {
			Category productCategory = categoryService.getCategoryById(productDto.getCategoryId());		
			Product productToCreate = convertToGestionEntity(productDto);
			productToCreate.setCategory(productCategory);	
			productService.createProduct(productToCreate);
			return new ResponseEntity<ProductGestionPageDto>(productDto, HttpStatus.CREATED);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur dans la requête");
	}
	
	private Product convertToGestionEntity(ProductGestionPageDto productDto) {
		return modelMapper.map(productDto, Product.class);
	}
	
	@GetMapping("/{id}")
	public ProductGestionPageDto getProductById(@PathVariable("id") Integer id) {
		if( productService.getProductById(id) != null ) {
			Product product = productService.getProductById(id);
			return convertToGestionDto(product);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductGestionPageDto> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductGestionPageDto productDto) {
		if( productService.getProductById(id) != null && !productDto.getName().isBlank() && !productDto.getPrice().isNaN() ) {
			Product updatedProduct = productService.getProductById(id);
			updatedProduct.setName(productDto.getName());
			updatedProduct.setDescription(productDto.getDescription());
//			updatedProduct.setCategory(productDto.getCategory());
			updatedProduct.setPrice(productDto.getPrice());
			updatedProduct.setTax(productDto.getTax());
			updatedProduct.setPicture(productDto.getPicture());
			updatedProduct.setUpdatedAt(new Date());
			productService.createProduct(updatedProduct);
			return new ResponseEntity<ProductGestionPageDto>(productDto, HttpStatus.CREATED);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur dans la requête");
	}
	
	private ProductGestionPageDto convertToGestionDto(Product product) {
		return modelMapper.map(product, ProductGestionPageDto.class);
	}
	
	@PatchMapping("/{id}")
	public ProductGestionPageDto updateProductStatus(@PathVariable("id") Integer id) {		
		return convertToGestionDto(productService.updateProductStatus(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ProductGestionPageDto> deleteProduct(@PathVariable("id") Integer id) {
		if( productService.getProductById(id) != null ) {
			ProductGestionPageDto deletedProduct = convertToGestionDto(productService.getProductById(id));
			productService.delete(id);
			return new ResponseEntity<ProductGestionPageDto>(deletedProduct, HttpStatus.OK);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le produit n'existe pas");
	}
}
