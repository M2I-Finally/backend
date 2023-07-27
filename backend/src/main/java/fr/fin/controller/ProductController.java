package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.model.dto.ProductTablePageDto;
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
}
