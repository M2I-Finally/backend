package fr.fin.model.dto;

public class CheckPasswordDto {

	private Integer userId;
	private String password;

	public CheckPasswordDto() { }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
