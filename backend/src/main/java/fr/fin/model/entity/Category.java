package fr.fin.model.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="category")
public class Category {

	@Id
	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length=50)
	private String name;

	@Column
	private Boolean status;

	@Column(name="created_by",nullable=false)
	private String createdBy;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "update_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@JsonBackReference
	@OneToMany(mappedBy = "category")
	@Where(clause = "deleted = false")
	private List<Product> products;

	@JsonBackReference
	@OneToMany(mappedBy = "category")
	@Where(clause = "deleted = true")
	private List<Product> inactiveProducts;

	@Column(name = "deleted")
	private Boolean deleted;

	public Category() {

	}

	public Category(Integer id, String name, Boolean status, String createdBy, Date createdAt, Boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.deleted = isDeleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}


	public List<Product> getInactiveProducts() {
		return inactiveProducts;
	}

	public void setInactiveProducts(List<Product> inactiveProducts) {
		this.inactiveProducts = inactiveProducts;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", status=" + status + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", products="
				+ products + "]";
	}

}
