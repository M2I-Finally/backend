package fr.fin.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="basket_detail")
public class BasketDetail {

	@Id
	@Column(name="basket_detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer basketDetailId;
	
	@Column
	private Integer quantity;
	
	@Column
	private Float discount;
	
	@ManyToOne
	@JoinColumn(name="basket_id")
	private Basket basket;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	

	public BasketDetail() {
		
	}



	public BasketDetail(Integer quantity, Float discount, Basket basket, Product product) {
		
		this.quantity = quantity;
		this.discount = discount;
		this.basket = basket;
		this.product = product;
	}



	public Integer getBasketDetailId() {
		return basketDetailId;
	}



	public void setBasketDetailId(Integer basketDetailId) {
		this.basketDetailId = basketDetailId;
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



	@Override
	public String toString() {
		return "BasketDetail [basketDetailId=" + basketDetailId + ", quantity=" + quantity + ", discount=" + discount
				+ ", basket=" + basket + ", product=" + product + "]";
	}	
	
}
