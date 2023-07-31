package fr.fin.model.dto;

public class BasketDetailDto {

	private Integer quantity;

	private Float discount;

	private Integer productId;

	public BasketDetailDto() {

	}

	public BasketDetailDto(Integer quantity, Float discount, Integer productId) {

		this.quantity = quantity;
		this.discount = discount;
		this.productId = productId;
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

	public Integer getproductId() {
		return productId;
	}

	public void setproductId(Integer productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "BasketDetailDto [quantity=" + quantity + ", discount=" + discount + ", productId=" + productId + "]";
	}

}
