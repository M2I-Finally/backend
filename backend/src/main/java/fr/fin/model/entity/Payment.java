package fr.fin.model.entity;

import java.util.Date;

import fr.fin.model.enumeration.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "payment")
public class Payment {

	@Id
	@Column(name = "payment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Float amount;

	@Column
	private PaymentType type;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@ManyToOne
	@JoinColumn(name = "basket_id")
	private Basket basket;

	public Payment() {
	}

	public Payment(Float amount, PaymentType type, Date createdAt, Basket basket) {
		this.amount = amount;
		this.type = type;
		this.createdAt = createdAt;
		this.basket = basket;
	}

	public Payment(Integer id, Float amount, PaymentType type, Date createdAt, Basket basket) {

		this.id = id;
		this.amount = amount;
		this.type = type;
		this.createdAt = createdAt;
		this.basket = basket;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}

}
