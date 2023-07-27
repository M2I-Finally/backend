package fr.fin.model.dto;


public class BasketDetailDto {
	
	private Integer quantity;
		
	private Float discount;
		
	private Integer product_id;
	
	

	public BasketDetailDto() {
	
	}

	public BasketDetailDto(Integer quantity, Float discount, Integer product_id) {
		
		this.quantity = quantity;
		this.discount = discount;		
		this.product_id = product_id;
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

	

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}

	@Override
	public String toString() {
		return "BasketDetailDto [quantity=" + quantity + ", discount=" + discount + ", product_id=" + product_id + "]";
	}
	
	

}
