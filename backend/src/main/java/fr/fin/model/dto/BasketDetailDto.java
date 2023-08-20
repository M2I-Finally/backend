package fr.fin.model.dto;

import fr.fin.exceptions.custom.ValidationErrorException;


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

	public void setQuantity(Integer quantity) throws ValidationErrorException {
        if (quantity == null || quantity <= 0) {        	
            throw new ValidationErrorException("Erreur de validation : La quantité doit être positive");            
        }
        this.quantity = quantity;
    }

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) throws ValidationErrorException {
        if (discount == null || discount < 0) {
            throw new ValidationErrorException("Erreur de validation : Le discount doit être positif");
        }
        this.discount = discount;
    }

	public Integer getproductId() {
		return productId;
	}

	public void setproductId(Integer productId) throws ValidationErrorException {		
		if (productId == null || productId <=0) {			
			throw new ValidationErrorException("Erreur de validation : il faut au moins un produit dans le panier");
		}
		this.productId = productId;
	}

}
