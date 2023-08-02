package fr.fin.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import fr.fin.model.dto.BasketDetailDto;
import fr.fin.model.dto.BasketPaymentDto;
import fr.fin.model.dto.PaymentDto;
import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.model.entity.Staff;
import fr.fin.service.BasketPaymentService;
import fr.fin.service.BasketService;

@RestController
@RequestMapping("/payment")
public class BasketController {
	
	@Autowired
	private BasketService basketService;	
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BasketPaymentService basketPaymentService;
		
	@PostMapping
	public ResponseEntity<Integer> insertBasket(@RequestBody BasketPaymentDto basketPaymentDto) {
		
		if(basketPaymentDto.getSellerId() != null 
				&& basketPaymentDto.getTotal() > 0 
				&& !(basketPaymentDto.getpaymentDtoList().size() <= 0) 
				&& !(basketPaymentDto.getBasketDetailDto().size() <= 0)) {
		Basket basketFromApp = convertToEntities(basketPaymentDto);
		Integer basketId = basketPaymentService.createBasket(basketFromApp);
		return new ResponseEntity<Integer>(basketId, HttpStatus.CREATED);
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur dans la requête");
		}
		
	}
	
	@GetMapping("/{id}")
	public BasketPaymentDto getBasket(@PathVariable("id") Integer id) {
		Float total =0f;
		Basket basket = basketService.getBasketById(id);
		BasketPaymentDto dto = new BasketPaymentDto();
		List<PaymentDto> listpaymentDto = mapList(basket.getPayments(), PaymentDto.class);
		List<BasketDetailDto> listDetailDto = mapList(basket.getBasketDetails(), BasketDetailDto.class);
		for (BasketDetailDto basketDetailDto : listDetailDto) {
			System.out.println(basketDetailDto.getDiscount() + " - " +basketDetailDto.getproductId() + " - "+basketDetailDto.getQuantity());
			total += basketDetailDto.getDiscount() * basketDetailDto.getproductId() * basketDetailDto.getQuantity();
		}
		dto.setBasketDetailDto(listDetailDto);
		dto.setpaymentDtoList(listpaymentDto);
		dto.setSellerId(basket.getStaff().getStaffId());
		dto.setDiscount(basket.getDiscount());
		System.out.println(basket.getBasketId() +" - " + basket.getStaff().getStaffId() + " - " +basket.getStaff().getUsername());
		System.out.println(dto);
		return dto;
	};
	
	
	
	private Basket convertToEntities(BasketPaymentDto dto) {
		System.out.println(dto.getpaymentDtoList());
		List<BasketDetail> listBasketDetail = mapList(dto.getBasketDetailDto(), BasketDetail.class);
		
		List<Payment> payments = mapList(dto.getpaymentDtoList(), Payment.class);
		for (Payment payment : payments) {
			System.out.println(payment);
		}
		Staff staff = new Staff();
		staff.setStaffId(dto.getSellerId());		
		
		return new Basket(null,dto.getDiscount(), new Date(), staff,listBasketDetail,payments);
				
	}
	
		
	/*
	 * Generic method to convert dto to entity / entity to dto
	 * <S> = List source
	 * <T> = targetClass
	 * exemple
	 * List<Basket> listBasket = mapList(dto,Basket.class)
	 */
	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
	    return source
	      .stream()
	      .map(element -> modelMapper.map(element, targetClass))
	      .collect(Collectors.toList());
	}
		

}
