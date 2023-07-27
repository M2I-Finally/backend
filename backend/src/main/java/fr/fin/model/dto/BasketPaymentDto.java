package fr.fin.model.dto;

import java.util.ArrayList;
import java.util.List;

public class BasketPaymentDto {

	/*
	 * class CartLine { private id: number; private name: string; private price:
	 * number; private discount: number; private quantity: number;
	 * 
	 * 
	 * class Cart { private cartLines: CartLine[]; private total: number; private
	 * discount: number;
	 * 
	 * class Basket { private cart : Cart; private discount : number; private total
	 * : number = 0;
	 * 
	 * 
	 */
	private Integer sellerId;
	private Float discount;
	private Float total;
	private List<BasketDetailDto> basketDetailDto = new ArrayList<>();
	private List<Integer> paymentsType = new ArrayList<>();

	public BasketPaymentDto() {

	}
	
	

	public BasketPaymentDto(Integer sellerId, Float discount, Float total, List<BasketDetailDto> basketDetailDto,
			List<Integer> paymentsType) {
		
		this.sellerId = sellerId;
		this.discount = discount;
		this.total = total;
		this.basketDetailDto = basketDetailDto;
		this.paymentsType = paymentsType;
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
		return basketDetailDto;
	}

	public void setBasketDetailDto(List<BasketDetailDto> basketDetailDto) {
		this.basketDetailDto = basketDetailDto;
	}

	public List<Integer> getPaymentsType() {
		return paymentsType;
	}

	public void setPaymentsType(List<Integer> paymentsType) {
		this.paymentsType = paymentsType;
	}

}
