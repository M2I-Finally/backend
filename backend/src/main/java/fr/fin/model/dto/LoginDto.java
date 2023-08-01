package fr.fin.model.dto;

public class LoginDto {
	
	//user.getId(), user.getUsername(), user.getRole()
	private Integer id;
	private String username;
	private String role;
	
	public LoginDto() {
	}

	public LoginDto(Integer id, String username, String role) {
		this.id = id;
		this.username = username;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
