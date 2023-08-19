package fr.fin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.exceptions.custom.ResourceNotFoundException;
import fr.fin.exceptions.custom.ValidationErrorException;
import fr.fin.model.dto.BasketPaymentDto;
import fr.fin.model.dto.PaymentDto;
import fr.fin.model.dto.TodaySaleDto;
import fr.fin.model.entity.Basket;
import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.model.entity.Staff;
import fr.fin.service.BasketPaymentService;
import fr.fin.service.BasketService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Basket", description = "Manage basket's payment")
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
	public ResponseEntity<Integer> insertBasket(@Valid @RequestBody BasketPaymentDto basketPaymentDto, BindingResult bindingResult) throws ValidationErrorException {
		
		if (bindingResult.hasErrors()) {
		    List<String> errorMessages = new ArrayList<>();
		    
		    for (FieldError error : bindingResult.getFieldErrors()) {
		        errorMessages.add(error.getDefaultMessage());
		    }		    
		    
		    String errorMessage = String.join(", ", errorMessages);
		    throw new ValidationErrorException("Erreurs de validation : " + errorMessage);
		}
		
		Basket basketFromApp = convertToEntities(basketPaymentDto);
		Integer basketId = basketPaymentService.createBasket(basketFromApp);
		return new ResponseEntity<Integer>(basketId, HttpStatus.CREATED);
	}
	

	@GetMapping("/today-sale/{id}")
	public TodaySaleDto getTodaySale(@PathVariable("id") Integer sellerId) throws ResourceNotFoundException {

		List<Basket> listBaskets = basketService.getBasketsByStaffId(sellerId);

		if (listBaskets.size() > 0) {

			try {
				return makerSaleDto(listBaskets);
			} catch (ValidationErrorException e) {
				
				throw new ResourceNotFoundException("Erreur lors de la récupération des données");
			}
		}

		throw new ResourceNotFoundException("il n'y a pas encore eu de vente réalisée pour cette journée");
	}

	
	/**convert datas received from frontend into a backend-exploitable entity.
	 * 
	 * @param BasketPaymentDto
	 * @return Basket entity
	 * @throws ValidationErrorException 	
	 */
	private Basket convertToEntities(BasketPaymentDto dto) throws ValidationErrorException {
		
		List<BasketDetail> listBasketDetail = mapList(dto.getBasketDetailDto(), BasketDetail.class);
		List<Payment> payments = mapList(dto.getpaymentDtoList(), Payment.class);		
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
	 * @throws ValidationErrorException 	 * 
	 *
	 */
	private TodaySaleDto makerSaleDto(List<Basket> listBaskets) throws ValidationErrorException {

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
