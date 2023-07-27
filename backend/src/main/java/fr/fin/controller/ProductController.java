package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.fin.model.dto.ProductGestionPageDto;
import fr.fin.model.dto.ProductShopPageDto;
import fr.fin.model.dto.ProductTablePageDto;
import fr.fin.model.entity.Product;
import fr.fin.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/shop")
	public List<ProductShopPageDto> getAllAvailableProducts() {
		List<Product> availableProducts = productService.getAvailableProducts();		
		List<ProductShopPageDto> availableProductsDto = new ArrayList<ProductShopPageDto>();
		for( Product availableProduct: availableProducts ) {
			availableProductsDto.add(convertToShopDto(availableProduct));
		}
		return availableProductsDto;
	}
	
	private ProductShopPageDto convertToShopDto(Product product) {
		return modelMapper.map(product, ProductShopPageDto.class);
	}
	
	@GetMapping("/products")
	public List<ProductTablePageDto> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		List<ProductTablePageDto> productsDto = new ArrayList<ProductTablePageDto>();
		for( Product product: products ) {
			productsDto.add(convertToTableDto(product));
		}
		return productsDto;
	}
	
	private ProductTablePageDto convertToTableDto(Product product) {
		return modelMapper.map(product, ProductTablePageDto.class);
	}
	
	@PostMapping("/product-add")
	public ResponseEntity<ProductGestionPageDto> addProduct(@RequestBody ProductGestionPageDto productDto) {
		if( productDto != null && !productDto.getName().isBlank() && !productDto.getPrice().isNaN() ) {
			productService.createProduct(convertToGestionEntity(productDto));
			return new ResponseEntity<ProductGestionPageDto>(productDto, HttpStatus.CREATED);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur dans la requête");
	}
	
	private Product convertToGestionEntity(ProductGestionPageDto productDto) {
		return modelMapper.map(productDto, Product.class);
	}
	
	@GetMapping("/product-edit/{id}")
	public ProductGestionPageDto getProductById(@PathVariable("id") Integer id) {
		if( productService.getProductById(id) != null ) {
			Product product = productService.getProductById(id);
			return convertToGestionDto(product);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
	}
	
	private ProductGestionPageDto convertToGestionDto(Product product) {
		return modelMapper.map(product, ProductGestionPageDto.class);
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id) {
		if( productService.getProductById(id) != null ) {
			productService.delete(id);
			return new ResponseEntity<String>("Produit supprimé", HttpStatus.OK);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le produit n'existe pas");
	}
}
