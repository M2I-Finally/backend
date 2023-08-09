package fr.fin.model.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum PaymentType {
	
	CASH(0), 
	BANK_CARD(1),
	OTHER(2);

	private static final Map<Integer, PaymentType> BY_POSITION = new HashMap<>();

	static {
		for (PaymentType e : values()) {
			BY_POSITION.put(e.position, e);
		}
	}

	public final int position;

	private PaymentType(int position) {
		this.position = position;

	}

	public static PaymentType valueOfPosition(int position) {
		return BY_POSITION.get(position);
	}
	
	

}
