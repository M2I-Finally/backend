package fr.fin.model.dto;

public class PaymentDto {

	private Float amount;

	private int paymentTypeId;

	public PaymentDto() {

	}

	public PaymentDto(Float amount, int paymentTypeId) {

		this.amount = amount;
		this.paymentTypeId = paymentTypeId;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public int getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	@Override
	public String toString() {
		return "PaymentDto [amount=" + amount + ", paymentTypeId=" + paymentTypeId + "]";
	}
	
	

}
