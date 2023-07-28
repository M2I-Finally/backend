package fr.fin.model.dto;

import java.util.ArrayList;
import java.util.List;

import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.model.entity.Staff;

public class BasketDto {
	private Staff staff;
	private Float discount;
	private List<BasketDetail> basketDetails = new ArrayList<BasketDetail>();
	private List<Payment> payments = new ArrayList<Payment>();
	
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	public List<BasketDetail> getBasketDetails() {
		return basketDetails;
	}
	public void setBasketDetails(List<BasketDetail> basketDetails) {
		this.basketDetails = basketDetails;
	}
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
	
}
