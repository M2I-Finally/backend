package fr.fin.model.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.fin.model.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public class ProductGestionPageDto {

	private Integer productId;
	
	@NotNull
	@Pattern(regexp = "[a-zA-Z0-9 ]+", message = "Un nom de produit ne peut contenir des lettres, des chiffres et des espaces")
	private String name;
	
	private String description;
	
	@JsonIgnore
	private Category category;
	
	@NotNull(message = "Le produit doit appartenir à une catégorie")
	private Integer categoryId;
	
	@Min(value = 0, message = "Le prix ne peut pas être inférieur à 0")
	private Double price;
	
	@Min(value = 0, message = "La taxe ne peut pas être inférieure à 0")
	private Double tax;
	
	private String picture;
		
	public ProductGestionPageDto() {
		
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
