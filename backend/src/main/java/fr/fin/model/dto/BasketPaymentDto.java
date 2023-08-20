package fr.fin.model.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class BasketPaymentDto {
	
	@NotNull(message="Il n'y a pas de vendeur pour ce panier")
	private Integer sellerId;
	
	@Positive(message="la reduction ne peut pas etre inférieur à zero")
	private Float discount;
	
	@NotNull(message="le total du panier ne peut étre vide")
	@Positive(message="le total du panier ne peut pas être négatif")
	private Float total;
	
	@NotNull(message="la liste des articles ne peut être nulle")
	@Size(min = 1, message = "Il doit y avoir au moins un article dans le panier")
	private List<BasketDetailDto> basketDetailDtoList = new ArrayList<>();
	
	@NotNull(message="la liste des moyens de paiments ne peut pas être nulle")
	@Size(min = 1, message = "Il doit y avoir au moins un moyen de paiment")
	private List<PaymentDto> paymentDtoList = new ArrayList<>();

	public BasketPaymentDto() {

	}

	public BasketPaymentDto(Integer sellerId, Float discount, Float total, List<BasketDetailDto> basketDetailDtoList,
			List<PaymentDto> paymentDtoList) {

		this.sellerId = sellerId;
		this.discount = discount;
		this.total = total;
		this.basketDetailDtoList = basketDetailDtoList;
		this.paymentDtoList = paymentDtoList;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public List<BasketDetailDto> getBasketDetailDto() {
		return basketDetailDtoList;
	}

	public void setBasketDetailDto(List<BasketDetailDto> basketDetailDtoList) {
		this.basketDetailDtoList = basketDetailDtoList;
	}

	public List<PaymentDto> getpaymentDtoList() {
		return paymentDtoList;
	}

	public void setpaymentDtoList(List<PaymentDto> paymentDtoList) {
		this.paymentDtoList = paymentDtoList;
	}

}
