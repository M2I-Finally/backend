package fr.fin.model.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BasketDetailKey implements Serializable {
	
	
	@Column(name = "basket_id")
	private Integer basketId;
	
	@Column(name="product_id")
	private Integer productId;

	@Override
	public int hashCode() {
		return Objects.hash(basketId, productId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasketDetailKey other = (BasketDetailKey) obj;
		return Objects.equals(basketId, other.basketId) && Objects.equals(productId, other.productId);
	}
	

	
	
}
