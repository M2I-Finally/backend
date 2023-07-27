package fr.fin.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="basket_detail")
public class BasketDetail {

	@EmbeddedId
	BasketDetailKey id;
	
	@Column
	private Integer quantity;
	
	@Column
	private Float discount;
	
	@ManyToOne
	@MapsId("basketId")
	@JoinColumn(name="basket_id")
	private Basket basket;
	
	@ManyToOne
	@MapsId("productId")
	@JoinColumn(name="product_id")
	private Product product;

	public BasketDetailKey getId() {
		return id;
	}

	public void setId(BasketDetailKey id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
