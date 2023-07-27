package fr.fin.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.model.dto.BasketDetailDto;
import fr.fin.model.dto.BasketPaymentDto;
import fr.fin.model.dto.BasketRegistredDto;
import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.model.entity.Staff;
import fr.fin.service.BasketService;

@RestController
public class BasketController {
	
	@Autowired
	private BasketService basketService;	
	
	@Autowired
	private ModelMapper modelMapper;
	/*
	@GetMapping("/payment")
	public List<ProductShopPageDto> getAllAvailableProducts() {
		List<Product> availableProducts = productService.getAvailableProducts();
		System.out.println(availableProducts.size());
		
		List<ProductShopPageDto> availableProductsDto = new ArrayList<ProductShopPageDto>();
		for( Product availableProduct: availableProducts ) {
			availableProductsDto.add(convertToDto(availableProduct));
		}
		return availableProductsDto;
	}*/
	
	@PostMapping("/payment")
	public BasketRegistredDto insertBasket(@RequestBody BasketPaymentDto basketPaymentDto) {
		System.out.println(basketPaymentDto);
		
		return null;
	}
	@GetMapping("/payment")
	public BasketPaymentDto getBasket() {
		BasketPaymentDto basketPaymentDto = new BasketPaymentDto();
		List<Integer> payments = new ArrayList<>();
		List<BasketDetailDto> detailsDto = new ArrayList<>();
		BasketDetailDto b1 = new BasketDetailDto(5, 1f, 1);
		BasketDetailDto b2 = new BasketDetailDto(5, 1f, 2);
		BasketDetailDto b3 = new BasketDetailDto(5, 1f, 3);
		BasketDetailDto b4 = new BasketDetailDto(5, 1f, 4);
		BasketDetailDto b5 = new BasketDetailDto(5, 1f, 5);
		detailsDto.add(b1);
		detailsDto.add(b2);
		detailsDto.add(b3);
		detailsDto.add(b4);
		detailsDto.add(b5);
		payments.add(0);
		payments.add(1);
		payments.add(2);
		basketPaymentDto.setPaymentsType(payments);
		basketPaymentDto.setBasketDetailDto(detailsDto);
		basketPaymentDto.setDiscount(1f);
		basketPaymentDto.setTotal(56.3f);
		
		return basketPaymentDto;
	};
	
	/*
	private ProductShopPageDto convertToDto(Product product) {
		return modelMapper.map(product, ProductShopPageDto.class);
	}
	
	private Product convertToEntity(ProductShopPageDto productDto) {
		return modelMapper.map(productDto, Product.class);
	}*/
	
	private Basket convertToEntity(BasketPaymentDto dto) {
		Basket basket = new Basket();
		List<BasketDetail> listBasketDetail = new ArrayList<>();
		List<Payment> payments = new ArrayList<>();
		basket.setDiscount(dto.getDiscount());
		basket.setStaff(new Staff(dto.getSellerId()));
		return null;		
	}
}
