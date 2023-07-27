package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.model.dto.ProductGestionPageDto;
import fr.fin.model.dto.ProductShopPageDto;
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
	
	@GetMapping("/products")
	public List<ProductGestionPageDto> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		List<ProductGestionPageDto> productsDto = new ArrayList<ProductGestionPageDto>();
		for( Product product: products ) {
			productsDto.add(convertToGestionDto(product));
		}
		return productsDto;
	}
	
	private ProductShopPageDto convertToShopDto(Product product) {
		return modelMapper.map(product, ProductShopPageDto.class);
	}
	
	private ProductGestionPageDto convertToGestionDto(Product product) {
		return modelMapper.map(product, ProductGestionPageDto.class);
	}
}
