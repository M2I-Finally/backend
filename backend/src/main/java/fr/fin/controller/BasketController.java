package fr.fin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import fr.fin.exceptions.custom.ResourceNotFoundException;
import fr.fin.model.dto.BasketPaymentDto;
import fr.fin.model.dto.PaymentDto;
import fr.fin.model.dto.TodaySaleDto;
import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.model.entity.Staff;
import fr.fin.service.BasketPaymentService;
import fr.fin.service.BasketService;

@RestController
@CrossOrigin
public class BasketController {

	@Autowired
	private BasketService basketService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BasketPaymentService basketPaymentService;

	@PostMapping("/payment")
	public ResponseEntity<Integer> insertBasket(@RequestBody BasketPaymentDto basketPaymentDto) {
		System.out.println(basketPaymentDto);
		Basket basketFromApp = convertToEntities(basketPaymentDto);
		System.out.println(basketFromApp);
		Integer basketId = basketPaymentService.createBasket(basketFromApp);
		return new ResponseEntity<Integer>(basketId, HttpStatus.CREATED);
	}

	/*
	 * @GetMapping() public BasketPaymentDto getBasket() { BasketPaymentDto
	 * basketPaymentDto = new BasketPaymentDto(); basketPaymentDto.setSellerId(1);
	 * List<PaymentDto> payments = new ArrayList<>(); List<BasketDetailDto>
	 * detailsDto = new ArrayList<>(); BasketDetailDto b1 = new BasketDetailDto(5,
	 * 1f, 1); BasketDetailDto b2 = new BasketDetailDto(5, 1f, 2); BasketDetailDto
	 * b3 = new BasketDetailDto(5, 1f, 3); BasketDetailDto b4 = new
	 * BasketDetailDto(5, 1f, 4); BasketDetailDto b5 = new BasketDetailDto(5, 1f,
	 * 5); detailsDto.add(b1); detailsDto.add(b2); detailsDto.add(b3);
	 * detailsDto.add(b4); detailsDto.add(b5); payments.add(new
	 * PaymentDto(12.3f,0)); payments.add(new PaymentDto(8.7f,1)); payments.add(new
	 * PaymentDto(9.0f,2)); basketPaymentDto.setpaymentDtoList(payments);
	 * basketPaymentDto.setBasketDetailDto(detailsDto);
	 * basketPaymentDto.setDiscount(1f); basketPaymentDto.setTotal(56.3f);
	 * convertToEntities(basketPaymentDto); return basketPaymentDto; };
	 */

	@GetMapping("/today-sale/{id}")
	public TodaySaleDto getTodaySale(@PathVariable("id") Integer sellerId) throws ResourceNotFoundException {

		List<Basket> listBaskets = basketService.getBasketsByStaffId(sellerId);

		if (listBaskets.size() > 0) {

			return makerSaleDto(listBaskets);
		}

		throw new ResourceNotFoundException("il n'y a pas encore eu de vente réalisé pour cette journée");
	}

	/*
	 * private Basket convertToEntity(BasketPaymentDto dto) { Basket basket = new
	 * Basket(); List<BasketDetail> listBasketDetail = new ArrayList<>();
	 * List<Payment> payments = new ArrayList<>(); List<PaymentDto> paymentDtoList =
	 * dto.getpaymentDtoList(); List<BasketDetailDto> listBasketDetailDto =
	 * dto.getBasketDetailDto();
	 * 
	 * 
	 * for (PaymentDto paymentDto : paymentDtoList) { payments.add(new
	 * Payment(paymentDto.getAmount(),PaymentType.valueOfPosition(paymentDto.
	 * getPaymentTypeId()))); }
	 * 
	 * for (BasketDetailDto basketDetailDto : listBasketDetailDto) { Product product
	 * = new Product(); product.setProductId(basketDetailDto.getproductId());
	 * listBasketDetail.add(new
	 * BasketDetail(basketDetailDto.getQuantity(),basketDetailDto.getDiscount(),
	 * product) ); }
	 * 
	 * basket.setDiscount(dto.getDiscount()); basket.setStaff(new
	 * Staff(dto.getSellerId()));
	 * 
	 * 
	 * 
	 * return null; }
	 */

	private Basket convertToEntities(BasketPaymentDto dto) {
		System.out.println(dto.getpaymentDtoList());
		List<BasketDetail> listBasketDetail = mapList(dto.getBasketDetailDto(), BasketDetail.class);

		List<Payment> payments = mapList(dto.getpaymentDtoList(), Payment.class);
		for (Payment payment : payments) {
			System.out.println(payment);
		}
		Staff staff = new Staff();
		staff.setId(dto.getSellerId());

		return new Basket(null, dto.getDiscount(), new Date(), staff, dto.getTotal(), listBasketDetail, payments);

	}

	/*
	 * Generic method to convert dto to entity/ entity to dto <S> = List source <T>
	 * = targetClass exemple List<Basket> listBasket = mapList(dto,Basket.class)
	 */
	private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}

	/**
	 * Make TodaySaleDto for the route GET /today-sale
	 * 
	 * @param listBaskets found in database
	 * @return TodaySaleDto PAymentType : cash = 0 , bank_card = 1 , other = 2
	 * 
	 */
	private TodaySaleDto makerSaleDto(List<Basket> listBaskets) {

		TodaySaleDto todaySales = new TodaySaleDto();
		Float total = 0f;
		PaymentDto cash = new PaymentDto(0f, 0);
		PaymentDto card = new PaymentDto(0f, 1);
		PaymentDto other = new PaymentDto(0f, 2);
		List<PaymentDto> paymentsDto = new ArrayList<>();

		// add datas to variables

		for (Basket basket : listBaskets) {
			total += basket.getTotal();
			List<Payment> payments = basket.getPayments();

			for (Payment payment : payments) {
				if (payment.getType() == 0) {
					cash.setAmount(cash.getAmount() + payment.getAmount());
				} else if (payment.getType() == 1) {
					card.setAmount(card.getAmount() + payment.getAmount());
				} else if (payment.getType() == 2) {
					other.setAmount(other.getAmount() + payment.getAmount());
				}

			}
		}
		paymentsDto.add(cash);
		paymentsDto.add(card);
		paymentsDto.add(other);

		todaySales.setTotal(total);
		todaySales.setSeller(listBaskets.get(0).getStaff().getUsername());
		todaySales.setPayments(paymentsDto);

		return todaySales;
	}

}
