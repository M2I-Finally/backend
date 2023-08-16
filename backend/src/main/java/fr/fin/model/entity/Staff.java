package fr.fin.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class Staff implements UserDetails {

	private static final long serialVersionUID = -4414646490025845750L;

	@Id
	@Column(name = "staff_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role));
	}

	@Override
	public boolean isAccountNonLocked() {
		return ((this.passwordTrial >= 50000) ? false: true);
	}

	@Override
	// Overriden getter for Spring Security from UserDetails implementation
	public String getUsername() {
		return username;
	}

	@Override
	// Overriden getter for Spring Security from UserDetails implementation
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.status;
	}

	// default constructor
	public Staff() {
	}
	
	public Staff(Integer id) {
		this.id = id;
	}
	
	public Staff(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	// for Spring Security
	public Staff(Integer id, String username, String role) {
		this.id = id;
		this.username = username;
		this.role = role;
	}

	// constructor w/o id
	public Staff(String username, String password, Integer passwordTrial, String role, boolean status, Date createdAt,
			Date updateAt) {
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
		this.id = id;
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

	public void setUsername(String username) {
		this.username = username;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Basket> getBaskets() {
		return baskets;
	}

	public void setBaskets(List<Basket> baskets) {
		this.baskets = baskets;
	}

	@Override
	public String toString() {
		return "Staff [id=" + id + ", username=" + username + ", password=" + password + ", passwordTrial="
				+ passwordTrial + ", role=" + role + ", status=" + status + ", createdAt=" + createdAt + ", updateAt="
				+ updateAt + ", baskets=" + baskets + "]";
	}



}
