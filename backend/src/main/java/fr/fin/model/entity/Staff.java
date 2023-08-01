package fr.fin.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@Table(name = "staff")
public class Staff {

	@Id
	@Column(name = "staff_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer staffId;

	@Column(name = "username", length = 50)
	private String username;

	@Column(name = "password", length = 100)
	private String password;

	@Column(name = "password_trial", nullable = false)
	private Integer passwordTrial;

	@Column(name = "role", length = 50, nullable = false)
	private String role;

	@Column
	private boolean status;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "update_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateAt;

	@OneToMany(targetEntity = Basket.class, mappedBy = "staff")
	private List<Basket> baskets = new ArrayList<>();
	
	// default constructor
	public Staff() {
	};
	
	

	public Staff(Integer staffId) {
		
		this.staffId = staffId;
	}



	// constructor w/o id
	public Staff(String username, String password, Integer passwordTrial, String role, boolean status,
			Date createdAt, Date updateAt) {
		this.username = username;
		this.password = password;
		this.passwordTrial = passwordTrial;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
		this.updateAt = updateAt;
	}

	// constructor w/ id
	public Staff(Integer id, String userName, String password, Integer passwordTrial, String role, boolean status,
			Date createdAt, Date updateAt) {
		this.staffId = id;
		this.username = userName;
		this.password = password;
		this.passwordTrial = passwordTrial;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
		this.updateAt = updateAt;
	}

	/*
	 * getters & setters (no id setter)
	 */
	public Integer getId() {
		return staffId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPasswordTrial() {
		return passwordTrial;
	}

	public void setPasswordTrial(Integer passwordTrial) {
		this.passwordTrial = passwordTrial;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public List<Basket> getBaskets() {
		return baskets;
	}

	public void setBaskets(List<Basket> baskets) {
		this.baskets = baskets;
	};

	
}
