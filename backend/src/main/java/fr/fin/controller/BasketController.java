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
import fr.fin.model.dto.BasketPaymentDto;
import fr.fin.model.dto.ProductGestionPageDto;
import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.model.entity.Product;
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
		/*
		 * private Integer sellerId;
			private Float discount;
			private Float total;
			private List<BasketDetailDto> basketDetailDtoList = new ArrayList<>();
			private List<PaymentDto> paymentDtoList = new ArrayList<>();
		 */
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
		
		//Basket basketLoaded = basketPaymentService.getBasket(id);
		//System.out.println(basketLoaded);
		
		/*
		BasketPaymentDto basketPaymentDto = new BasketPaymentDto();
		basketPaymentDto.setSellerId(1);
		List<PaymentDto> payments = new ArrayList<>();
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
		payments.add(new PaymentDto(12.3f,0));
		payments.add(new PaymentDto(8.7f,1));
		payments.add(new PaymentDto(9.0f,2));
		basketPaymentDto.setpaymentDtoList(payments);
		basketPaymentDto.setBasketDetailDto(detailsDto);
		basketPaymentDto.setDiscount(1f);
		basketPaymentDto.setTotal(56.3f);
		convertToEntities(basketPaymentDto);
		return basketPaymentDto;*/
		Basket basket = basketService.getBasketById(id);
		System.out.println(basket.getBasketId() +" - " + basket.getStaff().getStaffId() + " - " +basket.getStaff().getUsername());
		for(Payment payment : basket.getPayments()) {
			System.out.println(payment);
		}
		for(BasketDetail basketDetail : basket.getBasketDetails()) {
			System.out.println(basketDetail.getProduct().getName());
		}
		return null;
	};
	
	/*
	private ProductShopPageDto convertToDto(Product product) {
		return modelMapper.map(product, ProductShopPageDto.class);
	}
	
	private Product convertToEntity(ProductShopPageDto productDto) {
		return modelMapper.map(productDto, Product.class);
	}*/
	/*
	private Basket convertToEntity(BasketPaymentDto dto) {
		Basket basket = new Basket();
		List<BasketDetail> listBasketDetail = new ArrayList<>();
		List<Payment> payments = new ArrayList<>();
		List<PaymentDto> paymentDtoList = dto.getpaymentDtoList();
		List<BasketDetailDto> listBasketDetailDto = dto.getBasketDetailDto();
		
		
		for (PaymentDto paymentDto : paymentDtoList) {
			payments.add(new Payment(paymentDto.getAmount(),PaymentType.valueOfPosition(paymentDto.getPaymentTypeId())));
		}
		
		for (BasketDetailDto basketDetailDto : listBasketDetailDto) {
			Product product = new Product();
			product.setProductId(basketDetailDto.getproductId());
			listBasketDetail.add(new BasketDetail(basketDetailDto.getQuantity(),basketDetailDto.getDiscount(), product) );
		}
		
		basket.setDiscount(dto.getDiscount());
		basket.setStaff(new Staff(dto.getSellerId()));
		
		
		
		return null;		
	}*/
	
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
