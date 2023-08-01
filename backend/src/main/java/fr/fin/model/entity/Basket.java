package fr.fin.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "basket")
public class Basket {
	
	@Id
	@Column(name = "basket_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer basketId;

	@Column
	private Float discount;

	@Column(name = "created_at")
	private Date createdAt;

	@ManyToOne
	@JoinColumn(name = "staff_id")
	private Staff staff;

	@OneToMany(targetEntity = BasketDetail.class, mappedBy = "basket")
	private List<BasketDetail> basketDetails = new ArrayList<BasketDetail>();

	@OneToMany(targetEntity = Payment.class, mappedBy = "basket")
	private List<Payment> payments = new ArrayList<Payment>();

	public Basket() {
	}
	
	

	public Basket(Float discount, Date createdAt, Staff staff, List<BasketDetail> basketDetails,
			List<Payment> payments) {
		
		this.discount = discount;
		this.createdAt = createdAt;
		this.staff = staff;
		this.basketDetails = basketDetails;
		this.payments = payments;
	}
	
	public Basket(Float discount, Staff staff, List<BasketDetail> basketDetails, List<Payment> payments) {
		
		this.discount = discount;
		this.staff = staff;
		this.basketDetails = basketDetails;
		this.payments = payments;
	}

	public Basket(Integer basketId, Float discount, Date createdAt, Staff staff, List<BasketDetail> basketDetails,
			List<Payment> payments) {
		
		this.basketId = basketId;
		this.discount = discount;
		this.createdAt = createdAt;
		this.staff = staff;
		this.basketDetails = basketDetails;
		this.payments = payments;
	}



	public Integer getBasketId() {
		return basketId;
	}



	public void setBasketId(Integer basketId) {
		this.basketId = basketId;
	}



	public float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public List<BasketDetail> getBasketDetails() {
		return basketDetails;
	}

	public void setBasketDetails(List<BasketDetail> basketDetails) {
		this.basketDetails = basketDetails;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}


	/*
	@Override
	public String toString() {
		return "Basket [id=" + basketId + ", discount=" + discount + ", createdAt=" + createdAt + ", staff=" + staff
				+ ", basketDetails=" + basketDetails + ", payments=" + payments + "]";
	}*/

	
}
