package fr.fin.model.dto;

public class PaymentDto {

	private Float amount;

	private int type;

	public PaymentDto() {

	}

	public PaymentDto(Float amount, int type) {

		this.amount = amount;
		this.type = type;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PaymentDto [amount=" + amount + ", type=" + type + "]";
	}
	
	

}
