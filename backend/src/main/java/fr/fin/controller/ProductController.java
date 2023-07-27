package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
		System.out.println(availableProducts.size());
		
		List<ProductShopPageDto> availableProductsDto = new ArrayList<ProductShopPageDto>();
		for( Product availableProduct: availableProducts ) {
			availableProductsDto.add(convertToDto(availableProduct));
		}
		return availableProductsDto;
	}
	
	private ProductShopPageDto convertToDto(Product product) {
		return modelMapper.map(product, ProductShopPageDto.class);
	}
	
	private Product convertToEntity(ProductShopPageDto productDto) {
		return modelMapper.map(productDto, Product.class);
	}
}
