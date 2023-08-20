package fr.fin.model.dto;

import java.util.List;

public class TodaySaleDto {
	
	private String seller;
	private Float total;
	private List<PaymentDto> payments;
	
	public TodaySaleDto() {
		
	}

	public TodaySaleDto(String seller, Float total, List<PaymentDto> payments) {
		
		this.seller = seller;
		this.total = total;
		this.payments = payments;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public List<PaymentDto> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentDto> payments) {
		this.payments = payments;
	}

	
}
