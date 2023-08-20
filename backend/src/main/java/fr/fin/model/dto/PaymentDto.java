package fr.fin.model.dto;

import fr.fin.exceptions.custom.ValidationErrorException;

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

	public void setAmount(Float amount) throws ValidationErrorException {
		if( amount == null || amount < 0) {
			throw new ValidationErrorException("Erreur de validation : Le montant payé dois étre suéprieur à zero");
		}
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


}
