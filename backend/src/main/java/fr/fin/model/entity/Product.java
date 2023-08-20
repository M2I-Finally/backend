package fr.fin.model.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productId;

	@Column(length=50)
	private String name;

	@Column
	private String description;

	@Column(nullable=false)
	private Double price;

	@Column(nullable=false)
	private Double tax;

	@Column
	private String picture;

	@Column
	private Boolean status;

	@Column
	private Double stock;

	@Column(name="created_by",nullable=false)
	private String createdBy;

	@Column(name="updated_by",nullable=false)
	private String updatedBy;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "update_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Column(name = "deleted")
	private Boolean deleted;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="category_id")
	private Category category;

	@OneToMany(mappedBy="product")
	private List<BasketDetail> basketDetails;

	public Product() {
		this.status = true;
		this.createdBy = "admin";
		this.updatedBy = "admin";
		this.createdAt = new Date();
	}

	public Product(Integer productId, String name, Boolean isActive, Double price, Boolean isDeleted) {
		this.productId = productId;
		this.name = name;
		this.status = isActive;
		this.price = price;
		this.deleted = isDeleted;
	}

	public Product(String name, String description, Double price, Double tax, String picture,
			Double stock, String createdBy, String updatedBy, Date createdAt, Date updatedAt, Category category,
			List<BasketDetail> basketDetails) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.tax = tax;
		this.picture = picture;
		this.status = true;
		this.stock = stock;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.category = category;
		this.basketDetails = basketDetails;
	}

	public Product(String name, String description, Double price, Double tax, String picture, Boolean status,
			Double stock, String createdBy, String updatedBy, Date createdAt, Date updatedAt, Category category,
			List<BasketDetail> basketDetails) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.tax = tax;
		this.picture = picture;
		this.status = status;
		this.stock = stock;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.category = category;
		this.basketDetails = basketDetails;
	}

	public Product(Integer productId, String name, String description, Double price, Double tax, String picture,
			Boolean status, Double stock, String createdBy, String updatedBy, Date createdAt, Date updatedAt,
			Category category, List<BasketDetail> basketDetails) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.tax = tax;
		this.picture = picture;
		this.status = status;
		this.stock = stock;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.category = category;
		this.basketDetails = basketDetails;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<BasketDetail> getBasketDetails() {
		return basketDetails;
	}

	public void setBasketDetails(List<BasketDetail> basketDetails) {
		this.basketDetails = basketDetails;
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}


}
